package com.example.vishesh.modernartui.Activities;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;

import com.example.vishesh.modernartui.DialogFragment.MoreInfoDialog;
import com.example.vishesh.modernartui.R;


/**
 * This class defines main activity of the app.
 * The app contains five different rectangles
 * of varying sizes whose color will be changed
 * by a Seekbar which is placed at the bottom of the screen
 */

public class MainActivity extends ActionBarActivity {

    //Used for debugging purpose
    private static String TAG = "ModernArtUI";


    //Reference to a seekbar
    private SeekBar seek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "OnCreate is called");

        //Initialize the views
        final View leftTop = findViewById(R.id.leftTop);
        final View leftBottom = findViewById(R.id.leftBottom);
        final View rightTop = findViewById(R.id.rightTop);
        final View rightMid = findViewById(R.id.rightMid);
        final View rightBottom = findViewById(R.id.rightBottom);

        //Get reference to the seekbar
        seek = (SeekBar) findViewById(R.id.seek);


        //Set a Seekbar Change Listener on this Seekbar
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progressValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                progressValue = progress;
                int leftTopCol = 0, leftBottomCol = 0, rightTopCol = 0, rightMidCol = 0, rightBottomCol = 0;

                //Extract the drawables
                Drawable leftTopDraw = leftTop.getBackground();
                Drawable leftBottomDraw = leftBottom.getBackground();
                Drawable rightTopDraw = rightTop.getBackground();
                Drawable rightMidDraw = rightMid.getBackground();
                Drawable rightBottomDraw = rightBottom.getBackground();

                //Extract the color of the drawables in integer and add progressValue to it
                if(leftTopDraw instanceof ColorDrawable)
                    { leftTopCol = ((ColorDrawable) leftTopDraw).getColor() + progressValue;
                      Log.i(TAG, "Value of leftTopCol is:" + leftTopCol);  }
                if(leftTopDraw instanceof ColorDrawable)
                    { leftBottomCol = ((ColorDrawable) leftBottomDraw).getColor() + progressValue;}
                if(leftTopDraw instanceof ColorDrawable)
                    { rightTopCol = ((ColorDrawable) rightTopDraw).getColor() + progressValue;}
                if(leftTopDraw instanceof ColorDrawable)
                    { rightMidCol = ((ColorDrawable) rightMidDraw).getColor() + progressValue;}
                if(leftTopDraw instanceof ColorDrawable)
                    { rightBottomCol = ((ColorDrawable) rightBottomDraw).getColor() + progressValue;}


                // Set the background color value to be the updated value

                leftTop.setBackgroundColor(leftTopCol);
                leftBottom.setBackgroundColor(leftBottomCol);
                rightTop.setBackgroundColor(rightTopCol);
                rightMid.setBackgroundColor(rightMidCol);
                rightBottom.setBackgroundColor(rightBottomCol);
            }



            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    /**
     * Create and initialize Options Menu which will
     * have a single item More Info.
     * The layout of the menu is defined in
     * menu_main.xml file.
     *
     * @param menu The options menu in which items are placed
     * @return Returns true for the menu to be displayed;
     *         false if not displayed.
     */
    @Override
    public boolean onCreateOptionsMenu (Menu menu){

        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    /** Do actions based on Options Menu Clicks
     * @param item The item which has been clicked
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId()){
            case R.id.more_info:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Show the Dialog to the user which allows her to take further action
     */
    private void showDialog(){
        MoreInfoDialog infoDialog = new MoreInfoDialog();
        infoDialog.show(getFragmentManager(), "Dialog");
    }

}
