package hello.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.xray.spring.aop.XRayEnabled;

import hello.entity.User;
import hello.services.UserService;
import hello.utils.SecurityContextUtils;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/api/base")
@RequiredArgsConstructor
@XRayEnabled
public class BaseController {
	private final UserService userService;
	@GetMapping
	public String home() {
		User user = userService.findByUsername(SecurityContextUtils.authenticatedUserName());
		return "You're authorized! Your username is: " + user.getUsername();
	}
}
