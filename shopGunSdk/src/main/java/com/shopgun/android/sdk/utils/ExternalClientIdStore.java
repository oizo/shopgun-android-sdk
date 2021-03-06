/*******************************************************************************
 * Copyright 2015 ShopGun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.shopgun.android.sdk.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import com.shopgun.android.sdk.ShopGun;
import com.shopgun.android.sdk.log.SgnLog;
import com.shopgun.android.sdk.model.Session;

import java.io.File;
import java.io.RandomAccessFile;

import static android.os.Environment.MEDIA_MOUNTED;

public class ExternalClientIdStore {

    public static final String TAG = Constants.getTag(ExternalClientIdStore.class);

    private static final String CID_RANDOMJUNK = "randomjunkid";

    public static void updateCid(Session s, ShopGun sgn) {
        String extCid = getCid(sgn);
        if (CID_RANDOMJUNK.equals(s.getClientId()) || CID_RANDOMJUNK.equals(extCid)) {
            // Previously used this hardcoded cid in beta, recover it
            s.setClientId(SgnUtils.createUUID());
        } else if (s.getClientId() == null) {
            // No ClientID is set, try to get from disk
            s.setClientId(extCid);
        }
        sgn.getSettings().setClientId(s.getClientId());
    }

    public static String getCid(ShopGun sgn) {

        // First try SharedPrefs
        String cid = sgn.getSettings().getClientId();
        if (cid != null) {
            return cid;
        }

        // Then try external storage
        File cidFile = getCidFile(sgn.getContext());
        if (cidFile == null) {
            return null;
        }

        RandomAccessFile f = null;
        try {
            f = new RandomAccessFile(cidFile, "r");
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                return null;
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return new String(data);
        } catch (Exception e) {
            // Ignore
        } finally {
            try {
                f.close();
            } catch (Exception e) {
                // Ignore
            }

            // Cleanup the cid file, we won't need it any more
            deleteCidFile(sgn.getContext());
        }

        return null;
    }

    public static void clear(ShopGun sgn) {
        deleteCidFile(sgn.getContext());
        sgn.getSettings().setClientId(null);
    }

    private static boolean deleteCidFile(Context c) {
        File f = getCidFile(c);
        return f != null && f.exists() && f.delete();
    }

    private static File getCidFile(Context context) {

        if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

            try {
                File cacheDir = new File(Environment.getExternalStorageDirectory(), "cache");
                if (!cacheDir.exists() && !cacheDir.mkdirs()) {
                    SgnLog.w(TAG, "External directory couldn't be created");
                    return null;
                }
                String fileName = context.getPackageName() + ".txt";
                return new File(cacheDir, fileName);
            } catch (Exception e) {
                // If we are not allowed to access external storage
            }
        }

        return null;
    }

}
