/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package de.mkammerer.aluhut.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import de.mkammerer.aluhut.i18n.I18N;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

@Slf4j
public class LaunchRequestHandler implements RequestHandler {
    private final I18N i18N;

    public LaunchRequestHandler(I18N i18N) {
        this.i18N = i18N;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        log.info("handle()");

        return input.getResponseBuilder()
            .withSpeech(i18N.getString(input, I18N.Key.WELCOME))
            .withReprompt(i18N.getString(input, I18N.Key.WELCOME_REPROMPT))
            .build();
    }

}
