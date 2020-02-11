# Various Samples Java

## KMS Explorer

* configure credentials via aws configure
* user needs to have permissions to the key
* the arn for the key is stored in mainId.properties at the root,
which IS NOT in source control
* the demo will write out cpt files which ARE NOT in source code

## Named Parameters Store

See ParameterExplorer.java

### Setup

* configure credentials via aws configure
* user needs to have (at least) AmazonSSMReadOnlyAccess Policy
* aws configure will ask for password and secret for the user
* currently code is set to ask for the default configuration
* place a demonstration key at /dev/db/password in the Systems Manager/Parameter Store


## S3 

* user must have AmazonS3ReadOnlyAccess
* configure credentials via aws configure
* aws configure will ask for password and secret for the user

## Java Annotations

https://aws.amazon.com/blogs/developer/storing-json-documents-in-amazon-dynamodb-tables/
http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/dynamodbv2/datamodeling/DynamoDBMapper.html

## Dynamodb

https://docs.aws.amazon.com/sdk-for-java/v2/developer-guide/examples-dynamodb-tables.html
https://github.com/awsdocs/aws-doc-sdk-examples/blob/master/javav2/example_code/dynamodb/src/main/java/com/example/dynamodb/CreateTable.java
https://codurance.com/2019/02/13/working-with-dynamodb/
https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Tools.CLI.html#Tools.CLI.UsingWithDDBLocal
https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Query.html#Query.KeyConditionExpressions
https://www.javacodegeeks.com/2017/10/amazon-dynamodb-tutorial.html

###
1x examples to port: https://github.com/aws-samples/aws-dynamodb-examples/blob/master/src/main/java/com/amazonaws/codesamples/CreateTablesLoadData.java

### Local DynamoDB

A shell will be available at http://localhost:8000/shell

```docker run -p 8000:8000 amazon/dynamodb-local```
``` aws dynamodb list-tables --endpoint-url http://localhost:8000```
```aws dynamodb describe-table --table-name ProductCatalog --endpoint-url http://localhost:8000```
```bash
aws dynamodb query --table-name ProductCatalog --endpoint-url http://localhost:8000 --key-condition-expression \
"Id = :name" \
--expression-attribute-values '{":name" : {"S": "100"}}'
```

```shell
aws dynamodb query --table-name UserPosts --endpoint-url http://localhost:8000 --key-condition-expression \
"userName = :userName" \
--expression-attribute-values '{":userName" : {"S": "dmj130"}}'
```
