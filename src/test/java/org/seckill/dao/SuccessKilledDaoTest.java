package org.seckill.dao;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;
    
	@Test
	public void testInsertSuccessKilled() throws Exception{
		/*
		 * 第一次：insertCount=1
		 * 第二次：insertCount=0
		 */
		long seckillId = 1001L;
        long userPhone = 13476191897L;
        int insertCount =successKilledDao.insertSuccessKilled(seckillId, userPhone);
        System.out.println("insertCount=" + insertCount);
	}

	@Test
	public void testQueryByIdWithSeckill() throws Exception{

        long seckillId = 1001L;
        long userPhone = 13476191877L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
        /*
         * SuccessKilled{state=-1, createTime=Fri Feb 28 09:44:22 CST 2020, seckillId=1000, userPhone=13476191877}
			Seckill [seckillId=1000, name=8000元秒杀iphonex, number=100, createTime=Thu Feb 27 14:04:28 CST 2020, startTime=Fri Mar 15 00:00:00 CST 2019, endTime=Sun Mar 17 00:00:00 CST 2019]
         */
	}

}
