package me.harshithgoka.youtubedl.Fragments


import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

//import com.codekidlabs.storagechooser.StorageChooser
//import com.codekidlabs.storagechooser.utils.DiskUtil

import androidx.core.content.res.ResourcesCompat
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import me.harshithgoka.youtubedl.Activities.MainActivity
import me.harshithgoka.youtubedl.Activities.SettingsActivity
import me.harshithgoka.youtubedl.R
import me.harshithgoka.youtubedl.Utils.StringPreference

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {


    private var sharedPreferences: SharedPreferences? = null
    private var downloadFolderPref: StringPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()

        sharedPreferences = preferenceManager.sharedPreferences

        // we want to watch the preference values' changes
        sharedPreferences!!.registerOnSharedPreferenceChangeListener(this)

        val notImplemented = Preference.OnPreferenceClickListener {
            Toast.makeText(context, "Auto download coming soon...", Toast.LENGTH_SHORT).show()
            true
        }

        downloadFolderPref = findPreference("download_folder") as StringPreference
        findPreference("default_format").onPreferenceClickListener = notImplemented
        findPreference("autoDownload").onPreferenceClickListener = notImplemented

        // iterate through the preference entries and update their summary if they are an instance of EditTextPreference
        updateSummary(downloadFolderPref!!)

        downloadFolderPref!!.onPreferenceClickListener = this
    }

    private fun updateSummary(preferenceEntry: StringPreference) {
        preferenceEntry.summary = preferenceEntry.text
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val preferencesMap = sharedPreferences.all

        // get the preference that has been changed
        val changedPreference = preferencesMap[key]
        // and if it's an instance of EditTextPreference class, update its summary
        if (preferencesMap[key] is EditTextPreference) {
            if (changedPreference != null) {
                updateSummary((changedPreference as StringPreference?)!!)
            }
        }
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        if (preference.key == downloadFolderPref!!.key) {
//            val theme = StorageChooser.Theme(context)
//
//            val myScheme = theme.defaultScheme
//            myScheme[StorageChooser.Theme.OVERVIEW_HEADER_INDEX] = ResourcesCompat.getColor(resources, R.color.colorAccent, context!!.theme)
//            myScheme[StorageChooser.Theme.SEC_FOLDER_TINT_INDEX] = ResourcesCompat.getColor(resources, R.color.colorPrimary, context!!.theme)
//            myScheme[StorageChooser.Theme.SEC_ADDRESS_BAR_BG] = ResourcesCompat.getColor(resources, R.color.colorAccent, context!!.theme)
//            theme.scheme = myScheme
//
//            val chooser = StorageChooser.Builder()
//                    .allowCustomPath(true)
//                    .setType(StorageChooser.DIRECTORY_CHOOSER)
//                    .withActivity(activity)
//                    .withFragmentManager(activity!!.fragmentManager)
//                    .withMemoryBar(true)
//                    .setTheme(theme)
//                    .build()
//
//            chooser.show()
//
//            // get path that the user has chosen
//            chooser.setOnSelectListener { path ->
//                Log.d("SELECTED_PATH", path)
//                downloadFolderPref!!.text = path
//                downloadFolderPref!!.summary = path
//            }
//            return true
        }
        return false
    }
}// Required empty public constructor
