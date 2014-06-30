package net.kastan.spelling.engine;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by karlkastan on 6/26/14.
 */
public class Game {

  private static final char[] alphabet = {
          'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
          'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
  };

  private static final Random rand = new Random();

  private GameState state = GameState.LOADING;
  private char[] word;

  private int position = 0;
  private int attempts = 0;

  public Game(String str, int drawableId) {

    if(null == str || str.length() == 0) {
      throw new IllegalArgumentException("Game can not be played with an empty or null string.");
    }

    word = new char[str.length()];
    for(int i=0; i<str.length(); i++) {
      word[i] = str.charAt(i);
    }

  }

  public boolean advanceToNextChar() {
    if(position < word.length-1) {
      position++;
      return true;
    }
    return false;
  }


  public int getCurrentPosition() {
    return position;
  }

  public boolean isSelectionCorrect(char c) {
    return (c == word[position]);
  }

  public char getCharAt(int index) {
    if(index >= word.length || index < 0) {
      throw new IndexOutOfBoundsException();
    }
    return word[index];
  }


  public char[] generateChoices() {
    if(GameState.CHOOSING != state && GameState.SHOWING_RESULTS != state) {
      throw new IllegalStateException("Illegal state to generate choices in: " + state);
    }

    attempts++;

    int index = rand.nextInt(2000) % 3;


    char[] chars = new char[word.length];

    Set<Character> usedChars = new HashSet<Character>();
    usedChars.add(word[position]);

    for(int i=0; i<word.length; i++) {
      if(i == index) {
        chars[i] = word[position];
      } else {
        int attempt = 0;
        boolean done = false;

        while(!done && attempt < 10) {
          char c = alphabet[rand.nextInt(2000) % 26];
          if(!usedChars.contains(c)) {
            chars[i] = c;
            usedChars.add(c);
            done = true;
          }
        }
      }
    }

    return chars;
  }

  public GameState getState() {
    return state;
  }

  public void setState(GameState state) {
    this.state = state;
  }

}
