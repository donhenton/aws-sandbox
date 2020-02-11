package com.dhenton9000.aws.sdk.s3;

import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

//https://github.com/awsdocs/aws-doc-sdk-examples/blob/ master/javav2/example_code/s3/src/main/java/com/example/s3/S3BucketOps.java
public class S3Explorer {

    public static final Logger LOG = LoggerFactory.getLogger(S3Explorer.class);

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        LOG.debug("beginning program");

        Region region = Region.US_WEST_2;
        S3Client s3 = S3Client.builder().region(region).build();
        // snippet-end:[s3.java2.s3_bucket_ops.region]

        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
        ListBucketsResponse listBucketsResponse = s3.listBuckets(listBucketsRequest);
        listBucketsResponse.buckets().stream().forEach(x -> {
            LOG.debug(x.name());
        });
    }

}
