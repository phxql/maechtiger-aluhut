package de.mkammerer.aluhut.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class DebugHandler implements RequestHandler {
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        if (log.isDebugEnabled()) {
            try {
                log.debug("Request envelope: {}", mapper.writeValueAsString(handlerInput.getRequestEnvelope()));
            } catch (JsonProcessingException e) {
                log.warn("Failed to serialize to JSON", e);
            }
        }

        return false;
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        return Optional.empty();
    }
}
