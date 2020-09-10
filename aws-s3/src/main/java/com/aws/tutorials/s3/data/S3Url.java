package com.aws.tutorials.s3.data;

import lombok.Value;

@Value
public class S3Url {

    private String bucket;

    private String key;

    public S3Url(String url) {
        url = url.replace("s3://", "");
        url = url.replace("//", "");
        int indexSeparator = url.indexOf("/");
        this.bucket = url.substring(0, indexSeparator);
        this.key = url.substring(indexSeparator + 1);
    }
}
