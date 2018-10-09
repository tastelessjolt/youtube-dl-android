package me.harshithgoka.youtubedl.Fragments;


import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codekidlabs.storagechooser.StorageChooser;
import com.codekidlabs.storagechooser.utils.DiskUtil;

import java.util.Map;

import androidx.core.content.res.ResourcesCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import me.harshithgoka.youtubedl.Activities.MainActivity;
import me.harshithgoka.youtubedl.Activities.SettingsActivity;
import me.harshithgoka.youtubedl.R;
import me.harshithgoka.youtubedl.Utils.StringPreference;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener {


    private SharedPreferences sharedPreferences;
    private StringPreference downloadFolderPref;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onResume() {
        super.onResume();

        sharedPreferences = getPreferenceManager().getSharedPreferences();

        // we want to watch the preference values' changes
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        Preference.OnPreferenceClickListener notImplemented = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(), "Auto download coming soon...", Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        downloadFolderPref = (StringPreference) findPreference("download_folder");
        findPreference("default_format").setOnPreferenceClickListener(notImplemented);
        findPreference("autoDownload").setOnPreferenceClickListener(notImplemented);

        // iterate through the preference entries and update their summary if they are an instance of EditTextPreference
        updateSummary(downloadFolderPref);

        downloadFolderPref.setOnPreferenceClickListener(this);
    }

    private void updateSummary(StringPreference preferenceEntry) {
        preferenceEntry.setSummary(preferenceEntry.getText());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Map<String, ?> preferencesMap = sharedPreferences.getAll();

        // get the preference that has been changed
        Object changedPreference = preferencesMap.get(key);
        // and if it's an instance of EditTextPreference class, update its summary
        if (preferencesMap.get(key) instanceof EditTextPreference) {
            if (changedPreference != null) {
                updateSummary((StringPreference) changedPreference);
            }
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals(downloadFolderPref.getKey())) {
            StorageChooser.Theme theme = new StorageChooser.Theme(getContext());

            int[] myScheme = theme.getDefaultScheme();
            myScheme[StorageChooser.Theme.OVERVIEW_HEADER_INDEX] = ResourcesCompat.getColor(getResources(), R.color.colorAccent, getContext().getTheme());
            myScheme[StorageChooser.Theme.SEC_FOLDER_TINT_INDEX] = ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getContext().getTheme());
            myScheme[StorageChooser.Theme.SEC_ADDRESS_BAR_BG] = ResourcesCompat.getColor(getResources(), R.color.colorAccent, getContext().getTheme());
            theme.setScheme(myScheme);

            StorageChooser chooser = new StorageChooser.Builder()
                    .allowCustomPath(true)
                    .setType(StorageChooser.DIRECTORY_CHOOSER)
                    .withActivity(getActivity())
                    .withFragmentManager(getActivity().getFragmentManager())
                    .withMemoryBar(true)
                    .setTheme(theme)
                    .build();

            chooser.show();

            // get path that the user has chosen
            chooser.setOnSelectListener(new StorageChooser.OnSelectListener() {
                @Override
                public void onSelect(String path) {
                    Log.d("SELECTED_PATH", path);
                    downloadFolderPref.setText(path);
                    downloadFolderPref.setSummary(path);
                }
            });
            return true;
        }
        return false;
    }
}
