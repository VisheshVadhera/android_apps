package com.example.vishesh.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.provider.Settings;

import com.example.vishesh.locationserviceapp.R;

/**-------------------------------------------------------------------------------------------------
 *
 *  Author:     Vishesh Vadhera
 *  Written:    23/9/2015
 *
 *
 * This class creates the Alert Dialog which is shown to the user
 * when it has been confirmed by the service that an active internet
 * connection is available.
 * -------------------------------------------------------------------------------------------------
 */
public class InternetPromptDialog extends DialogFragment{


    public InternetPromptDialog(){

    }


    /*
     * Creates the dialog fragment which is displayed to the user.
     */
    public Dialog onCreateDialog(Bundle savedInstanceState){
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder internetPromptDialog = new AlertDialog.Builder(getActivity());

        internetPromptDialog.setMessage(R.string.prompt_dialog_message);

        internetPromptDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                return;
            }
        });

        internetPromptDialog.setPositiveButton(R.string.switch_on_internet, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent connectivityIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(connectivityIntent);
            }
        });

        return internetPromptDialog.create();
    }
}
