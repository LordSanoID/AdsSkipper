package com.sano.as;
import android.accessibilityservice.AccessibilityService; import android.view.accessibility.AccessibilityEvent; import android.view.accessibility.AccessibilityNodeInfo; import java.util.List;

public class SkipAccessibilityService extends AccessibilityService {
    private static SkipAccessibilityService instance;
    @Override protected void onServiceConnected() { super.onServiceConnected(); instance = this; }
    @Override public void onAccessibilityEvent(AccessibilityEvent event) { if (event.getSource() != null) autoSkip(event.getSource()); }
    private void autoSkip(AccessibilityNodeInfo root) {
        String[] keys = {"skip", "close", "tutup", "lewati", "Skip", "Close"};
        for (String key : keys) {
            List<AccessibilityNodeInfo> nodes = root.findAccessibilityNodeInfosByText(key);
            if (nodes == null) continue;
            for (AccessibilityNodeInfo n : nodes) {
                if (n == null) continue; AccessibilityNodeInfo clickable = n;
                while (clickable != null && !clickable.isClickable()) clickable = clickable.getParent();
                if (clickable != null) { clickable.performAction(AccessibilityNodeInfo.ACTION_CLICK); return; }
            }
        }
    }
    public static boolean triggerSkip() {
        if (instance == null) return false; AccessibilityNodeInfo root = instance.getRootInActiveWindow(); if (root == null) return false;
        String[] texts = {"skip", "Skip", "SKIP", "close", "Close", "Tutup", "Lewati", "X", "×"};
        for (String t : texts) {
            List<AccessibilityNodeInfo> nodes = root.findAccessibilityNodeInfosByText(t); if (nodes == null || nodes.isEmpty()) continue;
            for (AccessibilityNodeInfo n : nodes) {
                AccessibilityNodeInfo clickable = n; while (clickable != null && !clickable.isClickable()) clickable = clickable.getParent();
                if (clickable != null) { clickable.performAction(AccessibilityNodeInfo.ACTION_CLICK); return true; }
            }
        } return false;
    }
    @Override public void onInterrupt() {}
}