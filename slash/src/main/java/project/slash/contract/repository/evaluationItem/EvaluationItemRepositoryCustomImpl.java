package project.slash.contract.repository.evaluationItem;

import static com.querydsl.core.group.GroupBy.*;
import static com.querydsl.core.types.Projections.*;
import static project.slash.contract.model.QEvaluationItem.*;
import static project.slash.contract.model.QServiceDetail.*;
import static project.slash.contract.model.QServiceTarget.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;

import project.slash.contract.dto.GradeDto;
import project.slash.contract.dto.response.EvaluationItemDto;
import project.slash.contract.dto.response.ServiceDetailDto;

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
						constructor(ServiceDetailDto.class,
							serviceDetail.period,
							serviceDetail.purpose,
							serviceDetail.formula,
							list(constructor(GradeDto.class,
								serviceTarget.grade,
								serviceTarget.min,
								serviceTarget.max,
								serviceTarget.score,
								serviceTarget.minInclusive,
								serviceTarget.maxInclusive
							))
						)
					)
				));
	}
}
