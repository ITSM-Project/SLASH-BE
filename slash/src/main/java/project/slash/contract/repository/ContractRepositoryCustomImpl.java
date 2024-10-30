package project.slash.contract.repository;

import static project.slash.contract.model.QContract.*;
import static project.slash.contract.model.QTotalTarget.*;

import java.util.Optional;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.response.ContractDto;
import project.slash.contract.repository.ContractRepositoryCustom;

public class ContractRepositoryCustomImpl implements ContractRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public ContractRepositoryCustomImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public Optional<ContractDto> findContractById(Long contractId) {
		return queryFactory
			.from(contract)
			.leftJoin(totalTarget)
			.on(totalTarget.contract.id.eq(contract.id))
			.where(contract.id.eq(contractId))
			.transform(GroupBy.groupBy(contract.id)
				.list(Projections.constructor(ContractDto.class,
					contract.companyName,
					contract.startDate,
					contract.endDate,
					contract.isTerminate,
					GroupBy.list(Projections.fields(GradeDto.class,
						totalTarget.grade,
						totalTarget.min,
						totalTarget.max,
						totalTarget.minInclusive,
						totalTarget.maxInclusive)))))
			.stream()
			.findFirst();
	}
}