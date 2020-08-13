/**
 * 
 */
package com.example.demo.config;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.JSON;


/**
 * @author djinquan
 * 2020年1月13日
 * redis配置类
 * 热部署不能使用缓存
 */
@Configuration
@EnableCaching
public class RedisConfig {
	
	@Autowired
	private RedisConnectionFactory factory;
	@Bean
    public RedisCacheWriter writer() {
        return RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate().getConnectionFactory());
    }
	
	@Bean
	public KeyGenerator keyGenerator() {
		System.out.println("================================================keyGenerator==");
		return new KeyGenerator() {
			
			@Override
			public Object generate(Object target, Method method, Object... params) {
				// TODO Auto-generated method stub
				/*StringBuilder sb=new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object object : params) {
					sb.append(object.toString());
				}
				return sb.toString();*/
				StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                sb.append("&");
                for (Object obj : params) {
                       if (obj != null){
                            sb.append(obj.getClass().getName());
                            sb.append("&");
                            sb.append(JSON.toJSONString(obj));
                            sb.append("&");
                       }
                }
                return DigestUtils.sha256Hex(sb.toString());
			}
		};
	}
	
	/*@Bean(name="cacheManager")
	public CacheManager cacheManager() {
		System.out.println("================================================cacheManager==");
		RedisCacheConfiguration config=RedisCacheConfiguration.defaultCacheConfig();//生成默认配置
		config=config.entryTtl(Duration.ofMinutes(30))//默认1分钟
				.disableCachingNullValues();//不缓存空值
		// 设置一个初始化的缓存空间set集合
		// 对每个缓存空间应用不同的配置
		Map<String,RedisCacheConfiguration> configMap=new HashMap<>();
		configMap.put("cache1", config);
		configMap.put("cache2", config.entryTtl(Duration.ofSeconds(120)));//120秒
		
		RedisCacheManager redisCacheManager=RedisCacheManager.builder(factory)// 使用自定义的缓存配置初始化一个cacheManager
				.cacheDefaults(config)
				.initialCacheNames(configMap.keySet())// 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
				.withInitialCacheConfigurations(configMap)
				.build();
		return redisCacheManager;
		
	}*/
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate(){
		System.out.println("================================================redisTemplate==");
		RedisTemplate<String, Object> redisTemplate=new RedisTemplate<>();
		redisTemplate.setConnectionFactory(factory);
		//将字符串key序列化
		StringRedisSerializer stringRedisSerializer=new StringRedisSerializer();
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer=new Jackson2JsonRedisSerializer<>(Object.class);
		redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}
	


}
