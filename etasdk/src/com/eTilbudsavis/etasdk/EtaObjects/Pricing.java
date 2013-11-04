package com.eTilbudsavis.etasdk.EtaObjects;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.eTilbudsavis.etasdk.Utils.EtaLog;

public class Pricing extends EtaObject implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String TAG = "Pricing";
	
	private static final String S_PRICE = "price";
	private static final String S_PREPRICE = "pre_price";
	private static final String S_CURRENCY = "currency";
	
	private double mPrice = 0.0;
	private Double mPrePrice;
	private String mCurrency;
	
	public Pricing() {
	}
	
	public static Pricing fromJSON(String pricing) {
		Pricing p = new Pricing();
		try {
			p = fromJSON(p, new JSONObject(pricing));
		} catch (JSONException e) {
			EtaLog.d(TAG, e);
		}
		return p;
	}
	
	@SuppressWarnings("unchecked")
	public static Pricing fromJSON(JSONObject pricing) {
		return fromJSON(new Pricing(), pricing);
	}
	
	public static Pricing fromJSON(Pricing p, JSONObject pricing) {
		if (p == null) p = new Pricing();
		if (pricing == null) return p;
		
		try {
			p.setPrice(pricing.getDouble(S_PRICE));
			p.setPrePrice(pricing.isNull(S_PREPRICE) ? null : pricing.getDouble(S_PREPRICE));
			p.setCurrency(getJsonString(pricing, S_CURRENCY));
		} catch (JSONException e) {
			EtaLog.d(TAG, e);
		}
		return p;
	}
	
	public JSONObject toJSON() {
		return toJSON(this);
	}
	
	public static JSONObject toJSON(Pricing p) {
		JSONObject o = new JSONObject();
		try {
			o.put(S_PRICE, p.getPrice());
			o.put(S_PREPRICE, p.getPrePrice());
			o.put(S_CURRENCY, p.getCurrency());
		} catch (JSONException e) {
			EtaLog.d(TAG, e);
		}
		return o;
	}
	
	public double getPrice() {
		return mPrice;
	}

	public Pricing setPrice(double price) {
		mPrice = price;
		return this;
	}

	public Double getPrePrice() {
		return mPrePrice;
	}

	public Pricing setPrePrice(Double prePrice) {
		mPrePrice = prePrice;
		return this;
	}

	public String getCurrency() {
		return mCurrency;
	}

	public Pricing setCurrency(String currency) {
		mCurrency = currency;
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		
		if (!(o instanceof Pricing))
			return false;

		Pricing p = (Pricing)o;
		return mPrice == p.getPrice() &&
				mPrePrice == null ? p.getPrePrice() == null : mPrePrice == p.getPrePrice() &&
				stringCompare(mCurrency, p.getCurrency());
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
		.append(getClass().getSimpleName()).append("[")
		.append("price=").append(mPrice)
		.append(", preprice=").append(mPrePrice == null ? "null" : mPrePrice)
		.append(", currency=").append(mCurrency)
		.append("]").toString();
	}
}