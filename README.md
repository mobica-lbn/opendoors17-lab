# opendoors17-lab - reading from Dynamo DB

## The goal:
Implement two methods in DynamoDBServiceImpl to read from DynamoDB using AWS API.

## Endpoints:
* http://localhost:8080/id/{id}
* http://localhost:8080/id/all


## Know-how:

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
