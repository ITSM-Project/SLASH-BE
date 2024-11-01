package project.slash.statistics.repository;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import org.junit.jupiter.api.Test;

import project.slash.statistics.dto.MonthlyDataDto;

@SpringBootTest
@ActiveProfiles("test")
public class StatisticsRepositoryCustomImplTest {
	@Autowired
	private StatisticsRepository statisticsRepository;

	@Test
	@DisplayName("상품 명 조회 테스트")
	void getMonthlyData() {
		List<MonthlyDataDto> monthlyDataDtoList = statisticsRepository.getMonthlyData();

		monthlyDataDtoList.forEach(System.out::println);
		System.out.println("Total items: " + monthlyDataDtoList.size());

	}

}
