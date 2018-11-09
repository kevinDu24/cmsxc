package cn.com.leadu.cmsxc.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.sql.DataSource;

@Configuration
public class CmsOauth2WebConfig {

    public static final String RESOURCE_ID = "cms_authorization";

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public RedisTemplate redisTemplateInit(@Autowired(required = false)DataSource dataSource){
        if(dataSource != null){
            if(dataSource != null) {
                try {
                    dataSource.getConnection();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

}
