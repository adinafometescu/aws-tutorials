package com.aws.tutorials.sqs.listener;

import com.aws.tutorials.sqs.model.MyPOJO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MySqsListener {

    @SqsListener(value = "${queue.name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveMessage(MyPOJO pojo) {
        log.info("Received message: {}", pojo.getMessage());
    }

}
