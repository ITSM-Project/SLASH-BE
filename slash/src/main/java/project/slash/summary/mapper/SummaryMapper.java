package project.slash.summary.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import project.slash.summary.dto.SummaryDto;
import project.slash.summary.model.Summary;

@Mapper
public interface SummaryMapper {

	SummaryMapper INSTANCE = Mappers.getMapper(SummaryMapper.class);

	Summary toEntity(SummaryDto dto);

}
