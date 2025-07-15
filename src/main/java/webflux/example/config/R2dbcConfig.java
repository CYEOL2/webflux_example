package webflux.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@Configuration
@EnableR2dbcAuditing //@CreatedDate, @LastModifiedDate 등 어노테이션을 사용하기 위한 설정
public class R2dbcConfig {
    //conection 설정은 application.yml에 있습니다.
}

