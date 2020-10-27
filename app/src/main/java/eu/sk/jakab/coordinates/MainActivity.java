package eu.sk.jakab.coordinates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageView imgImage;
    TextView txtScreenRes, txtImageRes, txtImageViewRes,
            txtTouchCoordinatesScreen, txtTouchCoordinatesImage;

    int screenWidth, screenHeight, imgHeight, imgWidth, imgViewWidth, imgViewHeight;
    double factor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgImage = findViewById(R.id.img_image);
        txtScreenRes = findViewById(R.id.txt_screen_coordinates);
        txtImageRes = findViewById(R.id.txt_image_coordinates);
        txtTouchCoordinatesScreen = findViewById(R.id.txt_screen_touch_coordinates);
        txtTouchCoordinatesImage = findViewById(R.id.txt_image_touch_coordinates);
        txtImageViewRes = findViewById(R.id.txt_imageView_coordinates);

        //image touch listener
        imgImage.setOnTouchListener(new View.OnTouchListener() {
            @Override public boolean onTouch(View view, MotionEvent motionEvent) {
                //touch coordinates on screen
                float eventX = motionEvent.getX();
                float eventY = motionEvent.getY();
                StringBuilder sb = new StringBuilder();
                sb.append("touch coordinates on imageView: ")
                        .append(String.format("%.02f", eventX)).append(" x ")
                        .append(String.format("%.02f", eventY));
                txtTouchCoordinatesScreen.setText(sb.toString());

                //calculate coordinates for image
                float imageX = scale(eventX, 0, imgViewWidth, 0, imgWidth);
                float imageY = scale(eventY, 0, imgViewHeight, 0, imgHeight);
                sb.setLength(0);
                sb.append("touch coordinates on image: ")
                        .append(String.format("%.02f", imageX)).append(" x ")
                        .append(String.format("%.02f", imageY));
                txtTouchCoordinatesImage.setText(sb.toString());
                return false;
            }
        });
    }

    private float scale(final float valueIn, final float baseMin, final float baseMax, final float limitMin, float limitMax) {
        return ((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin;
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //image resolution
            int densityDpi = getResources().getDisplayMetrics().densityDpi;
            factor = densityDpi/160.0;
            imgHeight = (int)(imgImage.getDrawable().getIntrinsicHeight()/factor);
            imgWidth = (int)(imgImage.getDrawable().getIntrinsicWidth()/factor);
            StringBuilder sb = new StringBuilder();
            sb.append("image resolution: ").append(String.valueOf(imgWidth))
                    .append(" x ").append(String.valueOf(imgHeight));
            txtImageRes.setText(sb.toString());

            //get imageView size
            imgViewHeight = imgImage.getHeight();
            imgViewWidth = imgImage.getWidth();
            sb.setLength(0);
            sb.append("imageView resolution: ").append(String.valueOf(imgViewWidth))
                    .append(" x ").append(String.valueOf(imgViewHeight));
            txtImageViewRes.setText(sb.toString());

            //get screen resolution in pixels (without soft buttons)
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            screenWidth = metrics.widthPixels;
            screenHeight = metrics.heightPixels;
            sb.setLength(0);
            sb.append("screen resolution: ").append(String.valueOf(screenWidth))
                    .append(" x ").append(String.valueOf(screenHeight))
                    .append(", density: ").append(String.valueOf(densityDpi))
                    .append(", factor: ").append(String.valueOf(factor));
            txtScreenRes.setText(sb.toString());
        }

    }
}
