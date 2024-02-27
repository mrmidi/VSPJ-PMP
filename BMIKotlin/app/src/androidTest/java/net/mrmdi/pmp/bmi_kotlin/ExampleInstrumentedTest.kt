package net.mrmdi.pmp.bmi_kotlin

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import net.mrmdi.pmp.bmi_kotlin.ui.theme.BMIKotlinTheme

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("net.mrmdi.pmp.bmi_kotlin", appContext.packageName)
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    @Test
    fun calcTest() {

        // input height
        composeTestRule
            .onNodeWithTag("HeightTextField")
            .performTextInput("180")

        // input weight
        composeTestRule
            .onNodeWithTag("WeightTextField")
            .performTextInput("75")

        // click calculate
        composeTestRule
            .onNodeWithContentDescription("Calculate")
            .performClick()

        // verify result
        composeTestRule
            .onNodeWithText("BMI: 23.15\nNormal")
            .assertExists()
    }


}