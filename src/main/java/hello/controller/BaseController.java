package hello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hello.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/base")
@RequiredArgsConstructor
public class BaseController {

	@GetMapping
	public String home() {
		return "You're authorized! Your username is: " + SecurityContextUtils.authenticatedUserName();
	}
}
