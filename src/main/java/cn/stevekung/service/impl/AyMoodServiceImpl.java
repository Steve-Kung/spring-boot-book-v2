package cn.stevekung.service.impl;

import cn.stevekung.pojo.AyMood;
import cn.stevekung.producer.AyMoodProducer;
import cn.stevekung.repository.AyMoodRepository;
import cn.stevekung.service.AyMoodService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

@Service
public class AyMoodServiceImpl implements AyMoodService {
    @Autowired
    AyMoodRepository ayMoodRepository;


    @Override
    public AyMood save(AyMood ayMood) {
        return ayMoodRepository.save(ayMood);
    }


    @Autowired
    AyMoodProducer ayMoodProducer;

    // 队列
    private static Destination destination= new ActiveMQQueue("ay.queue.asyn.save");


    @Override
    public String asynSave(AyMood ayMood) {
        //往队列ay.queue.asyn.save推送消息，消息内容为说说实体
        ayMoodProducer.sendMessage(destination, ayMood);
        return "success";
    }
}
