package org.seckill.service;

import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

/**
	 * ҵ��ӿ�:վ��ʹ����(����Ա)�ĽǶ���ƽӿ�
	 * ��������:1.�����������ȣ����������Ҫ�ǳ����
	 * 2.������ҪԽ����Խ��
	 * 3.��������(return����һ��Ҫ�Ѻ�/����return�쳣������������쳣)
	 */
public interface SeckillService {
    /**
     * ��ѯȫ������ɱ��¼
     * @return
     */
    List<Seckill> getSeckillList();

    /**
     * ��ѯ������ɱ��¼
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);
    
    /**
     * ����ɱ����ʱ�����ɱ�ӿڵĵ�ַ���������ϵͳʱ�����ɱʱ��
     * @param seckillId ��ɱ��ƷId
     * @return ���ݶ�Ӧ��״̬���ض�Ӧ��״̬ʵ��
     */
    Exposer exportSeckillUrl(long seckillId);
    
    /**
     * ִ����ɱ�������п���ʧ�ܣ��п��ܳɹ�������Ҫ�׳�����������쳣
     * @param seckillId ��ɱ����ƷID
     * @param userPhone �ֻ�����
     * @param md5       md5����ֵ
     * @return ���ݲ�ͬ�Ľ�����ز�ͬ��ʵ����Ϣ
     */
    //  SeckillExecution executeSeckill(long seckillId, long userPhone, String md5);
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException;
    
    /**
     * ���ô洢������ִ����ɱ����������Ҫ�׳��쳣
     * @param seckillId ��ɱ����ƷID
     * @param userPhone �ֻ�����
     * @param md5       md5����ֵ
     * @return ���ݲ�ͬ�Ľ�����ز�ͬ��ʵ����Ϣ
     */
    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);
}
