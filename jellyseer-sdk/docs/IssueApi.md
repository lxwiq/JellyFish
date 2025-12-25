# IssueApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**issueCommentCommentIdDelete**](IssueApi.md#issueCommentCommentIdDelete) | **DELETE** /issueComment/{commentId} | Delete issue comment |
| [**issueCommentCommentIdGet**](IssueApi.md#issueCommentCommentIdGet) | **GET** /issueComment/{commentId} | Get issue comment |
| [**issueCommentCommentIdPut**](IssueApi.md#issueCommentCommentIdPut) | **PUT** /issueComment/{commentId} | Update issue comment |
| [**issueCountGet**](IssueApi.md#issueCountGet) | **GET** /issue/count | Gets issue counts |
| [**issueGet**](IssueApi.md#issueGet) | **GET** /issue | Get all issues |
| [**issueIssueIdCommentPost**](IssueApi.md#issueIssueIdCommentPost) | **POST** /issue/{issueId}/comment | Create a comment |
| [**issueIssueIdDelete**](IssueApi.md#issueIssueIdDelete) | **DELETE** /issue/{issueId} | Delete issue |
| [**issueIssueIdGet**](IssueApi.md#issueIssueIdGet) | **GET** /issue/{issueId} | Get issue |
| [**issueIssueIdStatusPost**](IssueApi.md#issueIssueIdStatusPost) | **POST** /issue/{issueId}/{status} | Update an issue&#39;s status |
| [**issuePost**](IssueApi.md#issuePost) | **POST** /issue | Create new issue |


<a id="issueCommentCommentIdDelete"></a>
# **issueCommentCommentIdDelete**
> issueCommentCommentIdDelete(commentId)

Delete issue comment

Deletes an issue comment. Only users with &#x60;MANAGE_ISSUES&#x60; or the user who created the comment can perform this action. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = IssueApi()
val commentId : kotlin.String = 1 // kotlin.String | Issue Comment ID
try {
    apiInstance.issueCommentCommentIdDelete(commentId)
} catch (e: ClientException) {
    println("4xx response calling IssueApi#issueCommentCommentIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling IssueApi#issueCommentCommentIdDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **commentId** | **kotlin.String**| Issue Comment ID | |

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

<a id="issueCommentCommentIdGet"></a>
# **issueCommentCommentIdGet**
> IssueComment issueCommentCommentIdGet(commentId)

Get issue comment

Returns a single issue comment in JSON format. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = IssueApi()
val commentId : kotlin.String = 1 // kotlin.String | 
try {
    val result : IssueComment = apiInstance.issueCommentCommentIdGet(commentId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling IssueApi#issueCommentCommentIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling IssueApi#issueCommentCommentIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **commentId** | **kotlin.String**|  | |

### Return type

[**IssueComment**](IssueComment.md)

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

<a id="issueCommentCommentIdPut"></a>
# **issueCommentCommentIdPut**
> IssueComment issueCommentCommentIdPut(commentId, issueCommentCommentIdPutRequest)

Update issue comment

Updates and returns a single issue comment in JSON format. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = IssueApi()
val commentId : kotlin.String = 1 // kotlin.String | 
val issueCommentCommentIdPutRequest : IssueCommentCommentIdPutRequest =  // IssueCommentCommentIdPutRequest | 
try {
    val result : IssueComment = apiInstance.issueCommentCommentIdPut(commentId, issueCommentCommentIdPutRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling IssueApi#issueCommentCommentIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling IssueApi#issueCommentCommentIdPut")
    e.printStackTrace()
}
```

### Parameters
| **commentId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **issueCommentCommentIdPutRequest** | [**IssueCommentCommentIdPutRequest**](IssueCommentCommentIdPutRequest.md)|  | |

### Return type

[**IssueComment**](IssueComment.md)

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

<a id="issueCountGet"></a>
# **issueCountGet**
> IssueCountGet200Response issueCountGet()

Gets issue counts

Returns the number of open and closed issues, as well as the number of issues of each type. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = IssueApi()
try {
    val result : IssueCountGet200Response = apiInstance.issueCountGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling IssueApi#issueCountGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling IssueApi#issueCountGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**IssueCountGet200Response**](IssueCountGet200Response.md)

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

<a id="issueGet"></a>
# **issueGet**
> IssueGet200Response issueGet(take, skip, sort, filter, requestedBy)

Get all issues

Returns a list of issues in JSON format. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = IssueApi()
val take : java.math.BigDecimal = 20 // java.math.BigDecimal | 
val skip : java.math.BigDecimal = 0 // java.math.BigDecimal | 
val sort : kotlin.String = sort_example // kotlin.String | 
val filter : kotlin.String = filter_example // kotlin.String | 
val requestedBy : java.math.BigDecimal = 1 // java.math.BigDecimal | 
try {
    val result : IssueGet200Response = apiInstance.issueGet(take, skip, sort, filter, requestedBy)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling IssueApi#issueGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling IssueApi#issueGet")
    e.printStackTrace()
}
```

### Parameters
| **take** | **java.math.BigDecimal**|  | [optional] |
| **skip** | **java.math.BigDecimal**|  | [optional] |
| **sort** | **kotlin.String**|  | [optional] [default to Sort.added] [enum: added, modified] |
| **filter** | **kotlin.String**|  | [optional] [default to Filter.&#x60;open&#x60;] [enum: all, open, resolved] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **requestedBy** | **java.math.BigDecimal**|  | [optional] |

### Return type

[**IssueGet200Response**](IssueGet200Response.md)

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

<a id="issueIssueIdCommentPost"></a>
# **issueIssueIdCommentPost**
> Issue issueIssueIdCommentPost(issueId, issueIssueIdCommentPostRequest)

Create a comment

Creates a comment and returns associated issue in JSON format. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = IssueApi()
val issueId : java.math.BigDecimal = 1 // java.math.BigDecimal | 
val issueIssueIdCommentPostRequest : IssueIssueIdCommentPostRequest =  // IssueIssueIdCommentPostRequest | 
try {
    val result : Issue = apiInstance.issueIssueIdCommentPost(issueId, issueIssueIdCommentPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling IssueApi#issueIssueIdCommentPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling IssueApi#issueIssueIdCommentPost")
    e.printStackTrace()
}
```

### Parameters
| **issueId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **issueIssueIdCommentPostRequest** | [**IssueIssueIdCommentPostRequest**](IssueIssueIdCommentPostRequest.md)|  | |

### Return type

[**Issue**](Issue.md)

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

<a id="issueIssueIdDelete"></a>
# **issueIssueIdDelete**
> issueIssueIdDelete(issueId)

Delete issue

Removes an issue. If the user has the &#x60;MANAGE_ISSUES&#x60; permission, any issue can be removed. Otherwise, only a users own issues can be removed.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = IssueApi()
val issueId : kotlin.String = 1 // kotlin.String | Issue ID
try {
    apiInstance.issueIssueIdDelete(issueId)
} catch (e: ClientException) {
    println("4xx response calling IssueApi#issueIssueIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling IssueApi#issueIssueIdDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **issueId** | **kotlin.String**| Issue ID | |

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

<a id="issueIssueIdGet"></a>
# **issueIssueIdGet**
> Issue issueIssueIdGet(issueId)

Get issue

Returns a single issue in JSON format. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = IssueApi()
val issueId : java.math.BigDecimal = 1 // java.math.BigDecimal | 
try {
    val result : Issue = apiInstance.issueIssueIdGet(issueId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling IssueApi#issueIssueIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling IssueApi#issueIssueIdGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **issueId** | **java.math.BigDecimal**|  | |

### Return type

[**Issue**](Issue.md)

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

<a id="issueIssueIdStatusPost"></a>
# **issueIssueIdStatusPost**
> Issue issueIssueIdStatusPost(issueId, status)

Update an issue&#39;s status

Updates an issue&#39;s status to approved or declined. Also returns the issue in a JSON object.  Requires the &#x60;MANAGE_ISSUES&#x60; permission or &#x60;ADMIN&#x60;. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = IssueApi()
val issueId : kotlin.String = 1 // kotlin.String | Issue ID
val status : kotlin.String = status_example // kotlin.String | New status
try {
    val result : Issue = apiInstance.issueIssueIdStatusPost(issueId, status)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling IssueApi#issueIssueIdStatusPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling IssueApi#issueIssueIdStatusPost")
    e.printStackTrace()
}
```

### Parameters
| **issueId** | **kotlin.String**| Issue ID | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **status** | **kotlin.String**| New status | [enum: open, resolved] |

### Return type

[**Issue**](Issue.md)

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

<a id="issuePost"></a>
# **issuePost**
> Issue issuePost(issuePostRequest)

Create new issue

Creates a new issue 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = IssueApi()
val issuePostRequest : IssuePostRequest =  // IssuePostRequest | 
try {
    val result : Issue = apiInstance.issuePost(issuePostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling IssueApi#issuePost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling IssueApi#issuePost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **issuePostRequest** | [**IssuePostRequest**](IssuePostRequest.md)|  | |

### Return type

[**Issue**](Issue.md)

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

