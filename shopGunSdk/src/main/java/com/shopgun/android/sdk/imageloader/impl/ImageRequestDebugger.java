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

package com.shopgun.android.sdk.imageloader.impl;

import com.shopgun.android.sdk.Constants;
import com.shopgun.android.sdk.imageloader.ImageDebugger;
import com.shopgun.android.sdk.imageloader.ImageRequest;
import com.shopgun.android.sdk.log.SgnLog;

public class ImageRequestDebugger implements ImageDebugger {

    public static final String TAG = Constants.getTag(ImageRequestDebugger.class);

    public void debug(ImageRequest ir) {
        SgnLog.d(TAG, ir.getLog().getString(ir.getUrl()));
    }

}
