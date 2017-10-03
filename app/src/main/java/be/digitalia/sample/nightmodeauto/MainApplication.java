package be.digitalia.sample.nightmodeauto;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

public class MainApplication extends Application {

	static {
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
	}
}
