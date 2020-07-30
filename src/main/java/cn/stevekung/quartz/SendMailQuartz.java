package cn.stevekung.quartz;

import cn.stevekung.mail.SendJunkMailService;
import cn.stevekung.pojo.AyUser;
import cn.stevekung.service.AyUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
@Configurable
@EnableScheduling // 开启对计划任务的支持 与 Scheduled注解在任务方法上配合使用
public class SendMailQuartz {
    private static final Logger log = LogManager.getLogger(SendMailQuartz.class);

    @Resource
    private SendJunkMailService sendJunkMailService;

    @Resource
    private AyUserService ayUserService;

    //每5秒执行一次
    @Scheduled(cron = "*/5 * *  * * * ")
    public void reportCurrentByCron(){
//        List<AyUser> userList = ayUserService.findAll();
//        if (userList == null || userList.size() <= 0) return;
        //发送邮件
//        sendJunkMailService.sendJunkMail(userList);
//        log.info("定时器运行了!!!");

    }
}
