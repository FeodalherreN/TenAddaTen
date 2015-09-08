package org.markusolsson.game.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import org.markusolsson.game.States.PlayState;
import org.markusolsson.game.TenAddaTen;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new TenAddaTen(), config);
	}

	@Override
	protected void onPause() {
		finish();
		super.onPause();
	}
}
