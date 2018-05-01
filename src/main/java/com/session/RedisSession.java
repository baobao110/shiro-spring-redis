package com.session;

import com.Util.JedisUtil;
import com.sun.xml.internal.ws.developer.Serialization;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RedisSession extends AbstractSessionDAO{

    @Autowired
    private JedisUtil jedisUtil;

    private byte[] getKey(String key){
        return ("id="+key).getBytes();
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable serializable=generateSessionId(session);
        //这里生成sessionId要和ession进行捆绑
        assignSessionId(session,serializable);
        byte[]keys=getKey(serializable.toString());
        //将value值序列化
        byte[]values= SerializationUtils.serialize(session);
        //存储数据
        jedisUtil.set(keys,values);
        //设置超时时间
        jedisUtil.expire(keys,1000);
       return serializable;
    }

    @Override
    protected Session doReadSession(Serializable serializable) {
        System.out.println("session");
        if(null==serializable){
            return null;
        }
        //这里的serializable=session.getId().toString()
        byte[]keys=getKey(serializable.toString());
        byte[]values=jedisUtil.get(keys);
        //value值反序列化
        return (Session)SerializationUtils.deserialize(values);
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
            if(null!=session&&null!=session.getId()){
                Serializable serializable=generateSessionId(session);
                byte[]keys=getKey(session.getId().toString());
                byte[]values= SerializationUtils.serialize(session);
                //修改数据
                jedisUtil.set(keys,values);
                //设置超时时间
                jedisUtil.expire(keys,1000);
            }
   }

    @Override
    public void delete(Session session) {
        if(null!=session&&null!=session.getId()){
            Serializable serializable=generateSessionId(session);
            byte[]keys=getKey(session.getId().toString());
            //删除数据
            jedisUtil.delete(keys);
        }
    }

    @Override
    public Collection<Session> getActiveSessions() {
      Set<byte[]>keys= jedisUtil.keys("id=");
      if(CollectionUtils.isEmpty(keys)){
          return null;
      }
      Collection<Session> session=new HashSet<>();
      for(byte[] i:keys){
       Session session1= (Session)SerializationUtils.deserialize(jedisUtil.get(i));
       session.add(session1);
      }
      return session;
    }
}
