package `in`.eoninfotech.eontechnician.di

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.eoninfotech.eontechnician.responses.LoginDetail
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SharedPreferenceManager @Inject constructor(
    @ApplicationContext context: Context
) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("login_user_pass", Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = prefs.edit()

    // =========================
    // ✅ Login Credentials
    // =========================
    fun saveLoginCredentials(username: String, password: String) {
        editor.putString("usname", username)
        editor.putString("pass", password)
        editor.apply()
    }

    fun getUsername(): String = prefs.getString("usname", "") ?: ""
    fun getPassword(): String = prefs.getString("pass", "") ?: ""

    // =========================
    // ✅ Login Details
    // =========================
    fun saveLoginDetails(detail: LoginDetail, imei: String) {
        with(editor) {
            putString("s_user_id", detail.usr_id)
            putString("s_uuser", detail.usrname)
            putString("location", detail.location)
            putString("usrtype", detail.usrtype)
            putString("zone", detail.zone)
            putString("version", detail.verno)
            putString("contact", detail.contact)
            putString("dis_user", detail.displayname)
            putString("image", detail.image)
            putString("imei1", imei)
            putString("track_status", detail.track_status)
            putString("track_interval", detail.track_interval)
            putString("bill_amt_limit", detail.bill_amt_limit)
            apply()
        }
    }

    // =========================
    // ✅ Other Getters/Setters
    // =========================
    fun getVersionName(): String = prefs.getString("version", "") ?: ""
    fun getImage(): String = prefs.getString("image", "") ?: ""
    fun getAlert(): String = prefs.getString("alert", "") ?: ""
    fun getDistrictId(): String = prefs.getString("s_distt", "") ?: ""
    fun getDisplayUsername(): String = prefs.getString("dis_user", "") ?: ""
    fun getZone(): String = prefs.getString("zone", "") ?: ""

    fun getUserId(): String  = prefs.getString("s_user_id","")?:""

    fun setUsername(value: String) = editor.putString("s_uuser", value).apply()
    fun setVersionName(value: String) = editor.putString("version", value).apply()
    fun setImage(value: String) = editor.putString("image", value).apply()
    fun setAlert(value: String) = editor.putString("alert", value).apply()

    // =========================
    // ✅ Utility
    // =========================
    fun clearAll() = editor.clear().apply()
}