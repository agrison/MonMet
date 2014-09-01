package me.grison.monmet.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 * Redis configuration class.
 */
@Configuration
@PropertySource(value = "classpath:conf/${user.name}/redis.properties")
public class RedisConfig {
    static final Logger LOG = LoggerFactory.getLogger(RedisConfig.class);
    @Value("${redis.host-name}")
    String hostName;
    @Value("${redis.port}") int port;
    @Value("${redis.default-expiration}") long defaultExpiration; // in minutes
    @Autowired
    Environment env;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestWhileIdle(true);
        //poolConfig.setMaxActive(50);
        return new JedisPool(poolConfig, hostName, port, 30 /* no timeout */);
    }
}