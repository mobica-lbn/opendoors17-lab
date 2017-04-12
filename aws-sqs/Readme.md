## AWS-SQS

The main purpose of this exercise is to make application responsible for receiving sqs messages and putting them into dynamodb. To achieve it follow with steps below:

1. Setup environment variables:
    + AWS_ACCESS_KEY
    + AWS_SECRET_KEY
    + AWS_REGION

2. Add method to `SqsMessageListener` responsible for receiving sqs messages. Sqs message is a string json format. This message should be passed to `SqsService#processMessage`. To verify if method is implemented correctly run `SqsServiceTest`. Name of SQS should be placed in `application.properties`

3. Add code to `DbMessageRepository#saveMessage` responsible for saving received message from sqs in dynamo db. To verify if method is implemented correctly run `DbServiceTest`

4. To verify if module works properly run `AwsSqsApplicationTests`
