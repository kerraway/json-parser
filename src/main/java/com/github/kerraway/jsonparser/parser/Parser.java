package com.github.kerraway.jsonparser.parser;

import com.github.kerraway.jsonparser.JsonArray;
import com.github.kerraway.jsonparser.JsonObject;
import com.github.kerraway.jsonparser.exception.JsonParseException;
import com.github.kerraway.jsonparser.tokenizer.Token;
import com.github.kerraway.jsonparser.tokenizer.TokenHolder;
import com.github.kerraway.jsonparser.tokenizer.TokenType;
import com.github.kerraway.jsonparser.util.Assert;

import java.text.MessageFormat;

/**
 * @author kerraway
 * @date 2019/1/7
 */
public class Parser {

  private TokenHolder tokenHolder;

  /**
   * Does parse from token holder's tokens.
   *
   * @param tokenHolder must not be {@literal null}.
   * @return Object
   */
  public Object parse(TokenHolder tokenHolder) {
    Assert.notNull(tokenHolder, "tokenHolder must not be null.");

    this.tokenHolder = tokenHolder;
    return parse();
  }

  /**
   * Does parse.
   *
   * @return Object
   */
  private Object parse() {
    Token token = tokenHolder.next();
    //null
    if (token == null) {
      return new JsonObject();
    }
    //obj
    if (token.getType() == TokenType.BEGIN_OBJECT) {
      return parseObject();
    }
    //array
    if (token.getType() == TokenType.BEGIN_ARRAY) {
      return parseArray();
    }
    throw new JsonParseException(MessageFormat.format("Unexpected token type: {} at begin.", token.getType()));
  }

  /**
   * Parses object.
   *
   * @return JsonObject
   */
  private JsonObject parseObject() {
    JsonObject jsonObject = new JsonObject();
    TokenType[] expected = {TokenType.STRING, TokenType.END_OBJECT};
    String key = null;
    while (tokenHolder.hasNext()) {
      Token token = tokenHolder.next();
      TokenType tokenType = token.getType();
      assertTokenType(expected, tokenType);
      switch (tokenType) {
        case BEGIN_OBJECT:
          jsonObject.put(key, parseObject());
          expected = new TokenType[]{TokenType.SEP_COMMA, TokenType.END_OBJECT};
          break;
        case END_OBJECT:
          break;
        case BEGIN_ARRAY:
          jsonObject.put(key, parseArray());
          expected = new TokenType[]{TokenType.SEP_COMMA, TokenType.END_OBJECT};
          break;
        case NULL:
          jsonObject.put(key, null);
          expected = new TokenType[]{TokenType.SEP_COMMA, TokenType.END_OBJECT};
          break;
        case NUMBER:
          jsonObject.put(key, parseNumber(token.getValue()));
          expected = new TokenType[]{TokenType.SEP_COMMA, TokenType.END_OBJECT};
          break;
        case BOOLEAN:
          jsonObject.put(key, Boolean.valueOf(token.getValue()));
          expected = new TokenType[]{TokenType.SEP_COMMA, TokenType.END_OBJECT};
          break;
        case STRING:
          Token prevToken = tokenHolder.previous();
          //value
          if (prevToken.getType() == TokenType.SEP_COLON) {
            jsonObject.put(key, token.getValue());
            expected = new TokenType[]{TokenType.SEP_COMMA, TokenType.END_OBJECT};
          }
          //key
          else {
            key = token.getValue();
            expected = new TokenType[]{TokenType.SEP_COLON};
          }
          break;
        case SEP_COLON:
          expected = new TokenType[]{TokenType.NULL, TokenType.NUMBER, TokenType.BOOLEAN,
              TokenType.STRING, TokenType.BEGIN_OBJECT, TokenType.BEGIN_ARRAY};
          break;
        case SEP_COMMA:
          expected = new TokenType[]{TokenType.STRING};
          break;
        case END_DOCUMENT:
          break;
        default:
          throw new JsonParseException(MessageFormat.format("Unexpected token type: {}.", tokenType));
      }
    }
    return jsonObject;
  }

  /**
   * Parses number.
   *
   * @param str must not be {@literal null}.
   * @return Number
   */
  private Number parseNumber(String str) {
    Assert.notNull(str, "str must not be null.");

    if (str.contains(".") || str.contains("e") || str.contains("E")) {
      return Double.valueOf(str);
    }
    Long longVal = Long.valueOf(str);
    if (longVal > Integer.MAX_VALUE || longVal < Integer.MIN_VALUE) {
      return longVal;
    }
    return longVal.intValue();
  }

  private JsonArray parseArray() {
    return null;
  }

  /**
   * Asserts token type, if actual is invalid, throw {@link JsonParseException}.
   *
   * @param expectedTokenTypes must not be {@literal empty}.
   * @param actualTokenType    must not be {@literal null}.
   */
  private void assertTokenType(TokenType[] expectedTokenTypes, TokenType actualTokenType) {
    Assert.notEmpty(expectedTokenTypes, "expectedTokenTypes must not be empty.");
    Assert.notNull(actualTokenType, "actualTokenType must not be null.");

    int expectedCode = expectedTokenTypes[0].getCode();
    for (int i = 1; i < expectedTokenTypes.length; i++) {
      expectedCode = expectedCode | expectedTokenTypes[i].getCode();
    }
    if ((actualTokenType.getCode() & expectedCode) == 0) {
      throw new JsonParseException(MessageFormat.format("Unexpected token type: {}.", actualTokenType));
    }
  }

}
