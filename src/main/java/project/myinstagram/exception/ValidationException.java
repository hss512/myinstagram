package project.myinstagram.exception;

import java.util.Map;

public class ValidationException extends RuntimeException{
    private Map<String, String> errorMap;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Map<String, String> errorMap) {
        super(message);
        this.errorMap = errorMap;
    }

    public Map<String, String> getErrorMap(){
        return errorMap;
    }
}
