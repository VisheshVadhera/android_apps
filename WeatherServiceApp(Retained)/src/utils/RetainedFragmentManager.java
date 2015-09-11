package utils;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

public class RetainedFragmentManager {

private static final String LOG_TAG = RetainedFragmentManager.class.getName();
	
	private String mFragmentTag;
	
	private WeakReference<FragmentManager> mFragmentManager;
	
	private RetainedFragment mRetainedFragment;
	
	public RetainedFragmentManager(FragmentManager fragmentManager, String fragmentTag){
		mFragmentTag = fragmentTag;
		mFragmentManager = new WeakReference<FragmentManager>(fragmentManager);
	}
	
	public boolean isItFirstTime(){
		
		mRetainedFragment = (RetainedFragment) mFragmentManager
										.get().findFragmentByTag(mFragmentTag);
		
		if(mRetainedFragment==null){
			
			mRetainedFragment = new RetainedFragment();
			Log.d(LOG_TAG, "Creating a new RetainedFragment");
			
			mFragmentManager.get().beginTransaction()
			.add(mRetainedFragment, mFragmentTag).commit();
		
			return true;
		}
		else{
			Log.d(LOG_TAG, "Extracting the RetainedFragment");
			return false;
		}
	}
	
	public void put(String key, Object object){
		mRetainedFragment.put(key, object);
	}
	
	public <T> T get(String key){
		return mRetainedFragment.get(key);
	}
	
	class RetainedFragment extends Fragment{
		
		private HashMap<String, Object> hashMap = new HashMap<String, Object>();
		
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			
			setRetainInstance(true);
		}
		
		public void put(String key, Object object){
			hashMap.put(key, object);
		}
		
		@SuppressWarnings("unchecked")
		public <T> T get(String key){
			return (T) hashMap.get(key);
		}
		
	}
	
}
