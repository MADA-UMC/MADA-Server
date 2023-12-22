package com.umc.mada.timetable.service;

import com.umc.mada.global.BaseResponseStatus;
import com.umc.mada.timetable.domain.Timetable;
import com.umc.mada.timetable.dto.TimetableRequestDto;
import com.umc.mada.timetable.dto.TimetableResponseDto;
import com.umc.mada.timetable.repository.TimetableRepository;
import com.umc.mada.user.domain.User;
import com.umc.mada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimetableService {
    private final TimetableRepository timetableRepository;
    private final UserRepository userRepository;

    @Autowired
    public TimetableService(UserRepository userRepository, TimetableRepository timetableRepository) {
        this.userRepository = userRepository;
        this.timetableRepository = timetableRepository;
    }

    // 시간표 생성 로직
    public TimetableResponseDto createTimetable(User user, TimetableRequestDto timetableRequestDto){
        // 유효성 검사 메서드를 호출하여 해당 ID가 데이터베이스에 존재하는지 확인
        validateUserId(user);
        validateTimetableName(timetableRequestDto.getScheduleName());

        // 시간표 앤티티 생성
        Timetable timetable = new Timetable(user, timetableRequestDto.getDate(), timetableRequestDto.getScheduleName(), timetableRequestDto.getColor(),
                timetableRequestDto.getStartTime(), timetableRequestDto.getEndTime(), timetableRequestDto.getMemo(), timetableRequestDto.getComment());

        // 시간표를 저장하고 저장된 시간표 앤티티 반환
        Timetable savedTimetable = timetableRepository.save(timetable);

        // 저장된 시간표 정보를 기반으로 TimetalbeResponseDto 생성 후 반환
        return new TimetableResponseDto(savedTimetable.getId(), savedTimetable.getDate(), savedTimetable.getScheduleName(), savedTimetable.getColor(),
                savedTimetable.getStartTime(), savedTimetable.getEndTime(), savedTimetable.getMemo(), savedTimetable.getComment());
    }
    @Transactional
    // 시간표 수정 로직
    public TimetableResponseDto updateTimetable(User user, int scheduleId, TimetableRequestDto timetableRequestDto){
        // 유효성 검사 메서드를 호출하여 해당 ID가 데이터베이스에 존재하는지 확인
        validateUserId(user);

        // 주어진 timetableId를 이용하여 카테고리 엔티티 조회
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

        //comment 내용 변경 처리
        if (timetableRequestDto.getComment() != null){
            timetable.setComment(timetableRequestDto.getComment());
        }

        // 수정된 시간표를 저장하고 저장된 투두 엔티티 반환
        Timetable updatedTimetable = timetableRepository.save(timetable);

        // 저장된 투두 정보를 기반으로 timetableRepository 생성하여 반환
        return new TimetableResponseDto(updatedTimetable.getId(), updatedTimetable.getDate(), updatedTimetable.getScheduleName(), updatedTimetable.getColor(),
                updatedTimetable.getStartTime(), updatedTimetable.getEndTime(), updatedTimetable.getMemo(), updatedTimetable.getComment());
    }

    @Transactional
    // 시간표 삭제 로직
    public void deleteTimetable(User userId, int scheduleId){
        // 주어진 scheduleId를 이용하여 투두 엔티티 조회
        Optional<Timetable> optionalTimetable = timetableRepository.deleteTimetableByUserIdAndId(userId, scheduleId);
        if (optionalTimetable.isPresent()){
            timetableRepository.deleteTimetableByUserIdAndId(userId, scheduleId);
        } else {
            throw new IllegalArgumentException(BaseResponseStatus.NOT_FOUND.getMessage());
        }
    }

    // 특정 유저 시간표 조회 로직
    public List<TimetableResponseDto> getUserTimetable(User userId, LocalDate date) {
        List<Timetable> userTimetables = timetableRepository.findTimetablesByUserIdAndDateIs(userId, date);
        // 조회 결과가 존재하는 경우에는 해당 할 일을 TodoResponseDto로 매핑하여 반환
        return userTimetables.stream().map(timetable -> new TimetableResponseDto(timetable.getId(), timetable.getDate(), timetable.getScheduleName(),
                timetable.getColor(), timetable.getStartTime(), timetable.getEndTime(), timetable.getMemo(), timetable.getComment())).collect(Collectors.toList());
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
