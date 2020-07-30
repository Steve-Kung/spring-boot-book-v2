package cn.stevekung.controller;

import cn.stevekung.error.BusinessException;
import cn.stevekung.pojo.AyUser;
import cn.stevekung.service.AyUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
