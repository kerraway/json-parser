package com.github.kerraway.jsonparser.tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kerraway
 * @date 2019/1/4
 */
public class TokenHolder {

  private static final int DEFAULT_INIT_CAPACITY = 256;
  private static final int CURSOR_INITIAL_VALUE = -1;

  private List<Token> tokens;
  private int cursor;

  public TokenHolder(int capacity) {
    this.tokens = new ArrayList<>(capacity);
    this.cursor = CURSOR_INITIAL_VALUE;
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
   * Gets current token at the cursor of tokens.
   *
   * @return Token
   */
  public Token current() {
    if (cursor < 0 || cursor >= tokens.size()) {
      return null;
    }
    return tokens.get(cursor);
  }

  /**
   * Gets previous token at the cursor of tokens.
   *
   * @return Token
   */
  public Token previous() {
    if (cursor <= 0) {
      return null;
    }
    return tokens.get(cursor - 1);
  }

  /**
   * Gets next token at the cursor of tokens, and increases cursor.
   *
   * @return Token
   */
  public Token next() {
    if (!hasNext()) {
      return null;
    }
    cursor = cursor + 1;
    return tokens.get(cursor);
  }

  /**
   * If has next token at the cursor of tokens, returns true.
   *
   * @return boolean
   */
  public boolean hasNext() {
    return cursor + 1 < tokens.size();
  }

  @Override
  public String toString() {
    return "TokenHolder{" +
        "tokens=" + tokens +
        '}';
  }
}
