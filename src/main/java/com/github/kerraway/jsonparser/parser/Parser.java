package com.github.kerraway.jsonparser.parser;

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

  /**
   * Common expected for json object: {@link TokenType#SEP_COMMA} or {@link TokenType#END_OBJECT}
   */
  private static final int COMMON_EXPECTED_FOR_JSON_OBJECT = TokenType.SEP_COMMA.getCode() | TokenType.END_OBJECT.getCode();
  /**
   * Common expected for json object: {@link TokenType#SEP_COMMA} or {@link TokenType#END_ARRAY}
   */
  private static final int COMMON_EXPECTED_FOR_JSON_ARRAY = TokenType.SEP_COMMA.getCode() | TokenType.END_ARRAY.getCode();

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
    throw new JsonParseException(MessageFormat.format("Unexpected token type: {0} at begin.", token.getType()));
  }

  /**
   * Parses object.
   *
   * @return JsonObject
   */
  private JsonObject parseObject() {
    JsonObject jsonObject = new JsonObject();
    int expected = TokenType.STRING.getCode() | TokenType.END_OBJECT.getCode();
    String key = null;
    while (tokenHolder.hasNext()) {
      Token token = tokenHolder.next();
      TokenType tokenType = token.getType();
      assertTokenType(expected, tokenType);
      switch (tokenType) {
        case BEGIN_OBJECT:
          jsonObject.put(key, parseObject());
          expected = COMMON_EXPECTED_FOR_JSON_OBJECT;
          break;
        case END_OBJECT:
          return jsonObject;
        case BEGIN_ARRAY:
          jsonObject.put(key, parseArray());
          expected = COMMON_EXPECTED_FOR_JSON_OBJECT;
          break;
        case NULL:
          jsonObject.put(key, null);
          expected = COMMON_EXPECTED_FOR_JSON_OBJECT;
          break;
        case NUMBER:
          jsonObject.put(key, parseNumber(token.getValue()));
          expected = COMMON_EXPECTED_FOR_JSON_OBJECT;
          break;
        case BOOLEAN:
          jsonObject.put(key, Boolean.valueOf(token.getValue()));
          expected = COMMON_EXPECTED_FOR_JSON_OBJECT;
          break;
        case STRING:
          Token prevToken = tokenHolder.previous();
          //value
          if (prevToken.getType() == TokenType.SEP_COLON) {
            jsonObject.put(key, token.getValue());
            expected = COMMON_EXPECTED_FOR_JSON_OBJECT;
          }
          //key
          else {
            key = token.getValue();
            expected = TokenType.SEP_COLON.getCode();
          }
          break;
        case SEP_COLON:
          expected = TokenType.NULL.getCode() | TokenType.NUMBER.getCode() | TokenType.BOOLEAN.getCode()
              | TokenType.STRING.getCode() | TokenType.BEGIN_OBJECT.getCode() | TokenType.BEGIN_ARRAY.getCode();
          break;
        case SEP_COMMA:
          expected = TokenType.STRING.getCode();
          break;
        case END_DOCUMENT:
          return jsonObject;
        default:
          throw new JsonParseException(MessageFormat.format("Unexpected token type: {0}.", tokenType));
      }
    }
    throw new JsonParseException("Parse json object error.");
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

  /**
   * Parses array.
   *
   * @return JsonArray
   */
  private JsonArray parseArray() {
    JsonArray jsonArray = new JsonArray();
    int expected = TokenType.BEGIN_ARRAY.getCode() | TokenType.END_ARRAY.getCode() | TokenType.BEGIN_OBJECT.getCode()
        | TokenType.NULL.getCode() | TokenType.NUMBER.getCode() | TokenType.BOOLEAN.getCode() | TokenType.STRING.getCode();
    String key = null;
    while (tokenHolder.hasNext()) {
      Token token = tokenHolder.next();
      TokenType tokenType = token.getType();
      assertTokenType(expected, tokenType);
      switch (tokenType) {
        case BEGIN_OBJECT:
          jsonArray.add(parseObject());
          expected = COMMON_EXPECTED_FOR_JSON_ARRAY;
          break;
        case BEGIN_ARRAY:
          jsonArray.add(parseArray());
          expected = COMMON_EXPECTED_FOR_JSON_ARRAY;
          break;
        case END_ARRAY:
          return jsonArray;
        case NULL:
          jsonArray.add(null);
          expected = COMMON_EXPECTED_FOR_JSON_ARRAY;
          break;
        case NUMBER:
          jsonArray.add(parseNumber(token.getValue()));
          expected = COMMON_EXPECTED_FOR_JSON_ARRAY;
          break;
        case BOOLEAN:
          jsonArray.add(Boolean.valueOf(token.getValue()));
          expected = COMMON_EXPECTED_FOR_JSON_ARRAY;
          break;
        case STRING:
          jsonArray.add(token.getValue());
          expected = COMMON_EXPECTED_FOR_JSON_ARRAY;
          break;
        case SEP_COMMA:
          expected = TokenType.STRING.getCode() | TokenType.NULL.getCode() | TokenType.NUMBER.getCode()
              | TokenType.BOOLEAN.getCode() | TokenType.BEGIN_ARRAY.getCode() | TokenType.BEGIN_OBJECT.getCode();
          break;
        case END_DOCUMENT:
          return jsonArray;
        default:
          throw new JsonParseException(MessageFormat.format("Unexpected token type: {0}.", tokenType));
      }
    }
    throw new JsonParseException("Parse json array error.");
  }

  /**
   * Asserts token type, if actual is invalid, throw {@link JsonParseException}.
   *
   * @param expectedCode    must not be {@literal null}.
   * @param actualTokenType must not be {@literal null}.
   */
  private void assertTokenType(int expectedCode, TokenType actualTokenType) {
    Assert.notNull(actualTokenType, "actualTokenType must not be null.");

    if ((actualTokenType.getCode() & expectedCode) == 0) {
      throw new JsonParseException(MessageFormat.format("Unexpected token type: {0}.", actualTokenType));
    }
  }

}
