/**
 * Name: Joshua Venable
 * Date: 10/15/2022
 * Description: Haptic feedback manager for Android wearOS devices
 * Notes: 
 * TODO: Implement haptic feedback if a user is running too fast or too slow
 * 
 **/

package com.example.runningpacenotifier;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;

public class HapticManager {
    private Context mContext;
    private Vibrator mVibrator;

    public HapticManager(Context context) {
        mContext = context;
        mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * If the user has enabled haptic feedback in the system settings, return true, otherwise return
     * false.
     *
     * @return The value of the setting for haptic feedback.
     */
    public boolean hapticEnabled() {
        int enabled = Settings.System.getInt(mContext.getContentResolver(),
                Settings.System.HAPTIC_FEEDBACK_ENABLED, 0);
        return enabled != 0;
    }

    /**
     * If the device has a vibrator, vibrate for 1000 milliseconds with the default amplitude.
     */
    public void vibrate() {
        if (mVibrator.hasVibrator()) {
            mVibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
        }
    }

    /**
     * Vibrate for 0 milliseconds, then vibrate for 100 milliseconds, then vibrate for 100
     * milliseconds, then vibrate for 100 milliseconds, then vibrate for 100 milliseconds.
     */
    public void vibrateSlowDown() {
        if(mVibrator.hasVibrator()) {
            mVibrator.vibrate(VibrationEffect.createWaveform(new long[] {0, 100, 100, 100, 100}, 1));
        }
    }

    /**
     * Vibrate for 1 second, pause for 1 second, vibrate for 1 second, pause for 1 second.
     */
    public void vibrateSpeedUp() {
        if(mVibrator.hasVibrator()) {
            mVibrator.vibrate(VibrationEffect.createWaveform(new long[] {0, 1000, 1000, 1000}, 0));
        }
    }
}