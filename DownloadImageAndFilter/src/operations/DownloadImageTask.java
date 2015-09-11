package operations;

import java.lang.ref.WeakReference;

import activities.MainActivity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import utils.Utils;

public class DownloadImageTask extends AsyncTask<String, Void, Uri> {

	private static final String LOG_TAG = DownloadImageTask.class.getName(); 
	
	private WeakReference<MainActivity> mActivity;
	
	public DownloadImageTask(MainActivity activity){
		
		mActivity = new WeakReference<MainActivity>(activity);
	}
	
	@Override
	protected void onPreExecute(){
		Log.d(LOG_TAG, "Starting the DownloadImageTask");
	}
	
	@Override
	protected Uri doInBackground(String... arg) {
		Log.d(LOG_TAG, "Downloading the image in a background thread");
		return Utils.downloadImage(mActivity.get(), arg[0]);
	}
	
	@Override
	protected void onPostExecute(Uri uri){
		
		if(uri==null){
			Utils.showToast(mActivity.get(), "Error: Image couldn't be downloaded");
		}
		else{
			new GreyFilterTask(mActivity.get()).execute(uri);
		}
	}

}
