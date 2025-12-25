# MediaApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**mediaGet**](MediaApi.md#mediaGet) | **GET** /media | Get media |
| [**mediaMediaIdDelete**](MediaApi.md#mediaMediaIdDelete) | **DELETE** /media/{mediaId} | Delete media item |
| [**mediaMediaIdFileDelete**](MediaApi.md#mediaMediaIdFileDelete) | **DELETE** /media/{mediaId}/file | Delete media file |
| [**mediaMediaIdStatusPost**](MediaApi.md#mediaMediaIdStatusPost) | **POST** /media/{mediaId}/{status} | Update media status |
| [**mediaMediaIdWatchDataGet**](MediaApi.md#mediaMediaIdWatchDataGet) | **GET** /media/{mediaId}/watch_data | Get watch data |


<a id="mediaGet"></a>
# **mediaGet**
> MediaGet200Response mediaGet(take, skip, filter, sort)

Get media

Returns all media (can be filtered and limited) in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = MediaApi()
val take : java.math.BigDecimal = 20 // java.math.BigDecimal | 
val skip : java.math.BigDecimal = 0 // java.math.BigDecimal | 
val filter : kotlin.String = filter_example // kotlin.String | 
val sort : kotlin.String = sort_example // kotlin.String | 
try {
    val result : MediaGet200Response = apiInstance.mediaGet(take, skip, filter, sort)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MediaApi#mediaGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MediaApi#mediaGet")
    e.printStackTrace()
}
```

### Parameters
| **take** | **java.math.BigDecimal**|  | [optional] |
| **skip** | **java.math.BigDecimal**|  | [optional] |
| **filter** | **kotlin.String**|  | [optional] [enum: all, available, partial, allavailable, processing, pending, deleted] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sort** | **kotlin.String**|  | [optional] [default to Sort.added] [enum: added, modified, mediaAdded] |

### Return type

[**MediaGet200Response**](MediaGet200Response.md)

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

<a id="mediaMediaIdDelete"></a>
# **mediaMediaIdDelete**
> mediaMediaIdDelete(mediaId)

Delete media item

Removes a media item. The &#x60;MANAGE_REQUESTS&#x60; permission is required to perform this action.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = MediaApi()
val mediaId : kotlin.String = 1 // kotlin.String | Media ID
try {
    apiInstance.mediaMediaIdDelete(mediaId)
} catch (e: ClientException) {
    println("4xx response calling MediaApi#mediaMediaIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MediaApi#mediaMediaIdDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **mediaId** | **kotlin.String**| Media ID | |

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

<a id="mediaMediaIdFileDelete"></a>
# **mediaMediaIdFileDelete**
> mediaMediaIdFileDelete(mediaId, is4k)

Delete media file

Removes a media file from radarr/sonarr. The &#x60;ADMIN&#x60; permission is required to perform this action.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = MediaApi()
val mediaId : kotlin.String = 1 // kotlin.String | Media ID
val is4k : kotlin.Boolean = false // kotlin.Boolean | Whether to remove from 4K service instance (true) or regular service instance (false)
try {
    apiInstance.mediaMediaIdFileDelete(mediaId, is4k)
} catch (e: ClientException) {
    println("4xx response calling MediaApi#mediaMediaIdFileDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MediaApi#mediaMediaIdFileDelete")
    e.printStackTrace()
}
```

### Parameters
| **mediaId** | **kotlin.String**| Media ID | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **is4k** | **kotlin.Boolean**| Whether to remove from 4K service instance (true) or regular service instance (false) | [optional] |

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

<a id="mediaMediaIdStatusPost"></a>
# **mediaMediaIdStatusPost**
> MediaInfo mediaMediaIdStatusPost(mediaId, status, mediaMediaIdStatusPostRequest)

Update media status

Updates a media item&#39;s status and returns the media in JSON format

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = MediaApi()
val mediaId : kotlin.String = 1 // kotlin.String | Media ID
val status : kotlin.String = available // kotlin.String | New status
val mediaMediaIdStatusPostRequest : MediaMediaIdStatusPostRequest =  // MediaMediaIdStatusPostRequest | 
try {
    val result : MediaInfo = apiInstance.mediaMediaIdStatusPost(mediaId, status, mediaMediaIdStatusPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MediaApi#mediaMediaIdStatusPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MediaApi#mediaMediaIdStatusPost")
    e.printStackTrace()
}
```

### Parameters
| **mediaId** | **kotlin.String**| Media ID | |
| **status** | **kotlin.String**| New status | [enum: available, partial, processing, pending, unknown, deleted] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **mediaMediaIdStatusPostRequest** | [**MediaMediaIdStatusPostRequest**](MediaMediaIdStatusPostRequest.md)|  | [optional] |

### Return type

[**MediaInfo**](MediaInfo.md)

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

<a id="mediaMediaIdWatchDataGet"></a>
# **mediaMediaIdWatchDataGet**
> MediaMediaIdWatchDataGet200Response mediaMediaIdWatchDataGet(mediaId)

Get watch data

Returns play count, play duration, and users who have watched the media.  Requires the &#x60;ADMIN&#x60; permission. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = MediaApi()
val mediaId : kotlin.String = 1 // kotlin.String | Media ID
try {
    val result : MediaMediaIdWatchDataGet200Response = apiInstance.mediaMediaIdWatchDataGet(mediaId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MediaApi#mediaMediaIdWatchDataGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MediaApi#mediaMediaIdWatchDataGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **mediaId** | **kotlin.String**| Media ID | |

### Return type

[**MediaMediaIdWatchDataGet200Response**](MediaMediaIdWatchDataGet200Response.md)

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

