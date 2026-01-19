package com.bank.management.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * Generic API response wrapper.
 *
 * This DTO provides a standardized response format for all API endpoints,
 * including success/failure status, data, and metadata.
 *
 * @param <T> the type of data being returned
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * Response success status.
     */
    private boolean success;

    /**
     * Response message.
     */
    private String message;

    /**
     * Response data.
     */
    private T data;

    /**
     * Error code (for error responses).
     */
    private String errorCode;

    /**
     * Response timestamp.
     */
    private LocalDateTime timestamp;

    /**
     * Request path.
     */
    private String path;

    /**
     * Default constructor.
     */
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Success response constructor.
     *
     * @param message the success message
     * @param data the response data
     */
    public ApiResponse(String message, T data) {
        this();
        this.success = true;
        this.message = message;
        this.data = data;
    }

    /**
     * Error response constructor.
     *
     * @param message the error message
     * @param errorCode the error code
     */
    public ApiResponse(String message, String errorCode) {
        this();
        this.success = false;
        this.message = message;
        this.errorCode = errorCode;
    }

    /**
     * Success response constructor with data only.
     *
     * @param data the response data
     */
    public ApiResponse(T data) {
        this();
        this.success = true;
        this.data = data;
    }

    // Getters and Setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Creates a success response.
     *
     * @param message the success message
     * @param data the response data
     * @param <T> the data type
     * @return the API response
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data);
    }

    /**
     * Creates a success response with data only.
     *
     * @param data the response data
     * @param <T> the data type
     * @return the API response
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    /**
     * Creates an error response.
     *
     * @param message the error message
     * @param errorCode the error code
     * @param <T> the data type
     * @return the API response
     */
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<>(message, errorCode);
    }

    /**
     * Creates an error response with message only.
     *
     * @param message the error message
     * @param <T> the data type
     * @return the API response
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message, null);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}