package utils;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;



public class RetainedFragmentManager{
	
	private static final String LOG = RetainedFragmentManager.class.getName();
	
	//Weak Reference to the fragment manager that will manage the fragment that will be retained
	private WeakReference<FragmentManager> mFragmentManager;
	
	//Tag that will be used to identify the stored fragment.
	private String mFragmentTag;
	
	//ReatinedFragment instance in which the ImageOps object will be stored.
	private RetainedFragment mRetainedFragment;
	
	/**
	 * Public constructor for the RetainedFragmentManager
	 * @param fragmentManager
	 * @param tag
	 */
	public RetainedFragmentManager(FragmentManager fragmentManager,
									String tag){
		mFragmentManager = new WeakReference<FragmentManager>(fragmentManager);
		mFragmentTag = tag;
	}
	
	/**
	 * 
	 * @return boolean If it is the first time that the 
	 * 				   activity is being created or the 
	 * 				   subsequent instances of its creation.
	 */
	public boolean isItFirstTime(){
		
		mRetainedFragment = (RetainedFragment) 
							mFragmentManager.get().findFragmentByTag(mFragmentTag);
		
		//If the fragment is null, it means that the activity is being created for the first time
		if(mRetainedFragment==null){
			
			Log.d(LOG, "RetainedFragment is being created for the first time");
			mRetainedFragment = new RetainedFragment();
			
			mFragmentManager.get()
			.beginTransaction()
			.add(mRetainedFragment, mFragmentTag)
			.commit();
			
			return true;
		}
		else{
			Log.d(LOG, "RetainedFragment has already been created");
			return false;
		}
	}
	
	public void put(String key, Object object){
		mRetainedFragment.put(key, object);
	}
	
	public <O> O get(String key){
		return mRetainedFragment.get(key);
	}
	
	
	// Class which creates the RetainedFragment instance and contains 
	// the hashMap which will store the ImageOps object.
	public class RetainedFragment extends Fragment{
		
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
		public <O> O get(String key){
			return (O) hashMap.get(key);
		}
	}
}