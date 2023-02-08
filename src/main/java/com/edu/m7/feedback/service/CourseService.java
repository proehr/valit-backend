package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.IntervalType;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Date;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.entity.Semester;
import com.edu.m7.feedback.model.mapping.CourseDtoMapper;
import com.edu.m7.feedback.model.mapping.EvaluationDtoMapper;
import com.edu.m7.feedback.model.repository.*;
import com.edu.m7.feedback.payload.request.CourseRequestDto;
import com.edu.m7.feedback.payload.response.CourseResponseDto;
import com.edu.m7.feedback.payload.response.EvaluationResponseDto;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private static final CourseDtoMapper mapper = Mappers.getMapper(CourseDtoMapper.class);
    private static final EvaluationDtoMapper evaluationMapper = Mappers.getMapper(EvaluationDtoMapper.class);
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;
    private final EvaluationRepository evaluationRepository;
    private final DateRepository dateRepository;
    private final LecturerRepository lecturerRepository;

    public CourseService(
            CourseRepository courseRepository,
            SemesterRepository semesterRepository,
            EvaluationRepository evaluationRepository,
            DateRepository dateRepository,
            LecturerRepository lecturerRepository) {
        this.courseRepository = courseRepository;
        this.semesterRepository = semesterRepository;
        this.evaluationRepository = evaluationRepository;
        this.dateRepository = dateRepository;
        this.lecturerRepository = lecturerRepository;
    }

    public Set<Date> getDatesBetween(
            LocalDate startDate,
            LocalDate endDate,
            DayOfWeek dayOfWeek,
            IntervalType interval
    ) {
        List<LocalDate> allDatesBetween = startDate.datesUntil(endDate)
                .filter(date -> date.getDayOfWeek() == dayOfWeek)
                .collect(Collectors.toList());

        Set<LocalDate> returnDates = new HashSet<>();

        int month = 0;

        for (int i = 0; i < allDatesBetween.size(); i++) {
            if (interval == IntervalType.BIWEEKLY) {
                if (i % 2 != 0) {
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
                .map((LocalDate temp) -> {
                    Date date = new Date();
                    date.setLocalDate(temp);
                    return date;
                })
                .collect(Collectors.toSet());
    }

    public List<CourseResponseDto> getAllCourses(Lecturer lecturer) {
        return courseRepository
                .findByLecturer(lecturer)
                .stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    public CourseResponseDto getCourseById(Long id, Lecturer lecturer) {
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isEmpty()) {
            return null;
        }
        if (!Objects.equals(optionalCourse.get().getLecturer(), lecturer)) {
            return null;
        }

        return optionalCourse.map(mapper::entityToDto)
                .map((CourseResponseDto dto) -> {
                    dto.setDates(getDatesBetween(
                            dto.getSemester().getStartDate(),
                            dto.getSemester().getEndDate(),
                            dto.getWeekday(),
                            dto.getInterval()
                    ).stream().map(mapper::dateEntityToLocalDate).collect(Collectors.toSet()));
                    return dto;
                })
                .orElseThrow();
    }


    public CourseResponseDto createCourse(CourseRequestDto courseDto, Lecturer lecturer) {
        Semester semester = semesterRepository.findById(courseDto.getSemester()).orElseThrow();
        Course course = mapper.dtoToEntity(courseDto);
        course.setLecturer(lecturer);
        course.setSemester(semester);
        courseRepository.save(course);
        getDatesBetween(
                semester.getStartDate(),
                semester.getEndDate(),
                course.getWeekday(),
                course.getInterval()
        ).forEach((Date date) -> {
            date.setCourse(course);
            dateRepository.save(date);
        });
        return mapper.entityToDto(courseRepository.save(course));
    }

    public CourseResponseDto updateCourse(Long id, CourseRequestDto courseDto) {
        Course course = courseRepository.findById(id).orElseThrow();
        mapper.updateEntityFromDto(courseDto, course);
        Semester semester = semesterRepository.findById(courseDto.getSemester()).orElseThrow();
        course.setSemester(semester);
        courseRepository.save(course);

        dateRepository.deleteAll(course.getDates());
        getDatesBetween(
                semester.getStartDate(),
                semester.getEndDate(),
                course.getWeekday(),
                course.getInterval()
        ).forEach((Date date) -> {
            date.setCourse(course);
            dateRepository.save(date);
        });

        return mapper.entityToDto(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public CourseResponseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow();
        course.setDates(getDatesBetween(course.getSemester().getStartDate(),course.getSemester().getEndDate(),course.getWeekday(),course.getInterval()));
        return mapper.entityToDto(course);
    }

    public Long getLecturerByCourseId(Long id) {
        Optional<Course> optionalCourse = courseRepository.findById(id);
        return optionalCourse.map(course -> course.getLecturer().getLecturerId()).orElseThrow();
    }

    public List<EvaluationResponseDto> loadEvaluationsByCourseId(Long id) {
        Course course = courseRepository.findById(id).orElseThrow();
        List<EvaluationResponseDto> evaluations = evaluationRepository.findByCourseOrderByDateDesc(course).stream().map(evaluationMapper::entityToDto).collect(Collectors.toList());
        return evaluations.stream().map(dto -> {
            if (dto.getQuestions().iterator().hasNext()) {
                dto.setParticipants(dto.getQuestions().iterator().next().getAnswers().size());
            } else {
                dto.setParticipants(0);
            }
            return dto;
        }).collect(Collectors.toList());
    }
    public List<String> getAllDates(Lecturer lecturer){
        // get all the courses
        List<Course> courses = courseRepository.findByLecturer(lecturer);

        List<LocalDate> dates = new ArrayList<>();
        for(Course c : courses){
            // get set<date> of each course
            Set<Date> currentCourseDates = c.getDates();
            // get each Date object from the set
            for(Date d: currentCourseDates){
                // get the LocalDate of each date and extract it into our list of dates
                dates.add(d.getLocalDate() );
            }
        }

        if (dates.isEmpty())
            return null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY/MM/DD");
        List<String> formattedDates = dates.stream()
                .map(date -> date.format(formatter))
                .collect(Collectors.toList());

        return formattedDates;
    }


}
