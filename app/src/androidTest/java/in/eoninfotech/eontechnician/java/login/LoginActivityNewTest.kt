//import androidx.test.core.app.ActivityScenario
//import androidx.test.ext.junit.runners.AndroidJUnit4
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import `in`.eoninfotech.eontechnician.activity.LoginActivityNew
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertNull
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//
//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//class LoginActivityNewTest {
//
//    @get:Rule
//    var hiltRule = HiltAndroidRule(this)
//
//    @Before
//    fun init() {
//        hiltRule.inject()
//    }
//
//    @Test
//    fun loginButton_emptyUsername_showsError() {
//
//        ActivityScenario.launch(LoginActivityNew::class.java).use { scenario ->
//
//            scenario.onActivity { activity ->
//                activity.binding.username.setText("")
//                activity.binding.passwordd.setText("1234")
//
//                activity.binding.login.performClick()
//
//                assertEquals(
//                    "Please Enter Username",
//                    activity.binding.user.error
//                )
//            }
//        }
//    }
//
//    @Test
//    fun loginButton_emptyPassword_showsError() {
//
//        ActivityScenario.launch(LoginActivityNew::class.java).use { scenario ->
//
//            scenario.onActivity { activity ->
//                activity.binding.username.setText("user")
//                activity.binding.passwordd.setText("")
//
//                activity.binding.login.performClick()
//
//                assertEquals(
//                    "Please Enter Password",
//                    activity.binding.pass.error
//                )
//            }
//        }
//    }
//
//    @Test
//    fun loginButton_validInput_noErrors() {
//
//        ActivityScenario.launch(LoginActivityNew::class.java).use { scenario ->
//
//            scenario.onActivity { activity ->
//                activity.binding.username.setText("user")
//                activity.binding.passwordd.setText("pass")
//
//                activity.binding.login.performClick()
//
//                assertNull(activity.binding.user.error)
//                assertNull(activity.binding.pass.error)
//            }
//        }
//    }
//}
