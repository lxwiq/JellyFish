# ServiceApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**serviceRadarrGet**](ServiceApi.md#serviceRadarrGet) | **GET** /service/radarr | Get non-sensitive Radarr server list |
| [**serviceRadarrRadarrIdGet**](ServiceApi.md#serviceRadarrRadarrIdGet) | **GET** /service/radarr/{radarrId} | Get Radarr server quality profiles and root folders |
| [**serviceSonarrGet**](ServiceApi.md#serviceSonarrGet) | **GET** /service/sonarr | Get non-sensitive Sonarr server list |
| [**serviceSonarrLookupTmdbIdGet**](ServiceApi.md#serviceSonarrLookupTmdbIdGet) | **GET** /service/sonarr/lookup/{tmdbId} | Get series from Sonarr |
| [**serviceSonarrSonarrIdGet**](ServiceApi.md#serviceSonarrSonarrIdGet) | **GET** /service/sonarr/{sonarrId} | Get Sonarr server quality profiles and root folders |


<a id="serviceRadarrGet"></a>
# **serviceRadarrGet**
> kotlin.collections.List&lt;RadarrSettings&gt; serviceRadarrGet()

Get non-sensitive Radarr server list

Returns a list of Radarr server IDs and names in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = ServiceApi()
try {
    val result : kotlin.collections.List<RadarrSettings> = apiInstance.serviceRadarrGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ServiceApi#serviceRadarrGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ServiceApi#serviceRadarrGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;RadarrSettings&gt;**](RadarrSettings.md)

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

<a id="serviceRadarrRadarrIdGet"></a>
# **serviceRadarrRadarrIdGet**
> ServiceRadarrRadarrIdGet200Response serviceRadarrRadarrIdGet(radarrId)

Get Radarr server quality profiles and root folders

Returns a Radarr server&#39;s quality profile and root folder details in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = ServiceApi()
val radarrId : java.math.BigDecimal = 0 // java.math.BigDecimal | 
try {
    val result : ServiceRadarrRadarrIdGet200Response = apiInstance.serviceRadarrRadarrIdGet(radarrId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ServiceApi#serviceRadarrRadarrIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ServiceApi#serviceRadarrRadarrIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **radarrId** | **java.math.BigDecimal**|  | |

### Return type

[**ServiceRadarrRadarrIdGet200Response**](ServiceRadarrRadarrIdGet200Response.md)

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

<a id="serviceSonarrGet"></a>
# **serviceSonarrGet**
> kotlin.collections.List&lt;SonarrSettings&gt; serviceSonarrGet()

Get non-sensitive Sonarr server list

Returns a list of Sonarr server IDs and names in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = ServiceApi()
try {
    val result : kotlin.collections.List<SonarrSettings> = apiInstance.serviceSonarrGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ServiceApi#serviceSonarrGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ServiceApi#serviceSonarrGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;SonarrSettings&gt;**](SonarrSettings.md)

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

<a id="serviceSonarrLookupTmdbIdGet"></a>
# **serviceSonarrLookupTmdbIdGet**
> kotlin.collections.List&lt;SonarrSeries&gt; serviceSonarrLookupTmdbIdGet(tmdbId)

Get series from Sonarr

Returns a list of series returned by searching for the name in Sonarr.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = ServiceApi()
val tmdbId : java.math.BigDecimal = 0 // java.math.BigDecimal | 
try {
    val result : kotlin.collections.List<SonarrSeries> = apiInstance.serviceSonarrLookupTmdbIdGet(tmdbId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ServiceApi#serviceSonarrLookupTmdbIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ServiceApi#serviceSonarrLookupTmdbIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **tmdbId** | **java.math.BigDecimal**|  | |

### Return type

[**kotlin.collections.List&lt;SonarrSeries&gt;**](SonarrSeries.md)

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

<a id="serviceSonarrSonarrIdGet"></a>
# **serviceSonarrSonarrIdGet**
> ServiceSonarrSonarrIdGet200Response serviceSonarrSonarrIdGet(sonarrId)

Get Sonarr server quality profiles and root folders

Returns a Sonarr server&#39;s quality profile and root folder details in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = ServiceApi()
val sonarrId : java.math.BigDecimal = 0 // java.math.BigDecimal | 
try {
    val result : ServiceSonarrSonarrIdGet200Response = apiInstance.serviceSonarrSonarrIdGet(sonarrId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ServiceApi#serviceSonarrSonarrIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ServiceApi#serviceSonarrSonarrIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sonarrId** | **java.math.BigDecimal**|  | |

### Return type

[**ServiceSonarrSonarrIdGet200Response**](ServiceSonarrSonarrIdGet200Response.md)

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

