package cn.stevekung.service.impl;

import cn.stevekung.dao.AyUserDao;
import cn.stevekung.error.BusinessException;
import cn.stevekung.pojo.AyUser;
import cn.stevekung.repository.AyUserRepository;
import cn.stevekung.service.AyUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.Future;

@Service
@Transactional
public class AyUserServiceImpl implements AyUserService {

//    private static final Logger log = LoggerFactory.getLogger(AyUserServiceImpl.class);
    Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    AyUserRepository ayUserRepository;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AyUserDao ayUserDao;

    private static final String ALL_USER = "ALL_USER_LIST";

    @Override
    public AyUser findById(String id) {

        AyUser ayUser = new AyUser();
        try {
            //step.1  查询Redis缓存中的所有数据
            List<String> redisStrings = stringRedisTemplate.opsForList().range(ALL_USER, 0, -1);
            List<AyUser> ayUserList = new ArrayList<>();
            for (String redisString : redisStrings) {
                AyUser user = objectMapper.readValue(redisString, AyUser.class);
                ayUserList.add(user);
            }
            if (null != ayUserList && ayUserList.size() > 0){
                for (AyUser user : ayUserList) {
                    if (user.getId().equals(id)){
                        return user;
                    }
                }
            }
            //step.2  查询数据库中的数据
            ayUser = ayUserRepository.findById(id).get();
            if (ayUser != null){
                //step.3 将数据插入到Redis缓存中
                stringRedisTemplate.opsForList().rightPush(ALL_USER, objectMapper.writeValueAsString(ayUser));
            }
        } catch (Exception e) {
            log.info("AyUserServiceImpl + findById() error");
            e.printStackTrace();
        }
        return ayUser;

    }

    @Override
    public List<AyUser> findAll() {
        try {
            log.info("开始做任务");
            long start = System.currentTimeMillis();
            List<AyUser> result = ayUserRepository.findAll();
            long end = System.currentTimeMillis();
            log.info("完成任务耗时" + (end - start) + "毫秒");
            return result;
        } catch (Exception e) {
            log.error("AyUserServiceImpl findAll() error", e);
            return Collections.EMPTY_LIST;
        }

    }

    @Override
    @Transactional
    public AyUser save(AyUser ayUser) {
        // 更新 和 保存
        AyUser save = new AyUser();
        try {
            // 先更新数据库
            save = ayUserRepository.save(ayUser);
//////        测试事务回滚
////        String error = null;
////        error.split("/");
            // 再更新缓存 (即删除缓存 设置缓存)
            List<String> redisStrings = stringRedisTemplate.opsForList().range(ALL_USER, 0, -1);
            for (String redisString : redisStrings) {
                AyUser user = objectMapper.readValue(redisString, AyUser.class);
                if (user.getName().equals(ayUser.getName())){
                    stringRedisTemplate.opsForList().remove(ALL_USER,1, redisString);
                }
            }
            stringRedisTemplate.opsForList().rightPush(ALL_USER, objectMapper.writeValueAsString(save));
        } catch (Exception e) {
            log.info("AyUserServiceImpl + save() error");
            e.printStackTrace();
        }
        return save;
    }

    @Override
    public void delete(String id) {

        try {
            // 先更新数据库
            ayUserRepository.deleteById(id);
            log.info("userId:" + id + "用户被删除");
            // 再删除缓存
            List<String> redisStrings = stringRedisTemplate.opsForList().range(ALL_USER, 0, -1);
            for (String redisString : redisStrings) {
                AyUser user = objectMapper.readValue(redisString, AyUser.class);
                if (user.getId().equals(id)){
                    stringRedisTemplate.opsForList().remove(ALL_USER, 1, redisString);
                }
            }

        } catch (Exception e) {
            log.info("AyUserServiceImpl + delete() error");
            e.printStackTrace();
        }
    }

    // 分页
    @Override
    public Page<AyUser> findAll(Pageable pageable) {
        return ayUserRepository.findAll(pageable);
    }

    @Override
    public List<AyUser> findByName(String name) {
        return ayUserRepository.findByName(name);
    }

    @Override
    public List<AyUser> findByNameLike(String name) {
        return ayUserRepository.findByNameLike(name);
    }

    @Override
    public List<AyUser> findByIdIn(Collection<String> ids) {
        return ayUserRepository.findByIdIn(ids);
    }

    // mybatis
    @Override
    public AyUser findByNameAndPassword(String name, String password) {
        return ayUserDao.findByNameAndPassword(name, password);
    }

    @Override
    @Async
    public Future<List<AyUser>> findAsynAll() {
        try{
            log.info("开始做任务");
            long start = System.currentTimeMillis();
            List<AyUser> ayUserList = ayUserRepository.findAll();
            long end = System.currentTimeMillis();
            log.info("完成任务，耗时：" + (end - start) + "毫秒");
            return new AsyncResult<List<AyUser>>(ayUserList) ;
        }catch (Exception e){
            log.error(this.getClass() + "method [findAsynAll()] error",e);
            return new AsyncResult<List<AyUser>>(null);
        }
    }

    @Override
    @Retryable(value= {BusinessException.class},maxAttempts = 5,
            backoff = @Backoff(delay = 5000,multiplier = 2))
    public AyUser findByNameAndPasswordRetry(String name, String password) {
        log.info("[findByNameAndPasswordRetry] 方法失败重试了！");
        throw new BusinessException();
    }

    @Override
    public AyUser findByUserName(String name) {
        List<AyUser> ayUsers = findByName(name);
        if(ayUsers == null && ayUsers.size() <= 0){
            return null;
        }
        return ayUsers.get(0);
    }
}
