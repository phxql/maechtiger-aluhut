package de.mkammerer.aluhut.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

@Slf4j
public class YesIntentHandler implements RequestHandler {
    private final ConspiracyIntentHandler conspiracyIntentHandler;

    public YesIntentHandler(ConspiracyIntentHandler conspiracyIntentHandler) {
        this.conspiracyIntentHandler = conspiracyIntentHandler;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        // This could be improved that 'yes' only works after a ConspiracyIntent
        return input.matches(intentName("AMAZON.YesIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        log.info("handle()");

        return conspiracyIntentHandler.handle(input);
    }
}
