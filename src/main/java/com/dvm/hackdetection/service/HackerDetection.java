package com.dvm.hackdetection.service;

import org.springframework.stereotype.Service;

@Service
public interface HackerDetection {
    public String parseLine(String line);
}
