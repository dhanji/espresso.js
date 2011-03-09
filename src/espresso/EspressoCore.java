package espresso;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class EspressoCore {
  private final CsCompiler csCompiler;
  private final Map<String, String> libraries;

  public EspressoCore(CsCompiler csCompiler, Map<String, String> libraries) {
    this.csCompiler = csCompiler;
    this.libraries = libraries;
  }

  public String fetchLib(String name) throws IOException {
    String library = libraries.get(name);
    if (null == library) {
      // Attempt to load this library.
      library = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(name + ".coffee"),
          "UTF-8");

      // Compile it from CS -> JS.
      library = csCompiler.compileRaw(library);
      libraries.put(name, library);
    }
    return library;
  }
}
