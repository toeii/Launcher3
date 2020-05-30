/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.launcher3;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.util.Log;

import com.android.launcher3.util.LooperExecutor;

/**
 * Settings activity for Launcher. Currently implements the following setting: Allow rotation
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new LauncherSettingsFragment())
                .commit();
    }

    /**
     * This fragment shows the launcher preferences.
     */
    public static class LauncherSettingsFragment extends PreferenceFragment
            implements OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.launcher_preferences);

            //读取保存的值，初始化 SwitchPreference 的初始状态，是否选中
//            int isFull = Settings.System.getInt(getActivity().getContentResolver(),
//                    "sys.launcher3.is_full_app", 0);
//            Log.d("Launcher3", "sys.launcher3.is_full_app="+isFull);
//            SwitchPreference fullSwitch = (SwitchPreference) findPreference("pref_is_full_app");
//            fullSwitch.setChecked(isFull==1);

            SwitchPreference pref = (SwitchPreference) findPreference(
                    Utilities.ALLOW_ROTATION_PREFERENCE_KEY);
            pref.setPersistent(false);

            Bundle extras = new Bundle();
            extras.putBoolean(LauncherSettings.Settings.EXTRA_DEFAULT_VALUE, false);
            Bundle value = getActivity().getContentResolver().call(
                    LauncherSettings.Settings.CONTENT_URI,
                    LauncherSettings.Settings.METHOD_GET_BOOLEAN,
                    Utilities.ALLOW_ROTATION_PREFERENCE_KEY, extras);
            pref.setChecked(value.getBoolean(LauncherSettings.Settings.EXTRA_VALUE));

            pref.setOnPreferenceChangeListener(this);
        }

//        @Override
//        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
//            boolean result = true;
//            final String key = preference.getKey();
//
//            if ("pref_is_full_app".equals(key)) {
//                boolean checked = ((SwitchPreference) preference).isChecked();
//
//                Settings.System.putInt(getActivity().getContentResolver(), "sys.launcher3.is_full_app",
//                        checked ? 1 : 0);
//
//                Log.e("Launcher3", "SwitchPreference checked="+checked);
//
//                // Value has changed
//                ProgressDialog.show(getActivity(),
//                        null /* title */,
//                        "应用覆盖进度",
//                        true /* indeterminate */,
//                        false /* cancelable */);
//
//                new LooperExecutor(LauncherModel.getWorkerLooper()).execute(
//                        new OverrideApplyHandler(getActivity()));
//
//            }
//            return result;
//        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            Bundle extras = new Bundle();
            extras.putBoolean(LauncherSettings.Settings.EXTRA_VALUE, (Boolean) newValue);
            getActivity().getContentResolver().call(
                    LauncherSettings.Settings.CONTENT_URI,
                    LauncherSettings.Settings.METHOD_SET_BOOLEAN,
                    preference.getKey(), extras);
            return true;
        }
    }

    //add for change is_full_app value
    private static class OverrideApplyHandler implements Runnable {

        private final Context mContext;

        private OverrideApplyHandler(Context context) {
            mContext = context;
        }

        @SuppressLint("WrongConstant")
        @Override
        public void run() {
            // Clear the icon cache.
            LauncherAppState.getInstance().getIconCache().removeAll();

            // Wait for it
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                Log.e("Launcher3", "Error waiting", e);
            }

            // Schedule an alarm before we kill ourself.
            Intent homeIntent = new Intent(Intent.ACTION_MAIN)
                    .addCategory(Intent.CATEGORY_HOME)
                    .setPackage(mContext.getPackageName())
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pi = PendingIntent.getActivity(mContext, 42,
                    homeIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_ONE_SHOT);
            mContext.getSystemService(AlarmManager.class).setExact(
                    AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 50, pi);

            //clear data will kill process
            Intent intent = new Intent("com.android.action.CLEAR_APP_DATA");
            intent.putExtra("pkgName", "com.android.launcher3");
            intent.addFlags(0x01000000);
            mContext.sendBroadcast(intent);
            Log.i("Launcher3", "Clearing user data com.android.launcher3");

            // Kill process
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }


}
