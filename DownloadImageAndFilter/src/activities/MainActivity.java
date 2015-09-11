package activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import operations.ImageOps;
import utils.RetainedFragmentManager;

import com.example.downloadimageandfilter.R;

import android.app.Activity;
import android.content.Context;

public class MainActivity extends LifecycleLoggingActivity {

	private static final String LOG_TAG = MainActivity.class.getName();

	
	//This tag will be used to store fragment in the RetainedFragmentManager
	private static final String FRAGMENT_TAG = "fragmentTag";
	
	
	//Reference to the RetainedFragmentManager that will handle the runtime configuration changes.
	private final RetainedFragmentManager mRetainedFragmentManager = 
					new RetainedFragmentManager(getFragmentManager(), FRAGMENT_TAG);
	
	
	//This object is the work horse of the project and will do the heavy lifting.
	private ImageOps mImageOps;
	
	private EditText mEditText;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		//Initialize the View
		mEditText = (EditText) findViewById(R.id.editText);
		
		handleConfigurationChange();
	}
	
	/**
	 * Handles the runtime configuration changes. 
	 */
	private void handleConfigurationChange(){
		
		
		//If the activity is being created for the first time, make a new ImageOps object
		//else get the existing ImageOps object from the Fragment Manager.
		if(mRetainedFragmentManager.isItFirstTime()){
			
			mImageOps = new ImageOps(this);
			
			Log.d(LOG_TAG, "Putting the ImageOps object into the RetainedFragment");
			mRetainedFragmentManager.put(FRAGMENT_TAG, mImageOps);
		}
		else{
			
			Log.d(LOG_TAG, "Extracting the ImageOps from the RetainedFragment");
			mImageOps = mRetainedFragmentManager.get(FRAGMENT_TAG);
			
			if(mImageOps==null){
				
				mImageOps = new ImageOps(this);
				
				mRetainedFragmentManager.put(FRAGMENT_TAG, mImageOps);
			}
			else{
				mImageOps.onConfigurationChange(this);
			}
		}
		
	}
	
	/**
	 * This method gets called when the user presses the Download Image and Filter Button.
	 */
	public void downloadAndFilterImage(View v){
		
		String url = getUrl();
		hideKeyboard();
		
		mImageOps.downloadAndFilterImage(url);
	}
	
	
	/**
	 * Extract the URL entered by the user from the EditText Box.
	 */
	private String getUrl(){
		return mEditText.getText().toString();
	}
	
	
	/**
	 * Hide the keyboard
	 */
	private void hideKeyboard() {   
	 
		View view = this.getCurrentFocus();
	    
	    if (view != null) {
	    	
	        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
	
}
