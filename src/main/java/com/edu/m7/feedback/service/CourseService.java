package com.edu.m7.feedback.service;

import com.edu.m7.feedback.model.IntervalType;
import com.edu.m7.feedback.model.entity.Course;
import com.edu.m7.feedback.model.entity.Date;
import com.edu.m7.feedback.model.entity.Lecturer;
import com.edu.m7.feedback.model.entity.Semester;
import com.edu.m7.feedback.model.mapping.CourseDtoMapper;
import com.edu.m7.feedback.model.mapping.EvaluationDtoMapper;
import com.edu.m7.feedback.model.repository.CourseRepository;
import com.edu.m7.feedback.model.repository.DateRepository;
import com.edu.m7.feedback.model.repository.EvaluationRepository;
import com.edu.m7.feedback.model.repository.SemesterRepository;
import com.edu.m7.feedback.payload.request.CourseRequestDto;
import com.edu.m7.feedback.payload.response.CourseResponseDto;
import com.edu.m7.feedback.payload.response.EvaluationResponseDto;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private static final CourseDtoMapper mapper = Mappers.getMapper(CourseDtoMapper.class);
    private static final EvaluationDtoMapper evaluationMapper = Mappers.getMapper(EvaluationDtoMapper.class);
    private final CourseRepository courseRepository;
    private final SemesterRepository semesterRepository;
    private final EvaluationRepository evaluationRepository;
    private final DateRepository dateRepository;
    private EvaluationService evaluationService;

    public CourseService(
            CourseRepository courseRepository,
            SemesterRepository semesterRepository,
            EvaluationRepository evaluationRepository,
            DateRepository dateRepository,
            EvaluationService evaluationService) {
        this.courseRepository = courseRepository;
        this.semesterRepository = semesterRepository;
        this.evaluationRepository = evaluationRepository;
        this.dateRepository = dateRepository;
        this.evaluationService = evaluationService;
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
        setDatesForCourse(semester, course);
        return mapper.entityToDto(courseRepository.save(course));
    }

    public CourseResponseDto updateCourse(Long id, CourseRequestDto courseDto) {
        Course course = courseRepository.findById(id).orElseThrow();
        mapper.updateEntityFromDto(courseDto, course);
        Semester semester = semesterRepository.findById(courseDto.getSemester()).orElseThrow();
        course.setSemester(semester);
        courseRepository.save(course);

        dateRepository.deleteAll(course.getDates());
        setDatesForCourse(semester, course);
        return mapper.entityToDto(course);
    }

    private void setDatesForCourse(Semester semester, Course course) {
        getDatesBetween(
                semester.getStartDate(),
                semester.getEndDate(),
                course.getWeekday(),
                course.getInterval()
        ).forEach((Date date) -> {
            date.setCourse(course);
            dateRepository.save(date);
        });
        if (course.getFinalEvaluationDate().isEqual(LocalDate.now())
                || course.getFinalEvaluationDate().isBefore(LocalDate.now())) {
            evaluationService.createSemesterEvaluationForCourse(course);
        } else {
            evaluationService.deleteActiveSemesterEvaluation(course);
        }
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public CourseResponseDto getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow();
        course.setDates(getDatesBetween(course.getSemester().getStartDate(), course.getSemester().getEndDate(), course.getWeekday(), course.getInterval()));
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

    public List<String> getAllDates(Lecturer lecturer) {
        // get all the courses
        List<Course> courses = courseRepository.findByLecturer(lecturer);

        List<LocalDate> dates = new ArrayList<>();
        for (Course c : courses) {
            // get set<date> of each course
            Set<Date> currentCourseDates = c.getDates();
            // get each Date object from the set
            for (Date d : currentCourseDates) {
                // get the LocalDate of each date and extract it into our list of dates
                dates.add(d.getLocalDate());
            }
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return dates.stream()
                .map(date -> date.format(formatter))
                .collect(Collectors.toList());
    }


    public List<CourseResponseDto> getPreviousThreeCourses(Lecturer lecturer) {
        // get all the courses associated with the Lecturer
        List<Course> courses = courseRepository.findByLecturer(lecturer);

        // get today's date
        LocalDate today = LocalDate.now();

        // get all the Dates of the upcoming courses
        List<Date> courseDates = courses.stream()
                .sorted((a, b) -> b.getTimeEnd().compareTo(a.getTimeEnd()))
                .flatMap(course -> course.getDates().stream())
                .filter((Date date) ->
                        (date.getLocalDate().equals(today) && date.getCourse().getTimeEnd().isBefore(LocalTime.now()))
                                || date.getLocalDate().isBefore(today))
                .sorted((a, b) -> b.getLocalDate().compareTo(a.getLocalDate()))
                .limit(3)
                .collect(Collectors.toList());

        List<Course> prevCourses = new ArrayList<>();
        for (Date courseDate : courseDates) {
            Course course = courseRepository.findById(courseDate.getCourse().getId()).orElseThrow();
            prevCourses.add(course);
        }

        return prevCourses.stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }

    public List<CourseResponseDto> getNextThreeCourses(Lecturer lecturer) {

        // get all the courses associated with the Lecturer
        List<Course> courses = courseRepository.findByLecturer(lecturer);

        // get today's date
        LocalDate today = LocalDate.now();

        List<Date> courseDates = courses.stream()
                .sorted(Comparator.comparing(Course::getTimeStart))
                .flatMap(course -> course.getDates().stream())
                .filter((Date date) ->
                        (date.getLocalDate().equals(today) && date.getCourse().getTimeEnd().isAfter(LocalTime.now()))
                                || date.getLocalDate().isAfter(today)
                )
                .sorted(Comparator.comparing(Date::getLocalDate))
                .limit(3)
                .collect(Collectors.toList());

        List<Course> nextCourses = new ArrayList<>();
        for (Date courseDate : courseDates) {
            nextCourses.add(courseDate.getCourse());
        }

        return nextCourses.stream()
                .map(mapper::entityToDto)
                .collect(Collectors.toList());
    }
}
