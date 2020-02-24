package de.mkammerer.aluhut;

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

@Slf4j
public class Main {
    public static void main(String[] args) throws Exception {
        log.info("Started");
        Options options = new Options();
        options.addOption("p", "port", true, "Port to run the server on");

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
        log.info("Starting server on port {}", port);

        Server server = new Server();

        disableUselessHeaders(server, port);

        ServletContextHandler handler = new ServletContextHandler();
        ServletHolder servlet = new ServletHolder(new TestServlet(new I18N()));
        handler.addServlet(servlet, "/");

        server.setHandler(handler);
        server.start();
        server.join();
    }

    private static void disableUselessHeaders(Server server, int port) {
        HttpConfiguration httpConfig = new HttpConfiguration();
        httpConfig.setSendServerVersion(false);
        httpConfig.setSendXPoweredBy(false);
        HttpConnectionFactory httpFactory = new HttpConnectionFactory(httpConfig);
        ServerConnector httpConnector = new ServerConnector(server, httpFactory);
        httpConnector.setPort(port);
        server.setConnectors(new Connector[]{httpConnector});
    }
}
