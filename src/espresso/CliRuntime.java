package espresso;

import com.google.common.collect.ImmutableMap;
import org.mozilla.javascript.Context;

import java.io.IOException;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class CliRuntime {
  public static void main(String...args) throws IOException {
    // Load coffee program.
    String file = args[0];

    CsCompiler compiler = new CsCompiler();
    String compiled = compiler.compile(file);

    Context.enter();
    compiler.evaluate(file, compiled, ImmutableMap.<String, Object>of());
    Context.exit();
  }
}
