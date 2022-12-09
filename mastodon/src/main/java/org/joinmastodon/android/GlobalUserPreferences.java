package org.joinmastodon.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GlobalUserPreferences{
	public static boolean playGifs;
	public static boolean useCustomTabs;
	public static boolean trueBlackTheme;
	public static ThemePreference theme;
	public static List<String> recentLanguages;

	private static String defaultRecentLanguages;

	static {
		List<Locale> systemLocales = new ArrayList<>();;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
			systemLocales.add(Resources.getSystem().getConfiguration().locale);
		} else {
			LocaleList localeList = Resources.getSystem().getConfiguration().getLocales();
			for (int i = 0; i < localeList.size(); i++) systemLocales.add(localeList.get(i));
		}

		defaultRecentLanguages = systemLocales.stream().map(Locale::getLanguage).collect(Collectors.joining(","));
	}

	private static SharedPreferences getPrefs(){
		return MastodonApp.context.getSharedPreferences("global", Context.MODE_PRIVATE);
	}

	public static void load(){
		SharedPreferences prefs=getPrefs();
		playGifs=prefs.getBoolean("playGifs", true);
		useCustomTabs=prefs.getBoolean("useCustomTabs", true);
		trueBlackTheme=prefs.getBoolean("trueBlackTheme", false);
		theme=ThemePreference.values()[prefs.getInt("theme", 0)];
		recentLanguages=Arrays.stream(prefs.getString("recentLanguages", defaultRecentLanguages).split(",")).filter(s->!s.isEmpty()).collect(Collectors.toList());
	}

	public static void save(){
		getPrefs().edit()
				.putBoolean("playGifs", playGifs)
				.putBoolean("useCustomTabs", useCustomTabs)
				.putBoolean("trueBlackTheme", trueBlackTheme)
				.putInt("theme", theme.ordinal())
				.putString("recentLanguages", String.join(",", recentLanguages))
				.apply();
	}

	public enum ThemePreference{
		AUTO,
		LIGHT,
		DARK
	}
}
