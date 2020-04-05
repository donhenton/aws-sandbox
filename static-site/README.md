# Static Site Creation

from <https://github.com/sjevs/cloudformation-s3-static-website-with-cloudfront-and-route-53>
the contents of the site to upload are from  <https://github.com/donhenton/sec-maint>

A certificate was created for sec.awsdhenton.com in the certificate manager. for the _us-east-1_ region 
because cloudfront only works there.

static-website-cloudfront-update.yml is the cloudformation template.

It will produce

* bucket
* cloudfront with index.html excluded from cache
* logging in the main logging bucket. See cf_code_samples_pipeline/logging_bucket.yaml
* permissions set to handle redirects for authentication (see the reference below)

## Bucket permissions for security

<https://www.jeremysimkins.com/thoughts/aws/how-to-host-spa-on-aws-with-s3-and-cloudfront/>

