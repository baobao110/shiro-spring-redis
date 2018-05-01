package com.Util;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

public class JedisUtil {

    @Autowired
    private JedisPool jedisPool;

    //获取Jedis对象
    private Jedis get(){
        return jedisPool.getResource();
    }
    //jedis存储
    public void set(byte[]key,byte[]value){
        Jedis jedis =get();
        try {
            jedis.set(key, value);
        }
        finally {
            jedis.close();
        }
    }
    //获取value值
    public byte[] get(byte[]key){
        Jedis jedis =get();
        try {
            return jedis.get(key);
        }
        finally {
            jedis.close();
        }
    }

    //设置超时时间
    public void expire(byte[]key,int time){
        Jedis jedis =get();
        try {
            jedis.expire(key, time);
        }
        finally {
            jedis.close();
        }
    }

    public  void delete(byte[]key){
        Jedis jedis =get();
        try {
            jedis.del(key);
        }
        finally {
            jedis.close();
        }
    }

    public Set<byte[]>keys(String keys){
        Jedis jedis =get();
        try {
            return jedis.keys((keys+"*").getBytes());
        }
        finally {
            jedis.close();
        }
    }
}
