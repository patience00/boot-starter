package com.linchtech.boot.starter.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author 107
 * @date 2020-05-10 21:02
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
     * @param key          锁的key
     * @param expireAt     过期时间戳(格林威治时间1970年01月01日00时00分00秒起至当下的总毫秒数),默认一个锁最长持有1s
     * @param waitInterval 每次自旋等待时长 ms ,默认一个锁最长持有1s
     * @return boolean 持有锁返回true
     */
    public boolean lock(String key,
                        Long expireAt,
                        Long waitInterval) {
        log.debug("key:{},redis锁:{}", key, redisTemplate.opsForValue().get(key));
        if (redisTemplate.opsForValue().setIfAbsent(key, expireAt)) {
            log.debug("获取锁成功:{}", key);
            return true;
        }
        Object currentObject = redisTemplate.opsForValue().get(key);
        Long currentValue = getLong(currentObject);
        log.debug("key:{},currentValue:{}", key, currentValue);
        // 锁已过期
        if (currentValue != null && currentValue < System.currentTimeMillis()) {
            Object getValue = redisTemplate.opsForValue().getAndSet(key, expireAt);
            // 获取上个锁的时间
            Long oldValue = getLong(getValue);
            log.debug("key:{},oldValue:{}", key, oldValue);
            if (oldValue != null && oldValue.equals(currentValue)) {
                log.debug("锁超时:{}", key);
                return true;
            }
        }

        try {
            log.warn("key:{},锁等待中", key);
            Thread.sleep(waitInterval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 未获取到锁，自旋
        return lock(key, expireAt, waitInterval);
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
            Object currentObject = redisTemplate.opsForValue().get(key);
            Long currentValue = getLong(currentObject);
            if (currentValue != null && currentValue.equals(expire)) {
                log.debug("删除操作锁:{}", key);
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            log.error("key:{},解锁异常", key);
        }
    }

    /**
     * 屏蔽redis中的数据类型,转换为long
     * @param currentObject
     * @return
     */
    private Long getLong(Object currentObject) {
        Long currentValue = null;
        if (currentObject instanceof String) {
            currentValue = Long.valueOf((String) currentObject);
        }
        if (currentObject instanceof Integer) {
            currentValue = ((Integer) currentObject).longValue();
        }
        if (currentObject instanceof Long) {
            currentValue = (Long) currentObject;
        }
        return currentValue;
    }
}
