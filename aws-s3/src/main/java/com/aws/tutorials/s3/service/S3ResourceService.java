package com.aws.tutorials.s3.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.aws.tutorials.s3.data.ResourceDetails;
import com.aws.tutorials.s3.data.S3Url;
import com.aws.tutorials.s3.data.UploadResourceRequest;
import com.aws.tutorials.s3.exception.BucketResourceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Slf4j
public class S3ResourceService implements ResourceService {

    private AmazonS3 amazonS3;

    @Override
    public <T> T getResource(String path, TypeReference<T> type) {
        if (path == null || !path.contains("/")) {
            throw new BucketResourceException(String.format("Malformed url %s, it must have format s3://bucket/key", path));
        }
        path = path.replaceAll("//", "/");
        S3Url s3Url = new S3Url(path);
        try {
            S3Object s3object = amazonS3.getObject(s3Url.getBucket(), s3Url.getKey());
            return convertResource(s3object.getObjectContent(), type);
        } catch (SdkClientException e) {
            log.error("Unable to download resource {} from s3 {} ", path, e.getMessage());
            throw new BucketResourceException(String.format("Unable to download resource %s from s3 ", s3Url.getKey()), e);
        }
    }

    private <T> T convertResource(InputStream inputStream, TypeReference<T> type) {
        try {
            if (type.getType().getTypeName().equals(String.class.getTypeName())) {
                return (T) IOUtils.toString(inputStream);
            }
            if (type.getType().getTypeName().equals(byte[].class.getTypeName())) {
                return (T) IOUtils.toByteArray(inputStream);
            }
            return Utils.objectMapper().readValue(inputStream, type);
        } catch (IOException e) {
            throw new BucketResourceException(String.format("Unable to download resource"), e);
        }
    }

    @Override
    public <T> T getResource(String url, Class<T> type) {
        return getResource(url, new TypeReference<T>() {
            @Override
            public Type getType() {
                return type;
            }
        });
    }

    @Override
    public <T> List<ResourceDetails<T>> getResourcesFromDir(String bucket, String dir, TypeReference<T> type) throws BucketResourceException {
        return amazonS3.listObjects(bucket, dir)
                .getObjectSummaries()
                .stream()
                .map(s3ObjectSummary -> getResourceDetails(bucket, s3ObjectSummary.getKey(), type))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void upload(UploadResourceRequest uploadResourceRequest) throws BucketResourceException {
        try {
            S3Url s3Url = new S3Url(uploadResourceRequest.getPath());
            amazonS3.putObject(s3Url.getBucket(), s3Url.getKey(), serializeObject(uploadResourceRequest.getResource()), null);
        } catch (SdkClientException e) {
            log.error("Unable to upload resource {} from s3 {} ", uploadResourceRequest.getPath(), e.getMessage());
            throw new BucketResourceException(String.format("Unable to upload resource %s ", uploadResourceRequest.getPath()), e);
        }
    }

    private InputStream serializeObject(Object object) {
        if (object instanceof InputStream) {
            return (InputStream) object;
        }
        if (object instanceof byte[]) {
            return new ByteArrayInputStream((byte[]) object);
        }
        String serializedObject;
        if (object instanceof String) {
            serializedObject = (String) object;
        } else {
            try {
                serializedObject = Utils.objectMapper().writeValueAsString(object);
            } catch (JsonProcessingException e) {
                throw new BucketResourceException(String.format("Unable to upload resource"), e);
            }
        }
        return new ByteArrayInputStream(serializedObject.getBytes(StandardCharsets.UTF_8));
    }

    private <T> ResourceDetails<T> getResourceDetails(String bucket, String path, TypeReference<T> typeReference) {
        if (path.endsWith("/")) {
            return null;
        }
        try {
            return (ResourceDetails<T>)
                    ResourceDetails.builder()
                            .path(path)
                            .resource(getResource(bucket + "/" + path, typeReference))
                            .build();
        } catch (BucketResourceException bucketResourceException) {
            log.warn("Unable to find resource ", bucketResourceException);
            return null;
        }
    }
}
