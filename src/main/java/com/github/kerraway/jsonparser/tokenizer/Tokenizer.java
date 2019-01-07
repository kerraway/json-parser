package com.github.kerraway.jsonparser.tokenizer;

import com.github.kerraway.jsonparser.exception.JsonParseException;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author kerraway
 * @date 2019/1/4
 */
public class Tokenizer {

  private CharReader charReader;
  private TokenHolder tokenHolder;

  /**
   * Tokenizes reader's content, and returns token holder.
   *
   * @param charReader
   * @return TokenHolder
   * @throws IOException
   */
  public TokenHolder tokenize(CharReader charReader) throws IOException {
    this.charReader = charReader;
    this.tokenHolder = new TokenHolder();

    //do tokenize
    tokenize();

    return tokenHolder;
  }

  /**
   * Gets next token and add into token holder.
   *
   * @throws IOException
   */
  private void tokenize() throws IOException {
    Token token;
    do {
      token = next();
      tokenHolder.add(token);
    } while (token.getType() != TokenType.END_DOCUMENT);
  }

  /**
   * Gets next token.
   *
   * @return Token
   * @throws IOException
   */
  private Token next() throws IOException {
    char ch;
    do {
      if (!charReader.hasNext()) {
        return new Token(TokenType.END_DOCUMENT);
      }
      ch = charReader.next();
    } while (isBlank(ch));

    switch (ch) {
      case '{':
        return new Token(TokenType.BEGIN_OBJECT, ch);
      case '}':
        return new Token(TokenType.END_OBJECT, ch);
      case '[':
        return new Token(TokenType.BEGIN_ARRAY, ch);
      case ']':
        return new Token(TokenType.END_ARRAY, ch);
      case ',':
        return new Token(TokenType.SEP_COMMA, ch);
      case ':':
        return new Token(TokenType.SEP_COLON, ch);
      case 'n':
        return readNull();
      case 't':
      case 'f':
        return readBoolean();
      case '"':
        return readString();
      case '-':
        return readNumber();
      default:
        if (isDigit(ch)) {
          return readNumber();
        }
        throw new JsonParseException(MessageFormat.format("Illegal character: ''{0}''.", ch));
    }
  }

  /**
   * Reads number, and returns token.
   *
   * @return Token
   * @throws IOException
   */
  private Token readNumber() throws IOException {
    StringBuilder strBuilder = new StringBuilder();
    char ch = charReader.peek();
    //negative number
    if (ch == '-') {
      strBuilder.append(ch);
      ch = charReader.next();
      if (!isDigit(ch)) {
        //ch is not digit, throw exception
        throw new JsonParseException(MessageFormat.format("The character ''{0}'' after ''-'' is illegal.", ch));
      }
      //fraction -0.xxx or integer number -1xxx/-9xxx
      strBuilder.append(readFracOrInteger());
    }
    //fraction 0.xxx or integer number 1xxx/9xxx
    else {
      strBuilder.append(readFracOrInteger());
    }
    return new Token(TokenType.NUMBER, strBuilder.toString());
  }

  /**
   * Reads fraction or integer number into string.
   *
   * @return String
   * @throws IOException
   */
  private String readFracOrInteger() throws IOException {
    char ch = charReader.peek();
    if (!isDigit(ch)) {
      //ch is not digit, throw exception
      throw new JsonParseException(MessageFormat.format("The character ''{0}'' isn''t digit.", ch));
    }

    StringBuilder strBuilder = new StringBuilder();
    //fraction 0.xxx
    if (ch == '0') {
      strBuilder.append(ch).append(readFracAndExp());
    }
    //integer number 1xxx/9xxx
    else {
      strBuilder.append(readDigit());
      //ch is not the end, reader should go back
      if (charReader.peek() != (char) -1) {
        charReader.back();
        //try read fraction and exponent
        strBuilder.append(readFracAndExp());
      }
    }
    return strBuilder.toString();
  }

  /**
   * Reads fraction and exponent into string.
   *
   * @return String
   */
  private String readFracAndExp() throws IOException {
    StringBuilder strBuilder = new StringBuilder();
    char ch = charReader.next();
    //fraction
    if (ch == '.') {
      strBuilder.append(ch);
      ch = charReader.next();
      if (!isDigit(ch)) {
        throw new JsonParseException(MessageFormat.format(
            "The character ''{0}'' in fraction is illegal.", ch));
      }
      strBuilder.append(readDigit());
      //exponent
      if (isExpSign(ch)) {
        strBuilder.append(ch);
        strBuilder.append(readExp());
      }
      //ch is not the end, reader should go back
      else if (ch != (char) -1) {
        charReader.back();
      }
    }
    //exponent
    else if (isExpSign(ch)) {
      strBuilder.append(ch);
      strBuilder.append(readExp());
    }
    //nothing to do, reader should go back
    else {
      charReader.back();
    }
    return strBuilder.toString();
  }

  /**
   * Reads exponent into string.
   *
   * @return String
   */
  private String readExp() throws IOException {
    char ch = charReader.next();
    if (ch != '+' && ch != '-') {
      //ch is not '+' or '-', throw exception
      throw new JsonParseException(MessageFormat.format("The character ''{0}'' after ''e'' or ''E'' is illegal.", ch));
    }

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append(ch);

    ch = charReader.next();
    if (!isDigit(ch)) {
      //ch is not digit, throw exception
      throw new JsonParseException(MessageFormat.format(
          "The character ''{0}'' after ''{1}'' is illegal.", ch, strBuilder.toString()));
    }
    strBuilder.append(readDigit());
    //reader has next character, reader should go back
    if (charReader.peek() != (char) -1) {
      charReader.back();
    }

    return strBuilder.toString();
  }

  /**
   * Reads digit into string.
   *
   * @return String
   * @throws IOException
   */
  private String readDigit() throws IOException {
    char ch = charReader.peek();
    if (!isDigit(ch)) {
      //ch is not digit, throw exception
      throw new JsonParseException(MessageFormat.format("The character ''{0}'' isn''t digit.", ch));
    }

    StringBuilder strBuilder = new StringBuilder();
    do {
      strBuilder.append(ch);
      ch = charReader.next();
    } while (isDigit(ch));
    return strBuilder.toString();
  }

  /**
   * Reads string, and returns token.
   *
   * @return Token
   * @throws IOException
   */
  private Token readString() throws IOException {
    StringBuilder strBuilder = new StringBuilder();
    char ch;
    do {
      ch = charReader.next();
      //escape string
      if (ch == '\\') {
        ch = charReader.next();
        String str = new String(new char[]{'\\', ch});
        if (!isEscapeString(str)) {
          throw new JsonParseException(MessageFormat.format("Illegal escape string: \"{0}\".", str));
        }
        strBuilder.append(str);

        //unicode string, [\u0000, \uFFFF]
        if (str.equals("\\u")) {
          StringBuilder hexStrBuilder = new StringBuilder();
          for (int i = 0; i < 4; i++) {
            ch = charReader.next();
            hexStrBuilder.append(ch);
          }
          if (!isHexString(hexStrBuilder.toString())) {
            throw new JsonParseException(MessageFormat.format("Illegal hex string: \"{0}\".", hexStrBuilder));
          }
          strBuilder.append(hexStrBuilder);
        }
      }
      //TODO newline isn't supported
      else if (ch == '\r' || ch == '\n') {
        throw new JsonParseException("Newline isn't supported.");
      }
      //others
      else {
        strBuilder.append(ch);
      }
    } while (ch != '"');
    return new Token(TokenType.STRING, strBuilder.toString());
  }

  /**
   * Reads boolean {@link true} or {@link false}, and returns token.
   *
   * @return Token
   * @throws IOException
   */
  private Token readBoolean() throws IOException {
    if (charReader.peek() == 't' && charReader.next() == 'r'
        && charReader.next() == 'u' && charReader.next() == 'e') {
      return new Token(TokenType.BOOLEAN, "true");
    }
    if (charReader.peek() == 'f' && charReader.next() == 'a'
        && charReader.next() == 'l' && charReader.next() == 's'
        && charReader.next() == 'e') {
      return new Token(TokenType.BOOLEAN, "false");
    }
    throw new JsonParseException("Illegal json string, should be \"true\" or \"false\".");
  }

  /**
   * Reads {@link null}, and returns token.
   *
   * @return Token
   * @throws IOException
   */
  private Token readNull() throws IOException {
    if (charReader.peek() == 'n' && charReader.next() == 'u'
        && charReader.next() == 'l' && charReader.next() == 'l') {
      return new Token(TokenType.NULL, "null");
    }
    throw new JsonParseException("Illegal json string, should be 'null'.");
  }

  /**
   * If character is blank, returns true.
   *
   * @param ch
   * @return boolean
   */
  private boolean isBlank(char ch) {
    return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
  }

  /**
   * If character is digit, returns true.
   *
   * @param ch
   * @return boolean
   */
  private boolean isDigit(char ch) {
    return ch >= '0' && ch <= '9';
  }

  /**
   * If character is exponent sign e/E, returns true.
   *
   * @param ch
   */
  private boolean isExpSign(char ch) {
    return ch == 'e' || ch == 'E';
  }

  /**
   * If string is escape string, returns true.
   *
   * @param str
   * @return boolean
   */
  private boolean isEscapeString(String str) {
    return str != null && (str.equals("\"") || str.equals("\\'")
        || str.equals("\\\\") || str.equals("\\u") || str.equals("\\r")
        || str.equals("\\n") || str.equals("\\b") || str.equals("\\t")
        || str.equals("\\f") || str.equals("\\/"));
  }

  /**
   * If string is hex string, returns true.
   *
   * @param str
   * @return boolean
   */
  private boolean isHexString(String str) {
    if (str == null || str.length() != 4) {
      return false;
    }
    for (int i = 0; i < str.length(); i++) {
      char ch = str.charAt(i);
      boolean isHexChar = (ch >= '0' && ch <= '9')
          || ('a' <= ch && ch <= 'f') || ('A' <= ch && ch <= 'F');
      if (!isHexChar) {
        return false;
      }
    }
    return true;
  }

}
