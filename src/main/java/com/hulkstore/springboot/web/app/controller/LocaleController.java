package com.hulkstore.springboot.web.app.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LocaleController {

	@GetMapping(value = "/locale")
	public String locale(HttpServletRequest request) {

		try {

			String ultimaUrl = request.getHeader("referer");
			return "redirect:".concat(ultimaUrl);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "redirect:";
		}

	}
}
