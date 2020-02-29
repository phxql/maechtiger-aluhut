package de.mkammerer.aluhut;

import com.amazon.ask.Skill;
import com.amazon.ask.builder.SkillConfiguration;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.handler.adapter.impl.BaseHandlerAdapter;
import com.amazon.ask.request.handler.chain.impl.BaseRequestHandlerChain;
import com.amazon.ask.request.mapper.GenericRequestMapper;
import com.amazon.ask.request.mapper.impl.BaseRequestMapper;
import com.amazon.ask.servlet.SkillServlet;
import de.mkammerer.aluhut.handler.ConspiracyIntentHandler;
import de.mkammerer.aluhut.handler.DebugHandler;
import de.mkammerer.aluhut.handler.HelpIntentHandler;
import de.mkammerer.aluhut.handler.LaunchRequestHandler;
import de.mkammerer.aluhut.handler.SessionEndedRequestHandler;
import de.mkammerer.aluhut.handler.StopIntentHandler;
import de.mkammerer.aluhut.handler.YesIntentHandler;
import de.mkammerer.aluhut.i18n.I18N;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class AluhutSkillServlet extends SkillServlet {
    public AluhutSkillServlet(I18N i18N, boolean debug) {
        super(buildSkill(i18N, debug));
    }

    private static Skill buildSkill(I18N i18N, boolean debug) {
        final ConspiracyIntentHandler conspiracyIntentHandler = new ConspiracyIntentHandler(i18N);
        List<RequestHandler> handlers = new ArrayList<>(List.of(
            new StopIntentHandler(i18N),
            conspiracyIntentHandler,
            new YesIntentHandler(conspiracyIntentHandler),
            new HelpIntentHandler(i18N),
            new LaunchRequestHandler(i18N),
            new SessionEndedRequestHandler()
        ));

        if (debug) {
            log.debug("Registering debug handler");
            // Debug handler must be first in the chain
            handlers.add(0, new DebugHandler());
        }

        return new Skill(SkillConfiguration.builder()
            .withSkillId("amzn1.ask.skill.789b02dc-3997-4f0d-8182-140aec7d9296")
            .withRequestMappers(buildMappers(handlers))
            .withHandlerAdapters(Collections.singletonList(new BaseHandlerAdapter<>(RequestHandler.class)))
            .build()
        );
    }

    private static List<GenericRequestMapper<HandlerInput, Optional<Response>>> buildMappers(List<RequestHandler> handlers) {
        // Wtf is wrong with those API designers...
        var chain = handlers.stream()
            .map(handler -> BaseRequestHandlerChain.<HandlerInput, Optional<Response>>builder().withRequestHandler(handler).build())
            .collect(Collectors.toList());

        return Collections.singletonList(BaseRequestMapper.<HandlerInput, Optional<Response>>builder()
            .withRequestHandlerChains(chain)
            .build());
    }
}
