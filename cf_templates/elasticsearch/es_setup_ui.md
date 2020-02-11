# Elastic Search Setup in UI

Review
Review the information below, and then choose Confirm and create.

Data nodesEdit
Availability zones :  1-AZ
Instance typet: 2.small.elasticsearch
Number of nodes: 1
Data nodes storage type: EBS
EBS volume type: General Purpose (SSD)
EBS volume size: 10 GiB

Require HTTPS: Enabled
Node-to-node encryption: Disabled
Encryption of data at rest: Disabled
 
Start hour for the daily automated snapshot: 00:00 UTC (default)
 
Allow APIs that can span multiple indices and 
bypass index-specific access policies: Enabled
Fielddata cache allocation: unbounded (default)
Max clause count: 1024 (default)

Network access: VPC access
VPCexperiment | vpc-0f84f5d5bb6ff6abc (10.0.0.0/16)
Security Groups
base-template-1-SecurityGroup-1V7E6XYHE3U7Z | (sg-0e8fb7637a25561b4)
IAM Role: AWSServiceRoleForAmazonElasticsearchService
AZs and Subnets
PrivateSubnetA | subnet-0bc034304c20ea395 (10.0.1.0/24) | us-east-2a

Amazon Cognito for authentication disabled

access policy

{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "AWS": [
          "*"
        ]
      },
      "Action": [
        "es:*"
      ],
      "Resource": "arn:aws:es:us-east-2:235926060045:domain/search-awsdhenton/*"
    }
  ]
}