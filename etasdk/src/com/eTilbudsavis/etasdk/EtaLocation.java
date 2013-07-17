/**
 * @fileoverview	Location.
 * @author			Danny Hvam <danny@eTilbudsavis.dk>
 */
package com.eTilbudsavis.etasdk;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.eTilbudsavis.etasdk.EtaObjects.Store;
import com.eTilbudsavis.etasdk.Utils.Params;
import com.eTilbudsavis.etasdk.Utils.Utils;

public class EtaLocation extends Location {

	public static final String TAG = "EtaLocation";
	
	private static final String ETA_PROVIDER	= "etasdk";
	
	/** API v2 parameter name for sensor. */
	public static final String SENSOR = Params.SENSOR;
	
	/** API v2 parameter name for latitude. */
	public static final String LATITUDE = Params.LATITUDE;
	
	/** API v2 parameter name for longitude. */
	public static final String LONGITUDE = Params.LONGITUDE;
	
	/** API v2 parameter name for radius. */
	public static final String RADIUS = Params.RADIUS;
	
	/** API v2 parameter name for bounds east. */
	public static final String BOUND_EAST = Params.BOUND_EAST;
	
	/** API v2 parameter name for bounds north. */
	public static final String BOUND_NORTH = Params.BOUND_NORTH;
	
	/** API v2 parameter name for bounds south. */
	public static final String BOUND_SOUTH = Params.BOUND_SOUTH;
	
	/** API v2 parameter name for bounds west. */
	public static final String BOUND_WEST = Params.BOUND_WEST;

	private static final String ADDRESS = "etasdk_loc_address";

	private static final String TIME = "etasdk_loc_time";
	
	// Location.
	private int mRadius = 700000;
	private boolean mSensor = false;
	private String mAddress = "";
	private double mBoundNorth = 0f;
	private double mBoundEast = 0f;
	private double mBoundSouth = 0f;
	private double mBoundWest = 0f;
	private SharedPreferences mSharedPrefs;
	private ArrayList<LocationListener> mSubscribers = new ArrayList<EtaLocation.LocationListener>();

	public EtaLocation(SharedPreferences prefs) {
		super(ETA_PROVIDER);
		mSharedPrefs = prefs;
		restoreFromSharedPrefs();
	}

	public Boolean isLocationSet() {
		return (getLatitude() != 0.0 && getLongitude() != 0.0);
	}

	public Boolean isBoundsSet() {
		return (mBoundNorth != 0f && mBoundSouth != 0f && mBoundEast != 0f && mBoundWest != 0f);
	}

	@Override
	public void set(Location l) {
		super.set(l);
		mAddress = "";
		mSensor = (getProvider().equals(LocationManager.GPS_PROVIDER) || getProvider().equals(LocationManager.NETWORK_PROVIDER) );
		saveToSharedPrefs();
	}
	
	public EtaLocation set(Location l, boolean sensor) {
		super.set(l);
		return set("", getLatitude(), getLongitude(), sensor);
	}

	public EtaLocation set(double latitude, double longitude) {
		return set("", latitude, longitude, false);
	}

	public EtaLocation set(double latitude, double longitude, boolean sensor) {
		return set("", latitude, longitude, sensor);
	}

	public EtaLocation set(String address, double latitude, double longitude) {
		return set(address, latitude, longitude, false);
	}
	
	private EtaLocation set(String address, double latitude, double longitude, boolean sensor) {
		mAddress = address;
		mSensor = sensor;
		setLatitude(latitude);
		setLongitude(longitude);
		setTime(System.currentTimeMillis());
		setProvider(ETA_PROVIDER);
		saveToSharedPrefs();
		return this;
	}
	
	/**
	 * Set the current search radius.
	 * @param radius in meters <li> Min value = 0 <li> Max value = 700000
	 * @return this Object, for easy chaining of set methods.
	 */
	public EtaLocation setRadius(int radius) {
		if (radius < 0)
			mRadius = 0;
		else if (radius > 700000)
			mRadius = 700000;
		else
			mRadius = radius;
		saveToSharedPrefs();
		return this;
	}

	/**
	 * Get current radius
	 * @return radius in meters.
	 */
	public int getRadius() {
		return mRadius;
	}

	public EtaLocation setSensor(boolean sensor) {
		mSensor = sensor;
		saveToSharedPrefs();
		return this;
	}
	
	public boolean isSensor() {
		return mSensor;
	}

	public EtaLocation setAddress(String address) {
		mAddress = address;
		saveToSharedPrefs();
		return this;
	}
	
	public String getAddress() {
		return mAddress;
	}

	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		try {
			o.put(LATITUDE, getLatitude());
			o.put(LONGITUDE, getLongitude());
			o.put(SENSOR, isSensor());
			o.put(RADIUS, getRadius());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return o;
	}

	public int distance(Store store) {
		Location tmp = new Location(EtaLocation.ETA_PROVIDER);
		tmp.setLatitude(store.getLatitude());
		tmp.setLongitude(store.getLongitude());
		double dist = distanceTo(tmp);
		return (int)dist;
	}

	/**
	 * Set the bounds for your search.
	 * All parameters should be GPS coordinates.
	 * @param boundsNorth 
	 * @param boundsEast
	 * @param boundsSouth
	 * @param boundsWest
	 */
	public void setBounds(double boundNorth, double boundEast,
			double boundSouth, double boundWest) {
		setBoundEast(boundEast);
		setBoundNorth(boundNorth);
		setBoundSouth(boundSouth);
		setBoundWest(boundWest);
		saveToSharedPrefs();
	}
	
	/**
	 * GPS coordinate for the northern bound of a search.
	 * @param boundsNorth
	 */
	public EtaLocation setBoundNorth(double boundNorth) {
		mBoundNorth = boundNorth;
		saveToSharedPrefs();
		return this;
	}

	/**
	 * GPS coordinate for the eastern bound of a search.
	 * @param boundEast
	 */
	public EtaLocation setBoundEast(double boundEast) {
		mBoundEast = boundEast;
		saveToSharedPrefs();
		return this;
	}

	/**
	 * GPS coordinate for the southern bound of a search.
	 * @param boundSouth
	 */
	public EtaLocation setBoundSouth(double boundSouth) {
		mBoundSouth = boundSouth;
		saveToSharedPrefs();
		return this;
	}

	/**
	 * GPS coordinate for the western bound of a search.
	 * @param boundWest
	 */
	public EtaLocation setBoundWest(double boundWest) {
		mBoundWest = boundWest;
		saveToSharedPrefs();
		return this;
	}
	
	public double getBoundEast() {
		return mBoundEast;
	}
	
	public double getBoundNorth() {
		return mBoundNorth;
	}
	
	public double getBoundSouth() {
		return mBoundSouth;
	}
	
	public double getBoundWest() {
		return mBoundWest;
	}

	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putBoolean(SENSOR, mSensor);
		savedInstanceState.putInt(RADIUS, mRadius);
		savedInstanceState.putDouble(LATITUDE, getLatitude());
		savedInstanceState.putDouble(LONGITUDE, getLongitude());
		savedInstanceState.putDouble(BOUND_EAST, mBoundEast);
		savedInstanceState.putDouble(BOUND_WEST, mBoundWest);
		savedInstanceState.putDouble(BOUND_NORTH, mBoundNorth);
		savedInstanceState.putDouble(BOUND_SOUTH, mBoundSouth);
		savedInstanceState.putString(ADDRESS, mAddress);
		savedInstanceState.putLong(TIME, getTime());

	}
	
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		setSensor(savedInstanceState.getBoolean(SENSOR));
		setRadius(savedInstanceState.getInt(RADIUS));
		setLatitude(savedInstanceState.getDouble(LATITUDE));
		setLongitude(savedInstanceState.getDouble(LONGITUDE));
		setBoundEast(savedInstanceState.getDouble(BOUND_EAST));
		setBoundWest(savedInstanceState.getDouble(BOUND_WEST));
		setBoundNorth(savedInstanceState.getDouble(BOUND_NORTH));
		setBoundSouth(savedInstanceState.getDouble(BOUND_SOUTH));
		setAddress(savedInstanceState.getString(ADDRESS));
		setTime(savedInstanceState.getLong(TIME));
	}

	private void saveToSharedPrefs() {
		new Thread() {
	        public void run() {
	        	mSharedPrefs.edit()
	    		.putBoolean(SENSOR, mSensor)
	    		.putInt(RADIUS, mRadius)
	    		.putFloat(LATITUDE, (float)getLatitude())
	    		.putFloat(LONGITUDE, (float)getLongitude())
	    		.putFloat(BOUND_EAST, (float)mBoundEast)
	    		.putFloat(BOUND_WEST, (float)mBoundWest)
	    		.putFloat(BOUND_NORTH, (float)mBoundNorth)
	    		.putFloat(BOUND_SOUTH, (float)mBoundSouth)
	    		.putString(ADDRESS, mAddress)
	    		.putLong(TIME, getTime())
	    		.commit();
	        }
		}.start();
	}
	
	private boolean restoreFromSharedPrefs() {
		if (mSharedPrefs.contains(SENSOR) && mSharedPrefs.contains(RADIUS) && mSharedPrefs.contains(LATITUDE) && 
				mSharedPrefs.contains(LONGITUDE) && mSharedPrefs.contains(BOUND_EAST) && mSharedPrefs.contains(BOUND_WEST) && 
				mSharedPrefs.contains(BOUND_NORTH) && mSharedPrefs.contains(BOUND_SOUTH) && mSharedPrefs.contains(TIME) ) {
			
			setSensor(mSharedPrefs.getBoolean(SENSOR, false));
			setRadius(mSharedPrefs.getInt(RADIUS, Integer.MAX_VALUE));
			setLatitude(mSharedPrefs.getFloat(LATITUDE, 0f));
			setLongitude(mSharedPrefs.getFloat(LONGITUDE, 0f));
			setBoundEast(mSharedPrefs.getFloat(BOUND_EAST, 0f));
			setBoundWest(mSharedPrefs.getFloat(BOUND_WEST, 0f));
			setBoundNorth(mSharedPrefs.getFloat(BOUND_NORTH, 0f));
			setBoundSouth(mSharedPrefs.getFloat(BOUND_SOUTH, 0f));
			setAddress(mSharedPrefs.getString(ADDRESS, null));
			setTime(mSharedPrefs.getLong(TIME, System.currentTimeMillis()));
			return true;
		} else {
			return false;
		}
		
	}

	/**
	 * Invoke notifications to subscribers of this location object.<br><br>
	 * This object automatically notifies all subscribers on changes.
	 */
	public void notifySubscribers() {
		for (LocationListener l : mSubscribers) {
			try {
				l.onLocationChange();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Subscribe to events and changes in location.
	 * @param listener for callbacks
	 */
	public void subscribe(LocationListener listener) {
		if (!mSubscribers.contains(listener))
			mSubscribers.add(listener);
	}

	/**
	 * Unsubscribe from events and changes in location.
	 * @param listener to remove
	 * @return true if this Collection is modified, false otherwise.
	 */
	public boolean unSubscribe(LocationListener listener) {
		return mSubscribers.remove(listener);
	}
	
	@Override 
	public String toString() {
        return "Location[mProvider=" + getProvider() +
                ",mTime=" + getTime() +
                ",mLatitude=" + getLatitude() +
                ",mLongitude=" + getLongitude() +
                ",mRadius=" + mRadius +
                ",mSensor=" + mSensor + "]";
	}
	
	public interface LocationListener {
		public void onLocationChange();
	}
	
}