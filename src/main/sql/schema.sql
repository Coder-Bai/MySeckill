-- ���ݿ��ʼ���ű�

-- �������ݿ�
CREATE DATABASE seckill;
-- ʹ�����ݿ�
use seckill;
CREATE TABLE seckill(
  `seckill_id` BIGINT NOT NUll AUTO_INCREMENT COMMENT '��Ʒ���ID',
  `name` VARCHAR(120) NOT NULL COMMENT '��Ʒ����',
  `number` int NOT NULL COMMENT '�������',
  `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '����ʱ��',
  `start_time` TIMESTAMP  NOT NULL COMMENT '��ɱ��ʼʱ��',
  `end_time`   TIMESTAMP   NOT NULL COMMENT '��ɱ����ʱ��',
  PRIMARY KEY (seckill_id),
  key idx_start_time(start_time),
  key idx_end_time(end_time),
  key idx_create_time(create_time)
)ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='��ɱ����';

-- ��ʼ������
INSERT into seckill(name,number,start_time,end_time)
VALUES
  ('8000Ԫ��ɱiphonex',100,'2019-03-15 00:00:00','2019-03-17 00:00:00'),
  ('3500Ԫ��ɱipad',200,'2019-03-15 00:00:00','2019-03-18 00:00:00'),
  ('18000Ԫ��ɱmac book pro',300,'2019-03-28 00:00:00','2019-03-29 00:00:00'),
  ('15000Ԫ��ɱiMac',400,'2019-03-15 00:00:00','2019-03-29 00:00:00');
  
  -- ��ɱ�ɹ���ϸ��
-- �û���¼��֤�����Ϣ(��Ϊ�ֻ���)
CREATE TABLE success_killed(
  `seckill_id` BIGINT NOT NULL COMMENT '��ɱ��ƷID',
  `user_phone` BIGINT NOT NULL COMMENT '�û��ֻ���',
  `state` TINYINT NOT NULL DEFAULT -1 COMMENT '״̬��ʶ:-1:��Ч 0:�ɹ� 1:�Ѹ��� 2:�ѷ���',
  `create_time` TIMESTAMP NOT NULL COMMENT '����ʱ��',
  PRIMARY KEY(seckill_id,user_phone),/*��������*/
  KEY idx_create_time(create_time)
)ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='��ɱ�ɹ���ϸ��';

  -- SHOW CREATE TABLE seckill;#��ʾ��Ĵ�����Ϣ


--�������ݿ����̨
mysql -uroot -p

--Ϊʲô��дDDL
--��¼ÿ�����ߵ�DDL�޸�
--����V1.1
ALTER TABLE seckill
DROP INDEX idx_create_time,
ADD index idx_c_s(start_time,create_time);

--����V1.2
--DDL