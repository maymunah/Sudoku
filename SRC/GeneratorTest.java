import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import java.io.IOException;

public class GeneratorTest {

  @Test
  public void testGenerateBasic() {

    Integer dimention = 9;
    InputStream input = new ByteArrayInputStream((dimention + " 3 3 15").getBytes());
    OutputStream output = new ByteArrayOutputStream();


    Generator gen = new Generator();
    try {
      gen.generate(input, output);
    } catch (IOException e) {
      System.out.println("Got IO IOEception: " + e.toString());
    }

    String outputStr = output.toString();
    String[] lines = outputStr.split("\n");

    System.out.println("output: len: " + lines.length);
    System.out.println(outputStr);

    assertEquals(lines.length, dimention + 1);
  }

}
