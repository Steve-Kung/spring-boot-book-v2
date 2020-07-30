package cn.stevekung.listener;

import cn.stevekung.pojo.AyUser;
import cn.stevekung.service.AyUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@WebListener
// ServletContextListener 对应整个application
public class AyUserListener implements ServletContextListener{

    @Resource
    ObjectMapper objectMapper;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    AyUserService ayUserService;

    private static final String ALL_USER = "ALL_USER_LIST";

//    private static final Logger log = LoggerFactory.getLogger(AyUserListener.class);

    Logger log = LogManager.getLogger(this.getClass());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 可以在上下文 contextInitialized 方法中查询所有的用户，
        // 利用缓存技术把用户数据存放在缓存中 Redis缓存
        log.info("ServletContext上下文初始化");
        //查询数据库所有的用户
        List<AyUser> ayUserList  = ayUserService.findAll();
        //清除缓存中的用户数据
        stringRedisTemplate.delete(ALL_USER);
        //将数据存放到redis缓存中
        try {
            for (AyUser ayUser : ayUserList) {
                stringRedisTemplate.opsForList().rightPush(ALL_USER, objectMapper.writeValueAsString(ayUser));
            }
            List<String> redisStrings = stringRedisTemplate.opsForList().range(ALL_USER, 0, -1);
            log.info("缓存中目前的用户数有：" + redisStrings.size() + " 人");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        log.info("ServletContext上下文销毁");
    }
}
