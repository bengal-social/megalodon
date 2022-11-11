package org.joinmastodon.android;

import android.content.Context;
import android.content.SharedPreferences;

public class GlobalUserPreferences{
	public static boolean playGifs;
	public static boolean useCustomTabs;
	public static boolean trueBlackTheme;
	public static boolean showReplies;
	public static boolean showBoosts;
	public static boolean loadNewPosts;
	public static boolean showInteractionCounts;
	public static ThemePreference theme;

	private static SharedPreferences getPrefs(){
		return MastodonApp.context.getSharedPreferences("global", Context.MODE_PRIVATE);
	}

	public static void load(){
		SharedPreferences prefs=getPrefs();
		playGifs=prefs.getBoolean("playGifs", true);
		useCustomTabs=prefs.getBoolean("useCustomTabs", true);
		trueBlackTheme=prefs.getBoolean("trueBlackTheme", false);
		showReplies=prefs.getBoolean("showReplies", true);
		showBoosts=prefs.getBoolean("showBoosts", true);
		loadNewPosts=prefs.getBoolean("loadNewPosts", true);
		showInteractionCounts=prefs.getBoolean("showInteractionCounts", true);
		theme=ThemePreference.values()[prefs.getInt("theme", 0)];
	}

	public static void save(){
		getPrefs().edit()
				.putBoolean("playGifs", playGifs)
				.putBoolean("useCustomTabs", useCustomTabs)
				.putBoolean("showReplies", showReplies)
				.putBoolean("showBoosts", showBoosts)
				.putBoolean("loadNewPosts", loadNewPosts)
				.putBoolean("trueBlackTheme", trueBlackTheme)
				.putBoolean("showInteractionCounts", showInteractionCounts)
				.putInt("theme", theme.ordinal())
				.apply();
	}

	public enum ThemePreference{
		AUTO,
		LIGHT,
		DARK
	}
}
