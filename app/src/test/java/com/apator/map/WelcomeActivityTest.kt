package com.apator.map

import android.os.Build
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.N_MR1])
class WelcomeActivityTest {
    private lateinit var activity: MainActivity

    @Before
    fun setUp() {
        activity =
            Robolectric.setupActivity(MainActivity::class.java)//buildActivity(MainActivity::class.java).setup().create().start().get()
    }

    @Test
    fun activityShouldNotBeNull() {
        assertNotNull(activity)
    }
}