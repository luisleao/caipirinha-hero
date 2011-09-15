package br.com.luisleao.caipirinhahero;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


//import com.android.future.usb.UsbAccessory;
//import com.android.future.usb.UsbManager;
import android.hardware.usb.*;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;


public class BaseActivity extends Activity implements Runnable  {

	private static final String TAG = "CaipirinhaHeroActivity";

	static List<Nota> notes = new ArrayList<Nota>();
	NotesAdapter adapter;


	FrameLayout framePad;
	FrameLayout frameSequencer;
	
	Button btnAddline;
	ListView lvSequencer;

	
	static Drawable mFocusedTabImage;
	static Drawable mNormalTabImage;

	static Drawable img_play;
	static Drawable img_pause;

	static final int VIEW_PAD = 1;
	static final int VIEW_SEQUENCER = 2;
	static int view_mode = VIEW_PAD;
	
	
	static Timer tmr;

	static boolean is_playing = false;
	static boolean repeat_music = false;
	static int play_position = 0;
	int frequency_value = 100;
	
	
	
	
	

	
	private static final String ACTION_USB_PERMISSION = "br.com.luisleao.caipirinhahero.action.USB_PERMISSION";

	private UsbManager mUsbManager;
	private PendingIntent mPermissionIntent;
	private boolean mPermissionRequestPending;

	UsbAccessory mAccessory;
	ParcelFileDescriptor mFileDescriptor;
	FileInputStream mInputStream;
	FileOutputStream mOutputStream;
	

	//private InputController mInputController;
	//mInputController eh o controlador principal

	public BaseActivity() {
		super();
	}


	@Override  
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {  
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.setHeaderTitle("Op›es");
		menu.add(0, v.getId(), 0, "Excluir linha");
		menu.add(0, v.getId(), 0, "Adicionar linha antes");
		menu.add(0, v.getId(), 0, "Adicionar linha depois");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

       	if(item.getTitle()=="Excluir linha"){
    		notes.remove(info.position);
       	}
    	else if(item.getTitle()=="Adicionar linha antes"){
    		notes.add(info.position, new Nota(0));
    	}
    	else if(item.getTitle()=="Adicionar linha depois"){
    		notes.add(info.position + 1, new Nota(0));
    	}
    	else {return false;}
       	
		adapter.notifyDataSetChanged();
		saveData();
		
       	return true;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_geral, menu);
	    return super.onCreateOptionsMenu(menu);  
	}
	

	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.menu_music).setVisible(view_mode == VIEW_SEQUENCER);

		menu.findItem(R.id.menu_playpause).setVisible(view_mode == VIEW_SEQUENCER);
		menu.findItem(R.id.menu_playpause).setTitle(is_playing ? "Pause" : "Play");
		menu.findItem(R.id.menu_playpause).setIcon(is_playing ? img_pause : img_play);
		
		menu.findItem(R.id.menu_frequency).setVisible(view_mode == VIEW_SEQUENCER);
		menu.findItem(R.id.menu_repeatmusic).setChecked(repeat_music);
		
		
		Log.d(TAG, "FREQUENCIA " + frequency_value);
		switch(frequency_value){
		case 100: menu.findItem(R.id.menu_frequency_100).setChecked(true); break;
		case 200: menu.findItem(R.id.menu_frequency_200).setChecked(true); break;
		case 300: menu.findItem(R.id.menu_frequency_300).setChecked(true); break;
		case 400: menu.findItem(R.id.menu_frequency_400).setChecked(true); break;
		case 500: menu.findItem(R.id.menu_frequency_500).setChecked(true); break;
		case 600: menu.findItem(R.id.menu_frequency_600).setChecked(true); break;
		case 700: menu.findItem(R.id.menu_frequency_700).setChecked(true); break;
		case 800: menu.findItem(R.id.menu_frequency_800).setChecked(true); break;
		case 900: menu.findItem(R.id.menu_frequency_900).setChecked(true); break;
		case 1000: menu.findItem(R.id.menu_frequency_1000).setChecked(true); break;
		case 1500: menu.findItem(R.id.menu_frequency_1500).setChecked(true); break;
		case 2000: menu.findItem(R.id.menu_frequency_2000).setChecked(true); break;
		}
		
		switch(view_mode) {
		case VIEW_PAD: menu.findItem(R.id.menu_mode_pad).setChecked(true); break;
		case VIEW_SEQUENCER: menu.findItem(R.id.menu_mode_sequencer).setChecked(true); break;
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_mode_pad:
			saveData();
	    	item.setChecked(true);
	    	showPad();
		    return true;
		    
		case R.id.menu_mode_sequencer:
	    	item.setChecked(true);
			showSequencer();
		    return true;
		    
		case R.id.menu_playpause:
			is_playing = !is_playing;
			if (is_playing) {
				play_position = 0;
				tmr = new Timer();
				tmr.scheduleAtFixedRate(new PlayerTask(), frequency_value, frequency_value);
			} else {
				tmr.cancel();
				tmr.purge();
			}
			return true;
		
		case R.id.menu_addline:
			// add new line
			notes.add(new Nota(0));
			saveData();
			adapter.notifyDataSetChanged();
			return true;
			
		case R.id.menu_clearlines:
			notes.clear();
			saveData();
			adapter.notifyDataSetChanged();
			return true;
			
		case R.id.menu_loadmusic:
			//TODO: load file
			loadData();
			adapter.notifyDataSetChanged();
			return true;
			
		case R.id.menu_savemusic:
			//TODO: save file
			saveData();
			return true;
		
		case R.id.menu_frequency_100:
		case R.id.menu_frequency_200:
		case R.id.menu_frequency_300:
		case R.id.menu_frequency_400:
		case R.id.menu_frequency_500:
		case R.id.menu_frequency_600:
		case R.id.menu_frequency_700:
		case R.id.menu_frequency_800:
		case R.id.menu_frequency_900:
		case R.id.menu_frequency_1000:
		case R.id.menu_frequency_1500:
		case R.id.menu_frequency_2000:
			//change frequency
	    	item.setChecked(true);

			//set frequency value
			switch (item.getItemId()) {
			case R.id.menu_frequency_100: frequency_value = 100; break;
			case R.id.menu_frequency_200: frequency_value = 200; break;
			case R.id.menu_frequency_300: frequency_value = 300; break;
			case R.id.menu_frequency_400: frequency_value = 400; break;
			case R.id.menu_frequency_500: frequency_value = 500; break;
			case R.id.menu_frequency_600: frequency_value = 600; break;
			case R.id.menu_frequency_700: frequency_value = 700; break;
			case R.id.menu_frequency_800: frequency_value = 800; break;
			case R.id.menu_frequency_900: frequency_value = 900; break;
			case R.id.menu_frequency_1000: frequency_value = 1000; break;
			case R.id.menu_frequency_1500: frequency_value = 1500; break;
			case R.id.menu_frequency_2000: frequency_value = 2000; break;
			}
			saveData();

			
			if (is_playing) {
				//change timer
				tmr.cancel();
				tmr.purge();
				play_position = 0;
				tmr = new Timer();
				tmr.scheduleAtFixedRate(new PlayerTask(), frequency_value, frequency_value);
			}
			return true;
			
		case R.id.menu_repeatmusic:
	    	if (item.isChecked()) item.setChecked(false);
		    else item.setChecked(true);
	    	
	    	repeat_music = item.isChecked();
			saveData();

			return true;
			
		case R.id.menu_quit:
			saveData();
			finish();
			System.exit(0);
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void enableControls(boolean enable) {
		if (enable) {
			showControls();
		} else {
			hideControls();
		}
	}

	protected void hideControls() {
		//setContentView(R.layout.no_device);
		//setContentView(R.layout.main);
		//mInputController = null;
	}

	
	protected void showControls() {
		//setContentView(R.layout.main);
		//mInputController = new InputController(this);
		//mInputController.accessoryAttached();
	}

	
	
	protected void showPad() {
		framePad.setVisibility(View.VISIBLE);
		frameSequencer.setVisibility(View.GONE);
		btnAddline.setVisibility(View.GONE);
    	view_mode = VIEW_PAD;
	}
	protected void showSequencer() {
		adapter = new NotesAdapter(getBaseContext());
        lvSequencer.setAdapter(adapter);
        //lvSequencer.setOnItemClickListener(this);

		framePad.setVisibility(View.GONE);
		frameSequencer.setVisibility(View.VISIBLE);
		btnAddline.setVisibility(View.VISIBLE);
    	view_mode = VIEW_SEQUENCER;
		loadData();
		adapter.notifyDataSetChanged();
	}
	
	
	
	
	protected void loadData() {
		notes.clear();

		// load data from app
		SharedPreferences sp = getPreferences(MODE_PRIVATE);
		String musicData = sp.getString("music", "");
		for (String note : musicData.split(";")) {
			if (note.length() > 0)
				notes.add(new Nota(Integer.parseInt(note, 10)));
		}
		
		repeat_music = sp.getBoolean("repeat_music", false);
		frequency_value = sp.getInt("frequency_value", 100);
		
	}
	protected void saveData() {
		//TODO: save data to app
		SharedPreferences.Editor spe = getPreferences(MODE_PRIVATE).edit();
		StringBuilder sb = new StringBuilder();
		int i;
		for (i = 0; i < notes.size(); i++)
			sb.append( ((i == 0) ? "" : ";") + String.valueOf(notes.get(i).getNotes()));
		spe.putString("music", sb.toString());
		
		spe.putBoolean("repeat_music", repeat_music);
		spe.putInt("frequency_value", frequency_value);
		
		spe.commit();
	}
	
	
	
	
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					//UsbAccessory accessory = UsbManager.getAccessory(intent);
					UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						openAccessory(accessory);
					} else {
						Log.d(TAG, "permission denied for accessory "
								+ accessory);
					}
					mPermissionRequestPending = false;
				}
			} else if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
				//UsbAccessory accessory = UsbManager.getAccessory(intent);
				UsbAccessory accessory = (UsbAccessory) intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
				if (accessory != null && accessory.equals(mAccessory)) {
					closeAccessory();
				}
			}
		}
	};
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//mUsbManager = UsbManager.getInstance(this);
		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

		mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
		registerReceiver(mUsbReceiver, filter);

		if (getLastNonConfigurationInstance() != null) {
			mAccessory = (UsbAccessory) getLastNonConfigurationInstance();
			openAccessory(mAccessory);
		}

		

		setContentView(R.layout.main);
		enableControls(false);

		framePad = (FrameLayout) findViewById(R.id.padContent);
		frameSequencer = (FrameLayout) findViewById(R.id.sequencerContent);
		btnAddline = (Button) findViewById(R.id.btn_addline);
		
		btnAddline.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				notes.add(new Nota(0));
				saveData();
				adapter.notifyDataSetChanged();
			}
		});
		
		if (mAccessory != null) {
			showControls();
		} else {
			hideControls();
		}

		mFocusedTabImage = getResources().getDrawable(R.drawable.tab_focused_holo_dark);
		mNormalTabImage = getResources().getDrawable(R.drawable.tab_normal_holo_dark);
		img_play = getResources().getDrawable(R.drawable.ic_menu_play);
		img_pause = getResources().getDrawable(R.drawable.ic_menu_pause);
		
        lvSequencer = (ListView) findViewById(R.id.sequencer_grid);
		registerForContextMenu(lvSequencer);
		
		//Log.d(TAG, "ONCREATE view_mode " + view_mode);
		loadData();
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		if (mAccessory != null) {
			return mAccessory;
		} else {
			return super.onRetainNonConfigurationInstance();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		//Log.d(TAG, "ONRESUME view_mode " + view_mode);
		switch(view_mode) {
		case VIEW_PAD: showPad(); break;
		case VIEW_SEQUENCER: showSequencer(); break;
		}

		//view_mode = VIEW_PAD;
		//framePad.setVisibility(View.GONE);
		//frameSequencer.setVisibility(View.VISIBLE);

		
		//Intent intent = getIntent();
		if (mInputStream != null && mOutputStream != null) {
			return;
		}

		UsbAccessory[] accessories = mUsbManager.getAccessoryList();
		UsbAccessory accessory = (accessories == null ? null : accessories[0]);
		if (accessory != null) {
			if (mUsbManager.hasPermission(accessory)) {
				openAccessory(accessory);
			} else {
				synchronized (mUsbReceiver) {
					if (!mPermissionRequestPending) {
						mUsbManager.requestPermission(accessory,
								mPermissionIntent);
						mPermissionRequestPending = true;
					}
				}
			}
		} else {
			Log.d(TAG, "mAccessory is null");
		}
	}
	
	@Override
	public void onPause() {
		saveData();
		
		if(is_playing) {
			tmr.cancel();
			tmr.purge();
			is_playing = false;
			play_position = 0;
			//repeat_music = false;
		}
		super.onPause();
		closeAccessory();
	}
	
	@Override
	public void onDestroy() {
		saveData();
		
		unregisterReceiver(mUsbReceiver);
		super.onDestroy();
	}
	
	private void openAccessory(UsbAccessory accessory) {
		mFileDescriptor = mUsbManager.openAccessory(accessory);
		if (mFileDescriptor != null) {
			mAccessory = accessory;
			FileDescriptor fd = mFileDescriptor.getFileDescriptor();
			mInputStream = new FileInputStream(fd);
			mOutputStream = new FileOutputStream(fd);
			Thread thread = new Thread(null, this, "CaipirinhaHero");
			thread.start();
			Log.d(TAG, "accessory opened");
			enableControls(true);
		} else {
			Log.d(TAG, "accessory open fail");
		}
	}
	
	private void closeAccessory() {
		enableControls(false);

		try {
			if (mFileDescriptor != null) {
				mFileDescriptor.close();
			}
		} catch (IOException e) {
		} finally {
			mFileDescriptor = null;
			mAccessory = null;
		}
	}
	
	
	public void run() {
		/*
		int ret = 0;
		byte[] buffer = new byte[16384];
		int i;

		while (ret >= 0) {
			try {
				ret = mInputStream.read(buffer);
			} catch (IOException e) {
				break;
			}

			i = 0;
			while (i < ret) {
				int len = ret - i;

				switch (buffer[i]) {
				case 0x1:
					if (len >= 3) {
						Message m = Message.obtain(mHandler, MESSAGE_SWITCH);
						m.obj = new SwitchMsg(buffer[i + 1], buffer[i + 2]);
						mHandler.sendMessage(m);
					}
					i += 3;
					break;

				case 0x4:
					if (len >= 3) {
						Message m = Message.obtain(mHandler,
								MESSAGE_TEMPERATURE);
						m.obj = new TemperatureMsg(composeInt(buffer[i + 1],
								buffer[i + 2]));
						mHandler.sendMessage(m);
					}
					i += 3;
					break;

				case 0x5:
					if (len >= 3) {
						Message m = Message.obtain(mHandler, MESSAGE_LIGHT);
						m.obj = new LightMsg(composeInt(buffer[i + 1],
								buffer[i + 2]));
						mHandler.sendMessage(m);
					}
					i += 3;
					break;

				case 0x6:
					if (len >= 3) {
						Message m = Message.obtain(mHandler, MESSAGE_JOY);
						m.obj = new JoyMsg(buffer[i + 1], buffer[i + 2]);
						mHandler.sendMessage(m);
					}
					i += 3;
					break;

				default:
					Log.d(TAG, "unknown msg: " + buffer[i]);
					i = len;
					break;
				}
			}

		}
		*/
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			/*
			switch (msg.what) {
			case MESSAGE_SWITCH:
				SwitchMsg o = (SwitchMsg) msg.obj;
				handleSwitchMessage(o);
				break;

			case MESSAGE_TEMPERATURE:
				TemperatureMsg t = (TemperatureMsg) msg.obj;
				handleTemperatureMessage(t);
				break;

			case MESSAGE_LIGHT:
				LightMsg l = (LightMsg) msg.obj;
				handleLightMessage(l);
				break;

			case MESSAGE_JOY:
				JoyMsg j = (JoyMsg) msg.obj;
				handleJoyMessage(j);
				break;

			}
			*/
		}
	};

	public void sendCommand(byte command, byte target, int value) {
		byte[] buffer = new byte[3];
		if (value > 255)
			value = 255;

		buffer[0] = command;
		buffer[1] = target;
		buffer[2] = (byte) value;
		if (mOutputStream != null && buffer[1] != -1) {
			try {
				mOutputStream.write(buffer);
			} catch (IOException e) {
				Log.e(TAG, "write failed", e);
			}
		}
	}
	

	public void sendNote(int vId) {
		
		if (mInputStream == null) //verify if stream is active
			return;
		
		int value = 0;

		switch (vId) {
		case R.id.tecla_c: value = 128; break;
		case R.id.tecla_d: value = 64; break;
		case R.id.tecla_e: value = 32; break;
		case R.id.tecla_f: value = 16; break;
		case R.id.tecla_g: value = 8; break;
		case R.id.tecla_a: value = 4; break;
		case R.id.tecla_b: value = 2; break;
		case R.id.tecla_c1: value = 1; break;
		}
		
		sendCommand((byte)1, (byte)0, value);

		//toast.cancel();
		//toast.setText(value);
		//toast.show();

	}
	public void sendNote(Nota note) {
		sendCommand((byte)1, (byte)0, note.getNotes());
	}
	

	

	class PlayerTask extends TimerTask {
	   public void run() {
		   if (play_position < notes.size()) {
			   //Log.d(TAG, "TICK " + play_position + " note " + notes.get(play_position).getNotes());
			   //Log.d(TAG, "SENDING");
			   sendNote(notes.get(play_position));
			   play_position++;
		   }

		   if (play_position == notes.size()) {
			   if (!repeat_music) {
					tmr.cancel();
					tmr.purge();
					is_playing = false;
			   }
			   play_position = 0;
			   //Log.d(TAG, "TICK ZERO");
		   }
	   }
	}
	
	
	
	
	
	
	
	

	

	/**
	* Make a view to hold each row.
	*
	* @see android.widget.ListAdapter#getView(int, android.view.View,
	*      android.view.ViewGroup)
	*/
	

	public static class NotesAdapter extends BaseAdapter implements Filterable {
		private LayoutInflater mInflater;
		//private Context context;
		
		 
		public NotesAdapter(Context context) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			//this.context = context;
		}
		 
		 
		public View getView(final int position, View convertView, ViewGroup parent) {
			// A ViewHolder keeps references to children views to avoid
			// unneccessary calls
			// to findViewById() on each row.
			ViewHolder holder;
			final Nota nota = notes.get(position);
			

			
			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.sequencer_item, null);
				
				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.position = position;
				holder.note = nota;

				
				holder.nota_C = (TextView) convertView.findViewById(R.id.nota_C);
				holder.nota_D = (TextView) convertView.findViewById(R.id.nota_D);
				holder.nota_E = (TextView) convertView.findViewById(R.id.nota_E);
				holder.nota_F = (TextView) convertView.findViewById(R.id.nota_F);
				holder.nota_G = (TextView) convertView.findViewById(R.id.nota_G);
				holder.nota_A = (TextView) convertView.findViewById(R.id.nota_A);
				holder.nota_B = (TextView) convertView.findViewById(R.id.nota_B);
				holder.nota_C1 = (TextView) convertView.findViewById(R.id.nota_C1);
				holder.nota_num = (TextView) convertView.findViewById(R.id.nota_num);

				holder.nota_C.setOnClickListener(new OnClickListener() {
					public void onClick(View v) { 
						ViewHolder h = (ViewHolder)((View)v.getParent()).getTag();
						h.note.setNotaC(!h.note.getNotaC());
						//h.nota_num.setText(String.valueOf(h.note.getNotes()));
						v.setBackgroundDrawable(h.note.getNotaC() ? mFocusedTabImage : null);
					}
				});
				holder.nota_D.setOnClickListener(new OnClickListener() {
					public void onClick(View v) { 
						ViewHolder h = (ViewHolder)((View)v.getParent()).getTag();
						h.note.setNotaD(!h.note.getNotaD());
						//h.nota_num.setText(String.valueOf(h.note.getNotes()));
						v.setBackgroundDrawable(h.note.getNotaD() ? mFocusedTabImage : null);
					}
				});
				holder.nota_E.setOnClickListener(new OnClickListener() {
					public void onClick(View v) { 
						ViewHolder h = (ViewHolder)((View)v.getParent()).getTag();
						h.note.setNotaE(!h.note.getNotaE());
						//h.nota_num.setText(String.valueOf(h.note.getNotes()));
						v.setBackgroundDrawable(h.note.getNotaE() ? mFocusedTabImage : null);
					}
				});
				holder.nota_F.setOnClickListener(new OnClickListener() {
					public void onClick(View v) { 
						ViewHolder h = (ViewHolder)((View)v.getParent()).getTag();
						h.note.setNotaF(!h.note.getNotaF());
						//h.nota_num.setText(String.valueOf(h.note.getNotes()));
						v.setBackgroundDrawable(h.note.getNotaF() ? mFocusedTabImage : null);
					}
				});
				holder.nota_G.setOnClickListener(new OnClickListener() {
					public void onClick(View v) { 
						ViewHolder h = (ViewHolder)((View)v.getParent()).getTag();
						h.note.setNotaG(!h.note.getNotaG());
						//h.nota_num.setText(String.valueOf(h.note.getNotes()));
						v.setBackgroundDrawable(h.note.getNotaG() ? mFocusedTabImage : null);
					}
				});
				holder.nota_A.setOnClickListener(new OnClickListener() {
					public void onClick(View v) { 
						ViewHolder h = (ViewHolder)((View)v.getParent()).getTag();
						h.note.setNotaA(!h.note.getNotaA());
						//h.nota_num.setText(String.valueOf(h.note.getNotes()));
						v.setBackgroundDrawable(h.note.getNotaA() ? mFocusedTabImage : null);
					}
				});
				holder.nota_B.setOnClickListener(new OnClickListener() {
					public void onClick(View v) { 
						ViewHolder h = (ViewHolder)((View)v.getParent()).getTag();
						h.note.setNotaB(!h.note.getNotaB());
						//h.nota_num.setText(String.valueOf(h.note.getNotes()));
						v.setBackgroundDrawable(h.note.getNotaB() ? mFocusedTabImage : null);
					}
				});
				holder.nota_C1.setOnClickListener(new OnClickListener() {
					public void onClick(View v) { 
						ViewHolder h = (ViewHolder)((View)v.getParent()).getTag();
						h.note.setNotaC1(!h.note.getNotaC1());
						//h.nota_num.setText(String.valueOf(h.note.getNotes()));
						v.setBackgroundDrawable(h.note.getNotaC1() ? mFocusedTabImage : null);
					}
				});
				
				convertView.setTag(holder);
				
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();

			}
			
			holder.position = position;
			holder.note = nota;
			
			
			holder.nota_C.setBackgroundDrawable(nota.getNotaC() ? mFocusedTabImage : null);
			holder.nota_D.setBackgroundDrawable(nota.getNotaD() ? mFocusedTabImage : null);
			holder.nota_E.setBackgroundDrawable(nota.getNotaE() ? mFocusedTabImage : null);
			holder.nota_F.setBackgroundDrawable(nota.getNotaF() ? mFocusedTabImage : null);
			holder.nota_G.setBackgroundDrawable(nota.getNotaG() ? mFocusedTabImage : null);
			holder.nota_A.setBackgroundDrawable(nota.getNotaA() ? mFocusedTabImage : null);
			holder.nota_B.setBackgroundDrawable(nota.getNotaB() ? mFocusedTabImage : null);
			holder.nota_C1.setBackgroundDrawable(nota.getNotaC1() ? mFocusedTabImage : null);
			holder.nota_num.setText(String.valueOf(holder.position + 1)); //nota.getNotes()
			
			return convertView;
		}
		
		
		
		
		
		
		
		
		
		static class ViewHolder {
			int position;
			Nota note;
			TextView nota_C;
			TextView nota_D;
			TextView nota_E;
			TextView nota_F;
			TextView nota_G;
			TextView nota_A;
			TextView nota_B;
			TextView nota_C1;

			TextView nota_num;
		}
		 
		//@Override
		public Filter getFilter() {
			// TODO Auto-generated method stub
			return null;
		}
		 
		//@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		 
		//@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return notes.size();
		}
	 
		//@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return notes.get(position);
		}
	 
	}
	
	
}