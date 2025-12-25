# WatchlistApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**userUserIdWatchlistGet**](WatchlistApi.md#userUserIdWatchlistGet) | **GET** /user/{userId}/watchlist | Get the Plex watchlist for a specific user |
| [**watchlistPost**](WatchlistApi.md#watchlistPost) | **POST** /watchlist | Add media to watchlist |
| [**watchlistTmdbIdDelete**](WatchlistApi.md#watchlistTmdbIdDelete) | **DELETE** /watchlist/{tmdbId} | Delete watchlist item |


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

val apiInstance = WatchlistApi()
val userId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
try {
    val result : UserUserIdWatchlistGet200Response = apiInstance.userUserIdWatchlistGet(userId, page)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling WatchlistApi#userUserIdWatchlistGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling WatchlistApi#userUserIdWatchlistGet")
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

<a id="watchlistPost"></a>
# **watchlistPost**
> Watchlist watchlistPost(watchlist)

Add media to watchlist

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = WatchlistApi()
val watchlist : Watchlist =  // Watchlist | 
try {
    val result : Watchlist = apiInstance.watchlistPost(watchlist)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling WatchlistApi#watchlistPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling WatchlistApi#watchlistPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **watchlist** | [**Watchlist**](Watchlist.md)|  | |

### Return type

[**Watchlist**](Watchlist.md)

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

<a id="watchlistTmdbIdDelete"></a>
# **watchlistTmdbIdDelete**
> watchlistTmdbIdDelete(tmdbId)

Delete watchlist item

Removes a watchlist item.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = WatchlistApi()
val tmdbId : kotlin.String = 1 // kotlin.String | tmdbId ID
try {
    apiInstance.watchlistTmdbIdDelete(tmdbId)
} catch (e: ClientException) {
    println("4xx response calling WatchlistApi#watchlistTmdbIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling WatchlistApi#watchlistTmdbIdDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **tmdbId** | **kotlin.String**| tmdbId ID | |

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

