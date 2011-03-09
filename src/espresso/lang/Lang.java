package espresso.lang;

import org.mozilla.javascript.NativeJavaMethod;
import org.mozilla.javascript.Scriptable;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class Lang {
  private final Scriptable scope;
  public Lang(Scriptable scope) {
    this.scope = scope;
  }

  public static NativeJavaMethod jsMethod(Class<?> clazz, String method, Class<?>...args) {
    try {
      return new NativeJavaMethod(clazz.getMethod(method, args), method);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException("No such method: " + clazz.getName() + "#" + method, e);
    }
  }
}
