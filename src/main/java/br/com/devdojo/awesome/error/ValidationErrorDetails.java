/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.devdojo.awesome.error;

/**
 *
 * @author mateus
 */
public class ValidationErrorDetails extends ErrorDetails{
    private String field;
    private String fieldMessage;
    
    public static final class Builder {

        private String title;
        private int status;
        private String detail;
        private long timestamp;
        private String developerMessage;
        private String field;
        private String fieldMessage;

        private Builder() {
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder developerMessage(String developerMessage) {
            this.developerMessage = developerMessage;
            return this;
        }
         public Builder field(String field) {
            this.field = field;
            return this;
        }
          public Builder fieldMessage(String fieldMessage) {
            this.fieldMessage = fieldMessage;
            return this;
        }
        
        

        public ValidationErrorDetails build() {
            ValidationErrorDetails ValidationErrorDetails = new ValidationErrorDetails();
            ValidationErrorDetails.setDeveloperMessage(developerMessage);
            ValidationErrorDetails.setTitle(title);
            ValidationErrorDetails.setDetail(detail);
            ValidationErrorDetails.setTimestamp(timestamp);
            ValidationErrorDetails.setStatus(status);
            ValidationErrorDetails.fieldMessage = fieldMessage;
            ValidationErrorDetails.field = field;
            return ValidationErrorDetails;
        }
    }

    public String getField() {
        return field;
    }

    public String getFieldMessage() {
        return fieldMessage;
    }
}
