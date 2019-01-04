package com.github.kerraway.jsonparser.exception;

/**
 * @author kerraway
 * @date 2019/1/4
 */
public class JsonTypeException extends RuntimeException {

  private static final long serialVersionUID = 4927043355209447938L;

  public JsonTypeException(String message) {
    super(message);
  }

  public JsonTypeException(String message, Throwable cause) {
    super(message, cause);
  }
}
