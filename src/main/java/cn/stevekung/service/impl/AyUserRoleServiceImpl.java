package cn.stevekung.service.impl;

import cn.stevekung.pojo.AyUserRoleRel;
import cn.stevekung.repository.AyUserRoleRelRepository;
import cn.stevekung.service.AyUserRoleRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AyUserRoleServiceImpl implements AyUserRoleRelService {

    @Autowired
    AyUserRoleRelRepository ayUserRoleRelRepository;

    @Override
    public List<AyUserRoleRel> findByUserId(String userId) {
        return ayUserRoleRelRepository.findByUserId(userId);
    }
}
