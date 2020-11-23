package com.aws.tutorials.sns.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSAsync;
import com.amazonaws.services.sns.AmazonSNSAsyncClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.model.QueueAttributeName;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;

@Configuration
public class SNSConfig {

    @Value("${cloud.aws.region.static}")
    protected String awsRegion;

    @Value("${localstack.sqs.url}")
    protected String localStackSqsUrl;

    @Bean
    public NotificationMessagingTemplate notificationMessagingTemplate() {
        return new NotificationMessagingTemplate(amazonLocalSNSAsync());
    }
    @Bean
    @Primary
    public AmazonSNSAsync amazonLocalSNSAsync() {
        AmazonSNSAsync amazonSQSAsync = AmazonSNSAsyncClientBuilder
                .standard()
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(localStackSqsUrl, awsRegion))
                .build();
        amazonSQSAsync.createTopicAsync("MyTopic");
        return amazonSQSAsync;
    }
}
