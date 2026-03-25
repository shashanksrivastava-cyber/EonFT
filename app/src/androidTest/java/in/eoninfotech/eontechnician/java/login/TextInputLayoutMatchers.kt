package `in`.eoninfotech.eontechnician.java.login

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.Description
import org.hamcrest.Matcher

object TextInputLayoutMatchers {

    fun hasTextInputLayoutError(expectedError: String): Matcher<View> {
        return object : BoundedMatcher<View, TextInputLayout>(TextInputLayout::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has error: $expectedError")
            }
            override fun matchesSafely(item: TextInputLayout): Boolean {
                return item.error?.toString() == expectedError
            }
        }
    }
}