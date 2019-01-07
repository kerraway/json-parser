package com.github.kerraway.jsonparser.parser;

import com.github.kerraway.jsonparser.exception.JsonParseException;
import com.github.kerraway.jsonparser.tokenizer.TokenHolder;
import com.github.kerraway.jsonparser.tokenizer.TokenType;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * @author kerraway
 * @date 2019/1/7
 */
public class Parser {

  private TokenHolder tokenHolder;

  public Object parse(TokenHolder tokenHolder) {
    this.tokenHolder = tokenHolder;
    return parse();
  }

  private Object parse() {
    return null;
  }

  /**
   * Assert token type, if actual is invalid, throw {@link JsonParseException}.
   *
   * @param expected
   * @param actual
   */
  private void assertTokenType(TokenType[] expected, TokenType actual) {
    Objects.requireNonNull(expected);
    Objects.requireNonNull(actual);

    int expectedCode = expected[0].getCode();
    for (int i = 1; i < expected.length; i++) {
      expectedCode = expectedCode | expected[i].getCode();
    }
    if ((actual.getCode() & expectedCode) == 0) {
      throw new JsonParseException(MessageFormat.format("Unexpected token type: {}.", actual));
    }
  }
}
