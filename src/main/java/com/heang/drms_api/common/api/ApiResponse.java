package com.heang.drms_api.common.api;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @JsonProperty("status")
    private ApiStatus statusCode;

    private T data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Common common;

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse(ApiStatus statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public ApiResponse(ApiStatus statusCode, T data,Common common) {
        this.statusCode = statusCode;
        this.data = data;
        this.common = common;
    }

//    Helper: success with a default message
public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(new ApiStatus(ExitCode.SUCCESS), data);
}

// Helper: success with a customer status message
public static <T> ApiResponse<T> success(ExitCode exitCode, T data) {
    return new ApiResponse<>(new ApiStatus(exitCode), data);

}

// Helper: error with a default message
    public static <T> ApiResponse<T> error(ExitCode exitCode, T data) {
        return new ApiResponse<>(new ApiStatus(exitCode), data);
    }

}
