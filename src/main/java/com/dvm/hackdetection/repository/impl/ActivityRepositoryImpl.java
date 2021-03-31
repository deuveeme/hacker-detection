package com.dvm.hackdetection.repository.impl;

import com.dvm.hackdetection.repository.ActivityRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ActivityRepositoryImpl implements ActivityRepository {
    Map<String, List<Long>> activities = new HashMap<String, List<Long>>();

    @Override
    public void addActivity(String ip, Long date) {
        if(containsAttemptsForIp(ip)){
            activities.get(ip).add(date);
        }else{
            activities.put(ip, new ArrayList<>(Arrays.asList(date)));
        }
    }

    @Override
    public boolean containsAttemptsForIp(String ip) {
        return activities.containsKey(ip);
    }

    @Override
    public void deleteTracesForIp(String ip) {
        activities.remove(ip);
    }

    @Override
    public List<Long> getAttemptDatesFromIp(String ip) {
        return activities.get(ip);
    }
}
