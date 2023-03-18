package com.project.secretdiary.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.Instant;
import java.util.Date;

@Component
public class ImageUploader {
    @Value("${s3.aws.region}")
    private Regions regions;

    @Value("${s3.aws.bucket}")
    private String bucket;

    @Value("${s3.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${s3.aws.credentials.secretKey}")
    private String secretKey;

    private AmazonS3 getAmazonS3Client() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(regions)
                .build();
    }

    private GeneratePresignedUrlRequest getPreSignedUrlRequest(final String objectKey, final Date expiration,
                                                               final HttpMethod httpMethod) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, objectKey)
                        .withMethod(httpMethod)
                        .withExpiration(expiration);
        return generatePresignedUrlRequest;
    }

    private Date getExpiration() {
        Date expiration = new java.util.Date();
        long expTimeMillis = Instant.now().toEpochMilli();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    public String createPreSignedUrl(final String objectKey, final HttpMethod httpMethod) {
        try {
            AmazonS3 s3Client = getAmazonS3Client();
            Date expiration = getExpiration();
            GeneratePresignedUrlRequest preSignedUrlRequest =
                    getPreSignedUrlRequest(objectKey, expiration, httpMethod);
            URL url = s3Client.generatePresignedUrl(preSignedUrlRequest);
            return url.toString();
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
        return null;
    }

}
