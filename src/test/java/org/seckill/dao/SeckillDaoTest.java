package org.seckill.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * ����Spring��Junit����,junit����ʱ����springIOC���� spring-test,junit
 */

@RunWith(SpringJUnit4ClassRunner.class)
// ����junit spring�������ļ�
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    // ע��Daoʵ��������
    @Resource
    private SeckillDao seckillDao;
    
	@Test
	public void testQueryById() throws Exception{
        long seckillId = 1000;
        Seckill seckill = seckillDao.queryById(seckillId);
        System.out.println(seckill.getName());
        System.out.println(seckill);
        /*
         * 8000Ԫ��ɱiphonex
			Seckill [seckillId=1000, 
			name=8000Ԫ��ɱiphonex, 
			number=100, 
			createTime=Thu Feb 27 14:04:28 CST 2020, 
			startTime=Fri Mar 15 00:00:00 CST 2019, 
			endTime=Sun Mar 17 00:00:00 CST 2019]
         */
	}
	
    @Test
    public void testQueryAll() throws Exception{
	 /*
	  ����    Caused by: org.apache.ibatis.binding.BindingException:
	      Parameter 'offset' not found.
	       Available parameters are [0, 1, param1, param2]
	  ԭ��     List<Seckill> queryAll(int offset,  int limit);
	         javaû�б����βεļ�¼��queryAll(int offset,  int limit) -> queryAll(arg0,arg1)
	   �޸ģ�    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
	*/
        List<Seckill> seckills = seckillDao.queryAll(0, 100);
        for (Seckill seckill : seckills) {
            System.out.println(seckill);
        }
        /*
         * Seckill [seckillId=1000, name=8000Ԫ��ɱiphonex, number=100, createTime=Thu Feb 27 14:04:28 CST 2020, startTime=Fri Mar 15 00:00:00 CST 2019, endTime=Sun Mar 17 00:00:00 CST 2019]
			Seckill [seckillId=1001, name=3500Ԫ��ɱipad, number=200, createTime=Thu Feb 27 14:04:28 CST 2020, startTime=Fri Mar 15 00:00:00 CST 2019, endTime=Mon Mar 18 00:00:00 CST 2019]
			Seckill [seckillId=1002, name=18000Ԫ��ɱmac book pro, number=300, createTime=Thu Feb 27 14:04:28 CST 2020, startTime=Thu Mar 28 00:00:00 CST 2019, endTime=Fri Mar 29 00:00:00 CST 2019]
			Seckill [seckillId=1003, name=15000Ԫ��ɱiMac, number=400, createTime=Thu Feb 27 14:04:28 CST 2020, startTime=Fri Mar 15 00:00:00 CST 2019, endTime=Fri Mar 29 00:00:00 CST 2019]
         */
    }

    @Test
    public void testReduceNumber() throws Exception{
        long seckillId = 1000;
        Date date = new Date();
        int updateCount = seckillDao.reduceNumber(seckillId, date);
        System.out.println("updateCount="+updateCount);
        /*
         *UPDATE seckill SET number = number-1 WHERE seckill_id=? AND start_time <= ? AND end_time >= ? AND number > 0; 
		  Parameters: 1000(Long), 2020-02-27 21:00:54.402(Timestamp), 2020-02-27 21:00:54.402(Timestamp)
		  Updates: 0
         */
    }
}
