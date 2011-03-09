package espresso.web;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
interface ScriptLoader {
  void reload(String...files);

  String get(String name);
}
