package org.markusolsson.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.markusolsson.game.TenAddaTen;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = TenAddaTen.WIDTH;
		config.height = TenAddaTen.HEIGHT;
		config.title = TenAddaTen.TITLE;
		new LwjglApplication(new TenAddaTen(), config);
	}
}
