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

package com.shopgun.android.sdk.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.shopgun.android.sdk.log.SgnLog;
import com.shopgun.android.sdk.model.interfaces.IErn;
import com.shopgun.android.sdk.model.interfaces.IJson;
import com.shopgun.android.sdk.utils.Constants;
import com.shopgun.android.sdk.utils.SgnJson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Country implements IErn<Country>, IJson<JSONObject>, Parcelable {

    public static final String TAG = Constants.getTag(Country.class);
    public static Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>() {
        public Country createFromParcel(Parcel source) {
            return new Country(source);
        }

        public Country[] newArray(int size) {
            return new Country[size];
        }
    };
    private String mErn;
    private String mUnsubscribeUrl;

    public Country() {

    }

    private Country(Parcel in) {
        this.mErn = in.readString();
        this.mUnsubscribeUrl = in.readString();
    }

    /**
     * Convert a {@link JSONArray} into a {@link List};.
     * @param array A {@link JSONArray}  with a valid API v2 structure for a country
     * @return A {@link List} of POJO
     */
    public static List<Country> fromJSON(JSONArray array) {
        List<Country> list = new ArrayList<Country>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.optJSONObject(i);
            list.add(Country.fromJSON(o));
        }
        return list;
    }

    /**
     * A factory method for converting {@link JSONObject} into a POJO.
     * @param object A {@link JSONObject} with a valid API v2 structure for a country
     * @return A {@link Country}, or {@code null} if {@code object} is {@code null}
     */
    public static Country fromJSON(JSONObject object) {
        if (object == null) {
            return null;
        }
        SgnJson o = new SgnJson(object);
        Country country = new Country()
                .setId(o.getId())
                .setErn(o.getErn())
                .setUnsubscribePrintUrl(o.getUnsubscribePrintUrl());

        o.getStats().ignoreRejectedKeys(SgnJson.ERN).log(TAG);

        return country;
    }

    public JSONObject toJSON() {
        return new SgnJson()
                .setId(getId())
                .setErn(getErn())
                .setUnsubscribePrintUrl(getUnsubscribePrintUrl())
                .toJSON();
    }

    /**
     * Get the country code of this country. The country codes are two-letter uppercase ISO country codes (such as "US")
     * as defined by ISO 3166-1 (alfa-2), see <a href="http://da.wikipedia.org/wiki/ISO_3166-1">wikipedia</a> for more info.
     * @return A String if id exists, or null
     */
    public String getId() {
        if (mErn == null) {
            return null;
        }
        String[] parts = mErn.split(":");
        return parts[parts.length - 1];
    }

    /**
     * Set the country code of this country. The country codes must two-letter uppercase ISO country codes (such as "US")
     * as defined by ISO 3166-1 (alfa-2), see <a href="http://da.wikipedia.org/wiki/ISO_3166-1">wikipedia</a> for more info.
     * @return A String
     */
    @SuppressLint("DefaultLocale")
    public Country setId(String id) {
        if (id == null) {
            setErn(null);
        } else if (id.length() == 2) {
            setErn(String.format("ern:%s:%s", getErnType(), id.toUpperCase()));
        } else {
            SgnLog.i(TAG, "The country code: " + id + " isn't allowed, see documentation for more details");
        }
        return this;
    }

    public String getErn() {
        return mErn;
    }

    public Country setErn(String ern) {
        if (ern == null || (ern.startsWith("ern:") && ern.split(":").length == 3 && ern.contains(getErnType()))) {
            mErn = ern;
        }
        return this;
    }

    public String getErnType() {
        return IErn.TYPE_COUNTRY;
    }

    /**
     * This method returns an URL to a website in which is it possible for the user to 'unsubscribe' them or their
     * household from receiving the physical catalogs.
     * @return An url if one exists, else null
     */
    public String getUnsubscribePrintUrl() {
        return mUnsubscribeUrl;
    }

    /**
     * Set the URL to a website in which is it possible for the user to 'unsubscribe' them or their
     * household from receiving the physical catalogs.
     * @param url A string
     * @return This object
     */
    public Country setUnsubscribePrintUrl(String url) {
        mUnsubscribeUrl = url;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mErn == null) ? 0 : mErn.hashCode());
        result = prime * result
                + ((mUnsubscribeUrl == null) ? 0 : mUnsubscribeUrl.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Country other = (Country) obj;
        if (mErn == null) {
            if (other.mErn != null)
                return false;
        } else if (!mErn.equals(other.mErn))
            return false;
        if (mUnsubscribeUrl == null) {
            if (other.mUnsubscribeUrl != null)
                return false;
        } else if (!mUnsubscribeUrl.equals(other.mUnsubscribeUrl))
            return false;
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mErn);
        dest.writeString(this.mUnsubscribeUrl);
    }

}
