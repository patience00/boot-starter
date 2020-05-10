package com.linchtech.boot.starter.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 107
 * @date 2020-05-10 21:02
 * @description
 **/
@Component
@Slf4j
public class RedisHelper {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * redis 分布式锁
     * 加锁
     *
     * @param key    锁的key
     * @param expire 过期时间 ms,默认一个锁最长持有1s
     * @param waitInterval 每次自旋等待时长 ms ,默认一个锁最长持有1s
     * @return 持有锁返回true
     */
    public boolean lock(String key,
                        Long expire,
                        Long waitInterval) {
        log.debug("key:{},redis锁:{}", key, redisTemplate.opsForValue().get(key));
        if (redisTemplate.opsForValue().setIfAbsent(key, expire)) {
            log.debug("获取锁成功:{}", key);
            return true;
        }
        Long currentValue = (Long) redisTemplate.opsForValue().get(key);
        log.debug("key:{},currentValue:{}", key, currentValue);
        // 锁已过期
        if (currentValue != null && currentValue < System.currentTimeMillis()) {
            // 获取上个锁的时间
            Long oldValue = (Long) redisTemplate.opsForValue().getAndSet(key, expire);
            log.debug("key:{},oldValue:{}", key, oldValue);
            if (oldValue != null && oldValue.equals(currentValue)) {
                log.debug("锁超时:{}", key);
                return true;
            }
        }

        try {
            log.warn("key:{},同时有多个定位,锁等待中", key);
            Thread.sleep(waitInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 未获取到锁，自旋
        return lock(key, expire, waitInterval);
    }

    /**
     * redis 分布式锁 解锁
     *
     * @param key
     * @param expire
     */
    public void unlock(String key,
                       Long expire) {
        try {
            Long currentValue = (Long) redisTemplate.opsForValue().get(key);
            if (currentValue != null && currentValue.equals(expire)) {
                log.debug("删除操作锁:{}", key);
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("key:{},解锁异常", key);
        }
    }
}
