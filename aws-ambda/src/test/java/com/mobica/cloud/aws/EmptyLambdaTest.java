package com.mobica.cloud.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for {@link EmptyLambda}
 */
@RunWith(MockitoJUnitRunner.class)
public class EmptyLambdaTest {

	@Mock
	private Context context;

	@Mock
	private LambdaLogger lambdaLogger;

	@InjectMocks
	private EmptyLambda lambda;

	@Before
	public void setUp() {
		when(context.getLogger()).thenReturn(lambdaLogger);
	}

	@Test
	public void testEmptyLambdaWithNull() {
		final Object response = lambda.handleRequest(null, context);

		assertNull(response);
		verify(lambdaLogger).log("Execute emptyLambda with parameter: null");
	}

	@Test
	public void testEmptyLambdaWithEmpty() {
		final Object response = lambda.handleRequest("", context);

		assertNull(response);
		verify(lambdaLogger).log("Execute emptyLambda with parameter: ");
	}

	@Test
	public void testEmptyLambdaWithString() {
		final Object response = lambda.handleRequest("anyString", context);

		assertNull(response);
		verify(lambdaLogger).log("Execute emptyLambda with parameter: anyString");
	}
}