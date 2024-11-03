package project.slash.taskrequest.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.slash.taskrequest.dto.request.RequestManagementDto;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestManagementResponseDto {
	private List<RequestManagementDto> results; // 요청 목록
	private int totalPages;                     // 총 페이지 수
	private int currentPage;                    // 현재 페이지
	private long totalItems;                    // 총 항목 수

}