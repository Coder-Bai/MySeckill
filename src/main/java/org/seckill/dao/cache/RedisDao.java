package org.seckill.dao.cache;

import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {
    // ��־����
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JedisPool jedisPool;
    
    public RedisDao(String ip, int port) {
        jedisPool = new JedisPool(ip, port);
    }
    
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
    
    public Seckill getSeckill(long seckillId) {
        // redis�����߼�
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckillId;
                // ��û��ʵ���ڲ����л�����
                //get->byte[ ] ->�����л� -> Object(Seckill)
                // �����Զ������л�
                // protostuff: pojo.
                byte[ ] bytes = jedis.get(key.getBytes());
                // �����ػ�ȡ��
                if (bytes != null) {
                    Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                    // seckill�������л�
                    return seckill;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
    
    public String putSeckill(Seckill seckill) {
    	//set Object(Seckill) ->���л� -> byte[ ]
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                byte[ ] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                // ��ʱ����
                int timeout = 60*60;// 1Сʱ
                String result = jedis.setex(key.getBytes(), timeout, bytes);

                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
        	  logger.error(e.getMessage(), e);
        }

        return null;
    }
}
