package com.qtech.bubbleblaster.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.qtech.bubbleblaster.QBubbles1;
import com.qtech.bubbleblaster.Test1;
import com.qtech.preloader.PreClassLoader;

public class DesktopLauncher {
	public static void main (String[] arg, PreClassLoader preClassLoader) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//		new LwjglApplication(new QBubbles1(), config);
		new LwjglApplication(new Test1(), config);
	}
}
