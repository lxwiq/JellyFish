# AuthApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**authJellyfinPost**](AuthApi.md#authJellyfinPost) | **POST** /auth/jellyfin | Sign in using a Jellyfin username and password |
| [**authLocalPost**](AuthApi.md#authLocalPost) | **POST** /auth/local | Sign in using a local account |
| [**authLogoutPost**](AuthApi.md#authLogoutPost) | **POST** /auth/logout | Sign out and clear session cookie |
| [**authMeGet**](AuthApi.md#authMeGet) | **GET** /auth/me | Get logged-in user |
| [**authPlexPost**](AuthApi.md#authPlexPost) | **POST** /auth/plex | Sign in using a Plex token |


<a id="authJellyfinPost"></a>
# **authJellyfinPost**
> User authJellyfinPost(authJellyfinPostRequest)

Sign in using a Jellyfin username and password

Takes the user&#39;s username and password to log the user in. Generates a session cookie for use in further requests. If the user does not exist, and there are no other users, then a user will be created with full admin privileges. If a user logs in with access to the Jellyfin server, they will also have an account created, but without any permissions.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = AuthApi()
val authJellyfinPostRequest : AuthJellyfinPostRequest =  // AuthJellyfinPostRequest | 
try {
    val result : User = apiInstance.authJellyfinPost(authJellyfinPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AuthApi#authJellyfinPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthApi#authJellyfinPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **authJellyfinPostRequest** | [**AuthJellyfinPostRequest**](AuthJellyfinPostRequest.md)|  | |

### Return type

[**User**](User.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="authLocalPost"></a>
# **authLocalPost**
> User authLocalPost(authLocalPostRequest)

Sign in using a local account

Takes an &#x60;email&#x60; and a &#x60;password&#x60; to log the user in. Generates a session cookie for use in further requests.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = AuthApi()
val authLocalPostRequest : AuthLocalPostRequest =  // AuthLocalPostRequest | 
try {
    val result : User = apiInstance.authLocalPost(authLocalPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AuthApi#authLocalPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthApi#authLocalPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **authLocalPostRequest** | [**AuthLocalPostRequest**](AuthLocalPostRequest.md)|  | |

### Return type

[**User**](User.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="authLogoutPost"></a>
# **authLogoutPost**
> AuthLogoutPost200Response authLogoutPost()

Sign out and clear session cookie

Completely clear the session cookie and associated values, effectively signing the user out.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = AuthApi()
try {
    val result : AuthLogoutPost200Response = apiInstance.authLogoutPost()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AuthApi#authLogoutPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthApi#authLogoutPost")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**AuthLogoutPost200Response**](AuthLogoutPost200Response.md)

### Authorization


Configure apiKey:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""
Configure cookieAuth:
    ApiClient.apiKey["connect.sid"] = ""
    ApiClient.apiKeyPrefix["connect.sid"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="authMeGet"></a>
# **authMeGet**
> User authMeGet()

Get logged-in user

Returns the currently logged-in user.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = AuthApi()
try {
    val result : User = apiInstance.authMeGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AuthApi#authMeGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthApi#authMeGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**User**](User.md)

### Authorization


Configure apiKey:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""
Configure cookieAuth:
    ApiClient.apiKey["connect.sid"] = ""
    ApiClient.apiKeyPrefix["connect.sid"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="authPlexPost"></a>
# **authPlexPost**
> User authPlexPost(authPlexPostRequest)

Sign in using a Plex token

Takes an &#x60;authToken&#x60; (Plex token) to log the user in. Generates a session cookie for use in further requests. If the user does not exist, and there are no other users, then a user will be created with full admin privileges. If a user logs in with access to the main Plex server, they will also have an account created, but without any permissions.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = AuthApi()
val authPlexPostRequest : AuthPlexPostRequest =  // AuthPlexPostRequest | 
try {
    val result : User = apiInstance.authPlexPost(authPlexPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling AuthApi#authPlexPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling AuthApi#authPlexPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **authPlexPostRequest** | [**AuthPlexPostRequest**](AuthPlexPostRequest.md)|  | |

### Return type

[**User**](User.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

