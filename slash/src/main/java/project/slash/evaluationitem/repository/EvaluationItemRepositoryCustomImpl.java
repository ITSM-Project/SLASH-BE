package project.slash.evaluationitem.repository;

import static com.querydsl.core.group.GroupBy.*;
import static com.querydsl.core.types.Projections.*;
import static project.slash.evaluationitem.model.QEvaluationItem.*;
import static project.slash.evaluationitem.model.QServiceTarget.*;

import java.util.List;
import java.util.Optional;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.response.PreviewEvaluationItemDto;
import project.slash.evaluationitem.dto.ServiceTargetDto;
import project.slash.evaluationitem.dto.response.EvaluationItemDto;

public class EvaluationItemRepositoryCustomImpl implements EvaluationItemRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public EvaluationItemRepositoryCustomImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public List<PreviewEvaluationItemDto> findEvaluationItem(Long contractId) {
		return queryFactory
			.from(evaluationItem)
			.leftJoin(serviceTarget).on(serviceTarget.evaluationItem.id.eq(evaluationItem.id))
			.where(evaluationItem.contract.id.eq(contractId))
			.transform(groupBy(evaluationItem.id)
				.list(constructor(PreviewEvaluationItemDto.class,
						evaluationItem.id,
						evaluationItem.category,
						list(constructTargetDto(GradeDto.class))
					)
				)
			);
	}

	@Override
	public Optional<EvaluationItemDto> findEvaluationItemDetail(Long evaluationItemId) {
		return queryFactory
			.from(evaluationItem)
			.leftJoin(serviceTarget).on(serviceTarget.evaluationItem.id.eq(evaluationItem.id))
			.where(evaluationItem.id.eq(evaluationItemId))
			.transform(groupBy(evaluationItem.id)
				.list(constructor(EvaluationItemDto.class,
						evaluationItem.contract.id,
						evaluationItem.id,
						evaluationItem.category,
						evaluationItem.weight,
						evaluationItem.period,
						evaluationItem.purpose,
						evaluationItem.formula,
						evaluationItem.unit,
						list(constructTargetDto(ServiceTargetDto.class)
						)
					)
				)).stream().findFirst();
	}

	private static <T> ConstructorExpression<T> constructTargetDto(Class<T> dtoClass) {
		return constructor(dtoClass,
			serviceTarget.grade,
			serviceTarget.min,
			serviceTarget.max,
			serviceTarget.score,
			serviceTarget.minInclusive,
			serviceTarget.maxInclusive
		);
	}
}
