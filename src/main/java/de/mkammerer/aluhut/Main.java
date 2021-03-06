package de.mkammerer.aluhut;

import com.amazon.ask.servlet.ServletConstants;
import de.mkammerer.aluhut.i18n.I18N;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        log.info("Started");
        Options options = new Options();
        options.addOption("p", "port", true, "Port to run the server on [8080]");
        options.addOption("h", "host", true, "Host to bind to [0.0.0.0]");
        options.addOption(null, "disable-signature-verification", false, "Disables signature request validation");
        options.addOption(null, "debug", false, "Enable debug log");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, args);
        } catch (ParseException e) {
            log.error("Failed to parse command line", e);
            new HelpFormatter().printHelp("maechtiger-aluhut", options);
            System.exit(1);
            return;
        }

        try {
            run(commandLine);
        } catch (Exception e) {
            log.error("Uncaught exception", e);
            System.exit(1);
        } finally {
            log.info("Stopped");
        }
    }

    private static void run(CommandLine commandLine) throws Exception {
        int port = Integer.parseInt(commandLine.getOptionValue("port", "8080"));
        String host = commandLine.getOptionValue("host", "0.0.0.0");
        log.info("Starting server on {}:{}", host, port);
        boolean debug = commandLine.hasOption("debug");
        if (debug) {
            log.info("Debug mode activated");
        }

        // Maximum worker threads 200, minimum 0. This ensures that Jetty releases all worker threads after some idle time
        ThreadPool threadPool = new QueuedThreadPool(200, 0);
        Server server = new Server(threadPool);

        disableUselessHeaders(server, host, port);
        configureSkillServlet(commandLine);

        ServletContextHandler handler = new ServletContextHandler();
        handler.addServlet(new ServletHolder(new AluhutSkillServlet(new I18N(), debug)), "/maechtiger-aluhut");
        handler.addServlet(new ServletHolder(new HelloServlet()), "/");

        server.setHandler(handler);
        server.start();
        server.join();
    }

    private static void configureSkillServlet(CommandLine commandLine) {
        if (commandLine.hasOption("disable-signature-verification")) {
            log.info("Disabling signature and timestamp verification");
            System.setProperty(ServletConstants.TIMESTAMP_TOLERANCE_SYSTEM_PROPERTY, Long.toString(ServletConstants.MAXIMUM_TOLERANCE_MILLIS));
            System.setProperty(ServletConstants.DISABLE_REQUEST_SIGNATURE_CHECK_SYSTEM_PROPERTY, "true");
        }
    }

    private static void disableUselessHeaders(Server server, String host, int port) {
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSendServerVersion(false);
        httpConfig.setSendXPoweredBy(false);
        HttpConnectionFactory httpFactory = new HttpConnectionFactory(httpConfig);
        ServerConnector httpConnector = new ServerConnector(server, httpFactory);
        httpConnector.setHost(host);
        httpConnector.setPort(port);
        server.setConnectors(new Connector[]{httpConnector});
    }
}
