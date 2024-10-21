package project.slash.system.repository;

import static project.slash.system.model.QEquipment.*;
import static project.slash.system.model.QSystems.*;

import java.util.List;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import project.slash.system.dto.response.AllSystemsInfo;
import project.slash.system.dto.response.EquipmentInfo;

public class SystemsRepositoryImpl implements SystemsRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public SystemsRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	public List<AllSystemsInfo> findAllSystemsWithEquipments() {
		return queryFactory
			.from(systems)
			.leftJoin(equipment).on(systems.id.eq(equipment.systems.id))
			.transform(GroupBy.groupBy(systems.id).list(
				Projections.constructor(AllSystemsInfo.class,
					systems.name.as("systemName"),
					GroupBy.list(Projections.constructor(EquipmentInfo.class,
						equipment.id.as("equipmentId"),
						equipment.name)
					)
				)
			));
	}
}
