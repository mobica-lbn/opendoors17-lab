package com.mobica.cloud.aws;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sqs.AmazonSQS;
import org.apache.http.client.methods.HttpPut;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link OpenDoorsLambda}
 */
@RunWith(MockitoJUnitRunner.class)
public class OpenDoorsLambdaTest {

	private static final String CSV = "col1,col2,col3\n" +
									  "1, \"description1\", true\n" +
									  "2, description2, false\n" +
									  "3, description3, true";

	private final static String BUCKET_NAME = "openDoors17-bucket-name";

	private final static String KEY = "openDoors17.key.csv";

	private static final String TOPIC_NAME = "openDoors17";

	@Mock
	private AmazonSQS sqs;

	@Mock
	private AmazonS3 s3;

	@Mock
	private S3Event s3Event;

	@Mock
	private Context context;

	@Mock
	private AWSCredentialsProvider credentialsProvider;

	@Mock
	private S3EventNotification.S3EventNotificationRecord s3EventNotificationRecord;

	@Spy
	private List<S3EventNotification.S3EventNotificationRecord> rec = new ArrayList<>();

	@Mock
	private S3EventNotification.S3Entity s3Entity;

	@Mock
	private S3EventNotification.S3BucketEntity s3BucketEntity;

	@Mock
	private S3EventNotification.S3ObjectEntity s3ObjectEntity;

	@Mock
	private LambdaLogger lambdaLogger;

	@Mock
	private S3Object s3Object;

	@InjectMocks
	private OpenDoorsLambda lambda = new OpenDoorsLambda();

	@Before
	public void setUp() throws Exception {
		final Field f3 = lambda.getClass().getDeclaredField("credentialsProvider");
		f3.setAccessible(true);
		f3.set(lambda, credentialsProvider);
		final Field f = lambda.getClass().getDeclaredField("sqs");
		f.setAccessible(true);
		f.set(lambda, sqs);
		final Field f2 = lambda.getClass().getDeclaredField("s3");
		f2.setAccessible(true);
		f2.set(lambda, s3);

		rec.add(s3EventNotificationRecord);

		when(s3Event.getRecords()).thenReturn(rec);
		when(rec.get(0)).thenReturn(s3EventNotificationRecord);
		when(rec.size()).thenReturn(1);
		when(s3EventNotificationRecord.getS3()).thenReturn(s3Entity);
		when(s3Entity.getBucket()).thenReturn(s3BucketEntity);
		when(s3Entity.getObject()).thenReturn(s3ObjectEntity);
		when(s3BucketEntity.getName()).thenReturn(BUCKET_NAME);
		when(s3ObjectEntity.getKey()).thenReturn(KEY);
		when(context.getLogger()).thenReturn(lambdaLogger);
		when(s3.getObject(any(GetObjectRequest.class))).thenReturn(s3Object);
		when(s3Object.getObjectContent()).thenReturn(new S3ObjectInputStream(new ByteArrayInputStream(CSV.getBytes()), new HttpPut()));
	}

	@Test
	public void testLambdaFunctionality() {
		lambda.handleRequest(s3Event, context);

		verify(s3).getObject(anyObject());
		verify(sqs).createQueue(TOPIC_NAME);
		verify(sqs, times(3)).sendMessage(anyObject());
		verify(s3).deleteObject(anyObject());
	}

	@Test
	public void testLambdaConsole() {
		when(s3Event.getRecords()).thenReturn(emptyList());

		lambda.handleRequest(s3Event, context);

		verify(s3, never()).getObject(anyObject());
		verify(sqs, never()).createQueue(TOPIC_NAME);
		verify(sqs, never()).sendMessage(anyObject());
		verify(s3, never()).deleteObject(anyObject());
	}
}