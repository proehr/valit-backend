package com.edu.m7.feedback.model.mapping;

import com.edu.m7.feedback.model.dto.SemesterDto;
import com.edu.m7.feedback.model.entity.Semester;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface SemesterMapper {
    Semester semesterDtoToSemester(SemesterDto semesterDto);

    SemesterDto semesterToSemesterDto(Semester semester);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Semester updateSemesterFromSemesterDto(SemesterDto semesterDto, @MappingTarget Semester semester);
}
