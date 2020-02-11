# Code Samples

This is the cloud formation documents for the samples.awsdhenton.com website

## Github Credentials

These are stored in SSM Parameter store

* /github/oauth -- stores the SecureString version of the github OAuth token (associated with the github user account)
* /github/${RepositoryName}/secret the secret associated with the specific repository 'code-samples' in this case

## Files

* code_samples_pipeline.yaml -- defines the working code pipeline which pushes to the bucket on a github site change
* code_samples_s3.yaml -- defines the website bucket, cloudfront, and logging
* code_samples_website.yaml -- create the spa bucket, and cloudfront with logging


## Logging

<https://docs.aws.amazon.com/AmazonCloudFront/latest/DeveloperGuide/AccessLogs.html>
