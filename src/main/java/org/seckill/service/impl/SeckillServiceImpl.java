package org.seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

@Service
public class SeckillServiceImpl implements SeckillService {
	// ��־����
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	// ����һ�������ַ���(��ɱ�ӿ�)��salt��Ϊ�˱����û��³����ǵ�md5ֵ��ֵ�������Խ����Խ��
	private final String salt = "akseh295sdssd5253624^*&swd";
	 
	// ע��Service����
    @Autowired
	private SeckillDao seckillDao;

    @Autowired
	private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;
    
	@Override
	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 4);
	}

	@Override
	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	private String getMD5(long seckillId) {
		String base = seckillId + "/" + salt;
		// spring�Ĺ�����
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		//�Ż��㣺�����Ż� ��ʱ�Ļ�����ά��һ����  ���������ݿ������
        // 1.����redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if (seckill == null) {
            // 2.�������ݿ�
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {// ˵���鲻�������ɱ��Ʒ�ļ�¼
                return new Exposer(false, seckillId);
            } else {
                // 3.����redis
                redisDao.putSeckill(seckill);
            }
        }
//		Seckill seckill = seckillDao.queryById(seckillId);
//		if (seckill == null) {
//			return new Exposer(false, seckillId);
//		}
		// ������ɱδ����
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		// ϵͳ��ǰʱ��
		Date nowTime = new Date();
		if (startTime.getTime() > nowTime.getTime() || endTime.getTime() < nowTime.getTime()) {
			return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
		}
		// ��ɱ������������ɱ��Ʒ��id���ø��ӿڼ��ܵ�md5
		// ת���ض��ַ��Ĺ��̣�������
		String md5 = getMD5(seckillId);
		return new Exposer(true, md5, seckillId);
	}

	@Override
	@Transactional
    /*
     * ʹ��ע��������񷽷����ŵ�:
     * 1.�����ŶӴ��һ��Լ������ȷ��ע���񷽷��ı�̷��
     * 2.��֤���񷽷���ִ��ʱ�価���̣ܶ���Ҫ���������������RPC/HTTP������߰��뵽���񷽷��ⲿ
     * 3.�������еķ�������Ҫ������ֻ��һ���޸Ĳ�����ֻ��������Ҫ�������
     */
	public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
			throws SeckillException, RepeatKillException, SeckillCloseException {
		if (md5 == null || !md5.equals(getMD5(seckillId))) {
			throw new SeckillException("seckill data rewrite");// ��ɱ���ݱ���д��
		}
		// ִ����ɱ�߼�:�����+���ӹ�����ϸ
		Date nowTime = new Date();
		
		try {
			//��¼������Ϊ
			int insertCount=successKilledDao.insertSuccessKilled(seckillId, userPhone);
			//Ψһ��seckillId,userPhone
			 if (insertCount <= 0) {
	                //�ظ���ɱ
	                throw new RepeatKillException("seckill repeated");
			}else {
				//����棬�ȵ㾺��
				int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
				if (updateCount <= 0) {
					// û�и��¿���¼��˵����ɱ���� rollback
					throw new SeckillCloseException("seckill is closed");
		         } else {
		        	 //��ɱ�ɹ�
		        	 SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
	                 return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS ,successKilled);
		         }
			}
		} catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
		       logger.error(e.getMessage(), e);
	            // ���������쳣ת��Ϊ�������쳣
	            throw new SeckillException("seckill inner error :" + e.getMessage());
		}
	}

	 @Override
	    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
	        if (md5 == null || !md5.equals(getMD5(seckillId))) {
	            return new SeckillExecution(seckillId, SeckillStatEnum.DATE_REWRITE);
	        }
	        Date killTime = new Date();
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("seckillId", seckillId);
	        map.put("phone", userPhone);
	        map.put("killTime", killTime);
	        map.put("result", null);
	        // ִ�д������,result������
	        try {
	            seckillDao.killByProcedure(map);
	            // ��ȡresult
	            int result = MapUtils.getInteger(map, "result", -2);
	            if (result == 1) {
	                SuccessKilled sk = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
	                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, sk);
	            } else {
	                return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
	            }
	        } catch (Exception e) {
	            logger.error(e.getMessage(), e);
	            return new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);

	        }
	    }
}
