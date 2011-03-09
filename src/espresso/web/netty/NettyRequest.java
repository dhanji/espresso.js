package espresso.web.netty;

import com.google.common.collect.Maps;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.Map;

/**
 * Request wrapper for Netty HTTP requests.
 *
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class NettyRequest extends ScriptableObject {
  private final Map<String, Object> props = Maps.newHashMap();

  public NettyRequest(HttpRequest request, Channel channel) {
    // Convert to local map.
    props.put("uri", request.getUri());
    props.put("method", request.getMethod().toString().toLowerCase());
    props.put("content_length", request.getHeader("Content-Length"));
    props.put("content_type", request.getHeader("Content-Type"));
    props.put("user_agent", request.getHeader("User-Agent"));

    props.put("ip", channel.getRemoteAddress().toString());
//    if (request.getUri())
//    props.put("scheme", request.getUri().substring(0, request.getUri().indexOf(":")));
  }

  @Override
  public Object get(String name, Scriptable start) {
    return props.get(name);
  }

  @Override
  public String getClassName() {
    return "HttpRequest";
  }
}
