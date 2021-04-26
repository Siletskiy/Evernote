package ltd.starthub.game.evernote;

import android.os.Bundle;
import android.util.DisplayMetrics;


import androidx.appcompat.app.AppCompatActivity;



public class puzzleactivity extends AppCompatActivity {

    private static   int screenWidth;
    private static int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024,1024);
        DisplayMetrics metrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        setContentView(new MainView(this));

    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

}