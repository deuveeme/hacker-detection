package com.dvm.hackdetection.service.impl;

import com.dvm.hackdetection.model.Action;
import com.dvm.hackdetection.model.ActivityLogged;
import com.dvm.hackdetection.repository.ActivityRepository;
import com.dvm.hackdetection.service.HackerDetection;
import com.dvm.hackdetection.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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

    @Override
    public String parseLine(String line) {
        log.info("Starting parseLine - New line is going to be analysed: {}", line);
        ActivityLogged activity = convertLine(line);
        if(activity.getAction().equals(Action.SIGNIN_FAILURE)){
            repository.addActivity(activity.getIp(),activity.getEpochDate());
            //addActivity(activity.getIp(), activity.getEpochDate());
            if (attempExceedThreshold(activity.getIp(), 5, activity.getEpochDate())){
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
     * @param date
     * @return
     */
    private boolean attempExceedThreshold(final String ip, final long threshold, final Long date){
        List<Long> dates = repository.getAttemptDatesFromIp(ip);
        if (dates.size()<5){
            return false;
        }
        dates.stream().sorted();
        Long lastAttempt = dates.get(dates.size()-1);
        Long firstAttempt = dates.get(dates.size()-5);
        Long interval = DateUtil.getDifferenceInMinutesFromEpoch(firstAttempt, lastAttempt);
        if(interval<= 5){
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
        return act;
    }
}
