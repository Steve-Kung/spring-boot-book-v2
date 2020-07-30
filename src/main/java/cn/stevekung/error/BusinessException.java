package cn.stevekung.error;
// 自定义 业务异常
public class BusinessException extends RuntimeException {
    public BusinessException(){}

    public BusinessException(String message) {
        super(message);
    }
}
