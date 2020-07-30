package cn.stevekung.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ay_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AyRole {
    @Id
    private String id;
    private String name;
}
