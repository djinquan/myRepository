/**
 * 
 */
package com.example.demo.config;

import java.util.Date;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author djinquan
 * 2020年3月24日
 * 
 */
@Configuration
public class ScheduleTask {
	
	@Scheduled(cron="0/5 * * * * ?")
	public void task() {
		System.out.println("执行任务:"+new Date());
	}

}
