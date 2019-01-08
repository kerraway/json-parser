package com.github.kerraway.jsonparser.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kerraway
 * @date 2019/1/7
 */
public class JsonArray {

  private List<Object> list;

  public JsonArray() {
    this.list = new ArrayList<>();
  }

  public int size() {
    return list.size();
  }

  public void add(Object obj) {
    list.add(obj);
  }

  public Object get(int index) {
    return list.get(index);
  }

  // TODO: 2019/1/7 fix
  @Override
  public String toString() {
    return "JsonArray{" +
        "list=" + list +
        '}';
  }
}
