package com.focus617.tankwar.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.focus617.tankwar.R
import timber.log.Timber


class SettingsFragment : PreferenceFragmentCompat() {

    private var themePreference: SwitchPreferenceCompat? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)

        setThemePreference()
    }

    private fun setThemePreference() {
        // 根据Key找到控件
        themePreference = findPreference(getString(R.string.theme_key))
        themePreference?.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { preference, newValue ->
                Timber.i("onPreferenceClick --->${preference.title}:$newValue")

                if (newValue as Boolean) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                true
            }
    }

}





