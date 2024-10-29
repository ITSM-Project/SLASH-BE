package project.slash.contract.repository.evaluationItem;

import static com.querydsl.core.group.GroupBy.*;
import static com.querydsl.core.types.Projections.*;
import static project.slash.contract.model.QEvaluationItem.*;
import static project.slash.contract.model.QServiceDetail.*;
import static project.slash.contract.model.QServiceTarget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.response.EvaluationItemDetailDto;
import project.slash.contract.dto.response.EvaluationItemDto;

public class EvaluationItemRepositoryCustomImpl implements EvaluationItemRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public EvaluationItemRepositoryCustomImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public List<EvaluationItemDto> findEvaluationItemInfos(Long contractId) {
		return queryFactory
			.from(evaluationItem)
			.leftJoin(serviceDetail).on(serviceDetail.evaluationItem.id.eq(evaluationItem.id))
			.leftJoin(serviceTarget).on(serviceTarget.evaluationItem.id.eq(evaluationItem.id))
			.where(evaluationItem.contract.id.eq(contractId))
			.transform(groupBy(evaluationItem.id)
				.list(constructor(EvaluationItemDto.class,
						evaluationItem.id.as("categoryId"),
						evaluationItem.category.as("categoryName"),
						constructor(EvaluationItemDto.ServiceDetailDto.class,
							serviceDetail.period,
							serviceDetail.purpose,
							serviceDetail.formula,
							list(constructGradeDto())
						)
					)
				));
	}

	@Override
	public Optional<EvaluationItemDetailDto> findEvaluationItemDetail(Long categoryId) {
		return queryFactory
			.from(evaluationItem)
			.leftJoin(serviceDetail).on(serviceDetail.evaluationItem.id.eq(evaluationItem.id))
			.leftJoin(serviceTarget).on(serviceTarget.evaluationItem.id.eq(evaluationItem.id))
			.where(evaluationItem.id.eq(categoryId))
			.transform(groupBy(evaluationItem.id)
				.list(constructor(EvaluationItemDetailDto.class,
						evaluationItem.id.as("categoryId"),
						evaluationItem.category.as("categoryName"),
						serviceDetail.weight,
						serviceDetail.period,
						serviceDetail.purpose,
						serviceDetail.formula,
						serviceDetail.unit,
						list(constructGradeDto()
						),
						Expressions.constant(new ArrayList<>())
					)
				)).stream().findFirst();
	}

	private static ConstructorExpression<GradeDto> constructGradeDto() {
		return constructor(GradeDto.class,
			serviceTarget.grade,
			serviceTarget.min,
			serviceTarget.max,
			serviceTarget.score,
			serviceTarget.minInclusive,
			serviceTarget.maxInclusive
		);
	}
}
