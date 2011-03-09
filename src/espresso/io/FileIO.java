package espresso.io;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author dhanji@gmail.com (Dhanji R. Prasanna)
 */
public class FileIO {
  public String read(String path) throws IOException {
    FileReader reader = new FileReader(new File(path));
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
}
