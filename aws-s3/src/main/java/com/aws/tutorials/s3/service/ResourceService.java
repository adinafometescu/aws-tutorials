package com.aws.tutorials.s3.service;

import com.aws.tutorials.s3.data.ResourceDetails;
import com.aws.tutorials.s3.data.UploadResourceRequest;
import com.aws.tutorials.s3.exception.BucketResourceException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public interface ResourceService {

    <T> T getResource(String path, TypeReference<T> type) throws BucketResourceException;

    <T> T getResource(String path, Class<T> type) throws BucketResourceException;

    <T> List<ResourceDetails<T>> getResourcesFromDir(String bucket, String dir, TypeReference<T> type) throws BucketResourceException;

    void upload(UploadResourceRequest uploadResourceRequest) throws BucketResourceException;

}
