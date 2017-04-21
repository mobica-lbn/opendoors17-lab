package com.mobica.cloud.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static java.lang.String.format;
import static java.lang.System.getenv;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.splitByWholeSeparatorPreserveAllTokens;

/**
 * Simple example of AWS Lambda, it reads file from s3 bucket and each line push into SQS topic
 * Should be defined env variables: LAMBDA_AWS_ACCESS_KEY, LAMBDA_AWS_SECRET_KEY and optionally LAMBDA_AWS_REGION
 */
public class OpenDoorsLambda implements RequestHandler<S3Event, Object> {

    private static final String DEFAULT_REGION = "us-east-1";
    private static final String DEFAULT_SQS_TOPIC_NAME = "opendoors17-dev-test";

    private AWSCredentialsProvider credentialsProvider;
    private AmazonSQS sqs;
    private AmazonS3 s3;

    @Override
    public Object handleRequest(S3Event event, Context context) {
        initConnections(context);

        if(event != null && event.getRecords() != null) {
            for (final S3EventNotificationRecord rec : event.getRecords()) {
                final String bucket = rec.getS3().getBucket().getName();
                final String key = rec.getS3().getObject().getKey();
                final String topic = extractTopicFromKey(key);
                createSqsTopicIfNecessary(topic, context);

                context.getLogger().log(format("Found: bucket: %s, key: %s, -> topic: %s", bucket, key, topic));

                final S3Object s3obj = s3.getObject(new GetObjectRequest(bucket, key));

                final BufferedReader reader = new BufferedReader(new InputStreamReader(s3obj.getObjectContent()));
                String line;
                List<String> headers = emptyList();
                try {
                    while ((line = reader.readLine()) != null) {
                        if(headers.isEmpty()) {
                            headers = asList(line.split(",")).stream().map(StringUtils::trim).collect(toList());
                            continue;
                        }
                        context.getLogger().log(format("Line to send: %s", line));
                        final String json = prepareJson(line, headers);
                        sqs.sendMessage(new SendMessageRequest(topic, json));
                        context.getLogger().log(format("Line: %s was sent to SQS", json));
                    }
                } catch(IOException e) {
                    context.getLogger().log(format("IOException: %s", e.getMessage()));
                } finally {
                    try {
                        reader.close();
                        s3obj.close();
                    } catch(IOException iox) {
                        context.getLogger().log(format("IOException on close streams: %s", iox.getMessage()));
                    }
                }
                s3.deleteObject(new DeleteObjectRequest(bucket, key));
                context.getLogger().log(format("Deleted: %s %s", bucket, key));
            }
        }
        return null;
    }

	/**
     * Create connections with s3 and sqs, be sure that env variables LAMBDA_AWS_ACCESS_KEY, LAMBDA_AWS_SECRET_KEY are defined
     * and optionally LAMBDA_AWS_REGION
     *
     * @param context
     */
    private void initConnections(Context context)  {
        if(credentialsProvider == null){
            final String accessKey = getenv("LAMBDA_AWS_ACCESS_KEY");
            final String secretKey = getenv("LAMBDA_AWS_SECRET_KEY");
            final String region = defaultIfBlank(getenv("LAMBDA_AWS_REGION"), DEFAULT_REGION);

            credentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));

            s3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(credentialsProvider)
                    .withRegion(region)
                    .build();

            sqs = AmazonSQSClientBuilder.standard()
                    .withCredentials(credentialsProvider)
                    .withRegion(region)
                    .build();

            context.getLogger().log("Initialize s3 and sqs");
        }
    }

	/**
     * Creates new topic on SQS
     *
     * @param topic
     * @param context
     */
    private void createSqsTopicIfNecessary(String topic, Context context) {
        try {
            sqs.createQueue(topic);
            context.getLogger().log(format("Created topic: %s", topic));
        } catch(Exception e) {
            context.getLogger().log(format("Error on create topic: %s", e.getMessage()));
        }
    }

	/**
     * Returns topic name from key (file name)
     *
     * @param key
     * @return
     */
    private static String extractTopicFromKey(String key) {
        return key.contains(".") ? key.split("\\.")[0] : DEFAULT_SQS_TOPIC_NAME;
    }

	/**
     * Converts String line to json based on header
     *
     * @param line
     * @param header
     * @return
     */
    private static String prepareJson(String line, List<String> header) {
        final StringBuilder str = new StringBuilder("{");
        final List<String> list = asList(splitByWholeSeparatorPreserveAllTokens(line, ",")).stream()
                .map(e -> isNotBlank(e) ? e.trim() : e)
                .map(e -> inQuotes(e) ? join("\"", e, "\"") : e)
                .collect(toList());
        range(0, header.size()).forEach(i -> str.append("\"")
                .append(header.get(i)).append("\": ")
				.append(i < list.size() ? list.get(i) : "\"\"").append(i < header.size() - 1 ? ", " : EMPTY));
        return str.append("}").toString();
    }

	/**
     * Checks if add quotes
     *
     * @param e
     * @return
     */
    private static boolean inQuotes(String e) {
        return !("true".equals(e) || "false".equals(e) ||
                isNumeric(e) || (e.startsWith("\"") && e.endsWith("\"")));
    }

}
