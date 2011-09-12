package br.com.luisleao.caipirinhahero;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


import android.view.LayoutInflater;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import android.widget.Toast;



public class CaipirinhaHeroPhone extends BaseActivity implements OnItemClickListener, OnClickListener, OnTouchListener {
	
	static final String TAG = "CaipirinhaHeroPhone";
	Toast toast; // = Toast.makeText(getBaseContext(), "", Toast.LENGTH_SHORT);

	
	TextView tecla_c, tecla_d, tecla_e, tecla_f, tecla_g, tecla_a, tecla_b, tecla_c1;
	
	ImageView mHardwareStatus;
	FrameLayout mStatusContainer;
	
	//OutputController mOutputController;

	
	
	
	@Override
	protected void hideControls() {
		super.hideControls();
		
		Drawable IndicatorOffImage = getResources().getDrawable(R.drawable.indicator_off);
		mHardwareStatus = (ImageView) findViewById(R.id.hardware_status);
		mHardwareStatus.setImageDrawable(IndicatorOffImage);
		
		setControls();
	}

	protected void showSequencer() {
		super.showSequencer();


	}
	protected void showPad() {
		super.showPad();
	}

	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tecla_c = (TextView) findViewById(R.id.tecla_c);
		tecla_d = (TextView) findViewById(R.id.tecla_d);
		tecla_e = (TextView) findViewById(R.id.tecla_e);
		tecla_f = (TextView) findViewById(R.id.tecla_f);
		tecla_g = (TextView) findViewById(R.id.tecla_g);
		tecla_a = (TextView) findViewById(R.id.tecla_a);
		tecla_b = (TextView) findViewById(R.id.tecla_b);
		tecla_c1 = (TextView) findViewById(R.id.tecla_c1);
		
		/*
		tecla_c.setOnClickListener(this);
		tecla_d.setOnClickListener(this);
		tecla_e.setOnClickListener(this);
		tecla_f.setOnClickListener(this);
		tecla_g.setOnClickListener(this);
		tecla_a.setOnClickListener(this);
		tecla_b.setOnClickListener(this);
		tecla_c1.setOnClickListener(this);
		*/
		
		tecla_c.setOnTouchListener(this);
		tecla_d.setOnTouchListener(this);
		tecla_e.setOnTouchListener(this);
		tecla_f.setOnTouchListener(this);
		tecla_g.setOnTouchListener(this);
		tecla_a.setOnTouchListener(this);
		tecla_b.setOnTouchListener(this);
		tecla_c1.setOnTouchListener(this);

		
		toast = Toast.makeText(getBaseContext(), "", Toast.LENGTH_SHORT);
		

		//mStatusContainer = (FrameLayout) findViewById(R.id.StatusContainer);
		
        notes.clear();
        /*
    	notes.add(new Nota(1));
    	notes.add(new Nota(2));
    	notes.add(new Nota(4));
    	notes.add(new Nota(8));
    	notes.add(new Nota(16));
    	notes.add(new Nota(32));
    	notes.add(new Nota(64));
    	notes.add(new Nota(128));
    	notes.add(new Nota(3));
    	notes.add(new Nota(7));
    	notes.add(new Nota(15));
    	notes.add(new Nota(31));
    	notes.add(new Nota(63));
    	notes.add(new Nota(127));
    	notes.add(new Nota(255));
		*/
    	

		
	}
	
	
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		//toast = Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT);
		//toast.show();
	}
	
	private void setControls() {
/*
        
        
        btn_do.setOnTouchListener(this);
        btn_re.setOnTouchListener(this);
        btn_mi.setOnTouchListener(this);
        btn_fa.setOnTouchListener(this);
        btn_sol.setOnTouchListener(this);
        btn_la.setOnTouchListener(this);
        btn_si.setOnTouchListener(this);
        btn_plus.setOnTouchListener(this);
		*/
		
	}

	protected void showControls() {
		super.showControls();

		Drawable IndicatorOnImage = getResources().getDrawable(R.drawable.indicator_on);
		mHardwareStatus = (ImageView) findViewById(R.id.hardware_status);
		mHardwareStatus.setImageDrawable(IndicatorOnImage);
		setControls();

		
		//mTabPad = (TextView) findViewById(R.id.tab_pad);
		//mTabPad.setOnClickListener(this);

		//mTabSequencer = (TextView) findViewById(R.id.tab_sequencer);
		//mTabSequencer.setOnClickListener(this);

		//mOutputController = new OutputController(this, false);
		//mOutputController.accessoryAttached();

		//mInputContainer = (LinearLayout) findViewById(R.id.inputContainer);
		//mOutputContainer = (LinearLayout) findViewById(R.id.outputContainer);

		//showTabContents(R.id.tab_pad);
	}
	
	protected void showTabContents(int showInput) {
		//super.showTabContents(showInput);
		
/*
		mPadContainer = (LinearLayout) findViewById(R.id.PadContainer);
		mSequencerContainer = (FrameLayout) findViewById(R.id.SequencerContainer);
		
		switch(showInput){
		case R.id.mode_pad:
			mPadContainer.setVisibility(View.VISIBLE);
			mSequencerContainer.setVisibility(View.GONE);
			//mTabPad.setBackgroundDrawable(mFocusedTabImage);
			//mTabSequencer.setBackgroundDrawable(mNormalTabImage);
			break;
			
		case R.id.mode_sequencer:
			
	        adapter = new NotesAdapter(this);
	        lvSequencer = (ListView) findViewById(R.id.sequencer_grid);
	        lvSequencer.setAdapter(adapter);
	        //lvSequencer.setOnItemClickListener(this);
	        
			
			mPadContainer.setVisibility(View.GONE);
			mSequencerContainer.setVisibility(View.VISIBLE);
			//mTabPad.setBackgroundDrawable(mNormalTabImage);
			//mTabSequencer.setBackgroundDrawable(mFocusedTabImage);
			break;
			
		default:
			mPadContainer.setVisibility(View.GONE);
			mSequencerContainer.setVisibility(View.GONE);
		
		}
		*/
		
	}

	public boolean onTouch(View v, MotionEvent event) {
		int paddingBottom = v.getPaddingBottom();
		
		if (event.getAction() == MotionEvent.ACTION_DOWN)
			v.setBackgroundDrawable(mFocusedTabImage);
		else if (event.getAction() == MotionEvent.ACTION_UP)
			v.setBackgroundDrawable(mNormalTabImage);
		
		v.setPadding(0, 0, 0, paddingBottom);
		
		
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{	
			int vId = v.getId();
			/*
			switch (vId) {
				case R.id.tecla_c: Log.d(TAG, "ONTOUCH C"); break;
				case R.id.tecla_d: Log.d(TAG, "ONTOUCH D"); break;
				case R.id.tecla_e: Log.d(TAG, "ONTOUCH E"); break;
				case R.id.tecla_f: Log.d(TAG, "ONTOUCH F"); break;
				case R.id.tecla_g: Log.d(TAG, "ONTOUCH G"); break;
				case R.id.tecla_a: Log.d(TAG, "ONTOUCH A"); break;
				case R.id.tecla_b: Log.d(TAG, "ONTOUCH B"); break;
				case R.id.tecla_c1: Log.d(TAG, "ONTOUCH C1"); break;
				default: Log.d(TAG, "ONTOUCH GERAL");
			}
			*/
			
			sendNote(vId);
		}
		
		return super.onTouchEvent(event);
	}
	
	public void onClick(View v) {
		//Log.d(TAG, "ONCLICK");
		toast.cancel();
		toast.setText(((TextView) v).getText());
		toast.show();
	}

	
	
	
	
	
	
	
	
}







