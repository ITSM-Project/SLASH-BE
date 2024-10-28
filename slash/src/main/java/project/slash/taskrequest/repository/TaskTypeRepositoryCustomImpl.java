package project.slash.taskrequest.repository;

import static project.slash.contract.model.QContract.*;
import static project.slash.contract.model.QEvaluationItem.*;
import static project.slash.taskrequest.model.QTaskType.*;

import java.util.List;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import project.slash.taskrequest.dto.response.TaskTypeDto;

public class TaskTypeRepositoryCustomImpl implements TaskTypeRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public TaskTypeRepositoryCustomImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public List<TaskTypeDto> findAllTaskTypes() {
		return queryFactory
			.from(taskType)
			.where(taskType.evaluationItem.id.in(
				JPAExpressions
					.select(evaluationItem.id)
					.from(evaluationItem)
					.leftJoin(contract)
					.on(evaluationItem.contract.id.eq(contract.id).and(
						contract.isTerminate.isFalse()
					))
			))
			.transform(GroupBy.groupBy(taskType.type).list(
				Projections.constructor(TaskTypeDto.class,
					taskType.type,
					GroupBy.list(taskType.taskDetail))
			));
	}
}
