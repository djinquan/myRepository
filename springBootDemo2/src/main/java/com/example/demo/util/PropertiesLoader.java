/**
 * 
 */
package com.example.demo.util;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * @author djinquan
 * 2020年2月28日
 * 
 */
public class PropertiesLoader {
	
	private static Logger logger=LoggerFactory.getLogger(PropertiesLoader.class);
	private static ResourceLoader rl=new DefaultResourceLoader();
	private final Properties properties;
	
	public PropertiesLoader(String filePath) {
		this.properties=this.loadProperties(filePath);
	}
	
	public Properties getProperties() {
		return this.properties;
	}
	
	private String getValue(String key) {
		//String systemProperty = System.getProperty(key);
		return this.properties.containsKey(key)?this.getProperties().getProperty(key):"";
	}
	
	public long getLong(String key) {
		String value=this.getValue(key);
		if(StringUtils.isNotBlank(value)) {
			return Long.valueOf(value);
		}
		else {
			throw new NoSuchElementException();
		}
	}
	
	public int getInt(String key) {
		String value=this.getValue(key);
		if(StringUtils.isNotBlank(value)) {
			return Integer.valueOf(value);
		}
		else {
			throw new NoSuchElementException();
		}
	}
	
	public String get(String key) {
		return this.getValue(key);
	}
	
	private Properties loadProperties(String resourcesPaths) {
        Properties props = new Properties();
            InputStream is = null;

            try {
                Resource resource = rl.getResource(resourcesPaths);
                is = resource.getInputStream();
                props.load(is);
            } catch (IOException var12) {
                logger.info("Could not load properties from path:" + resourcesPaths + ", " + var12.getMessage());
            } finally {
            	try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }

        return props;
    }

}
