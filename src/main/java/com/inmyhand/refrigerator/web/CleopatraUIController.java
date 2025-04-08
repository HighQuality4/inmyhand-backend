package com.inmyhand.refrigerator.web;





import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;

import com.cleopatra.spring.UIView;
import com.cleopatra.ui.PageGenerator;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class CleopatraUIController {

	public CleopatraUIController() {
	}

	@PostConstruct
	private void initPageGenerator() throws IOException {
		PageGenerator instance = PageGenerator.getInstance();
		instance.setURLSuffix(".clx");
	}

	@RequestMapping("/**/*.clx")
	public View index(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		return new UIView();
	}
	
	@RequestMapping("/*.clx")
	public View index2(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
		return new UIView();
	}

}