# GITHUB ANGULAR APP TO S3 Bucket website

This will create a angular to s3 website codepipeline/codebuild thing. It uses github authorization
stored in SSM Parameter store

## Github Credentials

These are stored in SSM Parameter store

* /github/oauth -- stores the SecureString version of the github OAuth token (associated with the github user account)
* /github/${RepositoryName}/secret the secret associated with the specific repository 'code-samples' in this case