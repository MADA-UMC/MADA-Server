package com.umc.mada.file.service;

import com.umc.mada.file.domain.File;
import com.umc.mada.file.dto.FileRequestDto;
import com.umc.mada.file.dto.FileResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.umc.mada.file.repository.FileRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public FileResponseDto createFile(FileRequestDto fileRequestDto) {
        // 생성된 파일을 반환
        File file = new File();
        file.setName(fileRequestDto.getName());
        File savedFile = fileRepository.save(file);
        return convertToResponseDto(savedFile);
    }

    public FileResponseDto getFileById(int id) {
        // 파일 ID에 해당하는 파일을 반환
        File file = fileRepository.findFileById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 파일 ID입니다."));
        return convertToResponseDto(file);
    }

    public List<FileResponseDto> getAllFiles() {
        // 모든 파일을 조회하여 FileResponseDto 리스트로 변환
        List<File> files = fileRepository.findAll();
        return files.stream().map(this::convertToResponseDto).collect(Collectors.toList());
    }

    public void deleteFileById(int id) {
        // 파일 ID에 해당하는 파일을 삭제
        fileRepository.deleteById(id);
    }

    private FileResponseDto convertToResponseDto(File file) {
        return new FileResponseDto(file.getId(), file.getName());
    }
}
