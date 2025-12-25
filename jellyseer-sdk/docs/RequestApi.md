# RequestApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**requestCountGet**](RequestApi.md#requestCountGet) | **GET** /request/count | Gets request counts |
| [**requestGet**](RequestApi.md#requestGet) | **GET** /request | Get all requests |
| [**requestPost**](RequestApi.md#requestPost) | **POST** /request | Create new request |
| [**requestRequestIdDelete**](RequestApi.md#requestRequestIdDelete) | **DELETE** /request/{requestId} | Delete request |
| [**requestRequestIdGet**](RequestApi.md#requestRequestIdGet) | **GET** /request/{requestId} | Get MediaRequest |
| [**requestRequestIdPut**](RequestApi.md#requestRequestIdPut) | **PUT** /request/{requestId} | Update MediaRequest |
| [**requestRequestIdRetryPost**](RequestApi.md#requestRequestIdRetryPost) | **POST** /request/{requestId}/retry | Retry failed request |
| [**requestRequestIdStatusPost**](RequestApi.md#requestRequestIdStatusPost) | **POST** /request/{requestId}/{status} | Update a request&#39;s status |


<a id="requestCountGet"></a>
# **requestCountGet**
> RequestCountGet200Response requestCountGet()

Gets request counts

Returns the number of requests by status including pending, approved, available, and completed requests. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = RequestApi()
try {
    val result : RequestCountGet200Response = apiInstance.requestCountGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling RequestApi#requestCountGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling RequestApi#requestCountGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**RequestCountGet200Response**](RequestCountGet200Response.md)

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

<a id="requestGet"></a>
# **requestGet**
> UserUserIdRequestsGet200Response requestGet(take, skip, filter, sort, sortDirection, requestedBy, mediaType)

Get all requests

Returns all requests if the user has the &#x60;ADMIN&#x60; or &#x60;MANAGE_REQUESTS&#x60; permissions. Otherwise, only the logged-in user&#39;s requests are returned.  If the &#x60;requestedBy&#x60; parameter is specified, only requests from that particular user ID will be returned. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = RequestApi()
val take : java.math.BigDecimal = 20 // java.math.BigDecimal | 
val skip : java.math.BigDecimal = 0 // java.math.BigDecimal | 
val filter : kotlin.String = filter_example // kotlin.String | 
val sort : kotlin.String = sort_example // kotlin.String | 
val sortDirection : kotlin.String = sortDirection_example // kotlin.String | 
val requestedBy : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val mediaType : kotlin.String = mediaType_example // kotlin.String | 
try {
    val result : UserUserIdRequestsGet200Response = apiInstance.requestGet(take, skip, filter, sort, sortDirection, requestedBy, mediaType)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling RequestApi#requestGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling RequestApi#requestGet")
    e.printStackTrace()
}
```

### Parameters
| **take** | **java.math.BigDecimal**|  | [optional] |
| **skip** | **java.math.BigDecimal**|  | [optional] |
| **filter** | **kotlin.String**|  | [optional] [enum: all, approved, available, pending, processing, unavailable, failed, deleted, completed] |
| **sort** | **kotlin.String**|  | [optional] [default to Sort.added] [enum: added, modified] |
| **sortDirection** | **kotlin.String**|  | [optional] [default to SortDirection.desc] [enum: asc, desc] |
| **requestedBy** | **java.math.BigDecimal**|  | [optional] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **mediaType** | **kotlin.String**|  | [optional] [default to MediaType.all] [enum: movie, tv, all] |

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

<a id="requestPost"></a>
# **requestPost**
> MediaRequest requestPost(requestPostRequest)

Create new request

Creates a new request with the provided media ID and type. The &#x60;REQUEST&#x60; permission is required.  If the user has the &#x60;ADMIN&#x60; or &#x60;AUTO_APPROVE&#x60; permissions, their request will be auomatically approved. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = RequestApi()
val requestPostRequest : RequestPostRequest =  // RequestPostRequest | 
try {
    val result : MediaRequest = apiInstance.requestPost(requestPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling RequestApi#requestPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling RequestApi#requestPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestPostRequest** | [**RequestPostRequest**](RequestPostRequest.md)|  | |

### Return type

[**MediaRequest**](MediaRequest.md)

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

<a id="requestRequestIdDelete"></a>
# **requestRequestIdDelete**
> requestRequestIdDelete(requestId)

Delete request

Removes a request. If the user has the &#x60;MANAGE_REQUESTS&#x60; permission, any request can be removed. Otherwise, only pending requests can be removed.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = RequestApi()
val requestId : kotlin.String = 1 // kotlin.String | Request ID
try {
    apiInstance.requestRequestIdDelete(requestId)
} catch (e: ClientException) {
    println("4xx response calling RequestApi#requestRequestIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling RequestApi#requestRequestIdDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestId** | **kotlin.String**| Request ID | |

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

<a id="requestRequestIdGet"></a>
# **requestRequestIdGet**
> MediaRequest requestRequestIdGet(requestId)

Get MediaRequest

Returns a specific MediaRequest in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = RequestApi()
val requestId : kotlin.String = 1 // kotlin.String | Request ID
try {
    val result : MediaRequest = apiInstance.requestRequestIdGet(requestId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling RequestApi#requestRequestIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling RequestApi#requestRequestIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestId** | **kotlin.String**| Request ID | |

### Return type

[**MediaRequest**](MediaRequest.md)

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

<a id="requestRequestIdPut"></a>
# **requestRequestIdPut**
> MediaRequest requestRequestIdPut(requestId, requestRequestIdPutRequest)

Update MediaRequest

Updates a specific media request and returns the request in a JSON object. Requires the &#x60;MANAGE_REQUESTS&#x60; permission.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = RequestApi()
val requestId : kotlin.String = 1 // kotlin.String | Request ID
val requestRequestIdPutRequest : RequestRequestIdPutRequest =  // RequestRequestIdPutRequest | 
try {
    val result : MediaRequest = apiInstance.requestRequestIdPut(requestId, requestRequestIdPutRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling RequestApi#requestRequestIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling RequestApi#requestRequestIdPut")
    e.printStackTrace()
}
```

### Parameters
| **requestId** | **kotlin.String**| Request ID | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestRequestIdPutRequest** | [**RequestRequestIdPutRequest**](RequestRequestIdPutRequest.md)|  | |

### Return type

[**MediaRequest**](MediaRequest.md)

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

<a id="requestRequestIdRetryPost"></a>
# **requestRequestIdRetryPost**
> MediaRequest requestRequestIdRetryPost(requestId)

Retry failed request

Retries a request by resending requests to Sonarr or Radarr.  Requires the &#x60;MANAGE_REQUESTS&#x60; permission or &#x60;ADMIN&#x60;. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = RequestApi()
val requestId : kotlin.String = 1 // kotlin.String | Request ID
try {
    val result : MediaRequest = apiInstance.requestRequestIdRetryPost(requestId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling RequestApi#requestRequestIdRetryPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling RequestApi#requestRequestIdRetryPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestId** | **kotlin.String**| Request ID | |

### Return type

[**MediaRequest**](MediaRequest.md)

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

<a id="requestRequestIdStatusPost"></a>
# **requestRequestIdStatusPost**
> MediaRequest requestRequestIdStatusPost(requestId, status)

Update a request&#39;s status

Updates a request&#39;s status to approved or declined. Also returns the request in a JSON object.  Requires the &#x60;MANAGE_REQUESTS&#x60; permission or &#x60;ADMIN&#x60;. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = RequestApi()
val requestId : kotlin.String = 1 // kotlin.String | Request ID
val status : kotlin.String = status_example // kotlin.String | New status
try {
    val result : MediaRequest = apiInstance.requestRequestIdStatusPost(requestId, status)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling RequestApi#requestRequestIdStatusPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling RequestApi#requestRequestIdStatusPost")
    e.printStackTrace()
}
```

### Parameters
| **requestId** | **kotlin.String**| Request ID | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **status** | **kotlin.String**| New status | [enum: approve, decline] |

### Return type

[**MediaRequest**](MediaRequest.md)

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

