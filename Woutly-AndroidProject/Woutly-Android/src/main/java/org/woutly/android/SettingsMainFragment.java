/**
 *  Copyright 2013 Woutly
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.woutly.android;

import android.annotation.TargetApi;
import android.media.audiofx.BassBoost;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;

import roboguice.fragment.RoboFragment;

/**
 * Main settings Fragment
 *
 * @author Alexandro Blanco <ti3r.bubblenet@gmail.com>
 */
//TODO Can't use PreferenceFragment since its not part of compat library. Using activity instead
//come back to work on this latter
public class SettingsMainFragment extends PreferenceActivity {

    /**
     * Constant to launch this activity using intents. As defined in AndroidManifest.xml for this class
     */
    public static final String INTENT_NAME = "org.woutly.android.SETTINGS";

//    public static SettingsMainFragment newInstance(){
//        SettingsMainFragment fragment = new SettingsMainFragment();
//        return fragment;
//    }

    public SettingsMainFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
