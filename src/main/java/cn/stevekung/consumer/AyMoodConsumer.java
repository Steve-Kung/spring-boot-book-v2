package cn.stevekung.consumer;

import cn.stevekung.pojo.AyMood;
import cn.stevekung.producer.AyMoodProducer;
import cn.stevekung.service.AyMoodService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AyMoodConsumer {
    Logger log = LogManager.getLogger(AyMoodConsumer.class);

    @JmsListener(destination = "ay.queue")
    public void receiveQueue(String text) {
        log.info("接收者 消费 这个字符串");
        log.info("用户发表说说【" + text + "】成功");
    }

    @Resource
    private AyMoodService ayMoodService;

    @JmsListener(destination = "ay.queue.asyn.save")
    public void receiveQueue(AyMood ayMood){
        log.info("接收者 消费 ayMood");
        log.info("异步调用真正的保存服务的方法");
        ayMoodService.save(ayMood);
    }
}
