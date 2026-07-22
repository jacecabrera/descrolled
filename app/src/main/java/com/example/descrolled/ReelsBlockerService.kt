package com.example.descrolled

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo


class ReelsBlockerService : AccessibilityService() {

    companion object {
        private const val TAG = "ReelsBlocker"
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "Service connected")

        val info = serviceInfo ?: AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        info.notificationTimeout = 100
        info.packageNames = arrayOf("com.instagram.android")
        serviceInfo = info

        Log.d(TAG, "Manually set serviceInfo: eventTypes=${info.eventTypes}, flags=${info.flags}")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.d(TAG, "Event: ${AccessibilityEvent.eventTypeToString(event.eventType)} " +
                "package=${event.packageName} class=${event.className}")

        val sourceNode = event.source
        if (sourceNode == null) {
            Log.d(TAG, "event.source is NULL, trying rootInActiveWindow")
            val rootNode = rootInActiveWindow
            if (rootNode == null) {
                Log.d(TAG, "rootInActiveWindow is ALSO NULL")
                return
            }
            dumpNodeTree(rootNode, depth = 0)
            return
        }

        dumpNodeTree(sourceNode, depth = 0)
    }
    // confirmed: com.instagram.android:id/clips_viewer_view_pager
    // identifies reels player window (root ViewPager, depth=0)
    private fun dumpNodeTree(node: AccessibilityNodeInfo, depth: Int) {
        val indent = "  ".repeat(depth)
        val id = node.viewIdResourceName
        val desc = node.contentDescription
        val text = node.text

        // Log EVERY node this time, not just ones with identifying info
        Log.d(TAG, "$indent [depth=$depth] class=${node.className} id=$id desc=$desc text=$text")

        for (i in 0 until node.childCount) {
            node.getChild(i)?.let { child ->
                dumpNodeTree(child, depth + 1)
            }
        }
    }

    override fun onInterrupt() {
        Log.d(TAG, "Service interrupted")
    }
}