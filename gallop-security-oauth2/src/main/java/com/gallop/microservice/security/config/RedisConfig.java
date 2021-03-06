package com.gallop.microservice.security.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Slf4j
@Configuration
@EnableAutoConfiguration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
    public static final String AUTH_CODE_TOPIC = "authCode-queue";

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {

        //??????Redis??????????????????RedisTemplate??????
        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(lettuceConnectionFactory);
        //??????????????????RedisTemplate???Value??????????????????JdkSerializationRedisSerializer?????????Jackson2JsonRedisSerializer
        //???????????????????????????????????????????????????????????????????????????????????????????????????

        /*Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);*/

        //template.setKeySerializer(keyPrefixRedisSerializer);
        //template.setHashKeySerializer(keyPrefixRedisSerializer);
        //template.setValueSerializer(jackson2JsonRedisSerializer);
        RedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);//key?????????
        template.setValueSerializer(valueSerializer());//value?????????
        template.setHashKeySerializer(stringSerializer);//Hash key?????????
        template.setHashValueSerializer(valueSerializer());//Hash value?????????

        template.afterPropertiesSet();
        return template;
    }

    /**
     * ?????????JSON?????????
     * @return
     */
    private RedisSerializer<Object> valueSerializer() {
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        //??????????????????redis??????json ?????????set ?????????????????????????????????Unrecognized field ??????????????????SysUser??????admin?????????????????????????????????
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //LocalDate???LocalDateTime????????????
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addDeserializer(LocalDate.class,
                new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addSerializer(LocalDate.class,
                new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(timeModule);

        jackson2JsonRedisSerializer.setObjectMapper(om);
        return jackson2JsonRedisSerializer;
    }

    /**
     * date @2021-06-02
     * Description: ??????????????????
     * return:
     **/
    @Bean(value = "lettuceConnectionFactory")
    public LettuceConnectionFactory getConnectionFactory(GenericObjectPoolConfig genericPoolConfig) {
        //????????????
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisProperties.getHost());
        configuration.setPort(redisProperties.getPort());
        configuration.setDatabase(redisProperties.getDatabase());
        configuration.setPassword(RedisPassword.of(redisProperties.getPassword()));

        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration, getPoolConfig(genericPoolConfig));
        //factory.setShareNativeConnection(false);//??????????????????????????????????????????????????????????????????true???false???????????????????????????????????????
        return factory;
    }

    @Bean
    @Scope(value = "prototype")
    public GenericObjectPoolConfig genericPoolConfig() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(redisProperties.getLettuce().getPool().getMaxActive());
        config.setMaxWaitMillis(redisProperties.getLettuce().getPool().getMaxWait().toMillis());
        config.setMaxIdle(redisProperties.getLettuce().getPool().getMaxIdle());
        config.setMinIdle(redisProperties.getLettuce().getPool().getMinIdle());
        return config;
    }

    /**
     * ?????????????????????
     **/
    @Bean
    public LettucePoolingClientConfiguration getPoolConfig(GenericObjectPoolConfig genericPoolConfig) {

        LettucePoolingClientConfiguration pool = LettucePoolingClientConfiguration.builder()
                .poolConfig(genericPoolConfig)
                .commandTimeout(redisProperties.getTimeout())
                .shutdownTimeout(redisProperties.getLettuce().getShutdownTimeout())
                .build();

        return pool;
    }

    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    @Override
    public CacheErrorHandler errorHandler() {
        // ??????????????????Redis??????????????????????????????????????????????????????
        log.info("????????? -> [{}]", "Redis CacheErrorHandler");
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                log.error("Redis occur handleCacheGetError???key -> [{}]", key, e);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                log.error("Redis occur handleCachePutError???key -> [{}]???value -> [{}]", key, value, e);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key)    {
                log.error("Redis occur handleCacheEvictError???key -> [{}]", key, e);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                log.error("Redis occur handleCacheClearError???", e);
            }
        };
        return cacheErrorHandler;
    }

    @Bean(value = "cacheManager")
    public CacheManager cacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisSerializer stringSerializer = new StringRedisSerializer();

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig() // ?????????????????????????????????config??????????????????????????????????????????
                // ??????key???String
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(stringSerializer))
                // ??????value ????????????Json???Object
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                // ?????????null
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(120));     // ????????????????????????????????????????????????Duration??????


        // ????????????????????????????????????set??????
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("third-company");
        cacheNames.add("corp-access-token");

        // ??????????????????????????????????????????,??????????????????????????????
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put("third-company", config.entryTtl(Duration.ofHours(8)));
        configMap.put("corp-access-token", config.entryTtl(Duration.ofMinutes(115)));//115??????

        RedisCacheManager cacheManager = RedisCacheManager.builder(lettuceConnectionFactory)     // ?????????????????????????????????????????????cacheManager
                .initialCacheNames(cacheNames)  // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                .withInitialCacheConfigurations(configMap)
                .build();
        return cacheManager;
    }

   /*@Bean
   public CacheManager cacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
       RedisCacheConfiguration config =
               RedisCacheConfiguration.defaultCacheConfig()
                       //????????????????????????(1??????)
                       .entryTtl(Duration.ofHours(12))
                       //?????????null??????????????????null?????????????????????
                       .disableCachingNullValues()
                       //???json?????????????????????
                       .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()));


       return RedisCacheManager.builder(lettuceConnectionFactory).cacheDefaults(config).build();
   }*/

}
