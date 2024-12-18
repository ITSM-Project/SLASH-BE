package project.slash.taskrequest.repository;

import static com.querydsl.core.types.Projections.*;
import static project.slash.contract.model.QContract.*;
import static project.slash.contract.model.QEvaluationItem.*;
import static project.slash.taskrequest.model.QTaskType.*;

import java.util.List;

import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import project.slash.taskrequest.dto.response.AllTaskTypeDto;

public class TaskTypeRepositoryCustomImpl implements TaskTypeRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public TaskTypeRepositoryCustomImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public List<AllTaskTypeDto> findAllTaskTypes(Long contractId) {
		return queryFactory
			.from(taskType)
			.where(taskType.evaluationItem.id.in(
				JPAExpressions
					.select(evaluationItem.id)
					.from(evaluationItem)
					.join(contract)
					.on(evaluationItem.contract.id.eq(contract.id))
					.where(contract.id.eq(contractId)
						.and(evaluationItem.isActive.isTrue()))
			))
			.transform(GroupBy.groupBy(taskType.type).list(
				constructor(AllTaskTypeDto.class,
					taskType.type,
					GroupBy.set(taskType.taskDetail))
			));
	}
}
