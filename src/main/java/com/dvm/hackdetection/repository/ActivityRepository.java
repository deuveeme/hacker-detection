package com.dvm.hackdetection.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository {
    public void addActivity(String ip, Long date);

    public boolean containsAttemptsForIp(String ip);

    public void deleteTracesForIp(String ip);

    public List<Long> getAttemptDatesFromIp(String ip);
}
