package cn.stevekung;


import cn.stevekung.api.AyUserDubboService;
import cn.stevekung.pojo.AyMood;
import cn.stevekung.pojo.AyUser;
import cn.stevekung.pojo.AyUserAttachmentRel;
import cn.stevekung.producer.AyMoodProducer;
import cn.stevekung.result.Result;
import cn.stevekung.service.AyMoodService;
import cn.stevekung.service.AyUserAttachmentRelService;
import cn.stevekung.service.AyUserService;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Future;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringBootBookV2ApplicationTests {

    Logger log = LogManager.getLogger(this.getClass());
//    private static final Logger log = LoggerFactory.getLogger(SpringBootBookV2ApplicationTests.class);

	@Resource
	private JdbcTemplate jdbcTemplate;

	@Test
	public void mysqlTest() {
		String sql = "select id,name,password from ay_user";
		List<AyUser> userList =
                (List<AyUser>) jdbcTemplate.query(sql, new RowMapper<AyUser>(){
                    @Override
                    public AyUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                        AyUser user = new AyUser();
                        user.setId(rs.getString("id"));
                        user.setName(rs.getString("name"));
                        user.setPassword(rs.getString("password"));
                        return user;
                    }
                });
		System.out.println("查询成功：");
        for(AyUser user:userList){
            System.out.println("【id】: " + user.getId() + "；【name】: " + user.getName());
        }
	}

	@Autowired
    DataSource dataSource;
    @Test
    public void druidTest() throws SQLException {
        System.out.println(dataSource.getClass());
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        DruidDataSource dataSource = (DruidDataSource) this.dataSource;
        System.out.println(dataSource.getMaxActive());
    }

    @Resource
    private AyUserService ayUserService;

    @Test
    public void testRepository(){
        //查询所有数据
        List<AyUser> userList =  ayUserService.findAll();
        System.out.println("========findAll() :" + userList.size());
        //通过name查询数据
        List<AyUser> userList2 = ayUserService.findByName("steve");
        System.out.println("========findByName() :" + userList2.size());
        Assert.isTrue(userList2.get(0).getName().equals("steve"),"data error!");
        //通过name模糊查询数据
        List<AyUser> userList3 = ayUserService.findByNameLike("%健%");
        System.out.println("========findByNameLike() :" + userList3.size());
        Assert.isTrue(userList3.get(0).getName().equals("龚健"),"data error!");
        //通过id列表查询数据
        List<String> ids = new ArrayList<String>();
        ids.add("1");
        ids.add("2");
        List<AyUser> userList4 = ayUserService.findByIdIn(ids);
        System.out.println("===========findByIdIn() :" + userList4.size());
        //分页查询数据
        PageRequest pageRequest = PageRequest.of(0,10);
        Page<AyUser> userList5 =  ayUserService.findAll(pageRequest);
        System.out.println("=======page findAll():" + userList5.getTotalPages() + "/" + userList5.getSize());
        //新增数据
        AyUser ayUser = new AyUser();
        ayUser.setId("3");
        ayUser.setName("test");
        ayUser.setPassword("123");
        AyUser save = ayUserService.save(ayUser);
        log.info("========新增数据 save" + save);
        //删除数据
        ayUserService.delete("3");
        log.info("=========删除数据 delete");
    }

    @Test
    public void testTransaction(){
        AyUser ayUser = new AyUser();
        ayUser.setId("3");
        ayUser.setName("阿华");
        ayUser.setPassword("123");
        ayUserService.save(ayUser);
    }

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
//    stringRedisTemplate 测试
    public void testRedis(){
        //增 key：name，value：ay
        stringRedisTemplate.opsForValue().set("name","ay");
        String name = (String)stringRedisTemplate.opsForValue().get("name");
        System.out.println("---------" + name);
        //删除
        stringRedisTemplate.delete("name");
//        更新
        stringRedisTemplate.opsForValue().set("name","al111");
        //查询
        name = stringRedisTemplate.opsForValue().get("name");
        System.out.println("======" + name);
    }
    private static final String ALL_USER = "ALL_USER_LIST";
    @Autowired
    ObjectMapper objectMapper;

    @Test
//    JackSon 测试 list json 转换
    public void testRedis1() throws JsonProcessingException {
        String s = stringRedisTemplate.opsForValue().get(ALL_USER);
//        List<LinkedHashMap> list = objectMapper.readValue(s, ArrayList.class);
//        for (LinkedHashMap linkedHashMap : list) {
//            Set set = linkedHashMap.keySet();
//            for (Object o : set) {
//                String o1 = (String)linkedHashMap.get(o);
//                System.out.println(o1);
//            }
//        }

        List<AyUser> ayUserList = objectMapper.readValue(s, new TypeReference<List<AyUser>>() {});
        for (AyUser ayUser : ayUserList) {
            System.out.println(ayUser);
        }
        System.out.println("===========");

    }

    @Test
//    fastjson 测试 list json 转换
    public void testRedis2() throws JsonProcessingException {
        String s = stringRedisTemplate.opsForValue().get(ALL_USER);
        List<AyUser> ayUserList = JSON.parseArray(s, AyUser.class);
        for (AyUser ayUser : ayUserList) {
            System.out.println(ayUser);
        }
        System.out.println(">>>>>>>>>>>>>>>>");

    }

    @Test
    // 测试 save 和 delete 写的缓存
    public void testSaveRedis3(){
        AyUser ayUser = new AyUser();
        ayUser.setId("5");
        ayUser.setName("jg");
        ayUser.setPassword("123456");
        ayUserService.save(ayUser);

    }

    @Test
    // 测试 save 和 delete 写的缓存
    public void testDeleteRedis3(){
        ayUserService.delete("5");
    }

    @Test
    public void testFindById(){
        Long redisUserSize = 0L;
        //查询id = 1 的数据，该数据存在于redis缓存中
        AyUser ayUser = ayUserService.findById("1");
        redisUserSize = stringRedisTemplate.opsForList().size("ALL_USER_LIST");
        System.out.println("目前缓存中的用户数量为：" + redisUserSize);
        System.out.println("--->>> id: " + ayUser.getId() + " name:" + ayUser.getName());
        //查询id = 2 的数据，该数据存在于redis缓存中
        AyUser ayUser1 = ayUserService.findById("2");
        redisUserSize = stringRedisTemplate.opsForList().size("ALL_USER_LIST");
        System.out.println("目前缓存中的用户数量为：" + redisUserSize);
        System.out.println("--->>> id: " + ayUser1.getId() + " name:" + ayUser1.getName());
        //查询id = 4 的数据，不存在于redis缓存中，存在于数据库中，所以会把数据库查询的数据更新到缓存中
        AyUser ayUser3 = ayUserService.findById("4");
        System.out.println("--->>> id: " + ayUser3.getId() + " name:" + ayUser3.getName());
        redisUserSize = stringRedisTemplate.opsForList().size("ALL_USER_LIST");
        System.out.println("目前缓存中的用户数量为：" + redisUserSize);

    }



    @Test
    public void testLog4j(){
        ayUserService.delete("4");
        log.info("delete success!!!");
    }

    @Test
    public void testMybatis(){
        AyUser ayUser = ayUserService.findByNameAndPassword("steve", "123456");
        log.info(ayUser.getId() + ayUser.getName());

    }

    @Resource
    private AyMoodService ayMoodService;

    @Test
    public void testAyMood(){
        AyMood ayMood = new AyMood();
        ayMood.setId("1");
        //用户steve id为1
        ayMood.setUserId("1");
        ayMood.setPraiseNum(0);
        //说说内容
        ayMood.setContent("这是我的第一条微信说说！！！");
        ayMood.setPublishTime(new Date());
        //往数据库保存一条数据，代码用户阿毅发表了一条说说
        AyMood mood = ayMoodService.save(ayMood);
    }

    @Resource
    private AyMoodProducer ayMoodProducer;

    @Test
    public void testActiveMQ() {

        Destination destination = new ActiveMQQueue("ay.queue");
        ayMoodProducer.sendMessage(destination, "hello,mq!!!");
    }

    @Test
    public void testActiveMQAsynSave() {
        AyMood ayMood = new AyMood();
        ayMood.setId("2");
        ayMood.setUserId("2");
        ayMood.setPraiseNum(0);
        ayMood.setContent("这是我的第一条微信说说！！！");
        ayMood.setPublishTime(new Date());
        String msg = ayMoodService.asynSave(ayMood);
        log.info("异步发表说说 :" + msg);
    }

    @Test
    public void testAsync(){
        long startTime = System.currentTimeMillis();
        log.info("第一次查询所有用户！");
        List<AyUser> ayUserList = ayUserService.findAll();
        log.info("第二次查询所有用户！");
        List<AyUser> ayUserList2 = ayUserService.findAll();
        log.info("第三次查询所有用户！");
        List<AyUser> ayUserList3 = ayUserService.findAll();
        long endTime = System.currentTimeMillis();
        log.info("总共消耗：" + (endTime - startTime) + "毫秒");
    }

    @Test
    public void testAsync2()throws Exception{
        long startTime = System.currentTimeMillis();
        System.out.println("第一次查询所有用户！");
        Future<List<AyUser>> ayUserList = ayUserService.findAsynAll();
        System.out.println("第二次查询所有用户！");
        Future<List<AyUser>> ayUserList2 = ayUserService.findAsynAll();
        System.out.println("第三次查询所有用户！");
        Future<List<AyUser>> ayUserList3 = ayUserService.findAsynAll();
        while (true){
            if(ayUserList.isDone() && ayUserList2.isDone() && ayUserList3.isDone()){
                break;
            }else {
                Thread.sleep(10);
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("总共消耗：" + (endTime - startTime) + "毫秒");
    }

    @Resource
    private AyUserAttachmentRelService ayUserAttachmentRelService;

    @Test
    public void testMongoDB(){
        AyUserAttachmentRel ayUserAttachmentRel = new AyUserAttachmentRel();
        ayUserAttachmentRel.setId("1");
        ayUserAttachmentRel.setUserId("1");
        ayUserAttachmentRel.setFileName("个人简历.doc");
        ayUserAttachmentRelService.save(ayUserAttachmentRel);
        log.info("保存成功");
    }

    @Autowired
    AyUserDubboService ayUserDubboService;

    @Test
    public void testDubbo(){
        Result steve = ayUserDubboService.findByUserNameAndPassword("steve", "123456");
        System.out.println(steve);
    }

}
