package com.github.kerraway.jsonparser.parser;

import com.github.kerraway.jsonparser.exception.JsonTypeException;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.kerraway.jsonparser.constant.Constants.*;

/**
 * @author kerraway
 * @date 2019/1/7
 */
public class JsonObject {

  private static final int INDENT_SIZE = 2;

  private Map<String, Object> map;

  public JsonObject() {
    this.map = new LinkedHashMap<>();
  }

  /**
   * Put key and value.
   *
   * @param key
   * @param value
   */
  public void put(String key, Object value) {
    map.put(key, value);
  }

  /**
   * Get value by key.
   *
   * @param key
   * @return Object
   */
  public Object get(String key) {
    return map.get(key);
  }

  /**
   * Get value by key and value type.
   *
   * @param key
   * @param valueType
   * @param <T>
   * @return T
   */
  public <T> T get(String key, Class<T> valueType) {
    Object value = map.get(key);
    if (value == null) {
      return null;
    }
    if (!valueType.isInstance(value)) {
      throw new JsonTypeException(MessageFormat.format(
          "{0} instance can't be cast to {1}.", value.getClass(), valueType));
    }
    return (T) value;
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
    strBuilder.append(buildIndent(depth)).append(LEFT_CURLY_BRACE);
    depth++;

    Iterator<Map.Entry<String, Object>> keyValItr = map.entrySet().iterator();
    while (keyValItr.hasNext()) {
      Map.Entry<String, Object> keyVal = keyValItr.next();

      strBuilder.append(NEWLINE)
          .append(buildIndent(depth)).append(DOUBLE_QUOTE).append(keyVal.getKey()).append(DOUBLE_QUOTE)
          .append(COLON);

      Object val = keyVal.getValue();
      if (val instanceof JsonObject) {
        strBuilder.append(NEWLINE).append(((JsonObject) val).toString(depth));
      } else if (val instanceof JsonArray) {
        strBuilder.append(NEWLINE).append(((JsonArray) val).toString(depth));
      } else if (val instanceof String) {
        strBuilder.append(DOUBLE_QUOTE).append(val).append(DOUBLE_QUOTE);
      } else {
        strBuilder.append(val);
      }

      if (keyValItr.hasNext()) {
        strBuilder.append(COMMA);
      }
    }

    depth--;
    strBuilder.append(NEWLINE)
        .append(buildIndent(depth)).append(RIGHT_CURLY_BRACE);
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
