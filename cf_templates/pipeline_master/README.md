# Using CodePipeline A Orchestrator For Infrastructure Deploys

## Description

Use one Pipeline to build your infrastructure 

### Build 

one pipeline that will run several Cloudformations:

* create s3 bucket
* create SSM secrets and non secrets
* create a VPC with subnets
* create a bunch of IAM rules

### TearDown

Tear these items down, in the proper order, since they may depend on each other .

## Folders

* deploy -- the contents of the zip file that will be on S3

## Actual Setup

* create s3 bucket with versioning (com.aws.dhenton.pipeline.input)
* create s3 bucket (com.aws.dhenton.pipeline.output)
* zip up the deploy folder into deploy.zip and place into the input S3 bucket
* run pipeline-iam.yaml to setup permissions
* run pipeline-inf.yaml this will run the cloudformation scripts in deploy folder
