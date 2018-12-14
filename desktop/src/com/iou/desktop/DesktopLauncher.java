package com.iou.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.iou.The_Main_Game_Class;
import com.iou.IOU;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = IOU.HEIGHT;//600
		config.width= IOU.WIDTH;//1000
		//new LwjglApplication(new The_Main_Game_Class(), config);
		new LwjglApplication(new IOU(),config);
	}
}
