package cn.com.leadu.cmsxc.extend.config;

import cn.com.leadu.cmsxc.extend.filter.ParameterFilter;
import cn.com.leadu.cmsxc.extend.interceptor.ParameterInterceptor;
import cn.com.leadu.cmsxc.extend.config.resolver.LoginUserArgumentResolver;
import cn.com.leadu.cmsxc.extend.service.LogService;
import cn.com.leadu.cmsxc.extend.util.UserInfoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author qiaomengnan
 * @ClassName: WebExtendConfigurer
 * @Description: 项目共通配置类
 * @date 2018/1/7
 */
@Configuration
public class WebExtendConfigurer extends WebMvcConfigurerAdapter implements AsyncConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebExtendConfigurer.class);

    @Bean
    public WebProperties webProperties(){
        LOGGER.info("***************************webProperties初始化***************************");
        return new WebProperties();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        LOGGER.info("***************************corsFilter初始化***************************");
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }


    @Bean
    public LoginUserArgumentResolver loginUserArgumentResolver(){
        LOGGER.info("***************************loginUserArgumentResolver初始化***************************");
        return new LoginUserArgumentResolver();
    }

    @Bean
    public UserInfoUtil userInfoUtil(@Autowired(required = false) DataSource dataSource){
        if(dataSource != null) {
            try {
                dataSource.getConnection();
                LOGGER.info("***************************数据库连接池初始化***************************");
            } catch (Exception ex) {
                ex.printStackTrace();
                LOGGER.info("***************************数据库连接池初始化失败***************************");
            }
        }
        LOGGER.info("***************************userInfoUtil初始化***************************");
        return new UserInfoUtil();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserArgumentResolver());
        super.addArgumentResolvers(argumentResolvers);
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public RedisTemplate redisTemplateInit(){
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        LOGGER.info("***************************redisTemplateInit初始化***************************");
        return redisTemplate;
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(2000);
        executor.setQueueCapacity(2000);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }


    @Autowired(required = false)
    private LogService logService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ParameterInterceptor(logService,webProperties())).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Bean
    public FilterRegistrationBean parameterFilter(){
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ParameterFilter());
        registration.addUrlPatterns("/*");
        registration.setName("parameterFilter");
        registration.setOrder(20000);
        LOGGER.info("***************************parameterFilter初始化***************************");
        return registration;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        LOGGER.info("***************************passwordEncoder初始化***************************");
        return new BCryptPasswordEncoder();
    }

    @Bean
    @LoadBalanced
    public RestTemplate commonRestTemplate(){
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setReadTimeout(20 * 1000);
        return new RestTemplate(httpRequestFactory);
    }

}
