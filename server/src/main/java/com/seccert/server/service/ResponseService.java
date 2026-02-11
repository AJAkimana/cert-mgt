package com.seccert.server.service;

import com.seccert.server.dto.common.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

    public <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public ApiResponse<Void> success(String message) {
        return new ApiResponse<>(true, message, null);
    }

    public ApiResponse<Void> failure(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
