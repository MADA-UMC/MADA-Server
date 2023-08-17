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

}
