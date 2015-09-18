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

package com.shopgun.android.sdk.palette;

public enum Shade {

    Shade50(50),
    Shade100(100),
    Shade200(200),
    Shade300(300),
    Shade400(400),
    Shade500(500),
    Shade600(600),
    Shade700(700),
    Shade800(800),
    Shade900(900);

    int mValue = 500;
    Shade(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }

}