package com.inmyhand.refrigerator.web;


import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;

import com.cleopatra.spring.UIView;
import com.cleopatra.ui.PageGenerator;

import jakarta.annotation.PostConstruct;



@Controller
public class CleopatraUIController {

	public CleopatraUIController() {
	}

	@PostConstruct
	private void initPageGenerator(){
		PageGenerator instance = PageGenerator.getInstance();
		instance.setURLSuffix(".clx");
	}

	@RequestMapping(value = {"/**/*.clx", "/*.clx"})
	public View handleClxRequests() {
		return new UIView();
	}

	@GetMapping(value = {"/{path:[^\\.]*}","/**/{path:[^\\.]*}"})
	public String forwardToLayout() {
		return "/app/layout/layout";
	}

}