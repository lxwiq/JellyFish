# UsersApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**authMeGet**](UsersApi.md#authMeGet) | **GET** /auth/me | Get logged-in user |
| [**authResetPasswordGuidPost**](UsersApi.md#authResetPasswordGuidPost) | **POST** /auth/reset-password/{guid} | Reset the password for a user |
| [**authResetPasswordPost**](UsersApi.md#authResetPasswordPost) | **POST** /auth/reset-password | Send a reset password email |
| [**settingsJellyfinUsersGet**](UsersApi.md#settingsJellyfinUsersGet) | **GET** /settings/jellyfin/users | Get Jellyfin Users |
| [**settingsPlexUsersGet**](UsersApi.md#settingsPlexUsersGet) | **GET** /settings/plex/users | Get Plex users |
| [**userGet**](UsersApi.md#userGet) | **GET** /user | Get all users |
| [**userImportFromJellyfinPost**](UsersApi.md#userImportFromJellyfinPost) | **POST** /user/import-from-jellyfin | Import all users from Jellyfin |
| [**userImportFromPlexPost**](UsersApi.md#userImportFromPlexPost) | **POST** /user/import-from-plex | Import all users from Plex |
| [**userPost**](UsersApi.md#userPost) | **POST** /user | Create new user |
| [**userPut**](UsersApi.md#userPut) | **PUT** /user | Update batch of users |
| [**userRegisterPushSubscriptionPost**](UsersApi.md#userRegisterPushSubscriptionPost) | **POST** /user/registerPushSubscription | Register a web push /user/registerPushSubscription |
| [**userUserIdDelete**](UsersApi.md#userUserIdDelete) | **DELETE** /user/{userId} | Delete user by ID |
| [**userUserIdGet**](UsersApi.md#userUserIdGet) | **GET** /user/{userId} | Get user by ID |
| [**userUserIdPushSubscriptionEndpointDelete**](UsersApi.md#userUserIdPushSubscriptionEndpointDelete) | **DELETE** /user/{userId}/pushSubscription/{endpoint} | Delete user push subscription by key |
| [**userUserIdPushSubscriptionEndpointGet**](UsersApi.md#userUserIdPushSubscriptionEndpointGet) | **GET** /user/{userId}/pushSubscription/{endpoint} | Get web push notification settings for a user |
| [**userUserIdPushSubscriptionsGet**](UsersApi.md#userUserIdPushSubscriptionsGet) | **GET** /user/{userId}/pushSubscriptions | Get all web push notification settings for a user |
| [**userUserIdPut**](UsersApi.md#userUserIdPut) | **PUT** /user/{userId} | Update a user by user ID |
| [**userUserIdQuotaGet**](UsersApi.md#userUserIdQuotaGet) | **GET** /user/{userId}/quota | Get quotas for a specific user |
| [**userUserIdRequestsGet**](UsersApi.md#userUserIdRequestsGet) | **GET** /user/{userId}/requests | Get requests for a specific user |
| [**userUserIdSettingsLinkedAccountsJellyfinDelete**](UsersApi.md#userUserIdSettingsLinkedAccountsJellyfinDelete) | **DELETE** /user/{userId}/settings/linked-accounts/jellyfin | Remove the linked Jellyfin account for a user |
| [**userUserIdSettingsLinkedAccountsJellyfinPost**](UsersApi.md#userUserIdSettingsLinkedAccountsJellyfinPost) | **POST** /user/{userId}/settings/linked-accounts/jellyfin | Link the provided Jellyfin account to the current user |
| [**userUserIdSettingsLinkedAccountsPlexDelete**](UsersApi.md#userUserIdSettingsLinkedAccountsPlexDelete) | **DELETE** /user/{userId}/settings/linked-accounts/plex | Remove the linked Plex account for a user |
| [**userUserIdSettingsLinkedAccountsPlexPost**](UsersApi.md#userUserIdSettingsLinkedAccountsPlexPost) | **POST** /user/{userId}/settings/linked-accounts/plex | Link the provided Plex account to the current user |
| [**userUserIdSettingsMainGet**](UsersApi.md#userUserIdSettingsMainGet) | **GET** /user/{userId}/settings/main | Get general settings for a user |
| [**userUserIdSettingsMainPost**](UsersApi.md#userUserIdSettingsMainPost) | **POST** /user/{userId}/settings/main | Update general settings for a user |
| [**userUserIdSettingsNotificationsGet**](UsersApi.md#userUserIdSettingsNotificationsGet) | **GET** /user/{userId}/settings/notifications | Get notification settings for a user |
| [**userUserIdSettingsNotificationsPost**](UsersApi.md#userUserIdSettingsNotificationsPost) | **POST** /user/{userId}/settings/notifications | Update notification settings for a user |
| [**userUserIdSettingsPasswordGet**](UsersApi.md#userUserIdSettingsPasswordGet) | **GET** /user/{userId}/settings/password | Get password page informatiom |
| [**userUserIdSettingsPasswordPost**](UsersApi.md#userUserIdSettingsPasswordPost) | **POST** /user/{userId}/settings/password | Update password for a user |
| [**userUserIdSettingsPermissionsGet**](UsersApi.md#userUserIdSettingsPermissionsGet) | **GET** /user/{userId}/settings/permissions | Get permission settings for a user |
| [**userUserIdSettingsPermissionsPost**](UsersApi.md#userUserIdSettingsPermissionsPost) | **POST** /user/{userId}/settings/permissions | Update permission settings for a user |
| [**userUserIdWatchDataGet**](UsersApi.md#userUserIdWatchDataGet) | **GET** /user/{userId}/watch_data | Get watch data |
| [**userUserIdWatchlistGet**](UsersApi.md#userUserIdWatchlistGet) | **GET** /user/{userId}/watchlist | Get the Plex watchlist for a specific user |


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

val apiInstance = UsersApi()
try {
    val result : User = apiInstance.authMeGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#authMeGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#authMeGet")
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

<a id="authResetPasswordGuidPost"></a>
# **authResetPasswordGuidPost**
> AuthLogoutPost200Response authResetPasswordGuidPost(guid, authResetPasswordGuidPostRequest)

Reset the password for a user

Resets the password for a user if the given guid is connected to a user

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val guid : kotlin.String = 9afef5a7-ec89-4d5f-9397-261e96970b50 // kotlin.String | 
val authResetPasswordGuidPostRequest : AuthResetPasswordGuidPostRequest =  // AuthResetPasswordGuidPostRequest | 
try {
    val result : AuthLogoutPost200Response = apiInstance.authResetPasswordGuidPost(guid, authResetPasswordGuidPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#authResetPasswordGuidPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#authResetPasswordGuidPost")
    e.printStackTrace()
}
```

### Parameters
| **guid** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **authResetPasswordGuidPostRequest** | [**AuthResetPasswordGuidPostRequest**](AuthResetPasswordGuidPostRequest.md)|  | |

### Return type

[**AuthLogoutPost200Response**](AuthLogoutPost200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="authResetPasswordPost"></a>
# **authResetPasswordPost**
> AuthLogoutPost200Response authResetPasswordPost(authResetPasswordPostRequest)

Send a reset password email

Sends a reset password email to the email if the user exists

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val authResetPasswordPostRequest : AuthResetPasswordPostRequest =  // AuthResetPasswordPostRequest | 
try {
    val result : AuthLogoutPost200Response = apiInstance.authResetPasswordPost(authResetPasswordPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#authResetPasswordPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#authResetPasswordPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **authResetPasswordPostRequest** | [**AuthResetPasswordPostRequest**](AuthResetPasswordPostRequest.md)|  | |

### Return type

[**AuthLogoutPost200Response**](AuthLogoutPost200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="settingsJellyfinUsersGet"></a>
# **settingsJellyfinUsersGet**
> kotlin.collections.List&lt;SettingsJellyfinUsersGet200ResponseInner&gt; settingsJellyfinUsersGet()

Get Jellyfin Users

Returns a list of Jellyfin Users in a JSON array.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
try {
    val result : kotlin.collections.List<SettingsJellyfinUsersGet200ResponseInner> = apiInstance.settingsJellyfinUsersGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#settingsJellyfinUsersGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#settingsJellyfinUsersGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;SettingsJellyfinUsersGet200ResponseInner&gt;**](SettingsJellyfinUsersGet200ResponseInner.md)

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

<a id="settingsPlexUsersGet"></a>
# **settingsPlexUsersGet**
> kotlin.collections.List&lt;SettingsPlexUsersGet200ResponseInner&gt; settingsPlexUsersGet()

Get Plex users

Returns a list of Plex users in a JSON array.  Requires the &#x60;MANAGE_USERS&#x60; permission. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
try {
    val result : kotlin.collections.List<SettingsPlexUsersGet200ResponseInner> = apiInstance.settingsPlexUsersGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#settingsPlexUsersGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#settingsPlexUsersGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;SettingsPlexUsersGet200ResponseInner&gt;**](SettingsPlexUsersGet200ResponseInner.md)

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

<a id="userGet"></a>
# **userGet**
> UserGet200Response userGet(take, skip, sort, q, includeIds)

Get all users

Returns all users in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val take : java.math.BigDecimal = 20 // java.math.BigDecimal | 
val skip : java.math.BigDecimal = 0 // java.math.BigDecimal | 
val sort : kotlin.String = sort_example // kotlin.String | 
val q : kotlin.String = q_example // kotlin.String | 
val includeIds : kotlin.String = includeIds_example // kotlin.String | 
try {
    val result : UserGet200Response = apiInstance.userGet(take, skip, sort, q, includeIds)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userGet")
    e.printStackTrace()
}
```

### Parameters
| **take** | **java.math.BigDecimal**|  | [optional] |
| **skip** | **java.math.BigDecimal**|  | [optional] |
| **sort** | **kotlin.String**|  | [optional] [default to Sort.created] [enum: created, updated, requests, displayname] |
| **q** | **kotlin.String**|  | [optional] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **includeIds** | **kotlin.String**|  | [optional] |

### Return type

[**UserGet200Response**](UserGet200Response.md)

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

<a id="userImportFromJellyfinPost"></a>
# **userImportFromJellyfinPost**
> kotlin.collections.List&lt;User&gt; userImportFromJellyfinPost(userImportFromJellyfinPostRequest)

Import all users from Jellyfin

Fetches and imports users from the Jellyfin server.  Requires the &#x60;MANAGE_USERS&#x60; permission. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userImportFromJellyfinPostRequest : UserImportFromJellyfinPostRequest =  // UserImportFromJellyfinPostRequest | 
try {
    val result : kotlin.collections.List<User> = apiInstance.userImportFromJellyfinPost(userImportFromJellyfinPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userImportFromJellyfinPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userImportFromJellyfinPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userImportFromJellyfinPostRequest** | [**UserImportFromJellyfinPostRequest**](UserImportFromJellyfinPostRequest.md)|  | [optional] |

### Return type

[**kotlin.collections.List&lt;User&gt;**](User.md)

### Authorization


Configure apiKey:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""
Configure cookieAuth:
    ApiClient.apiKey["connect.sid"] = ""
    ApiClient.apiKeyPrefix["connect.sid"] = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="userImportFromPlexPost"></a>
# **userImportFromPlexPost**
> kotlin.collections.List&lt;User&gt; userImportFromPlexPost(userImportFromPlexPostRequest)

Import all users from Plex

Fetches and imports users from the Plex server. If a list of Plex IDs is provided in the request body, only the specified users will be imported. Otherwise, all users will be imported.  Requires the &#x60;MANAGE_USERS&#x60; permission. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userImportFromPlexPostRequest : UserImportFromPlexPostRequest =  // UserImportFromPlexPostRequest | 
try {
    val result : kotlin.collections.List<User> = apiInstance.userImportFromPlexPost(userImportFromPlexPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userImportFromPlexPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userImportFromPlexPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userImportFromPlexPostRequest** | [**UserImportFromPlexPostRequest**](UserImportFromPlexPostRequest.md)|  | [optional] |

### Return type

[**kotlin.collections.List&lt;User&gt;**](User.md)

### Authorization


Configure apiKey:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""
Configure cookieAuth:
    ApiClient.apiKey["connect.sid"] = ""
    ApiClient.apiKeyPrefix["connect.sid"] = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="userPost"></a>
# **userPost**
> User userPost(userPostRequest)

Create new user

Creates a new user. Requires the &#x60;MANAGE_USERS&#x60; permission. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userPostRequest : UserPostRequest =  // UserPostRequest | 
try {
    val result : User = apiInstance.userPost(userPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userPostRequest** | [**UserPostRequest**](UserPostRequest.md)|  | |

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

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="userPut"></a>
# **userPut**
> kotlin.collections.List&lt;User&gt; userPut(userPutRequest)

Update batch of users

Update users with given IDs with provided values in request &#x60;body.settings&#x60;. You cannot update users&#39; Plex tokens through this request.  Requires the &#x60;MANAGE_USERS&#x60; permission. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userPutRequest : UserPutRequest =  // UserPutRequest | 
try {
    val result : kotlin.collections.List<User> = apiInstance.userPut(userPutRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userPut")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userPutRequest** | [**UserPutRequest**](UserPutRequest.md)|  | |

### Return type

[**kotlin.collections.List&lt;User&gt;**](User.md)

### Authorization


Configure apiKey:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""
Configure cookieAuth:
    ApiClient.apiKey["connect.sid"] = ""
    ApiClient.apiKeyPrefix["connect.sid"] = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="userRegisterPushSubscriptionPost"></a>
# **userRegisterPushSubscriptionPost**
> userRegisterPushSubscriptionPost(userRegisterPushSubscriptionPostRequest)

Register a web push /user/registerPushSubscription

Registers a web push subscription for the logged-in user

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userRegisterPushSubscriptionPostRequest : UserRegisterPushSubscriptionPostRequest =  // UserRegisterPushSubscriptionPostRequest | 
try {
    apiInstance.userRegisterPushSubscriptionPost(userRegisterPushSubscriptionPostRequest)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userRegisterPushSubscriptionPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userRegisterPushSubscriptionPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userRegisterPushSubscriptionPostRequest** | [**UserRegisterPushSubscriptionPostRequest**](UserRegisterPushSubscriptionPostRequest.md)|  | |

### Return type

null (empty response body)

### Authorization


Configure apiKey:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""
Configure cookieAuth:
    ApiClient.apiKey["connect.sid"] = ""
    ApiClient.apiKeyPrefix["connect.sid"] = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="userUserIdDelete"></a>
# **userUserIdDelete**
> User userUserIdDelete(userId)

Delete user by ID

Deletes the user with the provided userId. Requires the &#x60;MANAGE_USERS&#x60; permission.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
try {
    val result : User = apiInstance.userUserIdDelete(userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **java.math.BigDecimal**|  | |

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

<a id="userUserIdGet"></a>
# **userUserIdGet**
> User userUserIdGet(userId)

Get user by ID

Retrieves user details in a JSON object. Requires the &#x60;MANAGE_USERS&#x60; permission. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
try {
    val result : User = apiInstance.userUserIdGet(userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **java.math.BigDecimal**|  | |

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

<a id="userUserIdPushSubscriptionEndpointDelete"></a>
# **userUserIdPushSubscriptionEndpointDelete**
> userUserIdPushSubscriptionEndpointDelete(userId, endpoint)

Delete user push subscription by key

Deletes the user push subscription with the provided key.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
val endpoint : kotlin.String = endpoint_example // kotlin.String | 
try {
    apiInstance.userUserIdPushSubscriptionEndpointDelete(userId, endpoint)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdPushSubscriptionEndpointDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdPushSubscriptionEndpointDelete")
    e.printStackTrace()
}
```

### Parameters
| **userId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **endpoint** | **kotlin.String**|  | |

### Return type

null (empty response body)

### Authorization


Configure apiKey:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""
Configure cookieAuth:
    ApiClient.apiKey["connect.sid"] = ""
    ApiClient.apiKeyPrefix["connect.sid"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a id="userUserIdPushSubscriptionEndpointGet"></a>
# **userUserIdPushSubscriptionEndpointGet**
> UserUserIdPushSubscriptionsGet200Response userUserIdPushSubscriptionEndpointGet(userId, endpoint)

Get web push notification settings for a user

Returns web push notification settings for a user in a JSON object. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
val endpoint : kotlin.String = endpoint_example // kotlin.String | 
try {
    val result : UserUserIdPushSubscriptionsGet200Response = apiInstance.userUserIdPushSubscriptionEndpointGet(userId, endpoint)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdPushSubscriptionEndpointGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdPushSubscriptionEndpointGet")
    e.printStackTrace()
}
```

### Parameters
| **userId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **endpoint** | **kotlin.String**|  | |

### Return type

[**UserUserIdPushSubscriptionsGet200Response**](UserUserIdPushSubscriptionsGet200Response.md)

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

<a id="userUserIdPushSubscriptionsGet"></a>
# **userUserIdPushSubscriptionsGet**
> UserUserIdPushSubscriptionsGet200Response userUserIdPushSubscriptionsGet(userId)

Get all web push notification settings for a user

Returns all web push notification settings for a user in a JSON object. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
try {
    val result : UserUserIdPushSubscriptionsGet200Response = apiInstance.userUserIdPushSubscriptionsGet(userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdPushSubscriptionsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdPushSubscriptionsGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **java.math.BigDecimal**|  | |

### Return type

[**UserUserIdPushSubscriptionsGet200Response**](UserUserIdPushSubscriptionsGet200Response.md)

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

<a id="userUserIdPut"></a>
# **userUserIdPut**
> User userUserIdPut(userId, user)

Update a user by user ID

Update a user with the provided values. You cannot update a user&#39;s Plex token through this request.  Requires the &#x60;MANAGE_USERS&#x60; permission. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
val user : User =  // User | 
try {
    val result : User = apiInstance.userUserIdPut(userId, user)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdPut")
    e.printStackTrace()
}
```

### Parameters
| **userId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **user** | [**User**](User.md)|  | |

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

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="userUserIdQuotaGet"></a>
# **userUserIdQuotaGet**
> UserUserIdQuotaGet200Response userUserIdQuotaGet(userId)

Get quotas for a specific user

Returns quota details for a user in a JSON object. Requires &#x60;MANAGE_USERS&#x60; permission if viewing other users. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
try {
    val result : UserUserIdQuotaGet200Response = apiInstance.userUserIdQuotaGet(userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdQuotaGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdQuotaGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **java.math.BigDecimal**|  | |

### Return type

[**UserUserIdQuotaGet200Response**](UserUserIdQuotaGet200Response.md)

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

<a id="userUserIdRequestsGet"></a>
# **userUserIdRequestsGet**
> UserUserIdRequestsGet200Response userUserIdRequestsGet(userId, take, skip)

Get requests for a specific user

Retrieves a user&#39;s requests in a JSON object. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
val take : java.math.BigDecimal = 20 // java.math.BigDecimal | 
val skip : java.math.BigDecimal = 0 // java.math.BigDecimal | 
try {
    val result : UserUserIdRequestsGet200Response = apiInstance.userUserIdRequestsGet(userId, take, skip)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdRequestsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdRequestsGet")
    e.printStackTrace()
}
```

### Parameters
| **userId** | **java.math.BigDecimal**|  | |
| **take** | **java.math.BigDecimal**|  | [optional] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **skip** | **java.math.BigDecimal**|  | [optional] |

### Return type

[**UserUserIdRequestsGet200Response**](UserUserIdRequestsGet200Response.md)

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

<a id="userUserIdSettingsLinkedAccountsJellyfinDelete"></a>
# **userUserIdSettingsLinkedAccountsJellyfinDelete**
> userUserIdSettingsLinkedAccountsJellyfinDelete(userId)

Remove the linked Jellyfin account for a user

Removes the linked Jellyfin account for a specific user. Requires &#x60;MANAGE_USERS&#x60; permission if editing other users.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
try {
    apiInstance.userUserIdSettingsLinkedAccountsJellyfinDelete(userId)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdSettingsLinkedAccountsJellyfinDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdSettingsLinkedAccountsJellyfinDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **java.math.BigDecimal**|  | |

### Return type

null (empty response body)

### Authorization


Configure apiKey:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""
Configure cookieAuth:
    ApiClient.apiKey["connect.sid"] = ""
    ApiClient.apiKeyPrefix["connect.sid"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a id="userUserIdSettingsLinkedAccountsJellyfinPost"></a>
# **userUserIdSettingsLinkedAccountsJellyfinPost**
> userUserIdSettingsLinkedAccountsJellyfinPost(userId, userUserIdSettingsLinkedAccountsJellyfinPostRequest)

Link the provided Jellyfin account to the current user

Logs in to Jellyfin with the provided credentials, then links the associated Jellyfin account with the user&#39;s account. Users can only link external accounts to their own account.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
val userUserIdSettingsLinkedAccountsJellyfinPostRequest : UserUserIdSettingsLinkedAccountsJellyfinPostRequest =  // UserUserIdSettingsLinkedAccountsJellyfinPostRequest | 
try {
    apiInstance.userUserIdSettingsLinkedAccountsJellyfinPost(userId, userUserIdSettingsLinkedAccountsJellyfinPostRequest)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdSettingsLinkedAccountsJellyfinPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdSettingsLinkedAccountsJellyfinPost")
    e.printStackTrace()
}
```

### Parameters
| **userId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userUserIdSettingsLinkedAccountsJellyfinPostRequest** | [**UserUserIdSettingsLinkedAccountsJellyfinPostRequest**](UserUserIdSettingsLinkedAccountsJellyfinPostRequest.md)|  | |

### Return type

null (empty response body)

### Authorization


Configure apiKey:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""
Configure cookieAuth:
    ApiClient.apiKey["connect.sid"] = ""
    ApiClient.apiKeyPrefix["connect.sid"] = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="userUserIdSettingsLinkedAccountsPlexDelete"></a>
# **userUserIdSettingsLinkedAccountsPlexDelete**
> userUserIdSettingsLinkedAccountsPlexDelete(userId)

Remove the linked Plex account for a user

Removes the linked Plex account for a specific user. Requires &#x60;MANAGE_USERS&#x60; permission if editing other users.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
try {
    apiInstance.userUserIdSettingsLinkedAccountsPlexDelete(userId)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdSettingsLinkedAccountsPlexDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdSettingsLinkedAccountsPlexDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **java.math.BigDecimal**|  | |

### Return type

null (empty response body)

### Authorization


Configure apiKey:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""
Configure cookieAuth:
    ApiClient.apiKey["connect.sid"] = ""
    ApiClient.apiKeyPrefix["connect.sid"] = ""

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a id="userUserIdSettingsLinkedAccountsPlexPost"></a>
# **userUserIdSettingsLinkedAccountsPlexPost**
> userUserIdSettingsLinkedAccountsPlexPost(userId, authPlexPostRequest)

Link the provided Plex account to the current user

Logs in to Plex with the provided auth token, then links the associated Plex account with the user&#39;s account. Users can only link external accounts to their own account.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
val authPlexPostRequest : AuthPlexPostRequest =  // AuthPlexPostRequest | 
try {
    apiInstance.userUserIdSettingsLinkedAccountsPlexPost(userId, authPlexPostRequest)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdSettingsLinkedAccountsPlexPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdSettingsLinkedAccountsPlexPost")
    e.printStackTrace()
}
```

### Parameters
| **userId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **authPlexPostRequest** | [**AuthPlexPostRequest**](AuthPlexPostRequest.md)|  | |

### Return type

null (empty response body)

### Authorization


Configure apiKey:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""
Configure cookieAuth:
    ApiClient.apiKey["connect.sid"] = ""
    ApiClient.apiKeyPrefix["connect.sid"] = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="userUserIdSettingsMainGet"></a>
# **userUserIdSettingsMainGet**
> UserSettings userUserIdSettingsMainGet(userId)

Get general settings for a user

Returns general settings for a specific user. Requires &#x60;MANAGE_USERS&#x60; permission if viewing other users.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
try {
    val result : UserSettings = apiInstance.userUserIdSettingsMainGet(userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdSettingsMainGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdSettingsMainGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **java.math.BigDecimal**|  | |

### Return type

[**UserSettings**](UserSettings.md)

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

<a id="userUserIdSettingsMainPost"></a>
# **userUserIdSettingsMainPost**
> UserSettings userUserIdSettingsMainPost(userId, userSettings)

Update general settings for a user

Updates and returns general settings for a specific user. Requires &#x60;MANAGE_USERS&#x60; permission if editing other users.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
val userSettings : UserSettings =  // UserSettings | 
try {
    val result : UserSettings = apiInstance.userUserIdSettingsMainPost(userId, userSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdSettingsMainPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdSettingsMainPost")
    e.printStackTrace()
}
```

### Parameters
| **userId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userSettings** | [**UserSettings**](UserSettings.md)|  | |

### Return type

[**UserSettings**](UserSettings.md)

### Authorization


Configure apiKey:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""
Configure cookieAuth:
    ApiClient.apiKey["connect.sid"] = ""
    ApiClient.apiKeyPrefix["connect.sid"] = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="userUserIdSettingsNotificationsGet"></a>
# **userUserIdSettingsNotificationsGet**
> UserSettingsNotifications userUserIdSettingsNotificationsGet(userId)

Get notification settings for a user

Returns notification settings for a specific user. Requires &#x60;MANAGE_USERS&#x60; permission if viewing other users.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
try {
    val result : UserSettingsNotifications = apiInstance.userUserIdSettingsNotificationsGet(userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdSettingsNotificationsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdSettingsNotificationsGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **java.math.BigDecimal**|  | |

### Return type

[**UserSettingsNotifications**](UserSettingsNotifications.md)

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

<a id="userUserIdSettingsNotificationsPost"></a>
# **userUserIdSettingsNotificationsPost**
> UserSettingsNotifications userUserIdSettingsNotificationsPost(userId, userSettingsNotifications)

Update notification settings for a user

Updates and returns notification settings for a specific user. Requires &#x60;MANAGE_USERS&#x60; permission if editing other users.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
val userSettingsNotifications : UserSettingsNotifications =  // UserSettingsNotifications | 
try {
    val result : UserSettingsNotifications = apiInstance.userUserIdSettingsNotificationsPost(userId, userSettingsNotifications)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdSettingsNotificationsPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdSettingsNotificationsPost")
    e.printStackTrace()
}
```

### Parameters
| **userId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userSettingsNotifications** | [**UserSettingsNotifications**](UserSettingsNotifications.md)|  | |

### Return type

[**UserSettingsNotifications**](UserSettingsNotifications.md)

### Authorization


Configure apiKey:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""
Configure cookieAuth:
    ApiClient.apiKey["connect.sid"] = ""
    ApiClient.apiKeyPrefix["connect.sid"] = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="userUserIdSettingsPasswordGet"></a>
# **userUserIdSettingsPasswordGet**
> UserUserIdSettingsPasswordGet200Response userUserIdSettingsPasswordGet(userId)

Get password page informatiom

Returns important data for the password page to function correctly. Requires &#x60;MANAGE_USERS&#x60; permission if viewing other users.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
try {
    val result : UserUserIdSettingsPasswordGet200Response = apiInstance.userUserIdSettingsPasswordGet(userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdSettingsPasswordGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdSettingsPasswordGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **java.math.BigDecimal**|  | |

### Return type

[**UserUserIdSettingsPasswordGet200Response**](UserUserIdSettingsPasswordGet200Response.md)

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

<a id="userUserIdSettingsPasswordPost"></a>
# **userUserIdSettingsPasswordPost**
> userUserIdSettingsPasswordPost(userId, userUserIdSettingsPasswordPostRequest)

Update password for a user

Updates a user&#39;s password. Requires &#x60;MANAGE_USERS&#x60; permission if editing other users.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
val userUserIdSettingsPasswordPostRequest : UserUserIdSettingsPasswordPostRequest =  // UserUserIdSettingsPasswordPostRequest | 
try {
    apiInstance.userUserIdSettingsPasswordPost(userId, userUserIdSettingsPasswordPostRequest)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdSettingsPasswordPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdSettingsPasswordPost")
    e.printStackTrace()
}
```

### Parameters
| **userId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userUserIdSettingsPasswordPostRequest** | [**UserUserIdSettingsPasswordPostRequest**](UserUserIdSettingsPasswordPostRequest.md)|  | |

### Return type

null (empty response body)

### Authorization


Configure apiKey:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""
Configure cookieAuth:
    ApiClient.apiKey["connect.sid"] = ""
    ApiClient.apiKeyPrefix["connect.sid"] = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="userUserIdSettingsPermissionsGet"></a>
# **userUserIdSettingsPermissionsGet**
> UserUserIdSettingsPermissionsGet200Response userUserIdSettingsPermissionsGet(userId)

Get permission settings for a user

Returns permission settings for a specific user. Requires &#x60;MANAGE_USERS&#x60; permission if viewing other users.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
try {
    val result : UserUserIdSettingsPermissionsGet200Response = apiInstance.userUserIdSettingsPermissionsGet(userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdSettingsPermissionsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdSettingsPermissionsGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **java.math.BigDecimal**|  | |

### Return type

[**UserUserIdSettingsPermissionsGet200Response**](UserUserIdSettingsPermissionsGet200Response.md)

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

<a id="userUserIdSettingsPermissionsPost"></a>
# **userUserIdSettingsPermissionsPost**
> UserUserIdSettingsPermissionsGet200Response userUserIdSettingsPermissionsPost(userId, userUserIdSettingsPermissionsPostRequest)

Update permission settings for a user

Updates and returns permission settings for a specific user. Requires &#x60;MANAGE_USERS&#x60; permission if editing other users.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
val userUserIdSettingsPermissionsPostRequest : UserUserIdSettingsPermissionsPostRequest =  // UserUserIdSettingsPermissionsPostRequest | 
try {
    val result : UserUserIdSettingsPermissionsGet200Response = apiInstance.userUserIdSettingsPermissionsPost(userId, userUserIdSettingsPermissionsPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdSettingsPermissionsPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdSettingsPermissionsPost")
    e.printStackTrace()
}
```

### Parameters
| **userId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userUserIdSettingsPermissionsPostRequest** | [**UserUserIdSettingsPermissionsPostRequest**](UserUserIdSettingsPermissionsPostRequest.md)|  | |

### Return type

[**UserUserIdSettingsPermissionsGet200Response**](UserUserIdSettingsPermissionsGet200Response.md)

### Authorization


Configure apiKey:
    ApiClient.apiKey["X-Api-Key"] = ""
    ApiClient.apiKeyPrefix["X-Api-Key"] = ""
Configure cookieAuth:
    ApiClient.apiKey["connect.sid"] = ""
    ApiClient.apiKeyPrefix["connect.sid"] = ""

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="userUserIdWatchDataGet"></a>
# **userUserIdWatchDataGet**
> UserUserIdWatchDataGet200Response userUserIdWatchDataGet(userId)

Get watch data

Returns play count, play duration, and recently watched media.  Requires the &#x60;ADMIN&#x60; permission to fetch results for other users. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
try {
    val result : UserUserIdWatchDataGet200Response = apiInstance.userUserIdWatchDataGet(userId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdWatchDataGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdWatchDataGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **java.math.BigDecimal**|  | |

### Return type

[**UserUserIdWatchDataGet200Response**](UserUserIdWatchDataGet200Response.md)

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

<a id="userUserIdWatchlistGet"></a>
# **userUserIdWatchlistGet**
> UserUserIdWatchlistGet200Response userUserIdWatchlistGet(userId, page)

Get the Plex watchlist for a specific user

Retrieves a user&#39;s Plex Watchlist in a JSON object. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = UsersApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
try {
    val result : UserUserIdWatchlistGet200Response = apiInstance.userUserIdWatchlistGet(userId, page)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling UsersApi#userUserIdWatchlistGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling UsersApi#userUserIdWatchlistGet")
    e.printStackTrace()
}
```

### Parameters
| **userId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |

### Return type

[**UserUserIdWatchlistGet200Response**](UserUserIdWatchlistGet200Response.md)

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

