# PersonApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**personPersonIdCombinedCreditsGet**](PersonApi.md#personPersonIdCombinedCreditsGet) | **GET** /person/{personId}/combined_credits | Get combined credits |
| [**personPersonIdGet**](PersonApi.md#personPersonIdGet) | **GET** /person/{personId} | Get person details |


<a id="personPersonIdCombinedCreditsGet"></a>
# **personPersonIdCombinedCreditsGet**
> PersonPersonIdCombinedCreditsGet200Response personPersonIdCombinedCreditsGet(personId, language)

Get combined credits

Returns the person&#39;s combined credits based on the provided personId in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = PersonApi()
val personId : java.math.BigDecimal = 287 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : PersonPersonIdCombinedCreditsGet200Response = apiInstance.personPersonIdCombinedCreditsGet(personId, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling PersonApi#personPersonIdCombinedCreditsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling PersonApi#personPersonIdCombinedCreditsGet")
    e.printStackTrace()
}
```

### Parameters
| **personId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**PersonPersonIdCombinedCreditsGet200Response**](PersonPersonIdCombinedCreditsGet200Response.md)

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

<a id="personPersonIdGet"></a>
# **personPersonIdGet**
> PersonDetails personPersonIdGet(personId, language)

Get person details

Returns person details based on provided personId in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = PersonApi()
val personId : java.math.BigDecimal = 287 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : PersonDetails = apiInstance.personPersonIdGet(personId, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling PersonApi#personPersonIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling PersonApi#personPersonIdGet")
    e.printStackTrace()
}
```

### Parameters
| **personId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**PersonDetails**](PersonDetails.md)

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

