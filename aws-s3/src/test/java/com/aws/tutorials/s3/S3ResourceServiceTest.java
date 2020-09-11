package com.aws.tutorials.s3;

import com.aws.tutorials.s3.data.ResourceDetails;
import com.aws.tutorials.s3.data.UploadResourceRequest;
import com.aws.tutorials.s3.exception.BucketResourceException;
import com.aws.tutorials.s3.service.ResourceService;
import com.aws.tutorials.s3.service.S3ResourceService;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@ActiveProfiles("local")
@Testcontainers
public class S3ResourceServiceTest extends AbstractTest {

    private static final String TEST_BUCKET = "test";

    private static ResourceService resourceService = new S3ResourceService(amazonS3);

    @BeforeAll
    public static void init() {
        amazonS3.createBucket(TEST_BUCKET);
    }

    @Test
    @DisplayName("Upload object to s3 -> then download it")
    public void whenUploadS3Object_thenObjectCanBeDownloaded() {
        String content = "{ \"field1\":\"value1\"}";
        String path = TEST_BUCKET + "/testObject.json";
        resourceService.upload(UploadResourceRequest.builder()
                .path(path)
                .resource(content)
                .build());
        String value = resourceService.getResource(path, String.class);
        assertThat(content).isEqualTo(value);
    }

    @Test
    @DisplayName("Upload objects to s3 -> then list directory contains all")
    public void whenUploadS3Objects_thenGetObjectsFromDirContainsThem() {
        String dir = "dir";
        String prefix = TEST_BUCKET + "/" + dir;
        String content = "{ \"field1\":\"value1\"}";
        String path1 = prefix + "/testObject.json";
        String path2 = prefix + "/testObject2.json";

        resourceService.upload(UploadResourceRequest.builder()
                .path(path1)
                .resource(content)
                .build());

        resourceService.upload(UploadResourceRequest.builder()
                .path(path2)
                .resource(content)
                .build());
        List<ResourceDetails<String>> valuesFromDir = resourceService.getResourcesFromDir(TEST_BUCKET, dir, new TypeReference<String>() {
        });
        assertThat(valuesFromDir).hasSize(2);
        valuesFromDir.stream()
                .forEach(resourceDetails -> {
                    assertThat(resourceDetails.getResource()).isEqualTo(content);
                    assertThat(resourceDetails.getPath().startsWith(TEST_BUCKET + dir));
                });
    }

    @Test
    @DisplayName("When resource does not exit -> throw exception")
    public void whenResourceIsMissing_thenThrowException() {
        assertThatThrownBy(() -> resourceService.getResource("test/not-here.json", String.class))
                .isExactlyInstanceOf(BucketResourceException.class);
    }

    @Test
    @DisplayName("When bucket does not exit -> throw exception")
    public void whenBucketIsMissing_thenThrowException() {
        assertThatThrownBy(() -> resourceService.getResource("not-correct/not-here.json", String.class))
                .isExactlyInstanceOf(BucketResourceException.class);
    }
}
