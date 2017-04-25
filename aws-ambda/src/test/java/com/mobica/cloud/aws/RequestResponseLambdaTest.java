package com.mobica.cloud.aws;

import com.amazonaws.services.lambda.runtime.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RequestResponseLambdaTest {

	@Mock
	private Context context;

	@InjectMocks
	private RequestResponseLambda lambda;

	@Test
	public void testEmptyLambdaWithNull() {
		final MyResponse response = lambda.handleRequest(null, context);

		assertNotNull(response);
		assertTrue(new MyResponse("[null]->[null]").equals(response));
	}

	@Test
	public void testEmptyLambdaWithEmpty() {
		final MyResponse response = lambda.handleRequest(new MyRequest(), context);

		assertNotNull(response);
		assertTrue(new MyResponse("[null]->[null]").equals(response));
	}

	@Test
	public void testEmptyLambdaWithString() {
		final MyResponse response = lambda.handleRequest(new MyRequest("myKey", "myValue"), context);

		assertNotNull(response);
		assertTrue(new MyResponse("[myKey]->[myValue]").equals(response));
	}

}