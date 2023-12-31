package com.jef.mq.activeMq;

import com.jef.constant.BasicConstant;
import com.jef.entity.User;
import com.jef.mq.MQBasicConstant;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

/**
 * mq消息发送处理
 * @author Jef
 * @date 2019/3/29
 */
public class SenderUtil {
    private static final int SEND_NUMBER = 5;
    public static void sendMessage() {
        ConnectionFactory connectionFactory; // ConnectionFactory--连接工厂，JMS用它创建连接
        // Provider 的连接
        Connection connection = null; // Connection ：JMS 客户端到JMS
        Session session; // Session： 一个发送或接收消息的线程
        Destination destination; // Destination ：消息的目的地;消息发送给谁.
        MessageProducer producer; // MessageProducer：消息发送者
        // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD, MQBasicConstant.MQ_URL);
        try { // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值FirstQueue是一个服务器的queue，须在在ActiveMq的console配置
            destination = session.createQueue(MQBasicConstant.MQ_QUEUE_NAME);
            // 得到消息生成者【发送者】
            producer = session.createProducer(destination);
            // 设置不持久化，此处学习，实际根据项目决定
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // 构造消息，此处写死，项目就是参数，或者方法获取
            sendMessage(session, producer);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection) {
                    connection.close();
                }
            } catch (Throwable ignore) {
            }
        }
    }

    public static void sendMessage(Session session, MessageProducer producer) throws Exception {
        for (int i = 1; i <= SEND_NUMBER; i++) {
            User user = new User();
            user.setName(BasicConstant.USER_NAME + i);
            ObjectMessage message = session.createObjectMessage(user);
            // 发送消息到目的地方
            System.out.println("发送消息：" + "ActiveMq 发送的消息，用户名=" + user.getName());
            producer.send(message);
        }
    }

}