package com.github.kerraway.jsonparser.tokenizer;

import java.io.IOException;
import java.io.Reader;

/**
 * @author kerraway
 * @date 2019/1/4
 */
public class CharReader {

  private static final int BUFFER_SIZE = 4096;

  private Reader reader;
  private char[] buffer;
  private int index;
  private int size;

  public CharReader(Reader reader) {
    this.reader = reader;
    this.buffer = new char[BUFFER_SIZE];
    this.index = 0;
    this.size = 0;
  }

  /**
   * Peeks the character at the index of buffer.
   *
   * @return char
   */
  public char peek() {
    if (index > size) {
      return (char) -1;
    }
    return buffer[index];
  }

  /**
   * If index is greater than 0, decreases index.
   */
  public void back() {
    if (index == 0) {
      return;
    }
    index--;
  }

  /**
   * If has next character, returns it, and increases index.
   *
   * @return char
   * @throws IOException
   */
  public char next() throws IOException {
    if (!hasNext()) {
      return (char) -1;
    }
    return buffer[index++];
  }

  /**
   * If buffer has next character, returns true.
   *
   * @return
   * @throws IOException
   */
  public boolean hasNext() throws IOException {
    if (index < size) {
      return true;
    }
    fillBuffer();
    return index < size;
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
    index = 0;
    size = n;
  }

}
