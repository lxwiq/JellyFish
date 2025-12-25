# OtherApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**certificationsMovieGet**](OtherApi.md#certificationsMovieGet) | **GET** /certifications/movie | Get movie certifications |
| [**certificationsTvGet**](OtherApi.md#certificationsTvGet) | **GET** /certifications/tv | Get TV certifications |
| [**keywordKeywordIdGet**](OtherApi.md#keywordKeywordIdGet) | **GET** /keyword/{keywordId} | Get keyword |
| [**watchprovidersMoviesGet**](OtherApi.md#watchprovidersMoviesGet) | **GET** /watchproviders/movies | Get watch provider movies |
| [**watchprovidersRegionsGet**](OtherApi.md#watchprovidersRegionsGet) | **GET** /watchproviders/regions | Get watch provider regions |
| [**watchprovidersTvGet**](OtherApi.md#watchprovidersTvGet) | **GET** /watchproviders/tv | Get watch provider series |


<a id="certificationsMovieGet"></a>
# **certificationsMovieGet**
> CertificationResponse certificationsMovieGet()

Get movie certifications

Returns list of movie certifications from TMDB.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = OtherApi()
try {
    val result : CertificationResponse = apiInstance.certificationsMovieGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OtherApi#certificationsMovieGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OtherApi#certificationsMovieGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**CertificationResponse**](CertificationResponse.md)

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

<a id="certificationsTvGet"></a>
# **certificationsTvGet**
> CertificationResponse certificationsTvGet()

Get TV certifications

Returns list of TV show certifications from TMDB.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = OtherApi()
try {
    val result : CertificationResponse = apiInstance.certificationsTvGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OtherApi#certificationsTvGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OtherApi#certificationsTvGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**CertificationResponse**](CertificationResponse.md)

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

<a id="keywordKeywordIdGet"></a>
# **keywordKeywordIdGet**
> Keyword keywordKeywordIdGet(keywordId)

Get keyword

Returns a single keyword in JSON format. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = OtherApi()
val keywordId : java.math.BigDecimal = 1 // java.math.BigDecimal | 
try {
    val result : Keyword = apiInstance.keywordKeywordIdGet(keywordId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OtherApi#keywordKeywordIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OtherApi#keywordKeywordIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **keywordId** | **java.math.BigDecimal**|  | |

### Return type

[**Keyword**](Keyword.md)

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

<a id="watchprovidersMoviesGet"></a>
# **watchprovidersMoviesGet**
> kotlin.collections.List&lt;WatchProviderDetails&gt; watchprovidersMoviesGet(watchRegion)

Get watch provider movies

Returns a list of all available watch providers for movies. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = OtherApi()
val watchRegion : kotlin.String = US // kotlin.String | 
try {
    val result : kotlin.collections.List<WatchProviderDetails> = apiInstance.watchprovidersMoviesGet(watchRegion)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OtherApi#watchprovidersMoviesGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OtherApi#watchprovidersMoviesGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **watchRegion** | **kotlin.String**|  | |

### Return type

[**kotlin.collections.List&lt;WatchProviderDetails&gt;**](WatchProviderDetails.md)

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

<a id="watchprovidersRegionsGet"></a>
# **watchprovidersRegionsGet**
> kotlin.collections.List&lt;WatchProviderRegion&gt; watchprovidersRegionsGet()

Get watch provider regions

Returns a list of all available watch provider regions. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = OtherApi()
try {
    val result : kotlin.collections.List<WatchProviderRegion> = apiInstance.watchprovidersRegionsGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OtherApi#watchprovidersRegionsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OtherApi#watchprovidersRegionsGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;WatchProviderRegion&gt;**](WatchProviderRegion.md)

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

<a id="watchprovidersTvGet"></a>
# **watchprovidersTvGet**
> kotlin.collections.List&lt;WatchProviderDetails&gt; watchprovidersTvGet(watchRegion)

Get watch provider series

Returns a list of all available watch providers for series. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = OtherApi()
val watchRegion : kotlin.String = US // kotlin.String | 
try {
    val result : kotlin.collections.List<WatchProviderDetails> = apiInstance.watchprovidersTvGet(watchRegion)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OtherApi#watchprovidersTvGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OtherApi#watchprovidersTvGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **watchRegion** | **kotlin.String**|  | |

### Return type

[**kotlin.collections.List&lt;WatchProviderDetails&gt;**](WatchProviderDetails.md)

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

