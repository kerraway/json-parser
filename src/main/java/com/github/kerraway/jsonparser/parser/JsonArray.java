package com.github.kerraway.jsonparser.parser;

import java.util.ArrayList;
import java.util.List;

import static com.github.kerraway.jsonparser.constant.Constants.*;

/**
 * @author kerraway
 * @date 2019/1/7
 */
public class JsonArray {

  private static final int INDENT_SIZE = 2;

  private List<Object> list;

  public JsonArray() {
    this.list = new ArrayList<>();
  }

  /**
   * Get size of elements.
   *
   * @return int
   */
  public int size() {
    return list.size();
  }

  /**
   * Add element.
   *
   * @param obj
   */
  public void add(Object obj) {
    list.add(obj);
  }

  /**
   * Get element by index.
   *
   * @param index
   * @return Object
   */
  public Object get(int index) {
    return list.get(index);
  }

  @Override
  public String toString() {
    return toString(0);
  }

  /**
   * To string with depth.
   *
   * @param depth
   * @return String
   */
  public String toString(int depth) {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append(buildIndent(depth)).append(LEFT_SQUARE_BRACKET);
    depth++;

    int size = size();
    for (int i = 0; i < size; i++) {
      strBuilder.append(NEWLINE);

      Object element = get(i);
      if (element instanceof JsonObject) {
        strBuilder.append(((JsonObject) element).toString(depth));
      } else if (element instanceof JsonArray) {
        strBuilder.append(((JsonArray) element).toString(depth));
      } else if (element instanceof String) {
        strBuilder.append(buildIndent(depth)).append(DOUBLE_QUOTE).append(element).append(DOUBLE_QUOTE);
      } else {
        strBuilder.append(buildIndent(depth)).append(element);
      }

      if (i < size - 1) {
        strBuilder.append(COMMA);
      }
    }

    depth--;
    strBuilder.append(NEWLINE)
        .append(buildIndent(depth)).append(RIGHT_SQUARE_BRACKET);
    return strBuilder.toString();
  }

  /**
   * Build indent with space.
   *
   * @param depth
   * @return String
   */
  private String buildIndent(int depth) {
    StringBuilder strBuilder = new StringBuilder();
    for (int i = 0; i < depth * INDENT_SIZE; i++) {
      strBuilder.append(SPACE);
    }
    return strBuilder.toString();
  }
}
