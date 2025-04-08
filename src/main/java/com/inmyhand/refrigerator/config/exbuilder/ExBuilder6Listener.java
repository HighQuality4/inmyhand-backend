package com.inmyhand.refrigerator.config.exbuilder;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cleopatra.XBInitializer;


@Configuration
public class ExBuilder6Listener {


	@Bean
	public ServletListenerRegistrationBean<XBInitializer> getServletListenerRegistrationBean() {

        return new ServletListenerRegistrationBean<>(new XBInitializer());
	}


}