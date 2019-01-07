package com.github.kerraway.jsonparser.tokenizer;

import com.github.kerraway.jsonparser.util.FileUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

/**
 * @author kerraway
 * @date 2019/1/7
 */
public class TokenizerTest {

  @Test
  public void tokenizeTest1() throws IOException {
    String json = "{\"field\":123}";
    TokenHolder tokenHolder = tokenize(json);
    assertEquals(6, tokenHolder.size());
    // TODO: 2019/1/7 fix
//    assertEquals(TokenType.BEGIN_OBJECT, tokenHolder.current().getType());
//    assertEquals(TokenType.STRING, tokenHolder.next().getType());
  }

  @Test
  public void tokenizeTest2() throws IOException {
    String json = "[\"abc\",\"Single quotes: \'\",1,1.23,3E+5,false,null]";
    TokenHolder tokenHolder = tokenize(json);
    assertEquals(16, tokenHolder.size());
  }

  @Test
  public void tokenizeTest3() throws IOException {
    String json = FileUtils.read("simple-json-string.txt");
    TokenHolder tokenHolder = tokenize(json);
    assertEquals(44, tokenHolder.size());
  }

  @Test
  public void tokenizeTest4() throws IOException {
    String json = FileUtils.read("zhihu-news.txt");
    TokenHolder tokenHolder = tokenize(json);
    assertEquals(276, tokenHolder.size());
  }

  @Test
  public void tokenizeTest5() throws IOException {
    String json = FileUtils.read("netease-music-playlist.txt");
    TokenHolder tokenHolder = tokenize(json);
    assertEquals(665, tokenHolder.size());
  }

  private TokenHolder tokenize(String json) throws IOException {
    CharReader charReader = new CharReader(new StringReader(json));
    Tokenizer tokenizer = new Tokenizer();
    return tokenizer.tokenize(charReader);
  }
}