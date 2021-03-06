package com.github.kerraway.jsonparser.tokenizer;

/**
 * @author kerraway
 * @date 2019/1/4
 */
public class Token {

  private TokenType type;
  private String value;

  public Token(TokenType type) {
    this.type = type;
  }

  public Token(TokenType type, char value) {
    this.type = type;
    this.value = String.valueOf(value);
  }

  public Token(TokenType type, String value) {
    this.type = type;
    this.value = value;
  }

  public TokenType getType() {
    return type;
  }

  public void setType(TokenType type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return "Token{" +
        "type=" + type +
        ", value='" + value + '\'' +
        '}';
  }
}
