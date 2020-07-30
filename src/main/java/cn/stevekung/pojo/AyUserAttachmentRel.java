package cn.stevekung.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AyUserAttachmentRel {
    @Id
    private String id;
    private String userId;
    private String fileName;
}
