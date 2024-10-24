package project.slash.system.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AllSystemsInfo {
	private String systemName;
	private List<String> equipmentInfos;
}
