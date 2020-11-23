package com.aws.tutorials.sqs.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MySqsListener {

    @SqsListener("test")
    public void receiveMessage(String message) {
        log.info("Received message: ", message);
    }

}
