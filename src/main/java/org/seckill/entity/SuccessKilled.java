package org.seckill.entity;

import java.util.Date;

public class SuccessKilled {
	 	private short state; //�û���ɱ��Ʒ״̬

	    private Date createTime;   //�û���ɱ��Ʒʱ��

	    private Long seckillId;   //��ɱ��Ʒid

	    private Long userPhone;  //�û��ֻ���

	    // ���һ,��Ϊһ����Ʒ�ڿ�����кܶ���������Ӧ�Ĺ�����ϸҲ�кܶࡣ
	    private Seckill seckill;

	    public Seckill getSeckill() {
	        return seckill;
	    }

	    public void setSeckill(Seckill seckill) {
	        this.seckill = seckill;
	    }

	    public Long getSeckillId() {
	        return seckillId;
	    }

	    public void setSeckillId(Long seckillId) {
	        this.seckillId = seckillId;
	    }

	    public Long getUserPhone() {
	        return userPhone;
	    }

	    public void setUserPhone(Long userPhone) {
	        this.userPhone = userPhone;
	    }

	    public Short getState() {
	        return state;
	    }

	    public void setState(short state) {
	        this.state = state;
	    }

	    public Date getCreateTime() {
	        return createTime;
	    }

	    public void setCreateTime(Date createTime) {
	        this.createTime = createTime;
	    }

	    @Override
	    public String toString() {
	        return "SuccessKilled{" +
	                "state=" + state +
	                ", createTime=" + createTime +
	                ", seckillId=" + seckillId +
	                ", userPhone=" + userPhone +
	                '}';
	    }
}
