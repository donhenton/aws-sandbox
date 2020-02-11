# Cross Stack Use in CloudFormation

<https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/walkthrough-crossstackref.html>

## SampleNetworkCrossStack

builds a simple VPC that will be referenced the webapp.

## SampleWebAppCrossStack

references to the network stack are of the form

```json

 "GroupSet": [{ "Fn::ImportValue": { "Fn::Sub": "${NetworkStackName}-SecurityGroupID" } }],

```

This allows referring to one stacks resources in another stack

