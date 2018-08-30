package com.java.common.redis.configure;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@Lazy
public class RedisConfigure {
    private JedisPoolConfig jedisPoolConfig;
    private List<JedisShardInfo> hostList;

    //redis连接信息
    @Value("${redis.host:}")
    private String host;
    @Value("${redis.port:}")
    private String port;
    @Value("${redis.password:}")
    private String password;

    //redis配置信息
    @Value("${redis.maxTotal:20}")
    private int maxTotal;//最大连接数, 默认8个
    @Value("${redis.maxIdle:5}")
    private int maxIdle;//最大空闲连接数, 默认8个
    @Value("${redis.numTestsPerEvictionRun:3}")
    private int numTestsPerEvictionRun;
    @Value("${redis.timeBetweenEvictionRunsMillis:-1}")
    private long timeBetweenEvictionRunsMillis;
    @Value("${redis.minEvictableIdleTimeMillis:1800000}")
    private long minEvictableIdleTimeMillis;
    @Value("${redis.softMinEvictableIdleTimeMillis:1800000}")
    private long softMinEvictableIdleTimeMillis;
    @Value("${redis.testOnBorrow:false}")
    private boolean testOnBorrow;
    @Value("${redis.testOnReturn:false}")
    private boolean testOnReturn;
    @Value("${redis.testWhileIdle:true}")
    private boolean testWhileIdle;
    @Value("${redis.jmxEnabled:true}")
    private boolean jmxEnabled;
    @Value("${redis.jmxNamePrefix:pool}")
    private String jmxNamePrefix;
    @Value("${redis.maxWaitMillis:-1}")
    private long maxWaitMillis;
    @Value("${redis.blockWhenExhausted:false}")
    private boolean blockWhenExhausted;


    private void initJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        config.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        config.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        config.setTestWhileIdle(testWhileIdle);
        config.setJmxEnabled(jmxEnabled);
        config.setJmxNamePrefix(jmxNamePrefix);
        config.setMaxWaitMillis(maxWaitMillis);
        config.setBlockWhenExhausted(blockWhenExhausted);
        this.jedisPoolConfig = config;
    }

    @Bean
    public ShardedJedisPool initShardedJedisPool() {
        return new ShardedJedisPool(jedisPoolConfig, hostList);
    }

    private void initJedisShardInfo() {
        List<JedisShardInfo> list = new ArrayList<>();
        String[] hosts = host.split(",");
        String[] ports = port.split(",");
        String[] passwords = password.split(",");
        if (hosts != null && hosts.length > 0) {
            for (int i = 0; i < hosts.length; i++) {
                JedisShardInfo jedisShardInfo = new JedisShardInfo(hosts[i], ports[i]);
                if (StringUtils.isNotBlank(passwords[i])) jedisShardInfo.setPassword(passwords[i]);
                list.add(jedisShardInfo);
            }
        }
        this.hostList = list;
    }


    @PostConstruct
    public void init() {
        this.initJedisShardInfo();
        this.initJedisPoolConfig();
    }
}
