package project.slash.contract.repository;

import static project.slash.contract.model.QContract.*;
import static project.slash.contract.model.QEvaluationItem.*;
import static project.slash.contract.model.QServiceTarget.*;
import static project.slash.contract.model.QTotalTarget.*;

import java.util.List;
import java.util.Optional;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import project.slash.contract.dto.ContractDataDto;
import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.response.ContractDto;

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
					contract.contractName,
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

	@Override
	public List<ContractDataDto> findIndicatorByEvaluationItemId(long evaluationItmeId, long contractId) {
		return queryFactory
			.select(Projections.constructor(ContractDataDto.class,
				serviceTarget.grade,
				serviceTarget.max,
				serviceTarget.maxInclusive,
				serviceTarget.min,
				serviceTarget.minInclusive,
				serviceTarget.score,
				evaluationItem.weight,
				ExpressionUtils.as(
					JPAExpressions.select(evaluationItem.weight.sum())
						.from(evaluationItem)
						.where(evaluationItem.contract.id.eq(contractId)),
					"weightTotal"
				), evaluationItem.id,
				evaluationItem.category))
			.from(evaluationItem)
			.leftJoin(serviceTarget)
			.on(serviceTarget.evaluationItem.id.eq(evaluationItem.id))
			.where(evaluationItem.id.eq(evaluationItmeId))
			.fetch();
	}
}
