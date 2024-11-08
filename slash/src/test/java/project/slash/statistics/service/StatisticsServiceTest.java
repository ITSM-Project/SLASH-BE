package project.slash.statistics.service;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import project.slash.statistics.dto.response.MonthlyStatisticsDto;

@SpringBootTest
class StatisticsServiceTest {

	@Autowired
	StatisticsService statisticsService;

	// 여기서 각자 출력 테스트 하시면 됩니다.
	@Test
	@DisplayName("월간통계 출력")
	void calculateMonthlyStats() {
		List<MonthlyStatisticsDto> monthlyStatisticsDtoList = statisticsService.calculateMonthlyStats(
			"서비스 가동률", true);
		for (MonthlyStatisticsDto monthlyStatisticsDto : monthlyStatisticsDtoList) {
			System.out.println(monthlyStatisticsDto);
		}
	}
}

