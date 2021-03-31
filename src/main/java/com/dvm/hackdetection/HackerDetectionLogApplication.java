package com.dvm.hackdetection;

import com.dvm.hackdetection.repository.impl.ActivityRepositoryImpl;
import com.dvm.hackdetection.service.HackerDetection;
import com.dvm.hackdetection.service.impl.HackerDetectionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = "com.dvm.hackdetection")
public class HackerDetectionLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(HackerDetectionLogApplication.class, args);
		List<String> lines = new ArrayList<>(Arrays.asList(
								"88.88.55.956,161718290,SIGNIN_FAILURE,Peter Parker",
								"88.88.55.956,161718342,SIGNIN_FAILURE,Peter Parker",
								"88.88.55.956,161718355,SIGNIN_FAILURE,Peter Parker",
								"88.88.100.956,161719690,SIGNIN_SUCCESS,Martin McFly",
								"88.88.55.956,161718400,SIGNIN_FAILURE,Peter Parker",
								"88.88.55.956,161718450,SIGNIN_FAILURE,Peter Parker"));
		HackerDetection service = new HackerDetectionImpl(new ActivityRepositoryImpl());
		lines.stream().forEach(l-> service.parseLine(l));
	}

}
