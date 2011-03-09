package espresso.web;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Standalone Async/event-driven HTTP server powered by JBoss Netty.
 *
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class HttpServer {
  public static final int PORT = 8080;

  public static void main(String[] args) {
    // Configure the server.
    ServerBootstrap bootstrap = new ServerBootstrap(
        new NioServerSocketChannelFactory(
            Executors.newCachedThreadPool(),
            Executors.newCachedThreadPool()));

    // Set up the event pipeline factory.
    bootstrap.setPipelineFactory(new HttpServerPipelineFactory());

    // Bind and start to accept incoming connections.
    bootstrap.bind(new InetSocketAddress(PORT));
    System.out.println("Espresso running on port " + PORT);
  }
}

