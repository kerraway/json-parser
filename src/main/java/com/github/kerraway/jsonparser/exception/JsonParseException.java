package com.github.kerraway.jsonparser.exception;

/**
 * @author kerraway
 * @date 2019/1/4
 */
public class JsonParseException extends RuntimeException {

  private static final long serialVersionUID = 3890451412693439817L;

  public JsonParseException(String message) {
    super(message);
  }

  public JsonParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
