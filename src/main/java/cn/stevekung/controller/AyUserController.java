package cn.stevekung.controller;

import cn.stevekung.api.AyUserDubboService;
import cn.stevekung.error.BusinessException;
import cn.stevekung.pojo.AyUser;
import cn.stevekung.result.CodeMsg;
import cn.stevekung.result.Result;
import cn.stevekung.service.AyUserService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/ayUser")
public class AyUserController {

    @Resource
    private AyUserService ayUserService;

    @RequestMapping("/test")
    public String test(Model model) {
        //查询数据库所有用户
        List<AyUser> ayUser = ayUserService.findAll();
        model.addAttribute("users",ayUser);
        return "ayUser";
    }
    @RequestMapping("/test/findById")
    public String testRedis(Model model) {
        AyUser ayUser = ayUserService.findById("1");
        model.addAttribute("users",ayUser);
        return "ayUser";
    }

    @RequestMapping("/findAll")
    public String findAll(Model model) {
        List<AyUser> ayUser = ayUserService.findAll();
        model.addAttribute("users",ayUser);
        //
        throw new BusinessException("业务异常");
    }

    @RequestMapping("/findByNameAndPasswordRetry")
    public String findByNameAndPasswordRetry(Model model) {
        AyUser ayUser = ayUserService.findByNameAndPasswordRetry("steve","123456");
        model.addAttribute("users",ayUser);
        return "success";
    }

    // 测试dubbo服务
    @Resource
    AyUserDubboService ayUserDubboService;

    @RequestMapping("/testDubbo")
    @ResponseBody
    public Result testDubbo(){
        Result result = Result.success();
        System.out.println("------------");
        System.out.println(ayUserDubboService);
        System.out.println("---------------");
        try {
            return ayUserDubboService.findByUserNameAndPassword("steve", "123456");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
