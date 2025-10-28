package `in`.eoninfotech.eontechnician.di

import android.content.Context
import android.content.SharedPreferences
import android.telephony.TelephonyManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import `in`.eoninfotech.eontechnician.AppPreferences
import `in`.eoninfotech.eontechnician.helper.CheckConnection
import `in`.eoninfotech.eontechnician.storage.LocationPrefs
import javax.inject.Singleton

@Module  // ✅ REQUIRED
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        val prefs = context.getSharedPreferences("login_user_pass", Context.MODE_PRIVATE)

        fun getUsername() = prefs.getString("s_uuser", "") ?: ""
        fun getVersionName() = prefs.getString("version", "") ?: ""
        fun getImage() = prefs.getString("image", "") ?: ""
        fun getAlert() = prefs.getString("alert", "") ?: ""
        fun getDistrictId() = prefs.getString("s_distt", "") ?: ""
        fun getDisplayUsername() = prefs.getString("dis_user", "") ?: ""
        return context.getSharedPreferences("login_user_pass", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAppPreferences(@ApplicationContext context: Context): AppPreferences {

        return AppPreferences(context)
    }

    @Provides
    @Singleton
    fun provideLocationPrefs(@ApplicationContext context: Context): LocationPrefs {
        return LocationPrefs(context)
    }

    @Provides
    @Singleton
    fun provideTelephonyManager(@ApplicationContext context: Context): TelephonyManager {
        return context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    @Provides
    @Singleton
    fun provideCheckConnection(@ApplicationContext context: Context): CheckConnection {
        return CheckConnection(context)
    }

    @Provides
    @Singleton
    fun provideSharedPreferenceManager(
        @ApplicationContext context: Context
    ): SharedPreferenceManager {
        return SharedPreferenceManager(context)
    }
}