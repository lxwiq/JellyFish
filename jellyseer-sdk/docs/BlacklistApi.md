# BlacklistApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**blacklistPost**](BlacklistApi.md#blacklistPost) | **POST** /blacklist | Add media to blacklist |
| [**blacklistTmdbIdDelete**](BlacklistApi.md#blacklistTmdbIdDelete) | **DELETE** /blacklist/{tmdbId} | Remove media from blacklist |
| [**blacklistTmdbIdGet**](BlacklistApi.md#blacklistTmdbIdGet) | **GET** /blacklist/{tmdbId} | Get media from blacklist |


<a id="blacklistPost"></a>
# **blacklistPost**
> blacklistPost(blacklist)

Add media to blacklist

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = BlacklistApi()
val blacklist : Blacklist =  // Blacklist | 
try {
    apiInstance.blacklistPost(blacklist)
} catch (e: ClientException) {
    println("4xx response calling BlacklistApi#blacklistPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling BlacklistApi#blacklistPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **blacklist** | [**Blacklist**](Blacklist.md)|  | |

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

<a id="blacklistTmdbIdDelete"></a>
# **blacklistTmdbIdDelete**
> blacklistTmdbIdDelete(tmdbId)

Remove media from blacklist

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = BlacklistApi()
val tmdbId : kotlin.String = 1 // kotlin.String | tmdbId ID
try {
    apiInstance.blacklistTmdbIdDelete(tmdbId)
} catch (e: ClientException) {
    println("4xx response calling BlacklistApi#blacklistTmdbIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling BlacklistApi#blacklistTmdbIdDelete")
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

<a id="blacklistTmdbIdGet"></a>
# **blacklistTmdbIdGet**
> blacklistTmdbIdGet(tmdbId)

Get media from blacklist

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = BlacklistApi()
val tmdbId : kotlin.String = 1 // kotlin.String | tmdbId ID
try {
    apiInstance.blacklistTmdbIdGet(tmdbId)
} catch (e: ClientException) {
    println("4xx response calling BlacklistApi#blacklistTmdbIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling BlacklistApi#blacklistTmdbIdGet")
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

