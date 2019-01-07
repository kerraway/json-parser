package com.github.kerraway.jsonparser.tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kerraway
 * @date 2019/1/4
 */
public class TokenHolder {

  private static final int DEFAULT_INIT_CAPACITY = 256;

  private List<Token> tokens;
  private int index;

  public TokenHolder(int capacity) {
    this.tokens = new ArrayList<>(capacity);
    this.index = 0;
  }

  public TokenHolder() {
    this(DEFAULT_INIT_CAPACITY);
  }

  /**
   * Gets size of tokens.
   *
   * @return int
   */
  public int size() {
    return tokens.size();
  }

  /**
   * Adds token instance into tokens.
   *
   * @param token
   */
  public void add(Token token) {
    tokens.add(token);
  }

  /**
   * Gets current token at the index of tokens.
   *
   * @return Token
   */
  public Token current() {
    if (index >= tokens.size()) {
      return null;
    }
    return tokens.get(index);
  }

  /**
   * Gets previous token at the index of tokens.
   *
   * @return Token
   */
  public Token previous() {
    if (index == 0) {
      return null;
    }
    return tokens.get(index - 1);
  }

  /**
   * Gets next token at the index of tokens, and increases index.
   *
   * @return Token
   */
  public Token next() {
    return tokens.get(++index);
  }

  /**
   * If has next token at the index of tokens, returns true.
   *
   * @return boolean
   */
  public boolean hasNext() {
    return index + 1 < tokens.size();
  }

  @Override
  public String toString() {
    return "TokenHolder{" +
        "tokens=" + tokens +
        '}';
  }
}
