/*******************************************************************************
 * Copyright 2015 eTilbudsavis
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

package com.eTilbudsavis.sdkdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eTilbudsavis.etasdk.Eta;
import com.eTilbudsavis.etasdk.model.User;
import com.eTilbudsavis.etasdk.network.EtaError;
import com.eTilbudsavis.etasdk.network.Response;
import com.eTilbudsavis.sdkdemo.base.BaseActivity;

import org.json.JSONObject;

public class SessionActivity extends BaseActivity {

    private EditText mSigninEmail;
    private EditText mSigninPassword;
    private TextView mEmail;
    private Button mSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_view);

        mSigninEmail = (EditText) findViewById(R.id.signin_email);
        mSigninPassword = (EditText) findViewById(R.id.signin_password);
        mEmail = (TextView) findViewById(R.id.email);
        mSignin = (Button) findViewById(R.id.signin);
        mSignin.setOnClickListener(mSigninListener);

    }

    View.OnClickListener mSigninListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Eta e = Eta.getInstance();
            showProgress("", "Updating session");
            if (e.getUser().isLoggedIn()) {
                e.getSessionManager().signout(mListener);
            } else {
                e.getSessionManager().login(getEmail(), getPassword(), mListener);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    private void updateView() {

        boolean loggedin = Eta.getInstance().getUser().isLoggedIn();
        mSigninEmail.setVisibility(loggedin ? View.GONE : View.VISIBLE);
        mSigninPassword.setVisibility(loggedin ? View.GONE : View.VISIBLE);
        mEmail.setVisibility(loggedin ? View.VISIBLE : View.GONE);
        mEmail.setText(Eta.getInstance().getUser().getEmail());
        mSignin.setText(loggedin ? "Signout" : "Signin");
        hideProgress();

    }

    private String getEmail() {
        return mSigninEmail.getText().toString().trim();
    }

    private String getPassword() {
        return mSigninPassword.getText().toString().trim();
    }

    Response.Listener<JSONObject> mListener = new Response.Listener<JSONObject>() {
        @Override
        public void onComplete(JSONObject response, EtaError error) {
            updateView();
            if (error != null) {
                Tools.showDialog(SessionActivity.this, "Error", error.toString());
            }
        }
    };

}
