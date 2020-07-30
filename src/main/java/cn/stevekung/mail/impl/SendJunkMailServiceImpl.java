package cn.stevekung.mail.impl;

import cn.stevekung.mail.SendJunkMailService;
import cn.stevekung.pojo.AyUser;
import cn.stevekung.service.AyUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
public class SendJunkMailServiceImpl implements SendJunkMailService {

    public static final Logger log = LogManager.getLogger(SendJunkMailServiceImpl.class);

    @Autowired
    JavaMailSender mailSender; // 邮件发送

    @Resource
    private AyUserService ayUserService;

    @Value("${spring.mail.username}") // application.properties中的对应属性设置取值
    private String from;

    @Override
    public boolean sendJunkMail(List<AyUser> ayUserList) {
        try{
            if(ayUserList == null || ayUserList.size() <= 0 )
                return Boolean.FALSE;
            for(AyUser ayUser: ayUserList){
                MimeMessage mimeMessage = this.mailSender.createMimeMessage();
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                //邮件发送方
                message.setFrom(from);
                //邮件主题
                message.setSubject("地瓜今日特卖");
                //邮件接收方
                message.setTo("stevekung98@163.com");
                //邮件内容
                message.setText(ayUser.getName() +" ,你知道么？厦门地瓜今日特卖，一斤只要9元");
                //发送邮件
                this.mailSender.send(mimeMessage);
            }
        }catch(Exception ex){
            log.error("sendJunkMail error and ayUser=%s", ayUserList, ex);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
