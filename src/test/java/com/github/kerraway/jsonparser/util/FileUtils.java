package com.github.kerraway.jsonparser.util;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * File utils
 *
 * @author 小柯
 * @date 2019/1/7
 */
public class FileUtils {

  /**
   * Reads file and returns string.
   *
   * @param filePath must not be {@literal null}.
   * @param encoding must not be {@literal null}.
   * @return String
   */
  public static String read(String filePath, Charset encoding) {
    Objects.requireNonNull(filePath, "filePath must not be null.");
    Objects.requireNonNull(encoding, "encoding must not be null.");

    InputStream inputStream = getInputStream(filePath);
    try {
      return IOUtils.toString(inputStream, encoding);
    } catch (IOException e) {
      throw new IllegalArgumentException(String.format("Read file error, file path: %s.", filePath), e);
    }
  }

  /**
   * Reads file and returns string.
   *
   * @param filePath must not be {@literal null}.
   * @return String
   */
  public static String read(String filePath) {
    Objects.requireNonNull(filePath, "filePath must not be null.");

    return read(filePath, StandardCharsets.UTF_8);
  }

  /**
   * Reads file and returns input stream.
   *
   * @param filePath must not be {@literal null}.
   * @return InputStream
   */
  private static InputStream getInputStream(String filePath) {
    Objects.requireNonNull(filePath, "filePath must not be null.");

    InputStream inputStream;
    try {
      //use ClassLoader
      ClassLoader classLoader = FileUtils.class.getClassLoader();
      inputStream = classLoader.getResourceAsStream(filePath);
      if (inputStream == null) {
        //use FileInputStream
        inputStream = new FileInputStream(filePath);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException(String.format(
          "Can't open input stream, file path %s may be wrong.", filePath), e);
    }
    return inputStream;
  }

  private FileUtils() {
  }
}
