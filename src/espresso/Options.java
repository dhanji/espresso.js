package espresso;

import java.util.Properties;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class Options {
  private static final Properties props = System.getProperties();

  public static String get(String option) {
    return props.getProperty(option);
  }
}
