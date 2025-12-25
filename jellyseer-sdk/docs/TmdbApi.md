# TmdbApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**backdropsGet**](TmdbApi.md#backdropsGet) | **GET** /backdrops | Get backdrops of trending items |
| [**genresMovieGet**](TmdbApi.md#genresMovieGet) | **GET** /genres/movie | Get list of official TMDB movie genres |
| [**genresTvGet**](TmdbApi.md#genresTvGet) | **GET** /genres/tv | Get list of official TMDB movie genres |
| [**languagesGet**](TmdbApi.md#languagesGet) | **GET** /languages | Languages supported by TMDB |
| [**networkNetworkIdGet**](TmdbApi.md#networkNetworkIdGet) | **GET** /network/{networkId} | Get TV network details |
| [**regionsGet**](TmdbApi.md#regionsGet) | **GET** /regions | Regions supported by TMDB |
| [**studioStudioIdGet**](TmdbApi.md#studioStudioIdGet) | **GET** /studio/{studioId} | Get movie studio details |


<a id="backdropsGet"></a>
# **backdropsGet**
> kotlin.collections.List&lt;kotlin.String&gt; backdropsGet()

Get backdrops of trending items

Returns a list of backdrop image paths in a JSON array.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = TmdbApi()
try {
    val result : kotlin.collections.List<kotlin.String> = apiInstance.backdropsGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TmdbApi#backdropsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TmdbApi#backdropsGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**kotlin.collections.List&lt;kotlin.String&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="genresMovieGet"></a>
# **genresMovieGet**
> kotlin.collections.List&lt;GenresMovieGet200ResponseInner&gt; genresMovieGet(language)

Get list of official TMDB movie genres

Returns a list of genres in a JSON array.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = TmdbApi()
val language : kotlin.String = en // kotlin.String | 
try {
    val result : kotlin.collections.List<GenresMovieGet200ResponseInner> = apiInstance.genresMovieGet(language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TmdbApi#genresMovieGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TmdbApi#genresMovieGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**kotlin.collections.List&lt;GenresMovieGet200ResponseInner&gt;**](GenresMovieGet200ResponseInner.md)

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

<a id="genresTvGet"></a>
# **genresTvGet**
> kotlin.collections.List&lt;GenresTvGet200ResponseInner&gt; genresTvGet(language)

Get list of official TMDB movie genres

Returns a list of genres in a JSON array.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = TmdbApi()
val language : kotlin.String = en // kotlin.String | 
try {
    val result : kotlin.collections.List<GenresTvGet200ResponseInner> = apiInstance.genresTvGet(language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TmdbApi#genresTvGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TmdbApi#genresTvGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**kotlin.collections.List&lt;GenresTvGet200ResponseInner&gt;**](GenresTvGet200ResponseInner.md)

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

<a id="languagesGet"></a>
# **languagesGet**
> kotlin.collections.List&lt;LanguagesGet200ResponseInner&gt; languagesGet()

Languages supported by TMDB

Returns a list of languages in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = TmdbApi()
try {
    val result : kotlin.collections.List<LanguagesGet200ResponseInner> = apiInstance.languagesGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TmdbApi#languagesGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TmdbApi#languagesGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;LanguagesGet200ResponseInner&gt;**](LanguagesGet200ResponseInner.md)

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

<a id="networkNetworkIdGet"></a>
# **networkNetworkIdGet**
> ProductionCompany networkNetworkIdGet(networkId)

Get TV network details

Returns TV network details in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = TmdbApi()
val networkId : java.math.BigDecimal = 1 // java.math.BigDecimal | 
try {
    val result : ProductionCompany = apiInstance.networkNetworkIdGet(networkId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TmdbApi#networkNetworkIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TmdbApi#networkNetworkIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **networkId** | **java.math.BigDecimal**|  | |

### Return type

[**ProductionCompany**](ProductionCompany.md)

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

<a id="regionsGet"></a>
# **regionsGet**
> kotlin.collections.List&lt;RegionsGet200ResponseInner&gt; regionsGet()

Regions supported by TMDB

Returns a list of regions in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = TmdbApi()
try {
    val result : kotlin.collections.List<RegionsGet200ResponseInner> = apiInstance.regionsGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TmdbApi#regionsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TmdbApi#regionsGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;RegionsGet200ResponseInner&gt;**](RegionsGet200ResponseInner.md)

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

<a id="studioStudioIdGet"></a>
# **studioStudioIdGet**
> ProductionCompany studioStudioIdGet(studioId)

Get movie studio details

Returns movie studio details in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = TmdbApi()
val studioId : java.math.BigDecimal = 2 // java.math.BigDecimal | 
try {
    val result : ProductionCompany = apiInstance.studioStudioIdGet(studioId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TmdbApi#studioStudioIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TmdbApi#studioStudioIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **studioId** | **java.math.BigDecimal**|  | |

### Return type

[**ProductionCompany**](ProductionCompany.md)

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

