package io.bootify.l11_visitor_management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

//    @Value("${redis.url}")
//    private String redisURL;
//
//    @Value("${redis.password}")
//    private String redisPass;
//
//    @Value("${redis.port}")
//    private int port;

//    @Bean
//    public RedisConnectionFactory getRedisConnectionFactory(){
//        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory();
//        lettuceConnectionFactory.setHostName(redisURL);
//        lettuceConnectionFactory.setPort(port);
//        return lettuceConnectionFactory;
//    }

    @Bean
    public RedisTemplate<String,Object> getRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }
}

