package com.github.kerraway.jsonparser.parser;

import com.github.kerraway.jsonparser.parser.Parser;
import com.github.kerraway.jsonparser.tokenizer.CharReader;
import com.github.kerraway.jsonparser.tokenizer.TokenHolder;
import com.github.kerraway.jsonparser.tokenizer.Tokenizer;

import java.io.IOException;
import java.io.StringReader;

/**
 * @author kerraway
 * @date 2019/1/4
 */
public class JsonParser {

  private Tokenizer tokenizer;
  private Parser parser;

  public JsonParser() {
    this.tokenizer = new Tokenizer();
    this.parser = new Parser();
  }

  /**
   * Parse json to {@link Object}.
   *
   * @param json
   * @return Object
   * @throws IOException
   */
  public Object fromJson(String json) throws IOException {
    CharReader charReader = new CharReader(new StringReader(json));
    TokenHolder tokenHolder = tokenizer.tokenize(charReader);
    return parser.parse(tokenHolder);
  }

}
