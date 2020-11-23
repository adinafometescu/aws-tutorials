package com.aws.tutorials.sqs.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MyPOJO {

    private String message;
}
