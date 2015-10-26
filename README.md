ShopGun Android SDK
===================

The simple solution to querying for ShopGun-data.

API key and secret
-----------------
You will need to get an *API key* and *API secret* from our [developer site] (look for "Manage Apps").

If you wish to try our demo app, just clone and run it. We've included an API key and secret, that will work straight out of the box. But the key only provides a limited amount of quereis pr day so don't use it in production.

Download
--------
Grab it with Gradle:
```groovy
compile 'com.shopgun.android:sdk:3.1.0-beta'
```
or clone from github, and add this to your project's `settings.gradle`:
```groovy
include ':shopGunSdk'
project(':shopGunSdk').projectDir=new File('/path/to/shopgun-android-sdk/shopGunSdk')
```



<!-- 

The SDK Demo app included in the repo demonstrates some basic features, some of which are also described in this README. If you want to get started quickly, just start a new Android Application Project and import the ETA SDK into Eclipse as a library. 

## <a name="getting-started">Getting started
* [AndroidManifest.xml](#androidmanifest)
* [Init the SDK](#init-eta)
* [Location](#location)

## Need to know
* [Requests](#first-request)
* [Pageflip](#pageflip)

## Managers
* [SessionManager](#sessionmanager)
* [ListManager](#listmanager)

## Last but not least
* [Models](#models)
* [Utils](#utils)
* [Test](#test)
* [Debugging](#debugging)

### Best Shopping Buddy Ever - Right?


### <a name="androidmanifest"></a>AndroidManifest.xml
Update your AndroidManifest.xml file, by adding these lines:
```
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### <a name="init-eta"></a>Initialize Eta
To get the eTilbudsavis SDK started, you need to call
```
    Eta.createInstance("YOUR_API_KEY", "YOUR_API_SECRET", Context);
```

Managing lifecycle, in the activities

### <a name="location"></a>Location

The rest of this documentation will offer detailed insights into:


# Usage

The SDK offers several convenient packages. Each package has a short description below, where the essential classes are described.

Here we will give a short presentation of each class, it's purpose and functionality.
For any information on API specifics please refer to our [API Documentation Page](http://docs.api.etilbudsavis.dk/)

com.eTilbudsavis.etasdk

com.eTilbudsavis.etasdk.imageloader

com.eTilbudsavis.etasdk.log

com.eTilbudsavis.etasdk.model

com.eTilbudsavis.etasdk.network

com.eTilbudsavis.etasdk.pageflip

com.eTilbudsavis.etasdk.photoview

com.eTilbudsavis.etasdk.request - DO NOT USE YET

com.eTilbudsavis.etasdk.test

com.eTilbudsavis.etasdk.utils

## Eta
This is the main Class. Before you can start using the SDK, you must set the `Eta` object. This only needs to be done once
because the `Eta` object is a singleton. The singleton pattern assures that the database, session and other settings are not
ending up in a corrupt state.

    Eta.getInstance().set("YOUR_API_KEY", "YOUR_API_SECRET", Context);

The `Eta` class, must also be part of your app's Lifecycle, which means that `onResume()` and `onPause()`
must be called on the `mEta` object, this ensures all preferences are saved correctly, and database connections are
opened/closed.

	@Override
	protected void onResume() {
		super.onResume();
		Eta.getInstance().onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Eta.getInstance().onPause();
	}


To ebable the debug output, just set it to `true`:

	Eta.getInstance().debug(true);


The `eta` object offers several usefull methods, see each Class for details:

- `getUser()` - Get the current user logged in.
- `getLocation()` - To get the location that the SDK uses for queries, location should always be set, or the API won't respond.
- `getSession()` - The session is automatically generated by the SDK, and is needed for communication with the API.
- `getShoppinglistManager()` - This is a very convinient class, and allows to easily create an shoppinglist for both offline as well as online synchronization.
- `debug(boolean useDebug)` - Use this to enable debugging output to LogCat. NOTE: no debugging output will be displayed, if this isn't set to `true`


## Location
The EtaLocation object, is a pure state object, and is where you want to store any Location information.
Without a valid location set, the API won't respond with any data, as the whole service is geolocation based.

To set a valid location, you must provide at least a **latitude**, **longitude** and a **radius**.
If you are using `LocationManager` you can pass any new `location` objects directly into `EtaLocation`.

`ShopGunLocation` will save the last known location to shared preferences, so a valid location is always accessible,
once an initial location have been given.

## Request
You can include various options into the api.request() call, just create a Bundle 
with key/value pairs, and send it as a parameter. See more about REST API options
[here](https://etilbudsavis.dk/developers/docs/).


#### Request types
ETA SDK has four default request types, ready to use out of the box.

JsonObjectRequest
JsonArrayRequest
JsonStringRequest
StringRequest


## PageflipWebview
Pageflip, is basically just a simple and smooth catalog viewer. With a simple yet effective interface.
The Pageflip view, can be added to any XML layout you're using in eclipse, either via the GUI editor,
under _Custom and Library Views->Pageflip_ or with this XML tag:
	
	<com.eTilbudsavis.etasdk.Pageflip />

The `Pageflip` must be executed like other elements in the SDK with the `execute()` method. This way, you'll 
have full control of setting up any options, you want before actually loading the `Pageflip`, and make sure that
you to decide what happens, and when. See a simple working example of how to interact with the `Pageflip`, in the SDKDemo (bundled in the SDK).

#### Events
`Pageflip` takes a `PageflipListener`, through which you will recieve information about the actions the user
performs on the view. The current list of events, and corresponding JSONObjects can be found [here](http://engineering.etilbudsavis.dk/eta-web-app/#eta-catalog-view).

## Session
All API requests require a valid Session, and the session must opdate based on headers from the API. 
Furthermore, Session is a shared state between client and server, and also describes what permissions a given user/session has.

_luckily the SDK takes care of all of this, so you don't have to_ :-)

The commonly used methods in the session are:

- `login()` - For logging in a user
- `loginFacebook()` - For loggingin via Facebook (This requires you to implement the Facebook Android SDK)
- `forgotPassword()` - To retrieve a forgotton password
- `createUser()` - Create a new ShopGun user
- `signout()` - Sign a user out.

Furthermore the Session has a subscriber system, so anyone (class) who want's notification on state change will be notified via an `SessionListener`.
To subscribe/unsubscribe, use `subscribe()` and `unSubscribe()` methods respectively.

<big>Session is not yet intended for a multi user setup</big>


## Shoppinglist Manager
Though some functionality is developed, and integrated allready, the `ShoppinglistManager` isn't fully implemented yet. And you can experience some odd behavior at times.

## Caching
We have strived to make the API as well as the SDK as fast as possible, therefore we have implemented
caching of all objects, that is by nature cachable. This includes all objects which is identified by an "ern" (unique identifier). But never any list calls, as these are subject to change far more than identifiable objects.

This is on by default, and you don't have to do anything to harvest the benefits of this.

Cached callbacks can be identified, by the argument `isCache` present in the callback interface for the `Api` requests.
The callback interface will actually give you both the cached result as well as the API data, unless you set a flag on the `Api` object, stating otherwise (`ONLY_RETURN_CACHE`).

## ShopGun Objects
The SDK comes with a full set of Java Objects, to match responses from the API.
Where the base Class is `ShopGunObject`, all other classes inherits from this class.

There is a special group of identifiable classes, which has a unique identifier called `ern` as a key in the JSON response. 
These objects extends from the `ShopGunErnObject` and are the "base" for all objects, in the sense that they are what you are actually quering the API for.
The objects are:

- `Catalog`
- `Offer`
- `Dealer`
- `Store`
- `Shoppinglist`
- `ShoppinglistItem`
- `ShopGunError`

The objects will in someway contain parts of all other objects, that have been included in this SDK.

## Debugging


# Utilities




# Feedback
If you have any feedback or comments feel free to contact danny@etilbudsavis.dk :-)

[eclipse]:http://www.eclipse.org/
[Android Development Tools]:http://developer.android.com/tools/sdk/eclipse-adt.html
plugin. 
[developer site]:https://etilbudsavis.dk/developers/
[Native Android SDK]:https://github.com/eTilbudsavis/native-android-sdk.git
 -->