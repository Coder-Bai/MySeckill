package org.seckill.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring的配置文件
@ContextConfiguration({
	 "classpath:spring/spring-dao.xml",
     "classpath:spring/spring-service.xml"})
public class SeckillServiceTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// 注入Service实现类依赖
    @Autowired
    private SeckillService seckillService;

	@Test
	public void testGetSeckillList() {
		List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}", list);
        //Closing non transactional SqlSession 
	}

	@Test
	public void testGetById() {
        long seckillId = 1000;
        Seckill seckill = seckillService.getById(seckillId);
        logger.info("seckill={}", seckill);
	}

	@Test
	public void testExportSeckillUrl() {
	     long seckillId = 1000;
	     Exposer exposer = seckillService.exportSeckillUrl(seckillId);
	     logger.info("exposer={}", exposer);
	     //Exposer [exposed=true, md5=9156b63c521b71da1b8a62333caa976b, seckillId=1000, now=0, start=0, end=0]
	}

//	@Test
//	public void testExecuteSeckill() {
//		   long seckillId = 1000;
//        long userPhone = 13476191878L;
//        String md5 = "9156b63c521b71da1b8a62333caa976b";
//        try {
//            SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
//            logger.info("result={}", execution);
//		} catch (RepeatKillException e) {
//            logger.error(e.getMessage());
//        } catch (SeckillCloseException e) {
//            logger.error(e.getMessage());
//        }
//
//        //result=SeckillExecution [
//        //seckillId=1000, 
//        //state=1, 
//        //stateInfo=秒杀成功, successKilled=SuccessKilled{state=0, createTime=Fri Feb 28 15:27:34 CST 2020, seckillId=1000, userPhone=13476191878}]
//	}
	 // 集成测试代码完整逻辑，注意可重复执行
    @Test
    public void testSeckillLogic() throws Exception {
        long seckillId = 1000;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            logger.info("exposer={}", exposer);
            long userPhone = 13476191568L;
            String md5 = exposer.getMd5();

            try {
                SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
                logger.info("result={}", execution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        } else {
            // 秒杀未开启
            logger.warn("exposer={}", exposer);
        }
    }
    
    @Test
    public void executeSeckillProcedure() {
        long seckillId = 1001;
        long phone = 13680115101L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if (exposer.isExposed()) {
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
            logger.info(execution.getStateInfo());
        }
    }
}
