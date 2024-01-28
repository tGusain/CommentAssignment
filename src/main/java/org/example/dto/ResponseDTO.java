package org.example.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO<T> {
    Boolean success;
    String error;
    T data;

    public ResponseDTO(T data) {
        this.success = true;
        this.data = data;
    }

    public ResponseDTO(Boolean success, T data) {
        this(data);
        this.success = success;
    }

    public ResponseDTO(T data, Boolean success) {
        this(data);
        this.success = success;
    }

    public ResponseDTO(Boolean success, String errorMessage, T data) {
        this(data, success);
        this.error = errorMessage;
    }
}

