package `in`.eoninfotech.eontechnician.java.login

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import `in`.eoninfotech.eontechnician.R
import `in`.eoninfotech.eontechnician.activity.LoginActivityNew
import org.hamcrest.CoreMatchers.startsWith
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityUITest {

    @get:Rule
    val activityRule = ActivityScenarioRule(LoginActivityNew::class.java)

    // TC_01: Empty username shows error
    @Test
    fun emptyUsername_showsError() {
        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.passwordd)).perform(typeText("pass123"), closeSoftKeyboard())
        onView(withId(R.id.login)).perform(click())
        onView(withId(R.id.user)).check(matches(hasErrorText("Please Enter Username")))
    }

    // TC_02: Empty password shows error
    @Test
    fun emptyPassword_showsError() {
        onView(withId(R.id.username)).perform(typeText("user123"), closeSoftKeyboard())
        onView(withId(R.id.passwordd)).perform(clearText())
        onView(withId(R.id.login)).perform(click())
        onView(withId(R.id.pass)).check(matches(hasErrorText("Please Enter Password")))
    }

    // TC_03: Both empty shows username error first
    @Test
    fun bothEmpty_showsUsernameErrorFirst() {
        onView(withId(R.id.username)).perform(clearText())
        onView(withId(R.id.passwordd)).perform(clearText())
        onView(withId(R.id.login)).perform(click())
        onView(withId(R.id.user)).check(matches(hasErrorText("Please Enter Username")))
    }

    // TC_04: App version displayed
    @Test
    fun appVersion_isDisplayed() {
        onView(withId(R.id.app_version)).check(matches(isDisplayed()))
        onView(withId(R.id.app_version)).check(matches(withText(startsWith("Version"))))
    }
}