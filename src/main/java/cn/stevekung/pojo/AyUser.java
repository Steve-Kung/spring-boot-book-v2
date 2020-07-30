package cn.stevekung.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "ay_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AyUser implements Serializable{
    @Id
    private String id;
    private String name;
    private String password;
    private String mail;
}
