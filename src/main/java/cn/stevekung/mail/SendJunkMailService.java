package cn.stevekung.mail;

import cn.stevekung.pojo.AyUser;

import java.util.List;

public interface SendJunkMailService {
    boolean sendJunkMail(List<AyUser> ayUser);
}
