package com.pc.active.mq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;

/**
 * 消息的消费者
 * @author Administrator
 */
@Component
public class QueueConsumer {
    //使用JmsListener配置消费者监听的队列，其中Message是接收到的消息
    @JmsListener(destination = "Armyman.queue")
    public void receiveQueue(Message message) {
        try {
            MapMessage mapMessage = (MapMessage) message;
            String info = mapMessage.getString("info");
            System.out.println(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
