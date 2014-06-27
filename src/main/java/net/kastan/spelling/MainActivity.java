package net.kastan.spelling;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);

      Button play = (Button) findViewById(R.id.main_view_play_btn);

      final Intent intent = new Intent(this, SpellingActivity.class);

      play.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          startActivity(intent);
        }
      });
   }

}
