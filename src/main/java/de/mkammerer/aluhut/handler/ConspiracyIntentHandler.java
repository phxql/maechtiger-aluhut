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
import com.amazon.ask.model.Response;
import de.mkammerer.aluhut.i18n.I18N;
import de.mkammerer.aluhut.util.IterableUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static com.amazon.ask.request.Predicates.intentName;

@Slf4j
public class ConspiracyIntentHandler implements RequestHandler {
    private final I18N i18N;

    public ConspiracyIntentHandler(I18N i18N) {
        this.i18N = i18N;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("ConspiracyIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        log.info("handle()");

        ResourceBundle bundle = ResourceBundle.getBundle("aluhut-facts", I18N.parseLocale(input.getRequestEnvelope().getRequest().getLocale()));
        String randomFact = randomElement(bundle);

        String moreFacts = i18N.getString(input, I18N.Key.MORE_FACTS);

        return input.getResponseBuilder()
            .withSpeech(randomFact + " " + moreFacts)
            .withReprompt(moreFacts)
            .build();
    }

    private String randomElement(ResourceBundle bundle) {
        Set<String> keys = bundle.keySet();
        int element = ThreadLocalRandom.current().nextInt(keys.size());
        String key = IterableUtil.nthElement(keys, element);

        return bundle.getString(key);
    }
}
