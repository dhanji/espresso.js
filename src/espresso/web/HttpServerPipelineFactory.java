package espresso.web;

import com.google.common.collect.Maps;
import espresso.CsCompiler;
import org.apache.commons.io.FileUtils;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
class HttpServerPipelineFactory implements ChannelPipelineFactory, ScriptLoader {
  private final CsCompiler compiler = new CsCompiler();
  private volatile Map<String, String> namesAndJs;

  HttpServerPipelineFactory() {
    loadApp();
  }

  public ChannelPipeline getPipeline() throws Exception {
    // Create a default pipeline implementation.
    ChannelPipeline pipeline = Channels.pipeline();

    // Uncomment the following line if you want HTTPS
    //SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
    //engine.setUseClientMode(false);
    //pipeline.addLast("ssl", new SslHandler(engine));

    pipeline.addLast("decoder", new HttpRequestDecoder());
    // Uncomment the following line if you don't want to handle HttpChunks.
    //pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
    pipeline.addLast("encoder", new HttpResponseEncoder());
    // Remove the following line if you don't want automatic content compression.
//    pipeline.addLast("deflater", new HttpContentCompressor());
    pipeline.addLast("handler", new ScriptRequestHandler(compiler, this));
    return pipeline;
  }

  private void loadApp() {

    // Load script files.
    @SuppressWarnings("unchecked")
    Collection<File> collection = FileUtils.listFiles(new File("."), new String[]{"js", "coffee"},
        true);

    namesAndJs = Maps.newHashMap();
    for (File file : collection)
      reload(file); {
    }
  }

  @Override
  public void reload(String...files) {
    for (String name : files) {
      reload(new File(name + ".coffee"));
    }
  }

  private void reload(File file) {
    String js;// Coffee scripts need to be compiled.
    if (file.getName().endsWith(".coffee")) {
      try {
        js = compiler.compile(file);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      // Super geto hack to strip automatic coffee script wrapping.
      if (js.startsWith("({"))
        js = js.substring(2, js.length() - 3);
    } else
      return;

    // Slurp all scripts into the global scope.
//      Context.enter();
//      compiler.evaluate(file.getName(), js, ImmutableMap.<String, Object>of());
//      Context.exit();

    // Now for for exec using Rhino.
    namesAndJs.put(file.getName().split("\\.")[0], js);
  }

  @Override
  public String get(String name) {
    return namesAndJs.get(name);
  }
}