package cn.stevekung.error;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
/*
常用HttpStatus状态：
HttpStatus.OK = 200;
HttpStatus.BADREQUEST = 400;
HttpStatus.FORBIDDEN = 403;
HttpStatus.NOTFOUND = 404;
HttpStatus.TIMEOUT = 408;
HttpStatus.INTERNAL_SERVER_ERROR = 500;
'401' : 'Unauthorized',  //未经授权
 */
@Configuration
public class ErrorPageConfig {
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(){
        return (container -> {
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404.html");
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/401.html");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500.html");

            container.addErrorPages(error404Page, error401Page, error500Page);
        });
    }
}
