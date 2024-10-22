package project.slash.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@GetMapping("/")
	public String homePage() {
		return "home";
	}

	@GetMapping("/login")
	public String loginPage() {
		return "login-page";
	}

	@GetMapping("/contract-admin/dashboard")
	public String contractAdminDashboard() {
		return "contract-admin/dashboard";
	}

	@GetMapping("/service-admin/dashboard")
	public String serviceAdminDashboard() {
		return "service-admin/dashboard";
	}

	@GetMapping("/user/dashboard")
	public String userDashboard() {
		return "user/dashboard";
	}

}
