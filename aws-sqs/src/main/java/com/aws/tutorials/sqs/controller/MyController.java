package com.aws.tutorials.sqs.controller;

import com.aws.tutorials.sqs.model.MyPOJO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class MyController {

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Value("${queue.name:test}")
    protected String queue;

    @PostMapping("/queue")
    public void sendMessage(@RequestBody MyPOJO pojo) {
        log.info("Sending message {} ", pojo.getMessage());
        queueMessagingTemplate.convertAndSend(queue, pojo);
    }
}
