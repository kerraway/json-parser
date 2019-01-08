package com.github.kerraway.jsonparser;

import org.junit.Test;

import java.io.IOException;

import static com.github.kerraway.jsonparser.constant.JsonCases.*;

/**
 * @author kerraway
 * @date 2019/1/7
 */
public class JsonParserTest {

  @Test
  public void fromJsonTest1() throws IOException {
    JsonObject jsonObject = (JsonObject) fromJson(JSON_CASE_1);
    System.out.println("jsonObject = " + jsonObject);
  }

  @Test
  public void fromJsonTest2() throws IOException {
    JsonArray jsonArray = (JsonArray) fromJson(JSON_CASE_2);
    System.out.println("jsonArray = " + jsonArray);
  }

  @Test
  public void fromJsonTest3() throws IOException {
    JsonObject jsonObject = (JsonObject) fromJson(JSON_CASE_3);
    System.out.println("jsonObject = " + jsonObject);
  }

  private Object fromJson(String json) throws IOException {
    JsonParser jsonParser = new JsonParser();
    return jsonParser.fromJson(json);
  }
}