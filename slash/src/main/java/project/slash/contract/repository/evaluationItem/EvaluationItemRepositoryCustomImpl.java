package project.slash.contract.repository.evaluationItem;

import static com.querydsl.core.group.GroupBy.*;
import static com.querydsl.core.types.Projections.*;
import static project.slash.contract.model.QEvaluationItem.*;
import static project.slash.contract.model.QServiceTarget.*;
import static project.slash.statistics.model.QStatistics.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.response.EvaluationItemDto;
import project.slash.contract.model.EvaluationItem;

public class EvaluationItemRepositoryCustomImpl implements EvaluationItemRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public EvaluationItemRepositoryCustomImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public List<EvaluationItemDto> findAllEvaluationItems(Long contractId) { 	//활성화 된것만 찾기
		return findEvaluationItems(evaluationItem.contract.id.eq(contractId).and(evaluationItem.isActive.isTrue()));
	}

	@Override
	public Optional<EvaluationItemDto> findEvaluationItem(Long evaluationItemId) {
		return findEvaluationItems(evaluationItem.id.eq(evaluationItemId))
			.stream()
			.findFirst();
	}

	@Override
	public Integer findTotalWeightByEvaluationItemId(Long evaluationItemId) {
		return queryFactory.select(evaluationItem.weight.sum())
			.from(evaluationItem)
			.where(evaluationItem.contract.id.eq(
				queryFactory.select(evaluationItem.contract.id)
					.from(evaluationItem)
					.where(evaluationItem.id.eq(evaluationItemId))
			).and(evaluationItem.isActive.isTrue())).fetchOne();
  }
  
  @Override
	public List<EvaluationItem> findUnCalculatedEvaluationItem(Long contractId, LocalDate endDate) {
	  return queryFactory
		  .selectFrom(evaluationItem)
		  .where(evaluationItem.id.notIn(
			  JPAExpressions.select(statistics.evaluationItem.id)
				  .from(statistics)
				  .where(statistics.date.eq(endDate))
		  ).and(evaluationItem.createDate.before(endDate))
			  .and(evaluationItem.contract.id.eq(contractId)))
		  .fetch();
	}

	private List<EvaluationItemDto> findEvaluationItems(BooleanExpression condition) {
		return queryFactory
			.from(evaluationItem)
			.leftJoin(serviceTarget).on(serviceTarget.evaluationItem.id.eq(evaluationItem.id))
			.where(condition)
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
					list(createServiceTargetDto())
				)));
	}

	private ConstructorExpression<GradeDto> createServiceTargetDto() {
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
