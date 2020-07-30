package cn.stevekung.service;

import cn.stevekung.pojo.AyUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

public interface AyUserService {
    // 基本crud操作
    AyUser findById(String id); // 查
    List<AyUser> findAll(); // 查
    AyUser save(AyUser ayUser);// 增 改
    void delete(String id); // 删

    // 分页
    Page<AyUser> findAll(Pageable pageable);

    // 自定义查询方法
    List<AyUser> findByName(String name);
    List<AyUser> findByNameLike(String name);
    List<AyUser> findByIdIn(Collection<String> ids);

    // mybatis
    AyUser findByNameAndPassword(String name, String password);

    // 异步查询
    Future<List<AyUser>> findAsynAll();

    // retry
    AyUser findByNameAndPasswordRetry(String name, String password);

    // 权限角色查询
    public AyUser findByUserName(String name);
}
