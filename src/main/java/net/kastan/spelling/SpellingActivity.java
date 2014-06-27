package net.kastan.spelling;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.kastan.spelling.engine.Game;
import net.kastan.spelling.engine.GameState;
import net.kastan.spelling.util.Constants;


public class SpellingActivity extends Activity {

  private static String[] gameWords = { "cat", "dog", "car", "bat", "owl" };
  private static int[] gameImageIds = { R.drawable.cat, R.drawable.dog, R.drawable.c63amg, R.drawable.bat, R.drawable.owl };
  private int iteration = 0;

  private Game game;
  private TextView[] displayChars;
  private TextView[] choiceChars;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.spelling);

    displayChars = new TextView[3];
    displayChars[0] = (TextView) findViewById(R.id.spelling_view_text_1);
    displayChars[1] = (TextView) findViewById(R.id.spelling_view_text_2);
    displayChars[2] = (TextView) findViewById(R.id.spelling_view_text_3);

    choiceChars = new TextView[3];
    choiceChars[0] = (TextView) findViewById(R.id.spelling_view_choice_text_1);
    choiceChars[1] = (TextView) findViewById(R.id.spelling_view_choice_text_2);
    choiceChars[2] = (TextView) findViewById(R.id.spelling_view_choice_text_3);

    for(int i=0; i<choiceChars.length; i++) {
      final int index = i;
      choiceChars[i].setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          handleChoiceSelection(index);
        }
      });
    }

    startNextGame();
  }


  public void startNextGame() {
    game = new Game(gameWords[iteration], gameImageIds[iteration]);

    ImageView iv = (ImageView) findViewById(R.id.spelling_view_image);
    iv.setImageResource(gameImageIds[iteration]);
    showDisplayChars();

    iteration++;
    iteration = iteration < gameWords.length ? iteration : iteration % gameWords.length;
  }


  public void showDisplayChars() {
    for(int i=0; i<displayChars.length; i++) {
      choiceChars[i].setText("");
      setTextViewTextToChar(displayChars[i], game.getCharAt(i));
    }

    new HighlightDisplayCharsHandler(displayChars, this).start();
  }



  private void handleChoiceSelection(int index) {
    if(GameState.CHOOSING == game.getState()) {
      char selectedChar = choiceChars[index].getText().charAt(0);
      if(game.isSelectionCorrect(selectedChar)) {

        game.setState(GameState.SHOWING_RESULTS);
        choiceChars[index].setTextColor(Constants.VALIDATION_TEXT_COLOR);

        for(int i=0; i<choiceChars.length; i++) {
          if(i != index) {
            choiceChars[i].setTextColor(Constants.LIGHT_TEXT_COLOR);
          }
        }

        int pos = game.getCurrentPosition();
        setTextViewTextToChar(displayChars[pos], game.getCharAt(pos));

        boolean moreToPlay = game.advanceToNextChar();
        if (moreToPlay) {

          final int nextPos = pos+1;
          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              displayChars[nextPos].setText("_");
              updateChoiceSelection();
              game.setState(GameState.CHOOSING);
            }
          }, 1000l);

        } else {
          // this was the last letter, and the user got it right!
          game.setState(GameState.COMPLETE);
          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              startNextGame();
            }
          }, 2000l);
        }

      } else {
        choiceChars[index].setTextColor(Constants.ERROR_TEXT_COLOR);
      }
    }
  }


  public void clearDisplayAndPlay() {
    for(int i=0; i< displayChars.length; i++) {
      displayChars[i].setText("");
    }

    displayChars[0].setText("_");
    game.setState(GameState.CHOOSING);
    updateChoiceSelection();
  }


  private void updateChoiceSelection() {
    char[] choices = game.generateChoices();

    for(int i=0; i<3; i++) {
      choiceChars[i].setTextColor(Constants.DEFAULT_TEXT_COLOR);
      setTextViewTextToChar(choiceChars[i], choices[i]);
    }

  }



  private void setTextViewTextToChar(TextView tv, char c) {
    char[] arr = { c };
    tv.setText(arr, 0, 1);
  }


  /**
   *
   */
  private static class HighlightDisplayCharsHandler extends Handler {
    int index = 0;
    private TextView[] chars;
    private SpellingActivity parent;

    public HighlightDisplayCharsHandler(TextView[] chars, SpellingActivity parent) {
      this.chars = chars;
      this.parent = parent;
    }

    public void start() {
      postDelayed(new Runnable() {
        @Override
        public void run() {
          changeColors();
        }
      }, 1000l);
    }

    private void changeColors() {
      if(index > 0) {
        chars[index-1].setTextColor(Constants.DEFAULT_TEXT_COLOR);
      }

      if(index < 3) {
        chars[index].setTextColor(Constants.HIGHLIGHT_TEXT_COLOR);
      }

      index++;

      if(index <= 3) {
        postDelayed(new Runnable() {
          @Override
          public void run() {
            changeColors();
          }
        }, 1000l);
      } else {
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            parent.clearDisplayAndPlay();
          }
        }, 1000l);
      }

    }
  }


}
