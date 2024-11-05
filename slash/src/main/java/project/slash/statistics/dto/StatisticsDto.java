package project.slash.statistics.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatisticsDto {
	private LocalDate date;
	private String serviceType;
	private String grade;
	private double score;
	private long totalDowntime;
	private long requestCount;
	private long dueOnTimeCount;
	private String targetSystem;
	private String targetEquipment;
}
