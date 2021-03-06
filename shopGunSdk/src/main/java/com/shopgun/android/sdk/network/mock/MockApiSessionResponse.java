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

package com.shopgun.android.sdk.network.mock;

import android.content.Context;

import com.shopgun.android.sdk.log.SgnLog;
import com.shopgun.android.sdk.network.NetworkResponse;
import com.shopgun.android.sdk.network.Request;
import com.shopgun.android.sdk.utils.SgnUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class MockApiSessionResponse extends MockApiNetworkResponse {

    public MockApiSessionResponse(Context context, Request<?> request) {
        super(context, request);

    }

    @Override
    public NetworkResponse getResponse() {

        JSONObject session = getAssetJSONObject(FILE_SESSIONS);
        // Add some time to the session to make the sessionmanager happy
        long exp = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24);
        try {
            session.put("expires", SgnUtils.dateToString(new Date(exp)));
        } catch (JSONException e) {
            SgnLog.e(TAG, e.getMessage(), e);
        }
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("X-Token", "mock-token");
        headers.put("X-Signature", "mock-signature");
        return new NetworkResponse(200, session.toString().getBytes(), headers);

    }
}
