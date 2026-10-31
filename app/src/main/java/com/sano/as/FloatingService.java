package com.sano.as;
import android.app.Service; import android.content.Intent; import android.graphics.PixelFormat; import android.os.Build; import android.os.IBinder; import android.view.Gravity; import android.view.LayoutInflater; import android.view.MotionEvent; import android.view.View; import android.view.WindowManager; import android.widget.FrameLayout; import android.widget.Toast; import androidx.annotation.Nullable;

public class FloatingService extends Service {
    public static boolean isRunning = false; private WindowManager wm; private FrameLayout bubble; private WindowManager.LayoutParams params;
    @Nullable @Override public IBinder onBind(Intent intent) { return null; }
    @Override public void onCreate() {
        super.onCreate(); isRunning = true; wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        bubble = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.bubble_layout, null);
        int type = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE;
        params = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, type, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.START; params.x = 100; params.y = 200; wm.addView(bubble, params);
        bubble.setOnTouchListener(new View.OnTouchListener() {
            private int initX, initY; private float initTouchX, initTouchY; private boolean moved = false;
            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: initX = params.x; initY = params.y; initTouchX = event.getRawX(); initTouchY = event.getRawY(); moved = false; return true;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) (event.getRawX() - initTouchX); int dy = (int) (event.getRawY() - initTouchY);
                        if (Math.abs(dx) > 10 || Math.abs(dy) > 10) moved = true; params.x = initX + dx; params.y = initY + dy; wm.updateViewLayout(bubble, params); return true;
                    case MotionEvent.ACTION_UP:
                        if (!moved) { boolean done = SkipAccessibilityService.triggerSkip(); if (!done) Toast.makeText(FloatingService.this, "Belum Ada Iklan!!", Toast.LENGTH_SHORT).show(); }
                        return true;
                } return false;
            }
        });
    }
    @Override public int onStartCommand(Intent intent, int flags, int startId) { return START_STICKY; }
    @Override public void onDestroy() { super.onDestroy(); isRunning = false; if (bubble != null && wm != null) try { wm.removeView(bubble); } catch (Exception ignored) {} }
}