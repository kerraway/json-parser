package com.github.kerraway.jsonparser.tokenizer;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static com.github.kerraway.jsonparser.constant.JsonCases.*;
import static org.junit.Assert.assertEquals;

/**
 * @author kerraway
 * @date 2019/1/7
 */
public class TokenizerTest {

  @Test
  public void tokenizeTest1() throws IOException {
    TokenHolder tokenHolder = tokenize(JSON_CASE_1);
    assertEquals(6, tokenHolder.size());
    assertEquals(TokenType.BEGIN_OBJECT, tokenHolder.next().getType());
    assertEquals(TokenType.STRING, tokenHolder.next().getType());
    assertEquals(TokenType.SEP_COLON, tokenHolder.next().getType());
    assertEquals(TokenType.NUMBER, tokenHolder.next().getType());
    assertEquals(TokenType.END_OBJECT, tokenHolder.next().getType());
    assertEquals(TokenType.END_DOCUMENT, tokenHolder.next().getType());
  }

  @Test
  public void tokenizeTest2() throws IOException {
    TokenHolder tokenHolder = tokenize(JSON_CASE_2);
    assertEquals(16, tokenHolder.size());
  }

  @Test
  public void tokenizeTest3() throws IOException {
    TokenHolder tokenHolder = tokenize(JSON_CASE_3);
    assertEquals(44, tokenHolder.size());
  }

  @Test
  public void tokenizeTest4() throws IOException {
    TokenHolder tokenHolder = tokenize(JSON_CASE_4);
    assertEquals(276, tokenHolder.size());
  }

  @Test
  public void tokenizeTest5() throws IOException {
    TokenHolder tokenHolder = tokenize(JSON_CASE_5);
    assertEquals(665, tokenHolder.size());
  }

  private TokenHolder tokenize(String json) throws IOException {
    CharReader charReader = new CharReader(new StringReader(json));
    Tokenizer tokenizer = new Tokenizer();
    return tokenizer.tokenize(charReader);
  }
}