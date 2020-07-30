package cn.stevekung.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorInfo<T> {
    public static final Integer SUCCESS = 200;
    public static final Integer ERROR = 100;


    //错误码
    private Integer code;
    //错误信息
    private String message;

    private String url;
    private T data;
}
