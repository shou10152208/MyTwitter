package com.example.mytwitter;

import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.eaglesakura.lib.android.game.sound.SoundManager;

public class TweetMenuActivity extends FragmentActivity {
	  

	private Twitter mTwitter;

	


	//名前読み込み(不要)
	//	private String sName = intent.getStringExtra("screenName");

	protected SoundManager soundManager = null;
	protected Status status;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweet_menu);
//		int sItem = getIntent().getIntExtra("sItem", 0);
		status = (Status) getIntent().getSerializableExtra("sItem");
		mTwitter = TwitterUtils.getTwitterInstance(this);
/*		ResponseList<Status> homeTl = null;
		try {
			homeTl = mTwitter.getHomeTimeline();
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
*/
//		statuses = homeTl.get(sItem);
//		status = statuses.getClass();

		soundManager = new SoundManager(getApplicationContext());

		/*(不要)
		try {
			FileInputStream fis = openFileInput("SaveData.dat");
			ObjectInputStream ois = new ObjectInputStream(fis);
			item = (Status) ois.readObject();
			ois.close();
		} catch (Exception e) {
			Log.e(TAG, "Error");
		}
		 */
	}

	public void rep(View view) {
		playSE(R.raw.mikuxtu_se);
		//TODO 受け渡されたツイッター名の引数name格納
		//		Intent intent = new Intent(this, TweetActivity.class);
		//		intent.putExtra("screenName", sName);
		//		startActivity(intent);
	}

	public void ret(View view) throws TwitterException {
		playSE(R.raw.mikuxtu_se);
//		mTwitter.retweetStatus(status.getId());
  		try {
			mTwitter.retweetStatus(status.getId());
			playSE(R.raw.mikuxtu_se);
			showToast("リツイートが完了しました！");
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			showToast("リツイートに失敗しました。。。");

		}

	}

	public void fav(View view) throws TwitterException {
		playSE(R.raw.mikuxtu_se);
//		mTwitter.createFavorite(status.getId());
		try {
			mTwitter.createFavorite(status.getId());
			playSE(R.raw.mikuxtu_se);
			showToast("お気に入りが完了しました！");;
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
			showToast("お気に入りに失敗しました。。。");

		}
		
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
