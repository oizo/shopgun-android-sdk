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

import android.graphics.Bitmap;

import com.shopgun.android.sdk.Constants;
import com.shopgun.android.sdk.imageloader.BitmapProcessor;
import com.shopgun.android.sdk.log.SgnLog;
import com.shopgun.android.sdk.model.Catalog;
import com.shopgun.android.sdk.model.Dimension;

public class CatalogThumbBitmapProcessor implements BitmapProcessor {

    public static final String TAG = Constants.getTag(CatalogThumbBitmapProcessor.class);
    Dimension mDimension;
    boolean mPrint = false;

    public CatalogThumbBitmapProcessor(Dimension d) {
        mDimension = d;
    }

    public CatalogThumbBitmapProcessor(Catalog c) {
        this(c.getDimension());
    }

    public void setPrint(boolean print) {
        mPrint = print;
    }

    public Bitmap process(Bitmap b) {

        if (mDimension == null) {
            throw new IllegalArgumentException("Dimension cannot be null");
        }

        if (mDimension.getWidth() < 0 && mDimension.getHeight() < 0) {
            throw new IllegalArgumentException("Dimension.width and Dimension.height cannot be < 0");
        }

        if (mDimension == null || equal(mDimension.getHeight(), mDimension.getWidth())) {
            print(b, b, mDimension);
            return b;
        }

        int x = 0;
        int y = 0;
        int width = b.getWidth();
        int height = b.getHeight();
        int diff = Math.abs(height - width);
        if (mDimension.getHeight() > mDimension.getWidth()) {
            int absWidth = width + diff;
            width = (int) (absWidth / mDimension.getHeight());
            x = (b.getWidth() / 2) - (width / 2);
        } else {
            int absHeight = height - diff;
            height = (int) (absHeight * mDimension.getHeight());
            y = (b.getHeight() / 2) - (absHeight / 2);
        }

        Bitmap tmp = null;
        try {
            tmp = Bitmap.createBitmap(b, x, y, width, height);
        } catch (Exception e) {
            tmp = b;
        }
        print(b, tmp, mDimension);
        return tmp;
    }

    private boolean equal(double w, double h) {
        return Math.abs(w - h) < 0.01;
    }

    private void print(Bitmap src, Bitmap dst, Dimension d) {
        if (mPrint) {
            String format = "src[w:%s, h:%s], dst[w:%s, h:%s], dimen[w:%s, h:%s]";
            String text = String.format(format, src.getWidth(), src.getHeight(), dst.getWidth(), dst.getHeight(), d.getWidth(), d.getHeight());
            SgnLog.d(TAG, text);
        }
    }

}
