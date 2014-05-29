package com.example.mytwitter;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.eaglesakura.lib.android.game.sound.SoundManager;
import com.loopj.android.image.SmartImageView;

public class MainActivity extends ListActivity {
	private TweetAdapter mAdapter;
	private Twitter mTwitter;
	//	private String sName;
	protected SoundManager soundManager = null;
	private static final String TAG = "LogTest";  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!TwitterUtils.hasAccessToken(this)) {
			Intent intent = new Intent(this, TwitterOAuthActivity.class);
			startActivity(intent);
			finish();

		} else {

			soundManager = new SoundManager(getApplicationContext());

			mAdapter = new TweetAdapter(this);
			setListAdapter(mAdapter);

			mTwitter = TwitterUtils.getTwitterInstance(this);
			playSE(R.raw.nami);
			reloadTimeLine();
			//test
//			Intent intent = new Intent(MainActivity.this, TweetMenuActivity.class); 
//			intent.putExtra("sItem", sItem);
//			startActivity(intent); 

		}
	}

	/**
	 * rawリソースIDを指定して音を再生する
	 * 
	 * @param rawId
	 */
	public void playSE(int rawId) {
		// サウンドが読み込まれてなかったら、この場で読み込み
		if (!soundManager.isLoaded(rawId)) {
			// rawからサウンドの読み込み
			soundManager.loadFromRaw(rawId, rawId);
		}
		// サウンドの再生を行う
		soundManager.play(rawId);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private class TweetAdapter extends ArrayAdapter<twitter4j.Status> {
		
		private LayoutInflater mInflater;
		//シリアライズのクラスバージョンが異なってないかの識別するための定義
		//		private static final long serialVersionUID = 6255752248513019027L;

		public TweetAdapter(Context context) {
			super(context, android.R.layout.simple_list_item_1);
			mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.list_item_tweet, null);
			}
			final Status item = getItem(position);
			TextView name = (TextView) convertView.findViewById(R.id.name);
			name.setText(item.getUser().getName());
			TextView screenName = (TextView) convertView
					.findViewById(R.id.screen_name);
			screenName.setText("@" + item.getUser().getScreenName());
			//			sName = screenName.getText().toString();
			TextView text = (TextView) convertView.findViewById(R.id.text);
		/*	
			//シリアライズ処理(不要？)
				try {
					FileOutputStream fos = openFileOutput("SaveData.dat", MODE_PRIVATE);
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(item);
					oos.close();
				} catch (Exception e) {
					Log.e(TAG, "Error");
				}
		*/	 	
			
			//クリック画面遷移可能実証済み
			// textをクリックできるようにする
			text.setClickable(true);

			// クリックイベントのリスナーをセット
			text.setOnClickListener(new OnClickListener() {

				@Override 
				public void onClick(View v) {
					if (v.getId() == R.id.text) {
						Intent intent = new Intent(MainActivity.this, TweetMenuActivity.class); 
						intent.putExtra("sItem", item);
						startActivity(intent); 
					} 
				}
			});

			text.setText(item.getText());

			SmartImageView icon = (SmartImageView) convertView
					.findViewById(R.id.icon);
			icon.setImageUrl(item.getUser().getProfileImageURL());
			return convertView;
		}
	}
	/*
 	クリック画面遷移可能実証済み
	@Override
	protected void onListItemClick(ListView listView, View v, int position,
			long id) {
		super.onListItemClick(listView, v, position, id);
		Intent intent = new Intent(this, TweetMenuActivity.class);
		 intent.putExtra("screenName", sName);
		startActivity(intent);
	}
	 */

	private void reloadTimeLine() {
		AsyncTask<Void, Void, List<twitter4j.Status>> task = new AsyncTask<Void, Void, List<twitter4j.Status>>() {
			@Override
			protected List<twitter4j.Status> doInBackground(Void... params) {
				try {
					return mTwitter.getHomeTimeline();
				} catch (TwitterException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<twitter4j.Status> result) {
				if (result != null) {
					mAdapter.clear();
					for (twitter4j.Status status : result) {
						mAdapter.add(status);
					}
					getListView().setSelection(0);
				} else {
					showToast("タイムラインの取得に失敗しました。。。");
				}
			}
		};
		task.execute();
	}

	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			reloadTimeLine();
			playSE(R.raw.se_onepoint23);
			return true;
		case R.id.menu_tweet:
			Intent intent = new Intent(this, TweetActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * public void retweetMenu(View view) { Intent intent = new Intent(this,
	 * TweetActivity.class); startActivity(intent); return true; }
	 */
}
