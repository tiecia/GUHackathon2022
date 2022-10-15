/**
 * Haptic Manager Test Class
 */

package com.example.runningpacenotifier;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 **/
@RunWith(AndroidJUnit4.class)
public class HapticTest {
    private HapticManager hapticManager;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        hapticManager = new HapticManager(context);
    }

    @Test
    public void testTrue() {
        assertEquals(true, true);
    }

    @Test
    public void testHapticEnabled() {
        assertEquals(true, hapticManager.hapticEnabled());
    }

    @Test
    public void testVibrate() {
        hapticManager.vibrate();
        assertEquals(true, true);
    }

    @Test
    public void testVibrate2() {
        hapticManager.vibrateSlowDown();
        assertEquals(true, true);
    }

    @Test
    public void testVibrate3() {
        hapticManager.vibrateSpeedUp();
        assertEquals(true, true);
    }
}