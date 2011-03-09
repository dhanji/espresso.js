package espresso.web.netty;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.util.CharsetUtil;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class NettyResponse extends DefaultHttpResponse {
  /**
   * Creates a new instance.
   *
   * @param version the HTTP version of this response
   * @param status  the status of this response
   */
  public NettyResponse(HttpVersion version, HttpResponseStatus status) {
    super(version, status);
  }

  public void setBody(String body) {
    setContent(ChannelBuffers.copiedBuffer(body, CharsetUtil.UTF_8));
    setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=UTF-8");
  }

  public void setStatus(int status, String reason) {
    setStatus(new HttpResponseStatus(status, reason));
    if (getContent() == null) {
      setBody(reason);
    }
  }

  public void setStatus(int status) {
    setStatus(new HttpResponseStatus(status, Integer.toString(status)));
  }
}
