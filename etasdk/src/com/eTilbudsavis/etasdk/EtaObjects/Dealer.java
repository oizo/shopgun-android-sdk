package com.eTilbudsavis.etasdk.EtaObjects;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;

import com.eTilbudsavis.etasdk.Network.Request;
import com.eTilbudsavis.etasdk.Utils.EtaLog;

public class Dealer extends EtaErnObject implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String TAG = "Dealer";

	/** Sort a list by name in ascending order. (smallest to largest) */
	public static final String SORT_NAME = Request.Sort.NAME;

	/** Sort a list by name in descending order. (largest to smallest)*/
	public static final String SORT_NAME_DESC = Request.Sort.NAME_DESC;

	/** Sort a list by created in ascending order. (smallest to largest) */
	public static final String SORT_CREATED = Request.Sort.CREATED;

	/** Sort a list by created in ascending order. (smallest to largest) */
	public static final String SORT_CREATED_DESC = Request.Sort.CREATED_DESC;

	/** Parameter for getting a list of specific dealer id's */
	public static final String FILTER_DEALER_IDS = Request.Param.FILTER_DEALER_IDS;

	/** String identifying the query parameter */
	public static final String PARAM_QUERY = Request.Param.QUERY;
	
	/** Endpoint for dealer list resource */
	public static final String ENDPOINT_LIST = Request.Endpoint.DEALER_LIST;
	
	/** Endpoint for a single dealer resource */
	public static final String ENDPOINT_ID = Request.Endpoint.DEALER_ID;
	
	/** Endpoint for searching dealers */
	public static final String ENDPOINT_SEARCH = Request.Endpoint.DEALER_SEARCH;
	
	private String mName;
	private String mUrlName;
	private String mWebsite;
	private String mLogo;
	private Integer mColor;
	private Pageflip mPageflip;

	public Dealer() { }
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Dealer> fromJSON(JSONArray dealers) {
		ArrayList<Dealer> list = new ArrayList<Dealer>();
		try {
			for (int i = 0 ; i < dealers.length() ; i++ )
				list.add(Dealer.fromJSON((JSONObject)dealers.get(i)));
			
		} catch (JSONException e) {
			EtaLog.d(TAG, e);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static Dealer fromJSON(JSONObject dealer) {
		return fromJSON(new Dealer(), dealer);
	}
	
	private static Dealer fromJSON(Dealer d, JSONObject dealer) {
		if (d == null) d = new Dealer();
		if (dealer == null) return d;

		try {
			d.setId(getJsonString(dealer, S_ID));
			d.setErn(getJsonString(dealer, S_ERN));
			d.setName(getJsonString(dealer, S_NAME));
			d.setUrlName(getJsonString(dealer, S_URL_NAME));
			d.setWebsite(getJsonString(dealer, S_WEBSITE));
			d.setLogo(getJsonString(dealer, S_LOGO));
			d.setColor(Color.parseColor("#"+getJsonString(dealer, S_COLOR)));
			d.setPageflip(Pageflip.fromJSON(dealer.getJSONObject(S_PAGEFLIP)));
		} catch (JSONException e) {
			EtaLog.d(TAG, e);
		}
		return d;
	}
	
	public JSONObject toJSON(){
		return toJSON(this);
	}
	
	public static JSONObject toJSON(Dealer d) {
		JSONObject o = new JSONObject();
		try {
			o.put(S_ID, d.getId());
			o.put(S_ERN, d.getErn());
			o.put(S_NAME, d.getName());
			o.put(S_URL_NAME, d.getUrlName());
			o.put(S_WEBSITE, d.getWebsite());
			o.put(S_LOGO, d.getLogo());
			o.put(S_COLOR, d.getColorString());
			o.put(S_PAGEFLIP, d.getPageflip().toJSON());
		} catch (JSONException e) {
			EtaLog.d(TAG, e);
		}
		return o; 
	}
	
	public Dealer setName(String name) {
		mName = name;
		return this;
	}

	public String getName() {
		return mName;
	}

	public Dealer setUrlName(String url) {
		mUrlName = url;
		return this;
	}

	public String getUrlName() {
		return mUrlName;
	}

	public Dealer setWebsite(String website) {
		mWebsite = website;
		return this;
	}

	public String getWebsite() {
		return mWebsite;
	}

	public Dealer setLogo(String logo) {
		mLogo = logo;
		return this;
	}

	public String getLogo() {
		return mLogo;
	}

	public Dealer setColor(int color) {
		mColor = color;
		return this;
	}

	public int getColor() {
		return mColor;
	}
	
	public String getColorString() {
		return String.format("%06X", 0xFFFFFF & mColor);
	}

	public Dealer setPageflip(Pageflip pageflip) {
		mPageflip = pageflip;
		return this;
	}

	public Pageflip getPageflip() {
		return mPageflip;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		
		if (!(o instanceof Dealer))
			return false;

		Dealer dealer = (Dealer)o;
		return stringCompare(mId, dealer.getId()) &&
				stringCompare(mErn, dealer.getErn()) &&
				stringCompare(mName, dealer.getName()) &&
				stringCompare(mUrlName, dealer.getUrlName()) &&
				stringCompare(mWebsite, dealer.getWebsite()) &&
				stringCompare(mLogo, dealer.getLogo()) &&
				mColor.equals(dealer.getColor()) &&
				mPageflip == null ? dealer.getPageflip() == null : mPageflip.equals(dealer.getPageflip());
	}

	@Override
	public String toString() {
		return toString(false);
	}
	
	public String toString(boolean everything) {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append("[")
		.append("name=").append(mName)
		.append(", id=").append(mId);
		
		if (everything) {
			sb.append(", ern=").append(mErn)
			.append(", urlName=").append(mUrlName)
			.append(", website=").append(mWebsite)
			.append(", logo=").append(mLogo)
			.append(", color=").append(mColor)
			.append(", pageflip=").append(mPageflip == null ? null : mPageflip.toString());
		}
		return sb.append("]").toString();
	}
}
