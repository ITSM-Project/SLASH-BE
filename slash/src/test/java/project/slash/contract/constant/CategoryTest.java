package project.slash.contract.constant;

import static org.assertj.core.api.Assertions.*;
import static project.slash.contract.constant.Category.*;
import static project.slash.contract.exception.ContractErrorCode.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import project.slash.common.exception.BusinessException;

class CategoryTest {

	@DisplayName("이름으로 Category를 찾을 수 있다.")
	@Test
	void getCategory(){
	    //given
		String name = "서비스 가동률";

	    //when
		Category category = Category.getCategory(name);

		//then
		assertThat(category).isEqualTo(SERVICE_UPTIME_RATE);
	}

	@DisplayName("이름이 일치하지 않는 경우 예외가 발생한다.")
	@Test
	void getCategoryWithUnknown(){
	    //given
		String unknown = "프로그램 가동률";

	    //when //then
		assertThatThrownBy(() -> Category.getCategory(unknown))
			.isInstanceOf(BusinessException.class)
			.hasFieldOrPropertyWithValue("ErrorCode", NOT_FOUND_ITEMS);
	}
}
