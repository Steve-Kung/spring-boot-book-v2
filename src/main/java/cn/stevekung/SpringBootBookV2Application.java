package cn.stevekung;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ServletComponentScan // 配置过滤器 用到
@EnableAsync // 开启异步
@EnableRetry // 开启重试
public class SpringBootBookV2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBookV2Application.class, args);
	}

	@Bean
	public ObjectMapper objectMapper(){
		return new ObjectMapper();
	}

}
