package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.IntervalType;
import com.edu.m7.feedback.model.entity.Date;
import com.edu.m7.feedback.model.entity.Semester;
import com.edu.m7.feedback.model.mapping.CourseDtoMapper;
import com.edu.m7.feedback.model.repository.SemesterRepository;
import com.edu.m7.feedback.payload.request.CourseRequestDto;
import com.edu.m7.feedback.payload.response.CourseResponseDto;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.repository.CourseRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private static final CourseDtoMapper mapper = Mappers.getMapper(CourseDtoMapper.class);
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;

    public CourseService(CourseRepository courseRepository, SemesterRepository semesterRepository) {
        this.courseRepository = courseRepository;
        this.semesterRepository = semesterRepository;
    }

    public static Set<Date> getDatesBetween(
            LocalDate startDate, LocalDate endDate, DayOfWeek dayOfWeek, IntervalType interval) {
        List<LocalDate> allDatesBetween = startDate.datesUntil(endDate)
                .filter(date -> date.getDayOfWeek() == dayOfWeek )
                .collect(Collectors.toList());

        Set<LocalDate> returnDates = new HashSet<>();

        int month = 0;

        for (int i = 0; i < allDatesBetween.size(); i++) {
            if (interval == IntervalType.BIWEEKLY) {
                if ( i % 2 != 0) {
                    continue;
                }
            }
            if (interval == IntervalType.MONTHLY) {
                if (month == allDatesBetween.get(i).getMonthValue()) {
                    continue;
                } else {
                    month = allDatesBetween.get(i).getMonthValue();
                }
            }
            returnDates.add(allDatesBetween.get(i));
        }
        return returnDates.stream()
                .map(temp -> {
                    Date date = new Date();
                    date.setLocalDate(temp);
                    return date;})
                .collect(Collectors.toSet());
    }

    public List<CourseResponseDto> getAllCourses(Lecturer lecturer) {
       return courseRepository
                .findByLecturer(lecturer)
                .stream()
                .map(mapper::entityToDto)
                .map(dto -> {dto.setDates(getDatesBetween(dto.getSemester().getStartDate(),dto.getSemester().getEndDate(),dto.getWeekday(),dto.getInterval()).stream().map(mapper::dateEntityToLocalDate).collect(Collectors.toSet())); return  dto;})
                .collect(Collectors.toList());
    }

    public CourseResponseDto getCourseById(Long id, Lecturer lecturer) {
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if(optionalCourse.isEmpty())
            return null;
        if(!Objects.equals(optionalCourse.get().getLecturer(), lecturer) )
            return null;

        return optionalCourse.map(mapper::entityToDto)
                .map(dto -> {dto.setDates(getDatesBetween(dto.getSemester().getStartDate(),dto.getSemester().getEndDate(),dto.getWeekday(),dto.getInterval()).stream().map(mapper::dateEntityToLocalDate).collect(Collectors.toSet())); return  dto;})
                .get();
    }


    public CourseResponseDto createCourse(CourseRequestDto courseDto, Lecturer lecturer) {
        Semester semester = semesterRepository.findById(courseDto.getSemester()).orElseThrow();
        Course course = mapper.dtoToEntity(courseDto);
        course.setDates(getDatesBetween(semester.getStartDate(),semester.getEndDate(),course.getWeekday(),course.getInterval()));
        course.setLecturer(lecturer);
        course.setSemester(semester);
        return mapper.entityToDto(courseRepository.save(course));
    }

    public CourseResponseDto updateCourse(Long id, CourseRequestDto courseDto) {
        Course course = courseRepository.findById(id).orElseThrow();
        course = mapper.updateEntityFromDto(courseDto, course);
        Semester semester = semesterRepository.findById(courseDto.getSemester()).orElseThrow();
        course.setDates(getDatesBetween(semester.getStartDate(),semester.getEndDate(),course.getWeekday(),course.getInterval()));
        course.setSemester(semester);
        courseRepository.save(course);
        return mapper.entityToDto(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }


    public Long getLecturerByCourseId(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        return optionalCourse.map(course -> course.getLecturer().getLecturerId()).orElseThrow();
    }
}
