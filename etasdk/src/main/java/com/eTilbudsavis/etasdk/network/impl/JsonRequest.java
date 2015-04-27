/*******************************************************************************
* Copyright 2014 eTilbudsavis
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
*******************************************************************************/
package com.eTilbudsavis.etasdk.network.impl;

import java.io.UnsupportedEncodingException;

import com.eTilbudsavis.etasdk.Constants;
import com.eTilbudsavis.etasdk.log.EtaLog;
import com.eTilbudsavis.etasdk.network.Request;
import com.eTilbudsavis.etasdk.network.RequestQueue;
import com.eTilbudsavis.etasdk.network.Response.Listener;

public abstract class JsonRequest<T> extends Request<T> {

	public static final String TAG = Constants.getTag(JsonRequest.class);
	
	/** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", DEFAULT_PARAMS_ENCODING);
    
    private String mRequestBody;
    
    private Priority mPriority = Priority.MEDIUM;
    
    public JsonRequest(String url, Listener<T> listener) {
		super(Method.GET, url, listener);
		
	}
    
    public JsonRequest(Method method, String url, String requestBody, Listener<T> listener) {
		super(method, url, listener);
		boolean nonBodyRequest = (method == Method.GET || method == Method.DELETE);
		if (nonBodyRequest && requestBody != null) {
			EtaLog.i(TAG, "GET and DELETE requests doesn't take a body, and will be ignored.\n"
					+ "Please append any GET and DELETE parameters to Request.putQueryParameters()");
		}
		mRequestBody = requestBody;
	}
    
    /**
     * Append single query parameter to the given request.
     * @param key - a API v2 parameter key
     * @param value - The value matching the key
     * @return this object, for easy chaining
     */
    @Deprecated
    public Request<?> putQueryParam(String key, String value) {
    	getQueryParameters().putString(key, value);
    	return this;
    }
    
    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }
    
    @Override
    public byte[] getBody() {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(DEFAULT_PARAMS_ENCODING);
        } catch (UnsupportedEncodingException uee) {
            return null;
        }
    }
    
    public Request<T> setPriority(Priority p) {
    	mPriority = p;
    	return this;
    }

    @Override
    public Priority getPriority() {
    	return mPriority;
    }
    
	/**
	 * Returns a complete printable representation of this Request, e.g:
	 * 
	 * <li>GET: https://api.etilbudsavis.dk/v2/catalogs/{catalog_id}?param1=value1&amp;param2=value2</li>
	 * <li>PUT: https://api.etilbudsavis.dk/v2/catalogs/{catalog_id}?param1=value1&amp;param2=value2&amp;body=[json_string]</li>
	 * 
	 * <p>Body data is appended as the last query parameter for convenience, as
	 * seen in the examples above. The SDK/API parameters are not added to the 
	 * {@link Request#getQueryParameters() query parameters}, before the request
	 * is handed to the {@link RequestQueue}. So if you want to have the SDK/API
	 * parameters appended as well in the string do:</p>
	 * <li>Eta.getInstance().add(Request)</li>
	 * <p>and then call: </p>
	 * <li>toString()</li>
	 */
	@Override
	public String toString() {
		if (mRequestBody != null) {
			return super.toString() + "&body=[" + mRequestBody + "]";
		} else {
			return super.toString();
		}
	}
	
}