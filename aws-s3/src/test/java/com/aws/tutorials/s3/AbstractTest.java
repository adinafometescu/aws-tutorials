package com.aws.tutorials.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.aws.tutorials.s3.config.S3DevConfig;
import org.testcontainers.containers.localstack.LocalStackContainer;

public abstract class AbstractTest {

    private static final Integer LOCALSTACK_PORT = 4572;
    private static final LocalStackContainer LOCALSTACK_CONTAINER;

    protected static AmazonS3 amazonS3;

    static {
        LOCALSTACK_CONTAINER = new LocalStackContainer()
                .withServices(LocalStackContainer.Service.S3)
                .withExposedPorts(LOCALSTACK_PORT);

        LOCALSTACK_CONTAINER.start();
        amazonS3 = new S3DevConfig().amazonS3("us-east-1",
                "http://localhost:" + LOCALSTACK_CONTAINER.getMappedPort(LOCALSTACK_PORT));
    }
}
