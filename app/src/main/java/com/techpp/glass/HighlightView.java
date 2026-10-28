package com.techpp.glass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Full-screen overlay that draws the one focus ring: a white outline around the focused card or tile, moved
 * (with a small spring) by the activity, with a soft halo around it so it reads from across the room. In the day
 * theme a faint dark edge keeps it visible on light backgrounds; while an app is picked up for moving the ring is
 * thicker. The halo is two wider, fainter strokes: no blur, so it costs nothing per frame.
 */
public class HighlightView extends View {
    final RectF rect = new RectF();
    private float radius;
    private boolean day, moving;
    private final Paint ring = new Paint(Paint.ANTI_ALIAS_FLAG), edge = new Paint(Paint.ANTI_ALIAS_FLAG),
            halo = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final float dp;
    private final RectF tmp = new RectF();

    public HighlightView(Context c, AttributeSet a) {
        super(c, a);
        dp = c.getResources().getDisplayMetrics().density;
        ring.setStyle(Paint.Style.STROKE);
        ring.setColor(0xFFFFFFFF);
        edge.setStyle(Paint.Style.STROKE);
        edge.setColor(0x33000000);
        edge.setStrokeWidth(dp);
        halo.setStyle(Paint.Style.STROKE);
        setFocusable(false);
        setWillNotDraw(false);
    }

    void setRing(RectF r, float radiusPx) {
        rect.set(r);
        radius = radiusPx;
        invalidate();
    }

    void setDay(boolean d) { day = d; invalidate(); }

    void setMoving(boolean m) { moving = m; invalidate(); }

    @Override
    protected void onDraw(Canvas canvas) {
        if (rect.isEmpty()) return;
        float w = (moving ? 5f : 4f) * dp;
        // The halo: outermost and faintest first, just outside the ring.
        int haloRgb = day ? 0x000000 : 0xFFFFFF;
        for (int i = 2; i >= 1; i--) {
            float hw = i * 5 * dp;
            halo.setStrokeWidth(hw);
            halo.setColor(((day ? 0x14 : 0x22) / i << 24) | haloRgb);
            tmp.set(rect);
            tmp.inset(-w - hw / 2, -w - hw / 2);
            canvas.drawRoundRect(tmp, radius + w + hw / 2, radius + w + hw / 2, halo);
        }
        ring.setStrokeWidth(w);
        tmp.set(rect);
        tmp.inset(-w / 2, -w / 2);
        canvas.drawRoundRect(tmp, radius + w / 2, radius + w / 2, ring);
        if (day) {
            tmp.inset(-w / 2 - dp / 2, -w / 2 - dp / 2);
            canvas.drawRoundRect(tmp, radius + w + dp / 2, radius + w + dp / 2, edge);
        }
    }
}
