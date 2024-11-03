package project.slash.taskrequest.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import project.slash.taskrequest.service.OptionsService;

@RestController
@RequestMapping("/options")
@RequiredArgsConstructor
public class OptionsController {

	private final OptionsService optionsService;

	@GetMapping("/systems")
	public List<String> getDistinctSystemNames() {
		return optionsService.getDistinctSystemNames();
	}

	@GetMapping("/task-type")
	public List<String> getDistinctTaskType() {
		return optionsService.getDistinctTaskTypes();
	}

	@GetMapping("/task-detail")
	public List<String> getDistinctTaskDetails() {
		return optionsService.getDistinctTaskDetails();
	}

}
