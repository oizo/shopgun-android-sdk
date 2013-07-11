package com.eTilbudsavis.etasdk.EtaObjects;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

import com.eTilbudsavis.etasdk.Eta;
import com.eTilbudsavis.etasdk.Tools.Endpoint;
import com.eTilbudsavis.etasdk.Tools.Params;
import com.eTilbudsavis.etasdk.Tools.Sort;

public class Catalog  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String TAG = "Catalog";
	
	/** Sort a list by popularity in ascending order. (smallest to largest) */
	public static final String SORT_POPULARITY = Sort.POPULARITY;

	/** Sort a list by popularity in descending order. (largest to smallest)*/
	public static final String SORT_POPULARITY_DESC = Sort.POPULARITY_DESC;

	/** Sort a list by distance in ascending order. (smallest to largest) */
	public static final String SORT_DISTANCE = Sort.DISTANCE;

	/** Sort a list by distance in descending order. (largest to smallest)*/
	public static final String SORT_DISTANCE_DESC = Sort.DISTANCE_DESC;

	/** Sort a list by name in ascending order. (smallest to largest) */
	public static final String SORT_NAME = Sort.NAME;

	/** Sort a list by name in descending order. (largest to smallest)*/
	public static final String SORT_NAME_DESC = Sort.NAME_DESC;

	/** Sort a list by published in ascending order. (smallest to largest) */
	public static final String SORT_PUBLISHED = Sort.PUBLISHED;

	/** Sort a list by published in descending order. (largest to smallest)*/
	public static final String SORT_PUBLISHED_DESC = Sort.PUBLISHED_DESC;

	/** Sort a list by expired in ascending order. (smallest to largest) */
	public static final String SORT_EXPIRED = Sort.EXPIRED;

	/** Sort a list by expired in descending order. (largest to smallest)*/
	public static final String SORT_EXPIRED_DESC = Sort.EXPIRED_DESC;

	/** Sort a list by created in ascending order. (smallest to largest) */
	public static final String SORT_CREATED = Sort.CREATED;

	/** Sort a list by created in ascending order. (smallest to largest) */
	public static final String SORT_CREATED_DESC = Sort.CREATED_DESC;

	/** Parameter for getting a list of specific catalog id's */
	public static final String FILTER_CATALOG_IDS = Params.FILTER_CATALOG_IDS;

	/** Parameter for posting a list of store id's to publish the catalog in */
	public static final String FILTER_STORE_IDS = Params.FILTER_STORE_IDS;

	/** Parameter for posting a list of area id's to publish the catalog in */
	public static final String FILTER_AREA_IDS = Params.FILTER_AREA_IDS;

	/** Parameter for the location of the PDF to post */
	public static final String PARAM_PDF = Params.PDF;

	/** String identifying the offset parameter for all list calls to the API */
	public static final String PARAM_OFFSET = Params.OFFSET;

	/** String identifying the offset parameter for all list calls to the API */
	public static final String PARAM_LIMIT = Params.LIMIT;

	/** String identifying the query parameter */
	public static final String PARAM_QUERY = Params.QUERY;
	
	/** Endpoint for catalog list resource */
	public static final String ENDPOINT_LIST = Endpoint.CATALOG_LIST;

	/** Endpoint for a single catalog resource */
	public static final String ENDPOINT_ID = Endpoint.CATALOG_ID;

	/** Endpoint for searching catalogs */
	public static final String ENDPOINT_SEARCH = Endpoint.CATALOG_SEARCH;

	@SuppressLint("SimpleDateFormat")
	private SimpleDateFormat sdf = new SimpleDateFormat(Eta.DATE_FORMAT);

	private static final String S_ID = "id";
	private static final String S_ERN = "ern";
	private static final String S_LABEL = "label";
	private static final String S_BACKGROUND = "background";
	private static final String S_RUN_FROM = "run_from";
	private static final String S_RUN_TILL = "run_till";
	private static final String S_PAGE_COUNT = "page_count";
	private static final String S_OFFER_COUNT = "offer_count";
	private static final String S_BRANDING = "branding";
	private static final String S_DEALER_ID = "dealer_id";
	private static final String S_DEALER_URL = "dealer_url";
	private static final String S_STORE_ID = "store_id";
	private static final String S_STORE_URL = "store_url";
	private static final String S_DIMENSIONS = "dimensions";
	private static final String S_IMAGES = "images";
	private static final String S_PAGES = "pages";
	private static final String P_PAGE = "page";
	
	// From JSON blob
	private String mId;
	private String mErn;
	private String mLabel;
	private String mBackground;
	private long mRunFrom = 0L;
	private long mRunTill = 0L;
	private int mPageCount = 0;
	private int mOfferCount = 0;
	private Branding mBranding;
	private String mDealerId;
	private String mDealerUrl;
	private String mStoreId;
	private String mStoreUrl;
	private Dimension mDimension;
	private Images mImages;
	private Pages mPages;
	
	// From seperate queries
	private Dealer mDealer;
	private Store mStore;
	private int mOfferOnPage;

	public Catalog() {
	}

	public Catalog(Catalog c) {
		set(c);
	}

	public static ArrayList<Catalog> fromJSONArray(String catalogs) {
		ArrayList<Catalog> list = new ArrayList<Catalog>();
		try {
			list = fromJSONArray(new JSONArray(catalogs));
		} catch (JSONException e) {
			if (Eta.DEBUG)
				e.printStackTrace();
		}
		return list;
	}
	
	public static ArrayList<Catalog> fromJSONArray(JSONArray catalogs) {
		ArrayList<Catalog> list = new ArrayList<Catalog>();
		try {
			for (int i = 0 ; i < catalogs.length() ; i++ )
				list.add(Catalog.fromJSON((JSONObject)catalogs.get(i)));
			
		} catch (JSONException e) {
			if (Eta.DEBUG)
				e.printStackTrace();
		}
		return list;
	}
	
	public static Catalog fromJSON(String catalog) {
		Catalog c = new Catalog();
		try {
			c = fromJSON(c, new JSONObject(catalog));
		} catch (JSONException e) {
			if (Eta.DEBUG)
				e.printStackTrace();
		}
		return c;
	}

	public static Catalog fromJSON(JSONObject catalog) {
		return fromJSON(new Catalog(), catalog);
	}
	
	private static Catalog fromJSON(Catalog c, JSONObject catalog) {
		if(c == null)
			c = new Catalog();
		
		if (catalog == null)
			return c;
		
		if (catalog.has(S_STORE_ID) && catalog.has(S_OFFER_COUNT)) {
			// if we have a full catalog
			try {
				c.setId(catalog.getString(S_ID));
				c.setErn(catalog.getString(S_ERN));
				c.setLabel(catalog.getString(S_LABEL));
				c.setBackground(catalog.getString(S_BACKGROUND));
				c.setRunFrom(catalog.getString(S_RUN_FROM));
				c.setRunTill(catalog.getString(S_RUN_TILL));
				c.setPageCount(catalog.getInt(S_PAGE_COUNT));
				c.setOfferCount(catalog.getInt(S_OFFER_COUNT));
				c.setBranding(Branding.fromJSON(catalog.getJSONObject(S_BRANDING)));
				c.setDealerId(catalog.getString(S_DEALER_ID));
				c.setDealerUrl(catalog.getString(S_DEALER_URL));
				c.setStoreId(catalog.getString(S_STORE_ID));
				c.setStoreUrl(catalog.getString(S_STORE_URL));
				c.setDimension(Dimension.fromJSON(catalog.getJSONObject(S_DIMENSIONS)));
				c.setImages(Images.fromJSON(catalog.getJSONObject(S_IMAGES)));
				c.setPages(Pages.fromJSON(catalog.getJSONObject(S_PAGES)));
			} catch (JSONException e) {
				if (Eta.DEBUG)
					e.printStackTrace();
			}
		} else if (catalog.has(S_ID) && catalog.has(P_PAGE)) {
			// If it is a partial catalog
			try {
				c.setId(catalog.getString(S_ID));
				c.setOfferOnPage(catalog.getInt(P_PAGE));
			} catch (JSONException e) {
				if (Eta.DEBUG)
					e.printStackTrace();
			}
		}
		return c;
	}
	
	public JSONObject toJSON() {
		return toJSON(this);
	}
	
	public static JSONObject toJSON(Catalog c) {
		JSONObject o = new JSONObject();
		try {
			o.put(S_ID, c.getId());
			o.put(S_ERN, c.getErn());
			o.put(S_LABEL, c.getLabel());
			o.put(S_BACKGROUND, c.getBackground());
			o.put(S_RUN_FROM, c.getRunFromString());
			o.put(S_RUN_TILL, c.getRunFromString());
			o.put(S_PAGE_COUNT, c.getPageCount());
			o.put(S_OFFER_COUNT, c.getOfferCount());
			o.put(S_BRANDING, c.getBranding().toJSON());
			o.put(S_DEALER_ID, c.getDealerId());
			o.put(S_DEALER_URL, c.getDealerUrl());
			o.put(S_STORE_ID, c.getStoreId());
			o.put(S_STORE_URL, c.getStoreUrl());
			o.put(S_DIMENSIONS, c.getDimension().toJSON());
			o.put(S_IMAGES, c.getImages().toJSON());
			o.put(S_PAGES, c.getPages().toJSON());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return o;
	}
	
	public void set(Catalog c) {
		mId = c.getId();
		mErn = c.getErn();
		mLabel = c.getLabel();
		mBackground = c.getBackground();
		mRunFrom = c.getRunFrom();
		mRunTill = c.getRunTill();
		mPageCount = c.getPageCount();
		mOfferCount = c.getOfferCount();
		mBranding = c.getBranding();
		mDealerId = c.getDealerId();
		mDealerUrl = c.getDealerUrl();
		mStoreId = c.getStoreId();
		mStoreUrl = c.getStoreUrl();
		mDimension = c.getDimension();
		mImages = c.getImages();
		mPages = c.getPages();
	}

	public Catalog setId(String id) {
		this.mId = id;
		return this;
	}

	public String getId() {
		return mId;
	}

	public Catalog setErn(String ern) {
		mErn = ern;
		return this;
	}

	public String getErn() {
		return mErn;
	}

	public String getLabel() {
		return mLabel;
	}

	public Catalog setLabel(String label) {
		mLabel = label;
		return this;
	}

	public String getBackground() {
		return mBackground;
	}

	public Catalog setBackground(String background) {
		mBackground = background;
		return this;
	}

	public Catalog setRunFrom(Long time) {
		this.mRunFrom = time;
		return this;
	}

	public Catalog setRunFrom(String time) {
		try {
			mRunFrom = sdf.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return this;
	}

	public Long getRunFrom() {
		return mRunFrom;
	}

	public String getRunFromString() {
		return sdf.format(new Date(mRunFrom));
	}

	public Catalog setRunTill(Long time) {
		this.mRunTill = time;
		return this;
	}

	public Catalog setRunTill(String time) {
		try {
			mRunTill = sdf.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return this;
	}

	public Long getRunTill() {
		return mRunTill;
	}

	public String getRunTillString() {
		return sdf.format(new Date(mRunTill));
	}

	public Catalog setPageCount(Integer mPageCount) {
		this.mPageCount = mPageCount;
		return this;
	}

	public int getPageCount() {
		return mPageCount;
	}

	public Catalog setOfferCount(Integer mOfferCount) {
		this.mOfferCount = mOfferCount;
		return this;
	}

	public int getOfferCount() {
		return mOfferCount;
	}

	public Catalog setBranding(Branding branding) {
		this.mBranding = branding;
		return this;
	}

	public Branding getBranding() {
		return mBranding;
	}

	public Catalog setDealerId(String dealer) {
		this.mDealerId = dealer;
		return this;
	}

	public String getDealerId() {
		return mDealerId;
	}

	public String getDealerUrl() {
		return mDealerUrl;
	}

	public Catalog setDealerUrl(String url) {
		mDealerUrl = url;
		return this;
	}

	public Catalog setStoreId(String storeId) {
		mStoreId = storeId;
		return this;
	}

	public String getStoreId() {
		return mStoreId;
	}

	public String getStoreUrl() {
		return mStoreUrl;
	}

	public Catalog setStoreUrl(String url) {
		mStoreUrl = url;
		return this;
	}

	public Dimension getDimension() {
		return mDimension;
	}

	public Catalog setDimension(Dimension dimension) {
		mDimension = dimension;
		return this;
	}

	public Catalog setImages(Images images) {
		mImages = images;
		return this;
	}

	public Images getImages() {
		return mImages;
	}

	public Pages getPages() {
		return mPages;
	}

	public void setPages(Pages pages) {
		mPages = pages;
	}

	public Catalog setOfferOnPage(Integer offerOnPage) {
		this.mOfferOnPage = offerOnPage;
		return this;
	}

	public int getOfferOnPage() {
		return mOfferOnPage;
	}

	public Catalog setStore(Store store) {
		mStore = store;
		return this;
	}

	public Store getStore() {
		return mStore;
	}

	public Catalog setDealer(Dealer dealer) {
		this.mDealer = dealer;
		return this;
	}

	public Dealer getDealer() {
		return mDealer;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		
		if (!(o instanceof Catalog))
			return false;

		Catalog c = (Catalog)o;
		return mId.equals(c.getId()) &&
				mErn.equals(c.getErn()) &&
				mLabel.equals(c.getLabel()) &&
				mBackground.equals(c.getBackground()) &&
				mRunFrom == c.getRunFrom() &&
				mRunTill == c.getRunTill() &&
				mPageCount == c.getPageCount() &&
				mOfferCount == c.getOfferCount() &&
				mBranding == null ? c.getBranding() == null : mBranding.equals(c.getBranding()) &&
				mDealerId.equals(c.getDealerId()) &&
				mDealerUrl.equals(c.getDealerUrl()) &&
				mStoreId.equals(c.getStoreId()) &&
				mStoreUrl.equals(c.getStoreUrl()) &&
				mDimension == null ? c.getDimension() == null : mDimension.equals(c.getDimension()) &&
				mImages == null ? c.getImages() == null : mImages.equals(c.getImages()) &&
				mPages == null ? c.getPages() == null : mPages.equals(c.getPages()) &&
				mDealer == null ? c.getDealer() == null : (c.getDealer() == null ? false : mDealer.equals(c.getDealer())) &&
				mStore == null ? c.getStore() == null : (c.getStore() == null ? false : mStore.equals(c.getStore())) &&
				mOfferOnPage == c.getOfferOnPage();
	}
	
	@Override
	public String toString() {
		return toString(false);
	}
	
	public String toString(boolean everything) {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("[")
		.append("branding=").append(mBranding == null ? null : mBranding.toString(everything))
		.append(", id=").append(mId)
		.append(", from=").append(getRunFromString())
		.append(", till=").append(getRunTillString());
		if(everything) {
			sb.append(", ern=").append(mErn)
			.append(", background=").append(mBackground)
			.append(", pageCount=").append(mPageCount)
			.append(", offerCount=").append(mOfferCount)
			.append(", dealer=").append(mDealer == null ? mDealerId : mDealer.toString())
			.append(", store=").append(mStore == null ? mStoreId : mStore.toString())
			.append(", dimension=").append(mDimension == null ? null : mDimension.toString())
			.append(", images=").append(mImages == null ? null : mImages.toString())
			.append(", pages=").append(mPages == null ? null : mPages.toString());
		}
		return sb.append("]").toString();
	}
	
}
