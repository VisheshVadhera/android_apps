package operations;

import java.lang.ref.WeakReference;

import activities.MainActivity;
import android.webkit.URLUtil;
import utils.Utils;


public class ImageOps {

	private WeakReference<MainActivity> mActivity;
	
	public ImageOps(MainActivity activity){
		
		mActivity = new WeakReference<MainActivity>(activity);
	}
	
	public void onConfigurationChange(MainActivity activity){
		mActivity = new WeakReference<MainActivity>(activity);
	}
	
	
	public void downloadAndFilterImage(String url){
		
		if(URLUtil.isValidUrl(url)){
			downloadImage(url);
		}
		else{
			Utils.showToast(mActivity.get(), "Invalid URL. Please enter a valid URL");
		}
	}
	
	
	private void downloadImage(String url){
		new DownloadImageTask(mActivity.get()).execute(url);
	}
	
}
