package com.github.kerraway.jsonparser;

import com.github.kerraway.jsonparser.exception.JsonTypeException;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kerraway
 * @date 2019/1/7
 */
public class JsonObject {

  private Map<String, Object> map;

  public JsonObject() {
    this.map = new HashMap<>();
  }

  public void put(String key, Object value) {
    map.put(key, value);
  }

  public Object get(String key) {
    return map.get(key);
  }

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

  // TODO: 2019/1/7 fix
  @Override
  public String toString() {
    return "JsonObject{" +
        "map=" + map +
        '}';
  }
}
