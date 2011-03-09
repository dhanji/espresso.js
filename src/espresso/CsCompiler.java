/*
 * Copyright 2011 Dhanji R. Prasanna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package espresso;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Map;

/**
 * Based on David Yeung's https://github.com/yeungda/jcoffeescript/
 */
public class CsCompiler {
  public static final String ESPRESSO_BASE = "espresso_base";
  private final Scriptable globalScope;

  // Coffeescript/espresso platform libraries.
  private final Map<String, String> libraries = Maps.newHashMap();
  private final EspressoCore coreJsObject = new EspressoCore(this, libraries);

  public CsCompiler() {
    InputStream inputStream = CsCompiler.class.getResourceAsStream("coffee-script.js");
    try {
      try {
        Reader reader = new InputStreamReader(inputStream, "UTF-8");
        try {
          Context context = Context.enter();
          context.setOptimizationLevel(-1); // Without this, Rhino hits a 64K bytecode limit and fails
          try {
            globalScope = context.initStandardObjects();
            context.evaluateReader(globalScope, reader, "coffee-script.js", 0, null);

            // Load base import.
            libraries.put(ESPRESSO_BASE, compileRaw(readEspressoBase(), globalScope));
          } finally {
            Context.exit();
          }
        } finally {
          reader.close();
        }
      } catch (UnsupportedEncodingException e) {
        throw new Error(e); // This should never happen
      } finally {
        inputStream.close();
      }
    } catch (IOException e) {
      throw new Error(e); // This should never happen
    }
  }

  private static String readEspressoBase() throws IOException {
    return IOUtils.toString(CliRuntime.class.getResourceAsStream(ESPRESSO_BASE + ".coffee"));
  }

  public String compileRaw(String coffeeScriptSource, Scriptable scope) {
    Context context = Context.enter();
    try {
      Scriptable compileScope = context.newObject(scope);
      compileScope.setParentScope(globalScope);
      compileScope.put("coffeeScriptSource", compileScope, coffeeScriptSource);
      try {
        return (String) context.evaluateString(compileScope,
            "CoffeeScript.compile(coffeeScriptSource, { bare: true });",
            "CsCompiler", 0, null);
      } catch (JavaScriptException e) {
        throw new RuntimeException(e);
      }
    } finally {
      Context.exit();
    }
  }

  public String compile(String coffeeScriptFile) throws IOException {
    return compileRaw(load(coffeeScriptFile), globalScope);
  }

  public String compileRaw(String library) {
    return compileRaw(library, globalScope);
  }

  public String compile(File file) throws IOException {
    return compileRaw(load(file), globalScope);
  }

  public static String load(String file) {
    try {
      return load(new File(file));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String load(File file) throws IOException {
    BufferedReader fileReader = new BufferedReader(new FileReader(file));
    StringBuilder builder;
    try {
      builder = new StringBuilder();
      while (fileReader.ready()) {
        builder.append(fileReader.readLine());
        builder.append("\n");
      }
      return builder.toString();

    } finally {
      fileReader.close();
    }
  }

  public static String load(URL url) {
    try {
      return Resources.toString(url, Charsets.UTF_8);
    } catch (IOException e) {
      return null;
    }
  }

  public Scriptable evaluate(String name, String script, Map<String, Object> seeds) {
    // Execute the compiled JS in Rhino.
    Context context = Context.getCurrentContext();
    try {
      ScriptableObject scope = context.initStandardObjects();

      // Load standard library into object space (provides "require" etc.)
      String base = libraries.get(ESPRESSO_BASE);
      context.evaluateString(scope, base, ESPRESSO_BASE, 0, null);

      // Add the ability to look up libraries.
      ScriptableObject.putProperty(scope, "espresso", Context.javaToJS(coreJsObject, scope));

      for (Map.Entry<String, Object> entry : seeds.entrySet()) {
        ScriptableObject.putProperty(scope, entry.getKey(),
            Context.javaToJS(entry.getValue(), scope));
      }

      // Begin core library execution scope.
      context.evaluateString(scope, script, name, 0, null);
      return scope;
    } catch (JavaScriptException e) {
      e.printStackTrace();
      return null;
    }
  }

  public Object call(String function, Object... args) {
    // Execute the compiled JS in Rhino.
    Context context = Context.enter();
    try {
      ScriptableObject scope = context.initStandardObjects();
      scope.setParentScope(globalScope);

      Object o = scope.get(function, scope);
      if (!(o instanceof Function)) {
        throw new RuntimeException(function + " was not a function: " + Context.toString(o));
      }

      Function callee = (Function) o;
      return callee.call(context, scope, scope, args);

    } catch (JavaScriptException e) {
      e.printStackTrace();
      return null;
    } finally {
      Context.exit();
    }
  }
}
