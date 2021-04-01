package com.dvm.hackdetection.repository.impl;

import com.dvm.hackdetection.repository.ActivityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Slf4j
public class ActivityRepositoryImpl implements ActivityRepository {
    Map<String, List<Long>> activities = new HashMap<String, List<Long>>();

    @Override
    public void addActivity(String ip, Long date) {
        log.debug("Starts addActivity");
        if(containsAttemptsForIp(ip)){
            activities.get(ip).add(date);
        }else{
            activities.put(ip, new ArrayList<>(Arrays.asList(date)));
        }
        log.debug("Ends addActivity");
    }

    @Override
    public boolean containsAttemptsForIp(String ip) {
        log.debug("Starts containsAttemptsForIp");
        return activities.containsKey(ip);
    }

    @Override
    public void deleteTracesForIp(String ip) {
        log.debug("Starts deleteTracesForIp");
        activities.remove(ip);
        log.debug("Ends deleteTracesForIp");
    }

    @Override
    public List<Long> getAttemptDatesFromIp(String ip) {
        log.debug("Starts getAttemptDatesFromIp");
        return activities.get(ip);
    }
}
