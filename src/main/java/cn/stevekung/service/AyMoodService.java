package cn.stevekung.service;

import cn.stevekung.pojo.AyMood;

public interface AyMoodService {
    AyMood save(AyMood ayMood);
    String asynSave(AyMood ayMood);
}
