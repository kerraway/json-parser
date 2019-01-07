package com.github.kerraway.jsonparser.tokenizer;

import java.io.IOException;
import java.io.Reader;

/**
 * @author kerraway
 * @date 2019/1/4
 */
public class CharReader {

  private static final int BUFFER_SIZE = 4096;
  private static final int CURSOR_INITIAL_VALUE = -1;

  private Reader reader;
  private char[] buffer;
  private int size;
  private int cursor;

  public CharReader(Reader reader) {
    this.reader = reader;
    this.buffer = new char[BUFFER_SIZE];
    this.size = 0;
    this.cursor = CURSOR_INITIAL_VALUE;
  }

  /**
   * Peeks the character at the cursor of buffer.
   *
   * @return char
   */
  public char peek() {
    if (cursor >= size) {
      return (char) -1;
    }
    if (cursor == CURSOR_INITIAL_VALUE) {
      return buffer[0];
    }
    return buffer[cursor];
  }

  /**
   * If cursor is greater than 0, decreases cursor.
   */
  public void back() {
    if (cursor == CURSOR_INITIAL_VALUE) {
      return;
    }
    cursor = cursor - 1;
  }

  /**
   * If has next character, returns it, and increases cursor.
   *
   * @return char
   * @throws IOException
   */
  public char next() throws IOException {
    if (!hasNext()) {
      return (char) -1;
    }
    cursor = cursor + 1;
    return buffer[cursor];
  }

  /**
   * If buffer has next character, returns true.
   *
   * @return boolean
   * @throws IOException
   */
  public boolean hasNext() throws IOException {
    if (size > cursor + 1) {
      return true;
    }
    fillBuffer();
    return size > cursor + 1;
  }

  /**
   * Reads characters into buffer.
   *
   * @throws IOException
   */
  private void fillBuffer() throws IOException {
    int n = reader.read(buffer);
    if (n == -1) {
      return;
    }
    cursor = CURSOR_INITIAL_VALUE;
    size = n;
  }

}
