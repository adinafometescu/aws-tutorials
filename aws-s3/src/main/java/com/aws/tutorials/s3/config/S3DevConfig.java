package com.aws.tutorials.s3.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class S3DevConfig {

    @Bean
    public AmazonS3 amazonS3(@Value("${cloud.aws.region.static:us-east-1}") String awsRegion,
                             @Value("${localstack.s3.url:http://localhost:4572}") String localstackS3Url) {
        return AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(new ClientConfiguration()
                        .withConnectionTimeout(40000))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("mock", "mock")))
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(localstackS3Url, awsRegion))
                .build();
    }
}