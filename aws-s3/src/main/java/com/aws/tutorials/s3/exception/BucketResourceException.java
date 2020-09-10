package com.aws.tutorials.s3.exception;

public class BucketResourceException extends RuntimeException {

    public BucketResourceException(String message) {
        super(message);
    }

    public BucketResourceException(String message, Exception exception) {
        super(message, exception);
    }
}
