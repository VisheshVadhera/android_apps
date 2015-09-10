package com.example.vishesh.modernartui.DialogFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.vishesh.modernartui.R;

/**
 * This class defines the Dialog Box which
 * will open and prompt the user to choose
 * two options "Not Now" or "Visit MOMA"
 *
 * Created by Vishesh
 */
public class MoreInfoDialog extends DialogFragment {


    private static String MOMA = "http://www.moma.org";


    //Empty Constructor
    public MoreInfoDialog(){

    }

    /***
     * Intitializes and sets the parameters
     * of the dialog
     *
     * @param savedInstanceState
     * @return Dialog: Return value is the newly created Dialog
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder momaDialog = new AlertDialog.Builder(getActivity());

        momaDialog.setMessage(R.string.dialog_message);

        momaDialog.setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });


        /***
         * Set an OnclickListener to the Positive Button
         * and as soon as it is clicked and intent is created
         * and passed to the browser to open the requested URL
         */
        momaDialog.setPositiveButton(R.string.visit_moma, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent momaIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MOMA));
                startActivity(momaIntent);
            }
        });

        return momaDialog.create();
    }

}
