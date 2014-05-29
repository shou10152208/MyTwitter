package com.example.mytwitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.eaglesakura.lib.android.game.sound.SoundManager;

public class TweetActivity extends FragmentActivity {

	private EditText mInputText;
	private Twitter mTwitter;
	protected SoundManager soundManager = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet);

		mTwitter = TwitterUtils.getTwitterInstance(this);

		mInputText = (EditText) findViewById(R.id.input_text);
		
		soundManager = new SoundManager(getApplicationContext());


		findViewById(R.id.action_tweet).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						tweet();
					}
				});
	}

	private void tweet() {
		AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(String... params) {
				try {
					mTwitter.updateStatus(params[0]);
					return true;
				} catch (TwitterException e) {
					e.printStackTrace();
					return false;
				}
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					showToast("ツイートが完了しました！");
					playSE(R.raw.se_maoudamashii_onepoint17);
					finish();
				} else {
					showToast("ツイートに失敗しました。。。");
				}
			}
		};
		task.execute(mInputText.getText().toString());
	}

	private void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
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
}