/*
 * Copyright 2013-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.aws.messaging.support;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.cloud.aws.core.support.documentation.RuntimeUse;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NotificationMessageArgumentResolverTest {

	@Test
	public void supportsParameter_withNotificationMessageMethodParameter_shouldReturnTrue() throws Exception {
		// Arrange
		NotificationMessageArgumentResolver notificationMessageArgumentResolver = new NotificationMessageArgumentResolver();
		Method methodWithNotificationMessageArgument = this.getClass().getDeclaredMethod("methodWithNotificationMessageArgument", String.class);
		MethodParameter methodParameter = new MethodParameter(methodWithNotificationMessageArgument, 0);

		// Act
		boolean result = notificationMessageArgumentResolver.supportsParameter(methodParameter);

		// Assert
		assertTrue(result);
	}

	@SuppressWarnings("EmptyMethod")
	@RuntimeUse
	private void methodWithNotificationMessageArgument(@NotificationMessage String message) {
	}

	@Test
	public void supportsParameter_withWrongMethodParameter_shouldReturnFalse() throws Exception {
		// Arrange
		NotificationMessageArgumentResolver notificationMessageArgumentResolver = new NotificationMessageArgumentResolver();
		Method methodWithMissingAnnotation = this.getClass().getDeclaredMethod("methodWithMissingAnnotation", String.class);
		MethodParameter methodParameter = new MethodParameter(methodWithMissingAnnotation, 0);

		// Act
		boolean result = notificationMessageArgumentResolver.supportsParameter(methodParameter);

		// Assert
		assertFalse(result);
	}

	@SuppressWarnings("EmptyMethod")
	@RuntimeUse
	private void methodWithMissingAnnotation(String test) {
	}

	@Test
	public void supportsParameter_withWrongParameterType_shouldReturnFalse() throws Exception {
		// Arrange
		NotificationMessageArgumentResolver notificationMessageArgumentResolver = new NotificationMessageArgumentResolver();
		Method methodWithWrongParameterType = this.getClass().getDeclaredMethod("methodWithWrongParameterType", Long.class);
		MethodParameter methodParameter = new MethodParameter(methodWithWrongParameterType, 0);

		// Act
		boolean result = notificationMessageArgumentResolver.supportsParameter(methodParameter);

		// Assert
		assertFalse(result);
	}

	@SuppressWarnings("EmptyMethod")
	@RuntimeUse
	private void methodWithWrongParameterType(@NotificationMessage Long test) {
	}

	@Test
	public void resolveArgument_withValidMessagePayload_shouldReturnNotificationMessage() throws Exception {
		// Arrange
		NotificationMessageArgumentResolver notificationMessageArgumentResolver = new NotificationMessageArgumentResolver();
		Method methodWithNotificationMessageArgument = this.getClass().getDeclaredMethod("methodWithNotificationMessageArgument", String.class);
		MethodParameter methodParameter = new MethodParameter(methodWithNotificationMessageArgument, 0);

		ObjectNode jsonObject = JsonNodeFactory.instance.objectNode();
		jsonObject.put("Type", "Notification");
		jsonObject.put("Message", "Hello World!");
		String payload = jsonObject.toString();
		Message<String> message = MessageBuilder.withPayload(payload).build();

		// Act
		Object result = notificationMessageArgumentResolver.resolveArgument(methodParameter, message);

		// Assert
		assertTrue(String.class.isInstance(result));
		assertEquals("Hello World!", result);
	}

}