Tags: java, localstack, lambda

Steps to run the project using localstack:
1. run `mvn clean package shade:shade` to build the project jar

2. Create a new lambda function in Localstack

Open terminal in parent project root (our case aws-tutorials)

`aws --endpoint=http://localhost:4574 lambda create-function --function-name my_handler \
--zip-file fileb://aws-api-gateway/target/aws-api-gateway-1.0-SNAPSHOT.jar \
--handler com.aws.tutorials.gateway.GatewayDemoHandler --runtime java8 --role arn:aws:iam::123456789012:role/lambda-cli-role`

To update the lambda handler:

`aws --endpoint=http://localhost:4574 lambda update-function-code --function-name my_handler \
--zip-file fileb://aws-api-gateway/target/aws-api-gateway-1.0-SNAPSHOT.jar`

3. Check if lambda function is responsive
Check is my handler is available
`aws --endpoint=http://localhost:4574 lambda list-functions`

Invoke it
`aws --endpoint=http://localhost:4574 lambda invoke --function-name my_handler \
--payload '{"name":"name"}'  /dev/stdout`

4. Create gateway rest api

`aws apigateway create-rest-api --name 'API Test' --endpoint-url=http://localhost:4567`


```
{
    "id": "REST_API_ID",
    "name": "API Test",
}
```

`aws apigateway get-resources --rest-api-id REST_API_ID --endpoint-url=http://localhost:4567`

```
{
    "items": [
        {
            "id": "PARENT_ID",
            "path": "/",
            "resourceMethods": {
                "GET": {}
            }
        }
    ]
}

```

`aws apigateway create-resource \
--rest-api-id REST_API_ID \
--parent-id PARENT_ID \
--path-part "test" --endpoint-url=http://localhost:4567`

```
{
    "id": "RESOURCE_ID",
    "parentId": "PARENT_ID",
    "pathPart": "test",
    "path": "/test",
    "resourceMethods": {
        "GET": {}
    }
}

```

`aws apigateway put-method \
  --rest-api-id a5nitqcqwx \
  --resource-id 8b81kn9nms \
  --authorization-type "NONE" \
  --no-api-key-required \
  --http-method POST\
  --endpoint-url=http://localhost:4567`
 
 ```
 {
     "httpMethod": "POST",
     "authorizationType": "NONE"
 }
```

`
aws apigateway put-integration \
 --rest-api-id REST_API_ID \
 --resource-id RESOURCE_ID \
 --http-method POST \
 --type AWS_PROXY \
 --integration-http-method POST \
 --uri arn:aws:apigateway:us-east-1:lambda:path/2015-03-31/functions/ --uri arn:aws:iam::123456789012:function:my_handler/invocations \:function:my_handler/invocations \
 --passthrough-behavior WHEN_NO_MATCH \
 --endpoint-url=http://localhost:4567`
 
 `aws apigateway create-deployment \
  --region us-east-1 \
  --rest-api-id REST_API_ID \
  --stage-name my_stage \
  --endpoint-url=http://localhost:4567`
  
```
{
    "id": "DEPLOYMENT_ID",
    "description": "",
    "createdDate": 1577550074
}
```

`aws apigateway test-invoke-method --rest-api-id a5nitqcqwx \
 --resource-id 8b81kn9nms --http-method POST --path-with-query-string "" \
 --body '{"name":"name"}'\
 --endpoint-url=http://localhost:4567
`
  
`curl -X POST -d "{\"name\":\"create\"}" \
 http://localhost:4567/restapis/a5nitqcqwx/my_stage/_user_request_/test`
  