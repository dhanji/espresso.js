package espresso;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class Options {
  private static volatile Map<String, String> props;

  public static String get(String option) {
    return props.get(option);
  }

  public static Scriptable asFlags() {
    return new ScriptableObject() {
      @Override
      public String getClassName() {
        return "EspressoFlags";
      }

      @Override
      public Object get(String name, Scriptable start) {
        System.out.println("getting " + name);
        return props.get(name);
      }
    };
  }

  public static void set(String[] args) {
    Map<String, String> props = new HashMap<String, String>();
    for (String arg : args) {
      // this is a flag
      if (arg.startsWith("--")) {
        String[] pieces = arg.substring(2).split("=");
        if (pieces.length > 1) {
          props.put(pieces[0], pieces[1]);
        } else {
          props.put(pieces[0], "");
        }
      }
    }

    Options.props = props;
  }
}
