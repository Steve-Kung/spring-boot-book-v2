package cn.stevekung.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ay_user_role_rel")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AyUserRoleRel {
    @Id
    private String userId;
    private String roleId;
}
