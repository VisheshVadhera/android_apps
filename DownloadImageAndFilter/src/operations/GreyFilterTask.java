package operations;

import android.content.Context;

import java.lang.ref.WeakReference;

import activities.MainActivity;
import android.app.Activity;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import utils.Utils;

public class GreyFilterTask extends AsyncTask<Uri, Void, Uri> {

	private static final String LOG_TAG = GreyFilterTask.class.getName();
	
	private WeakReference<MainActivity> mActivity;

	public GreyFilterTask(MainActivity activity){
		mActivity = new WeakReference<MainActivity>(activity);
	}

	@Override
	protected void onPreExecute(){
		Log.d(LOG_TAG, "Starting GreyFilterTask");
	}
	
	@Override
	protected Uri doInBackground(Uri... params) {
		return Utils.grayScaleFilter(mActivity.get(), params[0]);
	}
	
	@Override
	protected void onPostExecute(Uri uri){
	
		mActivity.get().startActivity(Utils.makeGalleryIntent(uri));
	}
}
