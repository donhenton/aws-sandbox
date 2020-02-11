# CloudFormation Advanced

## Custom Resources -- Calling Out from CloudFormation for Passwords and Secrets

* <https://www.alexdebrie.com/posts/cloudformation-custom-resources/>
* <https://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-properties-lambda-function-code.html>
* <https://www.alexdebrie.com/posts/cloudformation-macros/>
* <https://sanderknape.com/2018/08/two-years-with-cloudformation-lessons-learned/>

In a cloudformation doc, need to get username and password from Secrets Manager
Use a lambda to fetch the stuff and then 'call' it in the cf template:

```yaml
DatabaseUserID:
    Type: Custom::SSMGetSecureString
    Properties:
        ServiceToken: !ImportValue
        Fn::Sub: 'ItemRefExportedByCFThatDefinesLambda'
        ParameterName: !Sub 'keytoTheSecret'
```

There should be a CF that defines the lambda, and that lambda is specially set up to take stuff in and return stuff to CF

## Bastion Cloudformation basic

* <https://github.com/aws-quickstart/quickstart-linux-bastion>
* <https://docs.aws.amazon.com/quickstart/latest/linux-bastion/welcome.html>