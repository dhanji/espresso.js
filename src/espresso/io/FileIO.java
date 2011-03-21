package espresso.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class FileIO {
  public static String read(File file) throws IOException {
    FileReader reader = new FileReader(file);
    try {
      return IOUtils.toString(reader);
    } finally {
      reader.close();
    }
  }

  public String readResource(String path) throws IOException {
    Reader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(path));
    try {
      return IOUtils.toString(reader);
    } finally {
      reader.close();
    }
  }

  public static Iterator<File> list(File dir) {
    return FileUtils.iterateFiles(dir, null, true);
  }

  public static void copy(InputStream in, OutputStream out) throws IOException {
    try {
      IOUtils.copyLarge(in, out);
    } finally {
      IOUtils.closeQuietly(in);
      IOUtils.closeQuietly(out);
    }
  }

  public static void copy(InputStream in, Writer out) throws IOException {
    try {
      IOUtils.copy(in, out);
    } finally {
      IOUtils.closeQuietly(in);
      IOUtils.closeQuietly(out);
    }
  }
}
