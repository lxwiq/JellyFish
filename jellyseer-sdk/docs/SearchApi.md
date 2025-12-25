# SearchApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**discoverGenresliderMovieGet**](SearchApi.md#discoverGenresliderMovieGet) | **GET** /discover/genreslider/movie | Get genre slider data for movies |
| [**discoverGenresliderTvGet**](SearchApi.md#discoverGenresliderTvGet) | **GET** /discover/genreslider/tv | Get genre slider data for TV series |
| [**discoverKeywordKeywordIdMoviesGet**](SearchApi.md#discoverKeywordKeywordIdMoviesGet) | **GET** /discover/keyword/{keywordId}/movies | Get movies from keyword |
| [**discoverMoviesGenreGenreIdGet**](SearchApi.md#discoverMoviesGenreGenreIdGet) | **GET** /discover/movies/genre/{genreId} | Discover movies by genre |
| [**discoverMoviesGet**](SearchApi.md#discoverMoviesGet) | **GET** /discover/movies | Discover movies |
| [**discoverMoviesLanguageLanguageGet**](SearchApi.md#discoverMoviesLanguageLanguageGet) | **GET** /discover/movies/language/{language} | Discover movies by original language |
| [**discoverMoviesStudioStudioIdGet**](SearchApi.md#discoverMoviesStudioStudioIdGet) | **GET** /discover/movies/studio/{studioId} | Discover movies by studio |
| [**discoverMoviesUpcomingGet**](SearchApi.md#discoverMoviesUpcomingGet) | **GET** /discover/movies/upcoming | Upcoming movies |
| [**discoverTrendingGet**](SearchApi.md#discoverTrendingGet) | **GET** /discover/trending | Trending movies and TV |
| [**discoverTvGenreGenreIdGet**](SearchApi.md#discoverTvGenreGenreIdGet) | **GET** /discover/tv/genre/{genreId} | Discover TV shows by genre |
| [**discoverTvGet**](SearchApi.md#discoverTvGet) | **GET** /discover/tv | Discover TV shows |
| [**discoverTvLanguageLanguageGet**](SearchApi.md#discoverTvLanguageLanguageGet) | **GET** /discover/tv/language/{language} | Discover TV shows by original language |
| [**discoverTvNetworkNetworkIdGet**](SearchApi.md#discoverTvNetworkNetworkIdGet) | **GET** /discover/tv/network/{networkId} | Discover TV shows by network |
| [**discoverTvUpcomingGet**](SearchApi.md#discoverTvUpcomingGet) | **GET** /discover/tv/upcoming | Discover Upcoming TV shows |
| [**discoverWatchlistGet**](SearchApi.md#discoverWatchlistGet) | **GET** /discover/watchlist | Get the Plex watchlist. |
| [**searchCompanyGet**](SearchApi.md#searchCompanyGet) | **GET** /search/company | Search for companies |
| [**searchGet**](SearchApi.md#searchGet) | **GET** /search | Search for movies, TV shows, or people |
| [**searchKeywordGet**](SearchApi.md#searchKeywordGet) | **GET** /search/keyword | Search for keywords |


<a id="discoverGenresliderMovieGet"></a>
# **discoverGenresliderMovieGet**
> kotlin.collections.List&lt;DiscoverGenresliderMovieGet200ResponseInner&gt; discoverGenresliderMovieGet(language)

Get genre slider data for movies

Returns a list of genres with backdrops attached

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val language : kotlin.String = en // kotlin.String | 
try {
    val result : kotlin.collections.List<DiscoverGenresliderMovieGet200ResponseInner> = apiInstance.discoverGenresliderMovieGet(language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#discoverGenresliderMovieGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#discoverGenresliderMovieGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**kotlin.collections.List&lt;DiscoverGenresliderMovieGet200ResponseInner&gt;**](DiscoverGenresliderMovieGet200ResponseInner.md)

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

<a id="discoverGenresliderTvGet"></a>
# **discoverGenresliderTvGet**
> kotlin.collections.List&lt;DiscoverGenresliderMovieGet200ResponseInner&gt; discoverGenresliderTvGet(language)

Get genre slider data for TV series

Returns a list of genres with backdrops attached

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val language : kotlin.String = en // kotlin.String | 
try {
    val result : kotlin.collections.List<DiscoverGenresliderMovieGet200ResponseInner> = apiInstance.discoverGenresliderTvGet(language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#discoverGenresliderTvGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#discoverGenresliderTvGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**kotlin.collections.List&lt;DiscoverGenresliderMovieGet200ResponseInner&gt;**](DiscoverGenresliderMovieGet200ResponseInner.md)

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

<a id="discoverKeywordKeywordIdMoviesGet"></a>
# **discoverKeywordKeywordIdMoviesGet**
> DiscoverMoviesGet200Response discoverKeywordKeywordIdMoviesGet(keywordId, page, language)

Get movies from keyword

Returns list of movies based on the provided keyword ID a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val keywordId : java.math.BigDecimal = 207317 // java.math.BigDecimal | 
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : DiscoverMoviesGet200Response = apiInstance.discoverKeywordKeywordIdMoviesGet(keywordId, page, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#discoverKeywordKeywordIdMoviesGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#discoverKeywordKeywordIdMoviesGet")
    e.printStackTrace()
}
```

### Parameters
| **keywordId** | **java.math.BigDecimal**|  | |
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

<a id="discoverMoviesGenreGenreIdGet"></a>
# **discoverMoviesGenreGenreIdGet**
> DiscoverMoviesGenreGenreIdGet200Response discoverMoviesGenreGenreIdGet(genreId, page, language)

Discover movies by genre

Returns a list of movies based on the provided genre ID in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val genreId : kotlin.String = 1 // kotlin.String | 
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : DiscoverMoviesGenreGenreIdGet200Response = apiInstance.discoverMoviesGenreGenreIdGet(genreId, page, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#discoverMoviesGenreGenreIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#discoverMoviesGenreGenreIdGet")
    e.printStackTrace()
}
```

### Parameters
| **genreId** | **kotlin.String**|  | |
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**DiscoverMoviesGenreGenreIdGet200Response**](DiscoverMoviesGenreGenreIdGet200Response.md)

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

<a id="discoverMoviesGet"></a>
# **discoverMoviesGet**
> DiscoverMoviesGet200Response discoverMoviesGet(page, language, genre, studio, keywords, excludeKeywords, sortBy, primaryReleaseDateGte, primaryReleaseDateLte, withRuntimeGte, withRuntimeLte, voteAverageGte, voteAverageLte, voteCountGte, voteCountLte, watchRegion, watchProviders, certification, certificationGte, certificationLte, certificationCountry, certificationMode)

Discover movies

Returns a list of movies in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
val genre : kotlin.String = 18 // kotlin.String | 
val studio : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val keywords : kotlin.String = 1,2 // kotlin.String | 
val excludeKeywords : kotlin.String = 3,4 // kotlin.String | Comma-separated list of keyword IDs to exclude from results
val sortBy : kotlin.String = popularity.desc // kotlin.String | 
val primaryReleaseDateGte : kotlin.String = 2022-01-01 // kotlin.String | 
val primaryReleaseDateLte : kotlin.String = 2023-01-01 // kotlin.String | 
val withRuntimeGte : java.math.BigDecimal = 60 // java.math.BigDecimal | 
val withRuntimeLte : java.math.BigDecimal = 120 // java.math.BigDecimal | 
val voteAverageGte : java.math.BigDecimal = 7 // java.math.BigDecimal | 
val voteAverageLte : java.math.BigDecimal = 10 // java.math.BigDecimal | 
val voteCountGte : java.math.BigDecimal = 7 // java.math.BigDecimal | 
val voteCountLte : java.math.BigDecimal = 10 // java.math.BigDecimal | 
val watchRegion : kotlin.String = US // kotlin.String | 
val watchProviders : kotlin.String = 8|9 // kotlin.String | 
val certification : kotlin.String = PG-13 // kotlin.String | Exact certification to filter by (used when certificationMode is 'exact')
val certificationGte : kotlin.String = G // kotlin.String | Minimum certification to filter by (used when certificationMode is 'range')
val certificationLte : kotlin.String = PG-13 // kotlin.String | Maximum certification to filter by (used when certificationMode is 'range')
val certificationCountry : kotlin.String = US // kotlin.String | Country code for the certification system (e.g., US, GB, CA)
val certificationMode : kotlin.String = exact // kotlin.String | Determines whether to use exact certification matching or a certification range (internal use only, not sent to TMDB API)
try {
    val result : DiscoverMoviesGet200Response = apiInstance.discoverMoviesGet(page, language, genre, studio, keywords, excludeKeywords, sortBy, primaryReleaseDateGte, primaryReleaseDateLte, withRuntimeGte, withRuntimeLte, voteAverageGte, voteAverageLte, voteCountGte, voteCountLte, watchRegion, watchProviders, certification, certificationGte, certificationLte, certificationCountry, certificationMode)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#discoverMoviesGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#discoverMoviesGet")
    e.printStackTrace()
}
```

### Parameters
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |
| **language** | **kotlin.String**|  | [optional] |
| **genre** | **kotlin.String**|  | [optional] |
| **studio** | **java.math.BigDecimal**|  | [optional] |
| **keywords** | **kotlin.String**|  | [optional] |
| **excludeKeywords** | **kotlin.String**| Comma-separated list of keyword IDs to exclude from results | [optional] |
| **sortBy** | **kotlin.String**|  | [optional] |
| **primaryReleaseDateGte** | **kotlin.String**|  | [optional] |
| **primaryReleaseDateLte** | **kotlin.String**|  | [optional] |
| **withRuntimeGte** | **java.math.BigDecimal**|  | [optional] |
| **withRuntimeLte** | **java.math.BigDecimal**|  | [optional] |
| **voteAverageGte** | **java.math.BigDecimal**|  | [optional] |
| **voteAverageLte** | **java.math.BigDecimal**|  | [optional] |
| **voteCountGte** | **java.math.BigDecimal**|  | [optional] |
| **voteCountLte** | **java.math.BigDecimal**|  | [optional] |
| **watchRegion** | **kotlin.String**|  | [optional] |
| **watchProviders** | **kotlin.String**|  | [optional] |
| **certification** | **kotlin.String**| Exact certification to filter by (used when certificationMode is &#39;exact&#39;) | [optional] |
| **certificationGte** | **kotlin.String**| Minimum certification to filter by (used when certificationMode is &#39;range&#39;) | [optional] |
| **certificationLte** | **kotlin.String**| Maximum certification to filter by (used when certificationMode is &#39;range&#39;) | [optional] |
| **certificationCountry** | **kotlin.String**| Country code for the certification system (e.g., US, GB, CA) | [optional] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **certificationMode** | **kotlin.String**| Determines whether to use exact certification matching or a certification range (internal use only, not sent to TMDB API) | [optional] [enum: exact, range] |

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

<a id="discoverMoviesLanguageLanguageGet"></a>
# **discoverMoviesLanguageLanguageGet**
> DiscoverMoviesLanguageLanguageGet200Response discoverMoviesLanguageLanguageGet(language, page, language2)

Discover movies by original language

Returns a list of movies based on the provided ISO 639-1 language code in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val language : kotlin.String = en // kotlin.String | 
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language2 : kotlin.String = en // kotlin.String | 
try {
    val result : DiscoverMoviesLanguageLanguageGet200Response = apiInstance.discoverMoviesLanguageLanguageGet(language, page, language2)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#discoverMoviesLanguageLanguageGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#discoverMoviesLanguageLanguageGet")
    e.printStackTrace()
}
```

### Parameters
| **language** | **kotlin.String**|  | |
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language2** | **kotlin.String**|  | [optional] |

### Return type

[**DiscoverMoviesLanguageLanguageGet200Response**](DiscoverMoviesLanguageLanguageGet200Response.md)

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

<a id="discoverMoviesStudioStudioIdGet"></a>
# **discoverMoviesStudioStudioIdGet**
> DiscoverMoviesStudioStudioIdGet200Response discoverMoviesStudioStudioIdGet(studioId, page, language)

Discover movies by studio

Returns a list of movies based on the provided studio ID in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val studioId : kotlin.String = 1 // kotlin.String | 
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : DiscoverMoviesStudioStudioIdGet200Response = apiInstance.discoverMoviesStudioStudioIdGet(studioId, page, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#discoverMoviesStudioStudioIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#discoverMoviesStudioStudioIdGet")
    e.printStackTrace()
}
```

### Parameters
| **studioId** | **kotlin.String**|  | |
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**DiscoverMoviesStudioStudioIdGet200Response**](DiscoverMoviesStudioStudioIdGet200Response.md)

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

<a id="discoverMoviesUpcomingGet"></a>
# **discoverMoviesUpcomingGet**
> DiscoverMoviesGet200Response discoverMoviesUpcomingGet(page, language)

Upcoming movies

Returns a list of movies in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : DiscoverMoviesGet200Response = apiInstance.discoverMoviesUpcomingGet(page, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#discoverMoviesUpcomingGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#discoverMoviesUpcomingGet")
    e.printStackTrace()
}
```

### Parameters
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

<a id="discoverTrendingGet"></a>
# **discoverTrendingGet**
> SearchGet200Response discoverTrendingGet(page, language)

Trending movies and TV

Returns a list of movies and TV shows in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : SearchGet200Response = apiInstance.discoverTrendingGet(page, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#discoverTrendingGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#discoverTrendingGet")
    e.printStackTrace()
}
```

### Parameters
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**SearchGet200Response**](SearchGet200Response.md)

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

<a id="discoverTvGenreGenreIdGet"></a>
# **discoverTvGenreGenreIdGet**
> DiscoverTvGenreGenreIdGet200Response discoverTvGenreGenreIdGet(genreId, page, language)

Discover TV shows by genre

Returns a list of TV shows based on the provided genre ID in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val genreId : kotlin.String = 1 // kotlin.String | 
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : DiscoverTvGenreGenreIdGet200Response = apiInstance.discoverTvGenreGenreIdGet(genreId, page, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#discoverTvGenreGenreIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#discoverTvGenreGenreIdGet")
    e.printStackTrace()
}
```

### Parameters
| **genreId** | **kotlin.String**|  | |
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**DiscoverTvGenreGenreIdGet200Response**](DiscoverTvGenreGenreIdGet200Response.md)

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

<a id="discoverTvGet"></a>
# **discoverTvGet**
> DiscoverTvGet200Response discoverTvGet(page, language, genre, network, keywords, excludeKeywords, sortBy, firstAirDateGte, firstAirDateLte, withRuntimeGte, withRuntimeLte, voteAverageGte, voteAverageLte, voteCountGte, voteCountLte, watchRegion, watchProviders, status, certification, certificationGte, certificationLte, certificationCountry, certificationMode)

Discover TV shows

Returns a list of TV shows in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
val genre : kotlin.String = 18 // kotlin.String | 
val network : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val keywords : kotlin.String = 1,2 // kotlin.String | 
val excludeKeywords : kotlin.String = 3,4 // kotlin.String | Comma-separated list of keyword IDs to exclude from results
val sortBy : kotlin.String = popularity.desc // kotlin.String | 
val firstAirDateGte : kotlin.String = 2022-01-01 // kotlin.String | 
val firstAirDateLte : kotlin.String = 2023-01-01 // kotlin.String | 
val withRuntimeGte : java.math.BigDecimal = 60 // java.math.BigDecimal | 
val withRuntimeLte : java.math.BigDecimal = 120 // java.math.BigDecimal | 
val voteAverageGte : java.math.BigDecimal = 7 // java.math.BigDecimal | 
val voteAverageLte : java.math.BigDecimal = 10 // java.math.BigDecimal | 
val voteCountGte : java.math.BigDecimal = 7 // java.math.BigDecimal | 
val voteCountLte : java.math.BigDecimal = 10 // java.math.BigDecimal | 
val watchRegion : kotlin.String = US // kotlin.String | 
val watchProviders : kotlin.String = 8|9 // kotlin.String | 
val status : kotlin.String = 3|4 // kotlin.String | 
val certification : kotlin.String = TV-14 // kotlin.String | Exact certification to filter by (used when certificationMode is 'exact')
val certificationGte : kotlin.String = TV-PG // kotlin.String | Minimum certification to filter by (used when certificationMode is 'range')
val certificationLte : kotlin.String = TV-MA // kotlin.String | Maximum certification to filter by (used when certificationMode is 'range')
val certificationCountry : kotlin.String = US // kotlin.String | Country code for the certification system (e.g., US, GB, CA)
val certificationMode : kotlin.String = exact // kotlin.String | Determines whether to use exact certification matching or a certification range (internal use only, not sent to TMDB API)
try {
    val result : DiscoverTvGet200Response = apiInstance.discoverTvGet(page, language, genre, network, keywords, excludeKeywords, sortBy, firstAirDateGte, firstAirDateLte, withRuntimeGte, withRuntimeLte, voteAverageGte, voteAverageLte, voteCountGte, voteCountLte, watchRegion, watchProviders, status, certification, certificationGte, certificationLte, certificationCountry, certificationMode)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#discoverTvGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#discoverTvGet")
    e.printStackTrace()
}
```

### Parameters
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |
| **language** | **kotlin.String**|  | [optional] |
| **genre** | **kotlin.String**|  | [optional] |
| **network** | **java.math.BigDecimal**|  | [optional] |
| **keywords** | **kotlin.String**|  | [optional] |
| **excludeKeywords** | **kotlin.String**| Comma-separated list of keyword IDs to exclude from results | [optional] |
| **sortBy** | **kotlin.String**|  | [optional] |
| **firstAirDateGte** | **kotlin.String**|  | [optional] |
| **firstAirDateLte** | **kotlin.String**|  | [optional] |
| **withRuntimeGte** | **java.math.BigDecimal**|  | [optional] |
| **withRuntimeLte** | **java.math.BigDecimal**|  | [optional] |
| **voteAverageGte** | **java.math.BigDecimal**|  | [optional] |
| **voteAverageLte** | **java.math.BigDecimal**|  | [optional] |
| **voteCountGte** | **java.math.BigDecimal**|  | [optional] |
| **voteCountLte** | **java.math.BigDecimal**|  | [optional] |
| **watchRegion** | **kotlin.String**|  | [optional] |
| **watchProviders** | **kotlin.String**|  | [optional] |
| **status** | **kotlin.String**|  | [optional] |
| **certification** | **kotlin.String**| Exact certification to filter by (used when certificationMode is &#39;exact&#39;) | [optional] |
| **certificationGte** | **kotlin.String**| Minimum certification to filter by (used when certificationMode is &#39;range&#39;) | [optional] |
| **certificationLte** | **kotlin.String**| Maximum certification to filter by (used when certificationMode is &#39;range&#39;) | [optional] |
| **certificationCountry** | **kotlin.String**| Country code for the certification system (e.g., US, GB, CA) | [optional] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **certificationMode** | **kotlin.String**| Determines whether to use exact certification matching or a certification range (internal use only, not sent to TMDB API) | [optional] [enum: exact, range] |

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

<a id="discoverTvLanguageLanguageGet"></a>
# **discoverTvLanguageLanguageGet**
> DiscoverTvLanguageLanguageGet200Response discoverTvLanguageLanguageGet(language, page, language2)

Discover TV shows by original language

Returns a list of TV shows based on the provided ISO 639-1 language code in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val language : kotlin.String = en // kotlin.String | 
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language2 : kotlin.String = en // kotlin.String | 
try {
    val result : DiscoverTvLanguageLanguageGet200Response = apiInstance.discoverTvLanguageLanguageGet(language, page, language2)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#discoverTvLanguageLanguageGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#discoverTvLanguageLanguageGet")
    e.printStackTrace()
}
```

### Parameters
| **language** | **kotlin.String**|  | |
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language2** | **kotlin.String**|  | [optional] |

### Return type

[**DiscoverTvLanguageLanguageGet200Response**](DiscoverTvLanguageLanguageGet200Response.md)

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

<a id="discoverTvNetworkNetworkIdGet"></a>
# **discoverTvNetworkNetworkIdGet**
> DiscoverTvNetworkNetworkIdGet200Response discoverTvNetworkNetworkIdGet(networkId, page, language)

Discover TV shows by network

Returns a list of TV shows based on the provided network ID in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val networkId : kotlin.String = 1 // kotlin.String | 
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : DiscoverTvNetworkNetworkIdGet200Response = apiInstance.discoverTvNetworkNetworkIdGet(networkId, page, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#discoverTvNetworkNetworkIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#discoverTvNetworkNetworkIdGet")
    e.printStackTrace()
}
```

### Parameters
| **networkId** | **kotlin.String**|  | |
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**DiscoverTvNetworkNetworkIdGet200Response**](DiscoverTvNetworkNetworkIdGet200Response.md)

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

<a id="discoverTvUpcomingGet"></a>
# **discoverTvUpcomingGet**
> DiscoverTvGet200Response discoverTvUpcomingGet(page, language)

Discover Upcoming TV shows

Returns a list of upcoming TV shows in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : DiscoverTvGet200Response = apiInstance.discoverTvUpcomingGet(page, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#discoverTvUpcomingGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#discoverTvUpcomingGet")
    e.printStackTrace()
}
```

### Parameters
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

<a id="discoverWatchlistGet"></a>
# **discoverWatchlistGet**
> UserUserIdWatchlistGet200Response discoverWatchlistGet(page)

Get the Plex watchlist.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
try {
    val result : UserUserIdWatchlistGet200Response = apiInstance.discoverWatchlistGet(page)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#discoverWatchlistGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#discoverWatchlistGet")
    e.printStackTrace()
}
```

### Parameters
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

<a id="searchCompanyGet"></a>
# **searchCompanyGet**
> SearchCompanyGet200Response searchCompanyGet(query, page)

Search for companies

Returns a list of TMDB companies matching the search query. (Will not return origin country)

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val query : kotlin.String = Disney // kotlin.String | 
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
try {
    val result : SearchCompanyGet200Response = apiInstance.searchCompanyGet(query, page)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#searchCompanyGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#searchCompanyGet")
    e.printStackTrace()
}
```

### Parameters
| **query** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |

### Return type

[**SearchCompanyGet200Response**](SearchCompanyGet200Response.md)

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

<a id="searchGet"></a>
# **searchGet**
> SearchGet200Response searchGet(query, page, language)

Search for movies, TV shows, or people

Returns a list of movies, TV shows, or people a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val query : kotlin.String = Mulan // kotlin.String | 
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : SearchGet200Response = apiInstance.searchGet(query, page, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#searchGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#searchGet")
    e.printStackTrace()
}
```

### Parameters
| **query** | **kotlin.String**|  | |
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**SearchGet200Response**](SearchGet200Response.md)

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

<a id="searchKeywordGet"></a>
# **searchKeywordGet**
> SearchKeywordGet200Response searchKeywordGet(query, page)

Search for keywords

Returns a list of TMDB keywords matching the search query

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SearchApi()
val query : kotlin.String = christmas // kotlin.String | 
val page : java.math.BigDecimal = 1 // java.math.BigDecimal | 
try {
    val result : SearchKeywordGet200Response = apiInstance.searchKeywordGet(query, page)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SearchApi#searchKeywordGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SearchApi#searchKeywordGet")
    e.printStackTrace()
}
```

### Parameters
| **query** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **page** | **java.math.BigDecimal**|  | [optional] [default to 1] |

### Return type

[**SearchKeywordGet200Response**](SearchKeywordGet200Response.md)

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

