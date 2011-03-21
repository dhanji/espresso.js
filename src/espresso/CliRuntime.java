package espresso;

import com.google.common.collect.ImmutableMap;
import org.mozilla.javascript.Context;

import java.io.IOException;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class CliRuntime {
  public static void main(String...args) throws IOException {
    // Set up options and flags.
    Options.set(args);

    // Load coffee program.
    if (args.length == 0) {
      System.out.println("Usage:\nespresso <file.coffee|js> [flags]");
      System.exit(1);
    }
    String file = args[0];

    CsCompiler compiler = new CsCompiler();
    String compiled = compiler.compile(file);

    Context.enter();
    compiler.evaluate(file, compiled, ImmutableMap.<String, Object>of());
    Context.exit();
  }
}
