package controller.WEB;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Homecontroller {

	@GetMapping("/index")
	public String Home() {
		return "index";

	}

}
