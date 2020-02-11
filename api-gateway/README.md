# Setting Up Api Gateway

## Configure for proxy for the birt service using api gateway

<https://cloudhut.io/how-to-setup-proxy-passthrough-on-api-gateway-console-cloudformation-55e923d02b9>

* Create the resource pointing to the app on http app on port 9000
* Use the wildcard for pass through

## edge optimized custom apigateway domain

<https://docs.aws.amazon.com/apigateway/latest/developerguide/how-to-edge-optimized-custom-domain-name.html>

Add a DNS record fror the custom api domain

<https://datanextsolutions.com/blog/setup-custom-domain-for-an-api-in-aws-api-gateway/>

* Create an CNAME (not  alias) in route 53 
* name is what you want (fred.bonzo.com)
* value is the cloudfront target domain name that is created when you do a custom domain name in apigateway with edge setup

## the mapped endpoint

https://secbirt.awsdhenton.com/gateway/birt/customers/all


## API gateway notes

* enable CORS by selecting the endpoint with the {any} and then actions Enable CORS
* in the Access-Control-Allow-Headers section you need to add the headers send by your SPA app
* you can get these by looking in Chrome inspector and see the request that fails because of CORS
* in angular case: access-control-allow-headers,access-control-allow-methods,access-control-allow-origin
* the api gate way is mapped to the public endpoint for the service http://birt.awsdhenton.com/{birt}
