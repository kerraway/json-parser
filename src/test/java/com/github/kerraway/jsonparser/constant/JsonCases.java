package com.github.kerraway.jsonparser.constant;

import com.github.kerraway.jsonparser.util.FileUtils;

/**
 * @author kerraway
 * @date 2019/1/8
 */
public final class JsonCases {

  public static final String JSON_CASE_1 = "{\"field\":123}";
  public static final String JSON_CASE_2 = "[\"abc\",\"Single quotes: \'\",1,1.23,3E+5,false,null]";
  public static final String JSON_CASE_3 = FileUtils.read("simple-json-string.txt");
  public static final String JSON_CASE_4 = FileUtils.read("zhihu-news.txt");
  public static final String JSON_CASE_5 = FileUtils.read("netease-music-playlist.txt");

  private JsonCases() {
  }
}
