package cn.stevekung.service.impl;

import cn.stevekung.pojo.AyRole;
import cn.stevekung.repository.AyRoleRepository;
import cn.stevekung.service.AyRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AyRoleServiceImpl implements AyRoleService{
    @Autowired
    AyRoleRepository ayRoleRepository;
    @Override
    public AyRole find(String id) {
        return ayRoleRepository.findById(id).get();
    }
}
