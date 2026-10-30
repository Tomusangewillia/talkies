package com.techpp.glass;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The apps you opened most recently through Talkies (the dock, All apps, picks, channel cards, remote buttons), newest
 * first: Control Center → Recent apps, or a remote button set to it. Only what Talkies itself opened, so the
 * accessibility service still watches no other app's windows. An idea from tvOS Launcher's Recent Apps.
 */
final class RecentApps {
    static final int MAX = 12;

    private RecentApps() { }

    private static SharedPreferences prefs(Context c) { return c.getSharedPreferences("recent_apps", Context.MODE_PRIVATE); }

    static List<String> list(Context c) {
        String s = prefs(c).getString("apps", "");
        return s.isEmpty() ? new ArrayList<>() : new ArrayList<>(Arrays.asList(s.split(",")));
    }

    /** Moves {@code pkg} to the front; writes only when the order changes. */
    static void note(Context c, String pkg) {
        List<String> l = list(c);
        if (!l.isEmpty() && l.get(0).equals(pkg)) return;
        l.remove(pkg);
        l.add(0, pkg);
        while (l.size() > MAX) l.remove(l.size() - 1);
        prefs(c).edit().putString("apps", String.join(",", l)).apply();
    }

    static void clear(Context c) { prefs(c).edit().remove("apps").apply(); }
}
