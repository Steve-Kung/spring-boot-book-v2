package cn.stevekung.producer;

import cn.stevekung.pojo.AyMood;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.Destination;

@Service
public class AyMoodProducer {
    Logger log = LogManager.getLogger(AyMoodProducer.class);
    @Resource // 发消息的工具类
    private JmsMessagingTemplate jmsMessagingTemplate;

    // destination 发送到的队列
    // message 待发送的消息
    public void sendMessage(Destination destination, final String message) {
        log.info("生产者发送消息");
        jmsMessagingTemplate.convertAndSend(destination, message);
    }


    public void sendMessage(Destination destination, final AyMood ayMood) {
        log.info("生产者发送ayMood");
        jmsMessagingTemplate.convertAndSend(destination, ayMood);
    }
}
