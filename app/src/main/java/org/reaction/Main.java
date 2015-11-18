package org.reaction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Main extends AppCompatActivity implements View.OnClickListener{

    private Button baby;
    private Button easy;
    private Button medium;
    private Button hard;
    private Button hardcore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.choose_difficult);
        baby = (Button)findViewById(R.id.baby);
        easy = (Button)findViewById(R.id.easy);
        medium = (Button)findViewById(R.id.medium);
        hard = (Button)findViewById(R.id.hard);
        hardcore = (Button)findViewById(R.id.hardcore);
        baby.setOnClickListener(this);
        easy.setOnClickListener(this);
        medium.setOnClickListener(this);
        hard.setOnClickListener(this);
        hardcore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.baby:
                setContentView(new GameField(this, 1));
                break;
            case R.id.easy:
                setContentView(new GameField(this, 2));
                break;
            case R.id.medium:
                setContentView(new GameField(this, 3));
                break;
            case R.id.hard:
                setContentView(new GameField(this, 4));
                break;
            case R.id.hardcore:
                setContentView(new GameField(this, 5));
                break;

        }
    }
}
