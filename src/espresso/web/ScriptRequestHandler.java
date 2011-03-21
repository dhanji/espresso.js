package espresso.web;

import com.google.common.collect.Maps;
import espresso.CsCompiler;
import espresso.web.netty.NettyRequest;
import espresso.web.netty.NettyResponse;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.mozilla.javascript.Context;

import java.util.Map;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
class ScriptRequestHandler extends SimpleChannelUpstreamHandler {
  private final CsCompiler compiler;
  private final ScriptLoader loader;

  public ScriptRequestHandler(CsCompiler compiler, ScriptLoader loader) {
    this.compiler = compiler;
    this.loader = loader;
  }

  @Override
  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
    HttpRequest request = (HttpRequest) e.getMessage();
    NettyResponse response = new NettyResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

    // Set up the appropriate javascript:
    Context.enter();
    Map<String, Object> seeds = Maps.newHashMap();
    seeds.put("request", new NettyRequest(request, e.getChannel()));
    seeds.put("response", response);

    // TODO(dhanji): Make a route config file later.
    // reload our scripts every time in development mode.
    loader.reload("main");

    // We always run main.coffee for the routes, and add dispatch command at the end.
    compiler.evaluate("main", loader.get("main") + "\nespresso.http_dispatch()", seeds);
    Context.exit();

    // Render response!
    Channel channel = e.getChannel();
    ChannelFuture future = channel.write(response);

    // Close the out channel when it is done writing.
    future.addListener(response);
  }
}
