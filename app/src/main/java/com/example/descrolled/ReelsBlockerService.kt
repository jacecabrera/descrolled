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

        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPES_ALL_MASK
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
            notificationTimeout = 100
            packageNames = arrayOf("com.instagram.android")
        }
        serviceInfo = info

        Log.d(TAG, "Manually set serviceInfo: eventTypes=${info.eventTypes}, flags=${info.flags}")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.d(TAG, "Event: ${AccessibilityEvent.eventTypeToString(event.eventType)} " +
                "package=${event.packageName} class=${event.className}")

        val rootNode = rootInActiveWindow ?: return
        dumpNodeTree(rootNode, depth = 0)
    }

    private fun dumpNodeTree(node: AccessibilityNodeInfo, depth: Int) {
        val indent = "  ".repeat(depth)
        val id = node.viewIdResourceName
        val desc = node.contentDescription
        val text = node.text

        if (id != null || desc != null || text != null) {
            Log.d(TAG, "$indent id=$id desc=$desc text=$text " +
                    "class=${node.className}")
        }

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