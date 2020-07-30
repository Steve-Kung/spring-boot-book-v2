package cn.stevekung.repository;

import cn.stevekung.pojo.AyUserRoleRel;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AyUserRoleRelRepository extends JpaRepository<AyUserRoleRel, String> {
    List<AyUserRoleRel> findByUserId(@Param("userId")String userID);
}
