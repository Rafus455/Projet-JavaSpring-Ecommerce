package com.b2.e_commerce.controller.MVC;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

	@GetMapping("/product")
	public String Home() {
		return "product";
	}
}
