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

  public TokenHolder tokenize(CharReader charReader) throws IOException {
    this.charReader = charReader;
    this.tokenHolder = new TokenHolder();

    //do tokenize
    tokenize();

    return tokenHolder;
  }

  private void tokenize() throws IOException {
    Token token;
    do {
      token = start();
      tokenHolder.add(token);
    } while (token.getType() != TokenType.END_DOCUMENT);
  }

  private Token start() throws IOException {
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
        throw new JsonParseException(MessageFormat.format("Illegal character: '{0}'.", ch));
    }
  }

  private Token readNumber() throws IOException {
    char ch = charReader.peek();
    StringBuilder strBuilder = new StringBuilder();
    //negative number
    if (ch == '-') {
      strBuilder.append(ch);
      ch = charReader.next();
      if (!isDigit(ch)) {
        //ch is not digit, throw exception
        throw new JsonParseException(MessageFormat.format("The character '{0}' after '-' is illegal.", ch));
      }
      //-0.xxx
      if (ch == '0') {
        strBuilder.append(ch);
        strBuilder.append(readFracAndExp());
      }
      //-1xxx/-9xxx
      else {
        do {
          strBuilder.append(ch);
          ch = charReader.next();
        } while (isDigit(ch));

        //ch is not the end, reader should go back
        if (ch != (char) -1) {
          charReader.back();
          //try read fraction and exponent
          strBuilder.append(readFracAndExp());
        }
      }
    }
    //fraction 0.xxx
    else if (ch == '0') {
      strBuilder.append(ch);
      strBuilder.append(readFracAndExp());
    }
    //number 1xxx/9xxx
    else {
      do {
        strBuilder.append(ch);
        ch = charReader.next();
      } while (isDigit(ch));
      //ch is not the end, reader should go back
      if (ch != (ch) - 1) {
        charReader.back();
        //try read fraction and exponent
        strBuilder.append(readFracAndExp());
      }
    }
    return new Token(TokenType.NUMBER, strBuilder.toString());
  }

  /**
   * Read fraction and exponent into string.
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
            "The character '{0}' in fraction is illegal.", ch));
      }
      do {
        strBuilder.append(ch);
        ch = charReader.next();
      } while (isDigit(ch));

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
   * Read exponent into string.
   *
   * @return String
   */
  private String readExp() throws IOException {
    char ch = charReader.next();
    if (ch != '+' && ch != '-') {
      //ch is not '+' or '-', throw exception
      throw new JsonParseException(MessageFormat.format("The character '{0}' after 'e' or 'E' is illegal.", ch));
    }

    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append(ch);

    ch = charReader.next();
    if (!isDigit(ch)) {
      //ch is not digit, throw exception
      throw new JsonParseException(MessageFormat.format(
          "The character '{0}' after '{1}' is illegal.", ch, strBuilder.toString()));
    }

    // TODO: 2019/1/4 refactor
    //strBuilder.append(readDigit());

    do {
      strBuilder.append(ch);
      ch = charReader.next();
    } while (isDigit(ch));

    //ch is not the end, reader should go back
    if (ch != (char) -1) {
      charReader.back();
    }

    return strBuilder.toString();
  }

  private String readDigit() throws IOException {
    char ch = charReader.peek();
    StringBuilder strBuilder = new StringBuilder();
    do {
      strBuilder.append(ch);
      ch = charReader.next();
    } while (isDigit(ch));
    return strBuilder.toString();
  }

  private Token readString() {
    return null;
  }

  private Token readBoolean() {
    return null;
  }

  private Token readNull() {
    return null;
  }

  private boolean isBlank(char ch) {
    return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
  }

  private boolean isDigit(char ch) {
    return ch >= '0' && ch <= '9';
  }

  // TODO: 2019/1/4 remove
  private boolean isDigitOneToNine(char ch) {
    return ch >= '1' && ch <= '9';
  }

  /**
   * If character is exponent sign e/E, returns true.
   *
   * @param ch
   */
  private boolean isExpSign(char ch) {
    return ch == 'e' || ch == 'E';
  }

}
