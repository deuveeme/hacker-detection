package com.dvm.hackdetection.service.impl;

import com.dvm.hackdetection.model.Action;
import com.dvm.hackdetection.model.ActivityLogged;
import com.dvm.hackdetection.repository.ActivityRepository;
import com.dvm.hackdetection.service.HackerDetection;
import com.dvm.hackdetection.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class HackerDetectionImpl implements HackerDetection {

    @Autowired
    private ActivityRepository repository;

    private static final long INTERVAL_MINUTES_THRESHOLD = 5;
    private static final int ATTEMPTS_TO_BE_CHECKED = 5;

    @Override
    public String parseLine(String line) {
        log.info("Starting parseLine - New line is going to be analysed: {}", line);
        ActivityLogged activity = convertLine(line);
        if(activity.getAction().equals(Action.SIGNIN_FAILURE)){
            repository.addActivity(activity.getIp(),activity.getEpochDate());
            if (attemptExceedsThreshold(activity.getIp(), INTERVAL_MINUTES_THRESHOLD, activity.getEpochDate())){
                log.info("The IP {} is being hacked", activity.getIp());
                return activity.getIp();
            }
        }
        if(activity.getAction().equals(Action.SIGNIN_SUCCESS)){
            repository.deleteTracesForIp(activity.getIp());
        }
        log.info("Normal activity for the IP {}", activity.getIp());
        log.info("Ending parseLine");
        return null;
    }

    /**
     * Method to calculate the time interval between first attempt and last attempt.
     * Returns true if that interval is higher than the threshold.
     * @param ip
     * @param threshold
     * @param lastAttempt
     * @return
     */
    private boolean attemptExceedsThreshold(final String ip, final long threshold, final Long lastAttempt){
        log.info("Validating IP {} is not being attempted more than 5 times in {} minutes", ip, threshold);
        List<Long> dates = repository.getAttemptDatesFromIp(ip);
        if (dates.size()<ATTEMPTS_TO_BE_CHECKED){
            return false;
        }
        dates.stream().sorted();
        Long firstAttempt = dates.get(dates.size()-ATTEMPTS_TO_BE_CHECKED);
        Long interval = DateUtil.getDifferenceInMinutesFromEpoch(firstAttempt, lastAttempt);
        if(interval<= threshold){
            return true;
        }
        return false;
    }

    /**
     * Method to convert String log line in a Activity Object
     * @param line
     * @return ActivityLogged
     * @throws IllegalArgumentException
     */
    private ActivityLogged convertLine(final String line) throws IllegalArgumentException{
        log.info("Parsing log line from String to ActivityLogged");
        if(ObjectUtils.isEmpty(line)){
            throw new IllegalArgumentException("Activity log is missing");
        }

        String[] activityProps = line.split(",");
        if(activityProps.length<4){
            throw new IllegalArgumentException("Activity log not correct");
        }
        ActivityLogged act = ActivityLogged.builder().ip(activityProps[0])
                .epochDate(Long.valueOf(activityProps[1]))
                .userName(activityProps[3]).build();

        try{
            act.setAction(Action.valueOf(activityProps[2]));
        }catch(IllegalArgumentException e){
            log.error("Incorrect ACTION was found");
            throw new IllegalArgumentException("Incorrect ACTION was found parsing the line");
        }
        log.info(act.toString());
        return act;
    }
}
