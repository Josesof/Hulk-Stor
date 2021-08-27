package com.hulkstore.springboot.web.app.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	@GetMapping(value = { "/login" })
	public String login(@RequestParam(value = "error", required = false) String error, Model model, Principal principal,
			RedirectAttributes flash) {

		try {

			if (principal != null) {
				flash.addFlashAttribute("info", "Ya ha iniciado sesion anteriormente");
				return "redirect:/";
			}

			if (error != null) {
				flash.addAttribute("danger", "El usuario o contraseña son incorrectos");
			}

			return "login";

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return "login";
		}

	}

}
