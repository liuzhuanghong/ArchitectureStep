package com.steven.activemq.base;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

/**
 * 验证通过消息生产者发出消息
 * Created by liuzhuanghong on 17/4/9.
 */
public class BaseJMSSenderTest {


    @Test
    public void testJMSSender() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("base-msg-queue");
            MessageProducer producer = session.createProducer(destination);
            for (int i = 0; i < 3; i++) {
                TextMessage message = session.createTextMessage("message--" + i);
                Thread.sleep(1000);
                //通过消息生产者发出消息
                producer.send(message);
            }
            session.commit();
            session.close();
            connection.close();
            System.out.println("消息发送完成");
        } catch (Exception ex) {
            System.out.println("执行过程出现异常");
            ex.printStackTrace();
        }
    }

    @Test
    public void testJMSReceiver() {
        try {
            ConnectionFactory cf = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
            Connection connection = cf.createConnection();
            connection.start();

            final Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("base-msg-queue");
            MessageConsumer consumer = session.createConsumer(destination);
            int i = 0;
            while (i < 3) {
                i++;
                TextMessage message = (TextMessage) consumer.receive();
                session.commit();
                System.out.println("收到消息:" + message.getText());
            }
            session.close();
            connection.close();

            System.out.println("接收消息完成");
        } catch (Exception ex) {
            System.out.println("接收出现异常");
            ex.printStackTrace();
        }
    }
}
