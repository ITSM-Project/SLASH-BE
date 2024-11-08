package project.slash.statistics.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MonthlyIndicatorsDto {
	IndicatorExtraInfoDto indicatorEtcInfo;
	List<IndicatorDto> indicatorList;
}
