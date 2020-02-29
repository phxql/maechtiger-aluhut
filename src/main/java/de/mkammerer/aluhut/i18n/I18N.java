package de.mkammerer.aluhut.i18n;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
public class I18N {
    private static final Locale DEFAULT_LOCALE = Locale.GERMAN;

    public String getString(HandlerInput handlerInput, Key key) {
        return getString(parseLocale(handlerInput.getRequestEnvelope().getRequest().getLocale()), key);
    }

    public String getString(Locale locale, Key key) {
        ResourceBundle bundle = ResourceBundle.getBundle("aluhut", locale);
        return bundle.getString(key.name().toLowerCase());
    }

    public static Locale parseLocale(@Nullable String locale) {
        if (locale == null) {
            log.trace("Locale is null, using default locale '{}'", DEFAULT_LOCALE);
            return DEFAULT_LOCALE;
        }

        log.trace("Parsing locale '{}'", locale);
        return Locale.forLanguageTag(locale);
    }

    public enum Key {
        WELCOME,
        WELCOME_REPROMPT,
        GOODBYE,
        MORE_FACTS
    }
}
