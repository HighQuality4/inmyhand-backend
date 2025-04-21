package com.inmyhand.refrigerator.web;


import org.springframework.stereotype.Controller;

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

	@RequestMapping("/**/*.clx")
	public View index() {
		return new UIView();
	}

	@RequestMapping("/*.clx")
	public View index2() {
		return new UIView();
	}

}