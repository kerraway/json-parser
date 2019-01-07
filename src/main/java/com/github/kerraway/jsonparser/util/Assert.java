package com.github.kerraway.jsonparser.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author kerraway
 * @date 2018/11/26
 */
public class Assert {

  /**
   * If expression is false, throw {@link IllegalArgumentException}.
   *
   * @param expression
   * @param message
   */
  public static void isTrue(boolean expression, String message) {
    if (!expression) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * If expression is false, throw {@link IllegalArgumentException}.
   *
   * @param expression
   * @param message
   */
  public static void isFalse(boolean expression, String message) {
    if (expression) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * If obj is not null, throw {@link IllegalArgumentException}.
   *
   * @param obj
   */
  public static void isNull(Object obj) {
    isNull(obj, "the obj argument must be null.");
  }

  /**
   * If obj is not null, throw {@link IllegalArgumentException}.
   *
   * @param obj
   * @param message
   */
  public static void isNull(Object obj, String message) {
    if (obj != null) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * If obj is null, throw {@link IllegalArgumentException}.
   *
   * @param obj
   */
  public static void notNull(Object obj) {
    notNull(obj, "the obj argument must not be null.");
  }

  /**
   * If obj is null, throw {@link IllegalArgumentException}.
   *
   * @param obj
   * @param message
   */
  public static void notNull(Object obj, String message) {
    if (obj == null) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * If text is null, empty or blank, throw {@link IllegalArgumentException}.
   *
   * @param text
   * @param message
   */
  public static void notBlank(String text, String message) {
    if (text == null || text.trim().length() == 0) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * If text is null or empty, throw {@link IllegalArgumentException}.
   *
   * @param text
   * @param message
   */
  public static void notEmpty(String text, String message) {
    if (text == null || text.length() == 0) {
      throw new IllegalArgumentException(text);
    }
  }

  /**
   * If array is null or empty, throw {@link IllegalArgumentException}.
   *
   * @param array
   * @param message
   */
  public static void notEmpty(Object[] array, String message) {
    if (array == null || array.length == 0) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * If collection is null or empty, throw {@link IllegalArgumentException}.
   *
   * @param collection
   * @param message
   */
  public static void notEmpty(Collection<?> collection, String message) {
    if (collection == null || collection.size() == 0) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * If map is null or empty, throw {@link IllegalArgumentException}.
   *
   * @param map
   * @param message
   */
  public static void notEmpty(Map<?, ?> map, String message) {
    if (map == null || map.size() == 0) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * If obj is not the instance of type, throw {@link IllegalArgumentException}.
   *
   * @param type    must not be {@literal null}.
   * @param obj
   * @param message
   */
  public static void isInstanceOf(Class<?> type, Object obj, String message) {
    notNull(type, "type must not be null.");
    if (!type.isInstance(obj)) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * If sub type is not the subclass of super type, throw {@link IllegalArgumentException}.
   *
   * @param superType must not be {@literal null}.
   * @param subType
   * @param message
   */
  public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
    notNull(superType, "superType must not be null.");
    if (subType == null || !superType.isAssignableFrom(subType)) {
      throw new IllegalArgumentException(message);
    }
  }

  private Assert() {
  }
}
