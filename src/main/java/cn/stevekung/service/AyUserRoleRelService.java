package cn.stevekung.service;

import cn.stevekung.pojo.AyUserRoleRel;

import java.util.List;

public interface AyUserRoleRelService {
    List<AyUserRoleRel> findByUserId(String userId);
}
