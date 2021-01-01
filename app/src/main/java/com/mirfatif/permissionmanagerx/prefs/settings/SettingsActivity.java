package com.mirfatif.permissionmanagerx.prefs.settings;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceFragmentCompat.OnPreferenceStartFragmentCallback;
import com.mirfatif.permissionmanagerx.R;
import com.mirfatif.permissionmanagerx.Utils;
import com.mirfatif.permissionmanagerx.main.MainActivityFlavor;
import com.mirfatif.permissionmanagerx.prefs.MySettings;
import com.mirfatif.privtasks.Util;

public class SettingsActivity extends AppCompatActivity
    implements OnPreferenceStartFragmentCallback {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    MainActivityFlavor.onCreateStart(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fragment_container);

    // Check null to avoid:
    // "IllegalStateException: Target fragment must implement TargetFragment interface"
    // on rotation when a DialogPreference is visible.
    // https://issuetracker.google.com/issues/137173772
    if (savedInstanceState == null) {
      getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.fragment_container, new SettingsFragment())
          .commit();
    }
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (MySettings.getInstance().isDebug()) {
      Util.debugLog("SettingsActivity", "onOptionsItemSelected(): " + item.getTitle());
    }

    // do not recreate parent (Main) activity
    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
    final Fragment fragment =
        getSupportFragmentManager()
            .getFragmentFactory()
            .instantiate(getClassLoader(), pref.getFragment());
    fragment.setArguments(pref.getExtras());
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .addToBackStack(null)
        .commit();
    setActionBarTitle(Utils.capitalizeWords(pref.getTitle().toString()));
    return true;
  }

  void setActionBarTitle(String title) {
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle(title);
    }
  }
}