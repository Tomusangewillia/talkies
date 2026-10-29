package com.techpp.glass;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.StatFs;
import android.util.AttributeSet;
import android.view.View;

import java.util.Locale;

/**
 * Beside the icon buttons: how much RAM and storage are free, each as a label, a figure and a slim bar of what's used.
 * Drawn on one canvas rather than six views; read again on every minute tick (both reads take well under a
 * millisecond). Talkies settings → RAM and storage hides it.
 */
public class Meters extends View {
    private final Paint label = new Paint(Paint.ANTI_ALIAS_FLAG), value = new Paint(Paint.ANTI_ALIAS_FLAG),
            bar = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF r = new RectF();
    private final float dp;
    private int text = 0xFFFFFFFF;
    private float ramUsed, storageUsed;
    private String ramFree = "", storageFree = "";

    public Meters(Context c, AttributeSet a) {
        super(c, a);
        dp = getResources().getDisplayMetrics().density;
        float sp = getResources().getDisplayMetrics().scaledDensity;
        Typeface outfit = getResources().getFont(R.font.outfit);
        label.setTypeface(outfit);
        label.setFontVariationSettings("'wght' 500");
        label.setTextSize(10 * sp);
        value.setTypeface(outfit);
        value.setFontVariationSettings("'wght' 400");
        value.setTextSize(10 * sp);
        value.setTextAlign(Paint.Align.RIGHT);
    }

    void setColor(int text) {
        this.text = text;
        invalidate();
    }

    /** Reads RAM and storage now. */
    void update() {
        ActivityManager am = getContext().getSystemService(ActivityManager.class);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        ramUsed = mi.totalMem > 0 ? 1f - (float) mi.availMem / mi.totalMem : 0;
        ramFree = gb(mi.availMem) + " free";
        try {
            StatFs fs = new StatFs(Environment.getDataDirectory().getPath());
            storageUsed = 1f - (float) fs.getAvailableBytes() / fs.getTotalBytes();
            storageFree = gb(fs.getAvailableBytes()) + " free";
        } catch (RuntimeException e) {
            storageFree = "";
        }
        invalidate();
    }

    /** "0.9 GB", "21.4 GB"; under 1 GB of RAM reads better as "860 MB". */
    private static String gb(long bytes) {
        if (bytes < 1e9) return Math.round(bytes / 1e6) + " MB";
        return String.format(Locale.getDefault(), "%.1f GB", bytes / 1e9);
    }

    @Override
    protected void onDraw(Canvas c) {
        float w = getWidth(), h = getHeight();
        row(c, "RAM", ramFree, ramUsed, 0, w);
        row(c, "Storage", storageFree, storageUsed, h / 2f, w);
    }

    /** A label and figure, with the bar under them, in the top or bottom half. */
    private void row(Canvas c, String name, String free, float used, float top, float w) {
        float base = top + 11 * dp;
        label.setColor((text & 0x00FFFFFF) | 0xCC000000);
        value.setColor((text & 0x00FFFFFF) | 0x99000000);
        c.drawText(name, 0, base, label);
        c.drawText(free, w, base, value);
        float y = base + 5 * dp, t = 3 * dp;
        bar.setColor((text & 0x00FFFFFF) | 0x38000000);
        r.set(0, y, w, y + t);
        c.drawRoundRect(r, t / 2, t / 2, bar);
        bar.setColor((text & 0x00FFFFFF) | 0xD8000000);
        r.set(0, y, Math.max(t, w * Math.min(1f, used)), y + t);
        c.drawRoundRect(r, t / 2, t / 2, bar);
    }
}
