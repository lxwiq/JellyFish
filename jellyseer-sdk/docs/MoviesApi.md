# MoviesApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**movieMovieIdGet**](MoviesApi.md#movieMovieIdGet) | **GET** /movie/{movieId} | Get movie details |
| [**movieMovieIdRatingsGet**](MoviesApi.md#movieMovieIdRatingsGet) | **GET** /movie/{movieId}/ratings | Get movie ratings |
| [**movieMovieIdRatingscombinedGet**](MoviesApi.md#movieMovieIdRatingscombinedGet) | **GET** /movie/{movieId}/ratingscombined | Get RT and IMDB movie ratings combined |
| [**movieMovieIdRecommendationsGet**](MoviesApi.md#movieMovieIdRecommendationsGet) | **GET** /movie/{movieId}/recommendations | Get recommended movies |
| [**movieMovieIdSimilarGet**](MoviesApi.md#movieMovieIdSimilarGet) | **GET** /movie/{movieId}/similar | Get similar movies |


<a id="movieMovieIdGet"></a>
# **movieMovieIdGet**
> MovieDetails movieMovieIdGet(movieId, language)

Get movie details

Returns full movie details in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = MoviesApi()
val movieId : java.math.BigDecimal = 337401 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : MovieDetails = apiInstance.movieMovieIdGet(movieId, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MoviesApi#movieMovieIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MoviesApi#movieMovieIdGet")
    e.printStackTrace()
}
```

### Parameters
| **movieId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**MovieDetails**](MovieDetails.md)

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

<a id="movieMovieIdRatingsGet"></a>
# **movieMovieIdRatingsGet**
> MovieMovieIdRatingsGet200Response movieMovieIdRatingsGet(movieId)

Get movie ratings

Returns ratings based on the provided movieId in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = MoviesApi()
val movieId : java.math.BigDecimal = 337401 // java.math.BigDecimal | 
try {
    val result : MovieMovieIdRatingsGet200Response = apiInstance.movieMovieIdRatingsGet(movieId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MoviesApi#movieMovieIdRatingsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MoviesApi#movieMovieIdRatingsGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **movieId** | **java.math.BigDecimal**|  | |

### Return type

[**MovieMovieIdRatingsGet200Response**](MovieMovieIdRatingsGet200Response.md)

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

<a id="movieMovieIdRatingscombinedGet"></a>
# **movieMovieIdRatingscombinedGet**
> MovieMovieIdRatingscombinedGet200Response movieMovieIdRatingscombinedGet(movieId)

Get RT and IMDB movie ratings combined

Returns ratings from RottenTomatoes and IMDB based on the provided movieId in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = MoviesApi()
val movieId : java.math.BigDecimal = 337401 // java.math.BigDecimal | 
try {
    val result : MovieMovieIdRatingscombinedGet200Response = apiInstance.movieMovieIdRatingscombinedGet(movieId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MoviesApi#movieMovieIdRatingscombinedGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MoviesApi#movieMovieIdRatingscombinedGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **movieId** | **java.math.BigDecimal**|  | |

### Return type

[**MovieMovieIdRatingscombinedGet200Response**](MovieMovieIdRatingscombinedGet200Response.md)

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

<a id="movieMovieIdRecommendationsGet"></a>
# **movieMovieIdRecommendationsGet**
> DiscoverMoviesGet200Response movieMovieIdRecommendationsGet(movieId, page, language)

Get recommended movies

Returns list of recommended movies based on provided movie ID in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = MoviesApi()
val movieId : java.math.BigDecimal = 337401 // java.math.BigDecimal | 
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : DiscoverMoviesGet200Response = apiInstance.movieMovieIdRecommendationsGet(movieId, page, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MoviesApi#movieMovieIdRecommendationsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MoviesApi#movieMovieIdRecommendationsGet")
    e.printStackTrace()
}
```

### Parameters
| **movieId** | **java.math.BigDecimal**|  | |
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**DiscoverMoviesGet200Response**](DiscoverMoviesGet200Response.md)

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

<a id="movieMovieIdSimilarGet"></a>
# **movieMovieIdSimilarGet**
> DiscoverMoviesGet200Response movieMovieIdSimilarGet(movieId, page, language)

Get similar movies

Returns list of similar movies based on the provided movieId in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = MoviesApi()
val movieId : java.math.BigDecimal = 337401 // java.math.BigDecimal | 
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : DiscoverMoviesGet200Response = apiInstance.movieMovieIdSimilarGet(movieId, page, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MoviesApi#movieMovieIdSimilarGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MoviesApi#movieMovieIdSimilarGet")
    e.printStackTrace()
}
```

### Parameters
| **movieId** | **java.math.BigDecimal**|  | |
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**DiscoverMoviesGet200Response**](DiscoverMoviesGet200Response.md)

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

