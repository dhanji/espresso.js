package espresso.web.template;

import com.petebevin.markdown.MarkdownProcessor;
import espresso.CsCompiler;
import espresso.Options;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class Templates {
  private static final String VIEWS_DIR = Options.get("views") == null
      ? "views/"
      : Options.get("views") + "/";

  public static String markdown(String name) {
    MarkdownProcessor processor = new MarkdownProcessor();

    // Load template (don't do this in production mode)
    String template = CsCompiler.load(VIEWS_DIR + name + ".markdown");
    return processor.markdown(template);
  }
}
