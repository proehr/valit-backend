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

    //@Mapping(target = "semester", ignore = true)
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
    default Course updateEntityFromDto(CourseRequestDto dto, Course course, Set<Date> dates, Semester semester) {
        if ( dto == null ) {
            return null;
        }
        if(dto.getSemester() != null) {
            course.setSemester(semester);
        }
        if(dto.getStudentCount() != null) {
            course.setStudentCount(dto.getStudentCount());
        }
        if(dto.getName() != null) {
            course.setName(dto.getName());
        }
        if(dto.getDegree() != null) {
            course.setDegree(dto.getDegree());
        }
        if(dto.getTimeStart() != null) {
            course.setTimeStart(dto.getTimeStart());
        }
        if(dto.getTimeEnd() != null) {
            course.setTimeEnd(dto.getTimeEnd());
        }
        if(dto.getInterval() != null) {
            course.setInterval(dto.getInterval());
        }
        if(dates != null) {
            course.setDates(dates);
        }
        if(dto.getWeekday() != null) {
            course.setWeekday(dto.getWeekday());
        }
        return course;
    }

    default LocalDate dateEntityToLocalDate(Date date){
        return date.getLocalDate();
    }

    default Long semesterToId(Semester semester) {
        return semester.getSemesterId();
    }

}
