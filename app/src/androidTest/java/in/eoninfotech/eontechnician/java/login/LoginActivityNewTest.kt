//package `in`.eoninfotech.eontechnician.java.login
//
//import androidx.test.core.app.ActivityScenario
//import androidx.test.espresso.Espresso.closeSoftKeyboard
//import androidx.test.espresso.Espresso.onView
//import androidx.test.espresso.action.ViewActions.click
//import androidx.test.espresso.action.ViewActions.typeText
//import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
//import androidx.test.espresso.matcher.ViewMatchers.withId
//import androidx.test.espresso.matcher.ViewMatchers.withText
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import `in`.eoninfotech.eontechnician.activity.LoginActivityNew
//import `in`.eoninfotech.eontechnician.di.SharedPreferenceManager
//import `in`.eoninfotech.eontechnician.test.R
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import java.util.regex.Pattern.matches
//import android.content.Intent
//import android.net.Uri
//import android.widget.EditText
//import android.widget.TextView
//import androidx.test.core.app.ApplicationProvider
//import com.google.android.material.textfield.TextInputLayout
//import `in`.eoninfotech.eontechnician.AppPreferences
//import `in`.eoninfotech.eontechnician.helper.CheckConnection
//import `in`.eoninfotech.eontechnician.storage.LocationPrefs
//import org.junit.Assert.*
//
//@HiltAndroidTest
//@RunWith(RobolectricTestRunner::class)
//class LoginActivityNewTest {
//
//    private lateinit var activity: LoginActivityNew
//
//    // ✅ Mocks (we will inject manually)
//    private val prefManager = mock<SharedPreferenceManager>()
//    private val appPrefs = mock<AppPreferences>()
//    private val locationPrefs = mock<LocationPrefs>()
//    private val telephonyManager = mock<android.telephony.TelephonyManager>()
//    private val chk = mock<CheckConnection>()
//
//    @Before
//    fun setup() {
//
//        // ✅ Default mock values
//        whenever(prefManager.getUsername()).thenReturn("saved_user")
//        whenever(prefManager.getPassword()).thenReturn("saved_pass")
//        whenever(appPrefs.getproviderInfo()).thenReturn("true")
//        whenever(chk.isConnected()).thenReturn(true)
//
//        // ✅ Launch activity using Robolectric
//        activity = Robolectric.buildActivity(LoginActivityNew::class.java)
//            .setup()
//            .get()
//
//        // ✅ IMPORTANT: manually set injected fields after activity launch
//        activity.prefManager = prefManager
//        activity.appPrefs = appPrefs
//        activity.locationPrefs = locationPrefs
//        activity.telephonyManager = telephonyManager
//        activity.chk = chk
//    }
//
//    // ✅ 1. Activity launch test
//    @Test
//    fun `activity should launch successfully`() {
//        assertNotNull(activity)
//    }
//
//    // ✅ 2. Check saved username/password loaded
//    @Test
//    fun `should load saved username and password on init`() {
//        val usernameEt = activity.findViewById<EditText>(R.id.username)
//        val passwordEt = activity.findViewById<EditText>(R.id.passwordd)
//
//        assertEquals("saved_user", usernameEt.text.toString())
//        assertEquals("saved_pass", passwordEt.text.toString())
//    }
//
//    // ✅ 3. Empty username validation
//    @Test
//    fun `login click with empty username should show username error`() {
//        val usernameEt = activity.findViewById<EditText>(R.id.username)
//        val passwordEt = activity.findViewById<EditText>(R.id.passwordd)
//
//        usernameEt.setText("")
//        passwordEt.setText("123")
//
//        activity.findViewById<TextView>(R.id.login).performClick()
//
//        val userLayout = activity.findViewById<TextInputLayout>(R.id.user)
//        assertEquals("Please Enter Username", userLayout.error)
//    }
//
//    // ✅ 4. Empty password validation
//    @Test
//    fun `login click with empty password should show password error`() {
//        val usernameEt = activity.findViewById<EditText>(R.id.username)
//        val passwordEt = activity.findViewById<EditText>(R.id.passwordd)
//
//        usernameEt.setText("admin")
//        passwordEt.setText("")
//
//        activity.findViewById<TextView>(R.id.login).performClick()
//
//        val passLayout = activity.findViewById<TextInputLayout>(R.id.pass)
//        assertEquals("Please Enter Password", passLayout.error)
//    }
//
//    // ✅ 5. Valid input should save credentials
//    @Test
//    fun `valid login should save credentials`() {
//        val usernameEt = activity.findViewById<EditText>(R.id.username)
//        val passwordEt = activity.findViewById<EditText>(R.id.passwordd)
//
//        usernameEt.setText("admin")
//        passwordEt.setText("1234")
//
//        activity.findViewById<TextView>(R.id.login).performClick()
//
//        verify(prefManager).saveLoginCredentials("admin", "1234")
//    }
//
//    // ✅ 6. No internet should show connection dialog
//    @Test
//    fun `login click without internet should show connection error dialog`() {
//        whenever(chk.isConnected()).thenReturn(false)
//
//        val usernameEt = activity.findViewById<EditText>(R.id.username)
//        val passwordEt = activity.findViewById<EditText>(R.id.passwordd)
//
//        usernameEt.setText("admin")
//        passwordEt.setText("1234")
//
//        activity.findViewById<TextView>(R.id.login).performClick()
//
//        verify(chk).showConnectionErrorDialog()
//    }
//
//    // ✅ 7. Phone click should fire call intent
//    @Test
//    fun `phone click should open call intent`() {
//        activity.findViewById<TextView>(R.id.phnnum).performClick()
//
//        val intent = shadowOf(activity).nextStartedActivity
//        assertEquals(Intent.ACTION_CALL, intent.action)
//        assertEquals(Uri.parse("tel:01725044700"), intent.data)
//    }
//
//    // ✅ 8. Email click should open chooser intent
//    @Test
//    fun `email click should open email intent chooser`() {
//        activity.findViewById<TextView>(R.id.emadd).performClick()
//
//        val intent = shadowOf(activity).nextStartedActivity
//        assertEquals(Intent.ACTION_CHOOSER, intent.action)
//    }
//
//    // ✅ 9. App version should be visible
//    @Test
//    fun `should display app version text`() {
//        val tv = activity.findViewById<TextView>(R.id.appVersion)
//        assertTrue(tv.text.toString().contains("Version"))
//    }
//
//    // ✅ 10. Back press should finish activity
//    @Test
//    fun `back press should finish activity`() {
//        activity.onBackPressedDispatcher.onBackPressed()
//        assertTrue(activity.isFinishing)
//    }
//
//}