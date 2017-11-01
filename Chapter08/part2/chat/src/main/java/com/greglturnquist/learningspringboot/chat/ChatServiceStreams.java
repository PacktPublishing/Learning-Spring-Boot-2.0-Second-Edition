/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.greglturnquist.learningspringboot.chat;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author Greg Turnquist
 */
// tag::code[]
public interface ChatServiceStreams {

	String NEW_COMMENTS = "newComments";
	String CLIENT_TO_BROKER = "clientToBroker";
	String BROKER_TO_CLIENT = "brokerToClient";

	@Input(NEW_COMMENTS)
	SubscribableChannel newComments();

	@Output(CLIENT_TO_BROKER)
	MessageChannel clientToBroker();

	@Input(BROKER_TO_CLIENT)
	SubscribableChannel brokerToClient();
}
// end::code[]
