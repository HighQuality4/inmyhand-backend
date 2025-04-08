package com.inmyhand.refrigerator.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.cleopatra.spring.DataRequestResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    //eXBuilder6
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new DataRequestResolver());
    }

    // view 디렉토리 내 파일들을 정적 리소스로 접근 가능하게 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/view/**").addResourceLocations("classpath:/view/");
    }
}
