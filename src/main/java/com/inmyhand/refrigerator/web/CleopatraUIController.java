package com.inmyhand.refrigerator.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.View;

import com.cleopatra.spring.UIView;
import com.cleopatra.ui.PageGenerator;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CleopatraUIController {

    @PostConstruct
    private void initPageGenerator() {
        PageGenerator.getInstance().setURLSuffix(".clx");
    }

    @GetMapping(value = {"/**/*.clx", "/*.clx"})
    public View handleClxRequests(HttpServletRequest request) {
        String requestId = request.getRequestURI();
        System.out.println("requestId = " + requestId);
        return new UIView();
    }

    @GetMapping(value = {"/{path:[^\\.]*}","/**/{path:[^\\.]*}"})
    public String forwardToLayout() {
        return "/app/layout/layout";
    }
    
    @GetMapping("/admin/user")
    public String adminUserPage() {
    	return "/app/admin/admin-user";
    }

    
}