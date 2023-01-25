package com.edu.m7.feedback.model.mapping;

import com.edu.m7.feedback.model.dto.CourseDto;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Date;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;

@Mapper
public interface CourseDtoMapper {

    @Mapping(target = "dates", ignore = true)
    Course dtoToEntity(CourseDto dto);
    CourseDto entityToDto(Course entity);

    @Mapping(target = "dates", ignore = true)
    Course updateEntityFromDto(CourseDto dto, @MappingTarget Course course);

    default LocalDate dateEntityToLocalDate(Date date){
        return date.getLocalDate();
    }

}
