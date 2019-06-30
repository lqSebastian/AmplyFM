package com.cibertec.amplyfm.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.cibertec.amplyfm.R;
import com.cibertec.amplyfm.utils.DialogFactory;
import com.preference.PowerPreference;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MyPreferenceFragment())
                .commit();
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            Preference version = findPreference("version");
            try {
                String versionName = getActivity().getPackageManager()
                        .getPackageInfo(getActivity().getPackageName(), 0).versionName;
                version.setSummary(versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            Preference buttonfeedback = findPreference(getString(R.string.send_feedback));
            buttonfeedback.setOnPreferenceClickListener(preference -> {
                Intent emailIntent =
                        new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "i201711625@cibertec.edu.pe", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.app_name_amplyfm) + " Feedback");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Escribe tus comentarios aquí...");
                startActivity(Intent.createChooser(emailIntent, "Enviar correo..."));

                return true;
            });

            Preference buttonSetAuto = findPreference("prefs_auto_play");
            buttonSetAuto.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean checked = Boolean.valueOf(newValue.toString());
                PowerPreference.getDefaultFile().put("auto_play", checked);
                return true;
            });

            Preference buttonSetMaxResults = findPreference("results_number");
            buttonSetMaxResults.setOnPreferenceChangeListener((preference, newValue) -> {
                Integer number = Integer.valueOf(newValue.toString());
                if (number < 1 || number > 50) {
                    DialogFactory.warning_toast(getActivity(), "Escriba un número entre 1 y 50").show();
                    return false;
                }


                PowerPreference.getDefaultFile().put("results_number", String.valueOf(number));
                return true;
            });

        }
    }


    @Override
    public void onBackPressed() {
        // startActivity(new Intent(this, MainActivity.class));

        finish();
    }
}