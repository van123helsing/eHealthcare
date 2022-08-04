package com.example.appointments.errorhadler;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GlobalErrorHandler implements ErrorController {

    private final ErrorAttributes errorAttributes;

    @RequestMapping(value = "/error")
    public ApiErrorResponse errorHandler(WebRequest request) {
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(request,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE, ErrorAttributeOptions.Include.BINDING_ERRORS));

        int status = (int) attributes.get("status");
        String message = (String) attributes.get("message");
        String path = (String) attributes.get("path");

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(status, message, path);

        if (attributes.containsKey("errors")) {
            List<FieldError> fieldErrors = (List<FieldError>) attributes.get("errors");
            Map<String, String> validationErrors = new HashMap<>();
            for (FieldError fieldError : fieldErrors) {
                validationErrors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
            }
            apiErrorResponse.setValidationErrors(validationErrors);
        }

        return apiErrorResponse;
    }
}
