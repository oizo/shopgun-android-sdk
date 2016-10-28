package com.shopgun.android.sdk.pagedpublicationkit;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

public interface PagedPublicationPage {

    public enum Size {
        VIEW, ZOOM
    }

    @NonNull String getUrl(Size q);
    int getPageIndex();
    @NonNull Bitmap.Config getBitmapConfig(Size q);
    boolean allowResize(Size q);

}
