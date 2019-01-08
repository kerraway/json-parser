package com.github.kerraway.jsonparser.constant;

import com.github.kerraway.jsonparser.util.FileUtils;

/**
 * @author kerraway
 * @date 2019/1/8
 */
public final class JsonCases {

  /**
   * json object
   */
  public static final String JSON_CASE_1 = "{\"field\":123}";
  /**
   * json array
   */
  public static final String JSON_CASE_2 = "[\"abc\",\"Single quotes: \'\",1,1.23,3E+5,false,null]";
  /**
   * json object
   */
  public static final String JSON_CASE_3 = FileUtils.read("simple-json-string.txt");
  /**
   * json object, zhihu news
   */
  public static final String JSON_CASE_4 = FileUtils.read("zhihu-news.txt");
  /**
   * json object, netease music playlist
   */
  public static final String JSON_CASE_5 = FileUtils.read("netease-music-playlist.txt");

  private JsonCases() {
  }
}
