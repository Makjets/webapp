package tech.stark.webapp.controller.error;

public class JsonFormatException extends RuntimeException{
    public JsonFormatException(String message) {
        super(message);
    }
}
