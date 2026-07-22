package com.example.descrolled

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class TestAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("TestA11y", "TEST SERVICE CONNECTED")

        val info = serviceInfo ?: AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        info.flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        info.notificationTimeout = 100
        serviceInfo = info

        Log.d("TestA11y", "serviceInfo set: eventTypes=${info.eventTypes}")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        Log.d("TestA11y", "EVENT FIRED: ${AccessibilityEvent.eventTypeToString(event.eventType)} package=${event.packageName}")
        val root = rootInActiveWindow
        Log.d("TestA11y", "rootInActiveWindow is ${if (root == null) "NULL" else "NOT NULL, childCount=${root.childCount}"}")
    }

    override fun onInterrupt() {}
}