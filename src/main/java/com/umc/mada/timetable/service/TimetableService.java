package com.umc.mada.timetable.service;

import com.umc.mada.category.domain.Category;
import com.umc.mada.global.BaseResponseStatus;
import com.umc.mada.timetable.domain.Comment;
import com.umc.mada.timetable.domain.Timetable;
import com.umc.mada.timetable.dto.CommentRequestDto;
import com.umc.mada.timetable.dto.CommentResponseDto;
import com.umc.mada.timetable.dto.TimetableRequestDto;
import com.umc.mada.timetable.dto.TimetableResponseDto;
import com.umc.mada.timetable.repository.CommentRepository;
import com.umc.mada.timetable.repository.TimetableRepository;
import com.umc.mada.todo.domain.Todo;
import com.umc.mada.timetable.domain.DayOfWeek;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimetableService {
    private final TimetableRepository timetableRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public TimetableService(UserRepository userRepository, TimetableRepository timetableRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.timetableRepository = timetableRepository;
        this.commentRepository = commentRepository;
    }

    // 시간표 일정 생성 로직
    public TimetableResponseDto createTimetable(User user, TimetableRequestDto timetableRequestDto){
        // 유효성 검사 메서드를 호출하여 해당 ID가 데이터베이스에 존재하는지 확인
        validateUserId(user);
        validateTimetableName(timetableRequestDto.getScheduleName());

        // 시간표 앤티티 생성
        Timetable timetable = new Timetable(user, timetableRequestDto.getDate(), timetableRequestDto.getScheduleName(), timetableRequestDto.getColor(),
                timetableRequestDto.getStartTime(), timetableRequestDto.getEndTime(), timetableRequestDto.getMemo(), timetableRequestDto.getIsDeleted(), timetableRequestDto.getDayOfWeek());

        // 시간표를 저장하고 저장된 시간표 앤티티 반환
        Timetable savedTimetable = timetableRepository.save(timetable);

        // 저장된 시간표 정보를 기반으로 TimetalbeResponseDto 생성 후 반환
        return new TimetableResponseDto(savedTimetable.getId(), savedTimetable.getDate(), savedTimetable.getScheduleName(), savedTimetable.getColor(),
                savedTimetable.getStartTime(), savedTimetable.getEndTime(), savedTimetable.getMemo(), savedTimetable.getIsDeleted(), savedTimetable.getDayOfWeek());
    }

    @Transactional
    // 시간표 수정 로직
    public TimetableResponseDto updateTimetable(User user, int scheduleId, TimetableRequestDto timetableRequestDto){
        // 유효성 검사 메서드를 호출하여 해당 ID가 데이터베이스에 존재하는지 확인
        validateUserId(user);

        Timetable timetable = timetableRepository.findTimetableByUserIdAndId(user, scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("NOT_FOUND_ERROR"));

        // 일정 이름 변경 처리
        if (timetableRequestDto.getScheduleName() != null){
            timetable.setScheduleName(timetableRequestDto.getScheduleName());
        }

        // 색상 변경 처리
        if (timetableRequestDto.getColor() != null){
            timetable.setColor(timetableRequestDto.getColor());
        }

        // 일정 시작 시간 변경 처리
        if (timetableRequestDto.getStartTime() != null){
            timetable.setStartTime(timetableRequestDto.getStartTime());
        }

        // 일정 종료 시간 변경 처리
        if (timetableRequestDto.getEndTime() != null){
            timetable.setEndTime(timetableRequestDto.getEndTime());
        }

        // 메모 내용 변경 처리
        if (timetableRequestDto.getMemo() != null){
            timetable.setMemo(timetableRequestDto.getMemo());
        }

        //요일 변경 처리
        if (timetableRequestDto.getDayOfWeek() != null){
            timetable.setDayOfWeek(timetableRequestDto.getDayOfWeek());
        }

        // 수정된 시간표를 저장하고 저장된 투두 엔티티 반환
        Timetable updatedTimetable = timetableRepository.save(timetable);

        // 저장된 투두 정보를 기반으로 timetableRepository 생성하여 반환
        return new TimetableResponseDto(updatedTimetable.getId(), updatedTimetable.getDate(), updatedTimetable.getScheduleName(), updatedTimetable.getColor(),
                updatedTimetable.getStartTime(), updatedTimetable.getEndTime(), updatedTimetable.getMemo(), updatedTimetable.getIsDeleted(), updatedTimetable.getDayOfWeek());
    }

    @Transactional
    // 시간표 일정 삭제 로직
    public void deleteTimetable(User userId, int scheduleId){
        // 주어진 scheduleId를 이용하여 투두 엔티티 조회
        Optional<Timetable> optionalTimetable = timetableRepository.findTimetableByUserIdAndId(userId, scheduleId);
        Timetable timetable = timetableRepository.findTimetableByUserIdAndId(userId, scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("NOT_FOUND_ERROR"));
        if (optionalTimetable.isPresent() && !timetable.getIsDeleted()){
            timetable.setIsDeleted(true);
            timetableRepository.save(timetable);
        } else {
            throw new IllegalArgumentException(BaseResponseStatus.NOT_FOUND.getMessage());
        }
    }

    // 특정 유저 일일시간표 조회 로직
    public List<TimetableResponseDto> getDailyTimetable(User userId, LocalDate date) {
        List<Timetable> DailyTimetables = timetableRepository.findTimetablesByUserIdAndDateIsAndDayOfWeek(userId, date, DayOfWeek.DAILY);
        // 조회 결과가 존재하는 경우에는 해당 할 일을 TodoResponseDto로 매핑하여 반환
        return DailyTimetables.stream().map(timetable -> new TimetableResponseDto(timetable.getId(), timetable.getDate(), timetable.getScheduleName(),
                timetable.getColor(), timetable.getStartTime(), timetable.getEndTime(), timetable.getMemo(), timetable.getIsDeleted(), timetable.getDayOfWeek())).collect(Collectors.toList());
    }

    // 특정 유저 주간시간표 조회 로직
    public List<TimetableResponseDto> getWeeklyTimetable(User userId) {
        List<Timetable> WeeklyTimetables = timetableRepository.findTimetablesByUserIdAndDayOfWeekIsNot(userId, DayOfWeek.DAILY);
        // 조회 결과가 존재하는 경우에는 해당 할 일을 TodoResponseDto로 매핑하여 반환
        return WeeklyTimetables.stream().map(timetable -> new TimetableResponseDto(timetable.getId(), timetable.getDate(), timetable.getScheduleName(),
                timetable.getColor(), timetable.getStartTime(), timetable.getEndTime(), timetable.getMemo(), timetable.getIsDeleted(), timetable.getDayOfWeek())).collect(Collectors.toList());
    }


    // 자정에 주간 시간표의 일정을 일일 시간표로 추가하는 메서드
    @Scheduled(cron="0 0 0 * * ?")
    public void scheduleCheckAndLoadDailyData() {
        List<User> allUsers = userRepository.findAll();

        DayOfWeek today = DayOfWeek.valueOf(LocalDate.now().getDayOfWeek().name());

        for (User user : allUsers) {
            checkAndLoadDailyData(user, today);
        }
    }

    // 주간 시간표의 일정을 기반으로 한 일일 시간표 생성 메서드
    public void checkAndLoadDailyData(User user, DayOfWeek targetDayOfWeek) {
        // 해당 사용자의 주간 시간표 조회
        List<Timetable> weeklyTimetable = timetableRepository.findTimetablesByUserIdAndDayOfWeek(user, targetDayOfWeek);

        // 주간 시간표를 기반으로 한 일일 시간표 생성
        for (Timetable weeklyEntry : weeklyTimetable) {
            LocalDate dailyDate = weeklyEntry.getDate();

            // 생성할 일일 시간표의 날짜가 이미 존재하는지 확인
            if (timetableRepository.existsByUserIdAndDateAndDayOfWeek(user, dailyDate, DayOfWeek.DAILY)) {
                continue;
            }

            // 주간 시간표를 기반으로 한 일일 시간표 생성
            Timetable dailyTimetable = Timetable.builder()
                    .userId(user)
                    .date(dailyDate)
                    .scheduleName(weeklyEntry.getScheduleName())
                    .color(weeklyEntry.getColor())
                    .startTime(weeklyEntry.getStartTime())
                    .endTime(weeklyEntry.getEndTime())
                    .memo(weeklyEntry.getMemo())
                    .isDeleted(weeklyEntry.getIsDeleted())
                    .dayOfWeek(DayOfWeek.DAILY)
                    .build();

            timetableRepository.save(dailyTimetable);
        }
    }

    // comment 생성 로직
    public CommentResponseDto createComment(User user, CommentRequestDto commentRequestDto){
        validateUserId(user);

        Comment comment = new Comment(user, commentRequestDto.getDate(), commentRequestDto.getContent());
        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(savedComment.getId(), savedComment.getDate(), savedComment.getContent());
    }

    @Transactional
    // comment 수정 로직
    public CommentResponseDto updateComment (User user, LocalDate date, CommentRequestDto commentRequestDto){
        validateUserId(user);

        Comment comment = commentRepository.findCommentByUserIdAndDateIs(user, date)
                .orElseThrow(() -> new IllegalArgumentException("NOT_FOUND_ERROR"));

        // 날짜 변경 처리
        if (commentRequestDto.getDate() != null){
            comment.setDate(commentRequestDto.getDate());
        }

        // 내용 변경 처리
        if (commentRequestDto.getContent() != null){
            comment.setContent(commentRequestDto.getContent());
        }

        Comment updatedComment = commentRepository.save(comment);
        return new CommentResponseDto(updatedComment.getId(), updatedComment.getDate(), updatedComment.getContent());
    }


    // 특정 유저 comment 조회 로직
    public CommentResponseDto getUserComment(User user, LocalDate date) {
        validateUserId(user);

        Comment comment = commentRepository.findCommentByUserIdAndDateIs(user, date)
                .orElseThrow(() -> new IllegalArgumentException("NOT_FOUND_ERROR"));

        return new CommentResponseDto(comment.getId(), comment.getDate(), comment.getContent());
    }

    // 투두 이름 유효성 검사 메서드
    private void validateTimetableName(String timetableName) {
        if (timetableName == null || timetableName.isEmpty()) {
            throw new IllegalArgumentException(BaseResponseStatus.REQUEST_ERROR.getMessage());
        }
    }

    // 유저 ID 유효성 검사 메서드
    private void validateUserId(User userId) {
        Optional<User> optionalUser = userRepository.findUserById(userId.getId());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 유저 ID입니다.");
        }
    }
}
