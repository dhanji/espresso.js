package espresso.lang;

import org.mozilla.javascript.Scriptable;

/**
 * Used to denote a Java service that can be 'required' in JS.
 *
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public interface Requireable {
  void require(Scriptable scope);
}
