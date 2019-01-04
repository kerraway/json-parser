package com.github.kerraway.jsonparser.tokenizer;

/**
 * @author kerraway
 * @date 2019/1/4
 */
public enum TokenType {
  /**
   * {
   */
  BEGIN_OBJECT(1),
  /**
   * }
   */
  END_OBJECT(2),
  /**
   * [
   */
  BEGIN_ARRAY(4),
  /**
   * ]
   */
  END_ARRAY(8),
  /**
   * null
   */
  NULL(16),
  /**
   * number
   */
  NUMBER(32),
  /**
   * string
   */
  STRING(64),
  /**
   * boolean true/false
   */
  BOOLEAN(128),
  /**
   * :
   */
  SEP_COLON(256),
  /**
   * ,
   */
  SEP_COMMA(512),
  /**
   * the end of json document
   */
  END_DOCUMENT(1024);

  /**
   * the code of token
   */
  private int code;

  TokenType(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
