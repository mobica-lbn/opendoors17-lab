# Open Day Mobica 2017 - AWS Lab

## General informations
Required environment:
* Maven
* Java SE JDK v1.8
* Internet Connection
* Git
* IDE ;)


## Simple Storage S3 Endpoint

### The goal:
Build endpoint that obtains file and stores this file in AWS S3 storage.

### Step to do:
1. Clone application template from: https://github.com/mobica-lbn/opendoors17-aws.git
```bash
git clone https://github.com/mobica-lbn/opendoors17-aws.git
```
2. Checkout branch for S3
```bash
git checkout aws-s3
```
3. Go to directory src\main\resources and create file with name application.properties
4. Open file application.properties and to them lines:
```
AWS_ACCESS_KEY_ID=xxxxx
AWS_SECRET_ACCESS_KEY=xxxxx
AWS_REGION=us-east-1
```
Where xxxxx it proper secret values for each Amazon account.
5. In directory src\main\java\hello in AWSController.java is missing some lines.
Missing lines are marked as TODO. 
    1. Go to line 43. In this line you need new S3Object which can return function getObject from AmazonS3. Function getObject needs new GetObjectRequest where in constructor you should pass bucket name and file name.
    2. Go to line 70. You should use there function deleteObject from AmazonS3, pass  bucket name and file name.
    3. Go to line 92. Here you need object of ObjectListing which return function listObjects from AmazonS3. As parameter type “new ListObjectsRequest().withBucketName(bucketName)”
    4. Go to line 127. You should use there function putObject from AmazonS3, pass  new object PutObjectRequest as parameter. PutObjectRequest’s constructor needs bucket name, string key to write / found file in bucket and file to upload. 
6. If you did all instructions from TODO comments then run command mvn clean install from directory which contain file pom.xml.
7. Go to directory target and run application by command
```bash
java -jar mobica-aws-s3-0.1.0.jar
 ```
8. In your browser type http://localhost:8080/ or http://127.0.0.1:8080/ to connect with application.

## AWS Lambda serverless

### The goal:

### Steps to do:

#### Opendoors17-lab create your own lambda function
* clone opendoors17-lab github project: 	
    - https://github.com/mobica-lbn/opendoors17-lab.git
    - git@github.com:mobica-lbn/opendoors17-lab.git
* checkout aws-lambda branch, from opendoors17-lab directory write:
    - git checkout aws-lambda
* create aws-lambda module with your IDE	
* point out pom.xml in your maven project
* look at requesthandler methods in EmptyLambda and RequsetResponseLambda classes
* look at the tests 	for lambdas implementation
* create jar package with lambdas using maven
    - mvn clean package	
* aws-lambda-1.0-SNAPSHOT.jar should be created in target directory

#### Deploy your function in AWS:
* login to the AWS console, choose “Lambda” from “Services” section	
* Select blueprint: create a Lambda Function, choose “Blank Function” 	
* Configure triggers: choose a trigger invokes function (API Gateway)	
* Configure function:
    - name
    - runtime: **jdk8**
    - code entity type
    - upload function package
    - handler packet: package with class name
+ Review: Create function

#### Test your lambda in AWS console:
+ click “Actions” button and choose “Configure test event”
+ choose “Hello Word” in Common section from Sample event template
+ write your text (String 	or json)
+ invoke “Test”
+ analise “Execution result”

#### Test your lambda as an http endpoint:
+ click “Triggers” button and choose Method: ANY
+ click your lambda in APIs list and Resources
+ select your lambda and choose Actions > Create method
+ select http method e.g. PUT, fill “region” (us-east-1) and “Lambda Function” and click “Save” button
+ click “TEST”
+ repeat test with different request body or request parameters 	 	


#### Test your lambda as a scheduled event:
+ click “Triggers” button and “Add trigger” (delete last trigger)
+ select CloudWatch Events – Schedule
+ create “Rule name” and optionally “Rule description”
+ choose Schedule expression, e.g. rare(1 minute) or cron expression
+ check “Enable trigger” and “Submit”
+ check logs, statistics and metrics

## AWS SQS communication

### The goal:
The main purpose of this exercise is to make application responsible for receiving sqs messages and putting them into dynamodb. To achieve it follow with steps below:

### Steps to do:
1. Setup environment variables:
    + AWS_ACCESS_KEY
    + AWS_SECRET_KEY
    + AWS_REGION

2. Add method to `SqsMessageListener` responsible for receiving sqs messages. Sqs message is a string json format. This message should be passed to `SqsService#processMessage`. To verify if method is implemented correctly run `SqsServiceTest`. Name of SQS should be placed in `application.properties`

3. Add code to `DbMessageRepository#saveMessage` responsible for saving received message from sqs in dynamo db. To verify if method is implemented correctly run `DbServiceTest`

4. To verify if module works properly run `AwsSqsApplicationTests`

## Reading data from DynamoDB

### The goal:
Implement two methods in **DynamoDBServiceImpl** to read from DynamoDB using AWS API.

### Endpoints:
* http://localhost:8080/id/{id}
* http://localhost:8080/id/all


### Know-how:

Two interfaces to use:
* [AmazonDynamoDB](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/dynamodbv2/AmazonDynamoDB.html)
* [DynamoDB](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/dynamodbv2/document/DynamoDB.html)

Methods:
* getItem - returns Item for given PrimaryKey in table
* scan - returns List<Map<String, AttributeValue>> (key-value) for given table

Tips:
* [GetItemSpec](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/dynamodbv2/document/spec/GetItemSpec.html)
* [ScanRequest](http://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/com/amazonaws/services/dynamodbv2/model/ScanRequest.html)
* don't forget to provide table name and credentials in properties file
