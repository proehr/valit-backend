package com.edu.m7.feedback.model.mapping;


import com.edu.m7.feedback.model.entity.Semester;
import com.edu.m7.feedback.payload.request.CourseRequestDto;
import com.edu.m7.feedback.payload.response.CourseResponseDto;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Date;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDate;
import java.util.Set;

@Mapper
public interface CourseDtoMapper {

    //@Mapping(target = "dates", ignore = true)
    default Course dtoToEntity(CourseRequestDto dto, Set<Date> dates, Semester semester) {
        if ( dto == null ) {
            return null;
        }

        Course course = new Course();

        course.setName( dto.getName() );
        course.setDegree( dto.getDegree() );
        course.setTimeStart( dto.getTimeStart() );
        course.setTimeEnd( dto.getTimeEnd() );
        course.setStudentCount( dto.getStudentCount() );
        course.setInterval(dto.getInterval());
        course.setSemester(semester);
        course.setDates(dates);
        course.setWeekday(dto.getWeekday());

        return course;
    }
    CourseResponseDto entityToDto(Course entity);

    //@Mapping(target = "dates", ignore = true)
    //Course updateEntityFromDto(CourseRequestDto dto, @MappingTarget Course course);

    default LocalDate dateEntityToLocalDate(Date date){
        return date.getLocalDate();
    }

    default Long semesterToId(Semester semester) {
        return semester.getSemesterId();
    }

}
