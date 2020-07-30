package cn.stevekung.service.impl;

import cn.stevekung.pojo.AyUserAttachmentRel;
import cn.stevekung.repository.AyUserAttachmentRelRepository;
import cn.stevekung.service.AyUserAttachmentRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AyUserAttachmentRelServiceImpl implements AyUserAttachmentRelService {

    @Autowired
    AyUserAttachmentRelRepository ayUserAttachmentRelRepository;

    @Override
    public AyUserAttachmentRel save(AyUserAttachmentRel ayUserAttachmentRel) {
        return ayUserAttachmentRelRepository.save(ayUserAttachmentRel);
    }
}
