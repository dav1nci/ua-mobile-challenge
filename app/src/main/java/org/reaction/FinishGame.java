package org.reaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class FinishGame extends AppCompatActivity implements View.OnClickListener{

    private TextView score;
    private Button startNewGame;
    private TextView share;
    private Intent currentIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.finish_game);
        currentIntent = getIntent();

        //getting from intent our score
        String result = currentIntent.getStringExtra("score");
        score = (TextView)findViewById(R.id.score);
        startNewGame = (Button)findViewById(R.id.start_new_game);
        share = (TextView)findViewById(R.id.share);

        //setting score on screen
        score.setText(result);
        startNewGame.setOnClickListener(this);
        share.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.start_new_game:
                Intent intent = new Intent(this, Main.class);
                startActivity(intent);
                finish();
                break;
            case R.id.share: // some functionality to share score in social networks and others
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "I finish this game with score " + currentIntent.getStringExtra("score"));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
        }
    }
}
