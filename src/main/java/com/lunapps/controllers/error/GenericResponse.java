package com.lunapps.controllers.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * Created by olegchorpita on 7/13/17.
 */
@Data
public class GenericResponse {
    private String message;
    private String error;
    private String code;

    public GenericResponse(final String message) {
        super();
        this.message = message;
    }

    public GenericResponse(final String message, final String error) {
        super();
        this.message = message;
        this.error = error;
    }

    public GenericResponse(String message, String error, String code) {
        super();
        this.message = message;
        this.error = error;
        this.code = code;
    }

    public GenericResponse(final List<FieldError> fieldErrors, final List<ObjectError> globalErrors) {
        super();
        final ObjectMapper mapper = new ObjectMapper();
        try {
            this.message = mapper.writeValueAsString(fieldErrors);
            this.error = mapper.writeValueAsString(globalErrors);
        } catch (final JsonProcessingException e) {
            this.message = "";
            this.error = "";
        }
    }
}