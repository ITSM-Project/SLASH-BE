package project.slash.contract.repository.contract;

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

import project.slash.contract.dto.response.ContractDataDto;
import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.response.ContractDto;

public class ContractRepositoryCustomImpl implements ContractRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public ContractRepositoryCustomImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public List<ContractDataDto> findContractByEvaluationItemId(long evaluationItemId, long contractId) {
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
						.where(evaluationItem.contract.id.eq(contractId).and(evaluationItem.isActive.isTrue())),
					"weightTotal"
				), evaluationItem.id,
				evaluationItem.category))
			.from(evaluationItem)
			.leftJoin(serviceTarget)
			.on(serviceTarget.evaluationItem.id.eq(evaluationItem.id))
			.where(evaluationItem.id.eq(evaluationItemId))
			.fetch();
	}
	}


