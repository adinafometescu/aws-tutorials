package com.aws.tutorials.sqs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Value("${queue.name:test}")
    protected String queue;

    @GetMapping("/queue")
    public void sendMessage(@RequestParam String message) {
        queueMessagingTemplate.convertAndSend(queue, message);
    }
}
