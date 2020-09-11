package com.aws.tutorials.s3.config;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!local")
public class S3ProdConfig {

    @Value("${cloud.aws.region.static:eu-west-1}")
    private String awsRegion;

    @Bean
    public AmazonS3 amazonS3() {
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(new ClientConfiguration()
                        .withConnectionTimeout(40000))
                .withRegion(awsRegion)
                .build();
        return s3;
    }
}