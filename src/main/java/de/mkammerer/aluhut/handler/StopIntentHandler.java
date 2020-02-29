package de.mkammerer.aluhut.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import de.mkammerer.aluhut.i18n.I18N;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

@Slf4j
public class StopIntentHandler implements RequestHandler {
    private final I18N i18N;

    public StopIntentHandler(I18N i18N) {
        this.i18N = i18N;
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(
            intentName("AMAZON.StopIntent")
                .or(intentName("AMAZON.CancelIntent"))
                .or(intentName("AMAZON.NoIntent"))
        );
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        log.info("handle()");

        return input.getResponseBuilder()
            .withSpeech(i18N.getString(input, I18N.Key.GOODBYE))
            .build();
    }
}
