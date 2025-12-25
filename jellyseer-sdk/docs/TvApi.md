# TvApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**tvTvIdGet**](TvApi.md#tvTvIdGet) | **GET** /tv/{tvId} | Get TV details |
| [**tvTvIdRatingsGet**](TvApi.md#tvTvIdRatingsGet) | **GET** /tv/{tvId}/ratings | Get TV ratings |
| [**tvTvIdRecommendationsGet**](TvApi.md#tvTvIdRecommendationsGet) | **GET** /tv/{tvId}/recommendations | Get recommended TV series |
| [**tvTvIdSeasonSeasonNumberGet**](TvApi.md#tvTvIdSeasonSeasonNumberGet) | **GET** /tv/{tvId}/season/{seasonNumber} | Get season details and episode list |
| [**tvTvIdSimilarGet**](TvApi.md#tvTvIdSimilarGet) | **GET** /tv/{tvId}/similar | Get similar TV series |


<a id="tvTvIdGet"></a>
# **tvTvIdGet**
> TvDetails tvTvIdGet(tvId, language)

Get TV details

Returns full TV details in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = TvApi()
val tvId : java.math.BigDecimal = 76479 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : TvDetails = apiInstance.tvTvIdGet(tvId, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TvApi#tvTvIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TvApi#tvTvIdGet")
    e.printStackTrace()
}
```

### Parameters
| **tvId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**TvDetails**](TvDetails.md)

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

<a id="tvTvIdRatingsGet"></a>
# **tvTvIdRatingsGet**
> TvTvIdRatingsGet200Response tvTvIdRatingsGet(tvId)

Get TV ratings

Returns ratings based on provided tvId in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = TvApi()
val tvId : java.math.BigDecimal = 76479 // java.math.BigDecimal | 
try {
    val result : TvTvIdRatingsGet200Response = apiInstance.tvTvIdRatingsGet(tvId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TvApi#tvTvIdRatingsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TvApi#tvTvIdRatingsGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **tvId** | **java.math.BigDecimal**|  | |

### Return type

[**TvTvIdRatingsGet200Response**](TvTvIdRatingsGet200Response.md)

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

<a id="tvTvIdRecommendationsGet"></a>
# **tvTvIdRecommendationsGet**
> DiscoverTvGet200Response tvTvIdRecommendationsGet(tvId, page, language)

Get recommended TV series

Returns list of recommended TV series based on the provided tvId in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = TvApi()
val tvId : java.math.BigDecimal = 76479 // java.math.BigDecimal | 
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : DiscoverTvGet200Response = apiInstance.tvTvIdRecommendationsGet(tvId, page, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TvApi#tvTvIdRecommendationsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TvApi#tvTvIdRecommendationsGet")
    e.printStackTrace()
}
```

### Parameters
| **tvId** | **java.math.BigDecimal**|  | |
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**DiscoverTvGet200Response**](DiscoverTvGet200Response.md)

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

<a id="tvTvIdSeasonSeasonNumberGet"></a>
# **tvTvIdSeasonSeasonNumberGet**
> Season tvTvIdSeasonSeasonNumberGet(tvId, seasonNumber, language)

Get season details and episode list

Returns season details with a list of episodes in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = TvApi()
val tvId : java.math.BigDecimal = 76479 // java.math.BigDecimal | 
val seasonNumber : java.math.BigDecimal = 123456 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : Season = apiInstance.tvTvIdSeasonSeasonNumberGet(tvId, seasonNumber, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TvApi#tvTvIdSeasonSeasonNumberGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TvApi#tvTvIdSeasonSeasonNumberGet")
    e.printStackTrace()
}
```

### Parameters
| **tvId** | **java.math.BigDecimal**|  | |
| **seasonNumber** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**Season**](Season.md)

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

<a id="tvTvIdSimilarGet"></a>
# **tvTvIdSimilarGet**
> DiscoverTvGet200Response tvTvIdSimilarGet(tvId, page, language)

Get similar TV series

Returns list of similar TV series based on the provided tvId in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = TvApi()
val tvId : java.math.BigDecimal = 76479 // java.math.BigDecimal | 
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : DiscoverTvGet200Response = apiInstance.tvTvIdSimilarGet(tvId, page, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TvApi#tvTvIdSimilarGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TvApi#tvTvIdSimilarGet")
    e.printStackTrace()
}
```

### Parameters
| **tvId** | **java.math.BigDecimal**|  | |
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**DiscoverTvGet200Response**](DiscoverTvGet200Response.md)

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

