package br.com.luisleao.caipirinhahero;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;


public class CaipirinhaHeroLaunch extends Activity {
	static final String TAG = "CaipirinhaHeroLaunch";

	static Intent createIntent(Activity activity) {
		//Display display = activity.getWindowManager().getDefaultDisplay();
		//int maxExtent = Math.max(display.getWidth(), display.getHeight());

		Intent intent;
		//Log.i(TAG, "starting phone ui");
		intent = new Intent(activity, CaipirinhaHeroPhone.class);
		return intent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.main);

		Intent intent = createIntent(this);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP); //| Intent.FLAG_ACTIVITY_NO_ANIMATION 
		try {
			startActivity(intent);
		} catch (ActivityNotFoundException e) {
			Log.e(TAG, "unable to start CaipirinhaHero activity", e);
		}
		finish();

		super.onCreate(savedInstanceState);
	}
}
