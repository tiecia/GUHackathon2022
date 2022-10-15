package com.example.runningpacenotifier;

import static org.junit.Assert.*;

import android.location.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class LocationServiceTest {

    /**
     * Tests the LocationService class, and see's if its listening to movement
     */
        private LocationService locationService;
        @Before
        public void setUp() {
            Context context = ApplicationProvider.getApplicationContext();
            locationService = new LocationService(context);
        }

        @Test
        public void testTrue() {
            assertEquals(true, true);
        }

        @Test
        public void testLocationService() {
            Location location = new Location("test");
            location.setLatitude(0);
            location.setLongitude(0);
            location.setSpeed(0);
            locationService.onLocationChanged(location);
            assertEquals(0, locationService.getLastLocation().getLatitude(), 0);
            assertEquals(0, locationService.getLastLocation().getLongitude(), 0);
            assertEquals(0, locationService.getLastLocation().getSpeed(), 0);
        }

        @Test
        public void testLocationService2() {
            Location location = new Location("test");
            location.setLatitude(1);
            location.setLongitude(1);
            location.setSpeed(1);
            locationService.onLocationChanged(location);
            assertEquals(1, locationService.getLastLocation().getLatitude(), 0);
            assertEquals(1, locationService.getLastLocation().getLongitude(), 0);
            assertEquals(1, locationService.getLastLocation().getSpeed(), 0);
        }

        @Test
        public void testLocationService3() {
            Location location = new Location("test");
            location.setLatitude(2);
            location.setLongitude(2);
            location.setSpeed(2);
            locationService.onLocationChanged(location);
            assertEquals(2, locationService.getLastLocation().getLatitude(), 0);
            assertEquals(2, locationService.getLastLocation().getLongitude(), 0);
            assertEquals(2, locationService.getLastLocation().getSpeed(), 0);
        }

        @Test
        public void testLocationService4() {
            Location location = new Location("test");
            location.setLatitude(3);
            location.setLongitude(3);
            location.setSpeed(3);
            locationService.onLocationChanged(location);
            assertEquals(3, locationService.getLastLocation().getLatitude(), 0);
            assertEquals(3, locationService.getLastLocation().getLongitude(), 0);
            assertEquals(3, locationService.getLastLocation().getSpeed(), 0);
        }

        @Test
        public void testLocationService5() {
            Location location = new Location("test");
            location.setLatitude(4);
            location.setLongitude(4);
            location.setSpeed(4);
            locationService.onLocationChanged(location);
            assertEquals(4, locationService.getLastLocation().getLatitude(), 0);
            assertEquals(4, locationService.getLastLocation().getLongitude(), 0);
        }

}