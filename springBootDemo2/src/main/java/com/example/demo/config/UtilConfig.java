/**
 * 
 */
package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author djinquan
 * 2020年2月28日
 * 
 */
@Configuration
@PropertySource("classpath:/set.properties")
public class UtilConfig {
	
	
	private long typeId;
	
	private long chassisId;
	
	public long getTypeId() {
		return typeId;
	}
	@Value("${typeId}")
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}
	public long getChassisId() {
		return chassisId;
	}
	@Value("${chassisId}")
	public void setChassisId(long chassisId) {
		this.chassisId = chassisId;
	}
	
	

}
