# CollectionApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**collectionCollectionIdGet**](CollectionApi.md#collectionCollectionIdGet) | **GET** /collection/{collectionId} | Get collection details |


<a id="collectionCollectionIdGet"></a>
# **collectionCollectionIdGet**
> Collection collectionCollectionIdGet(collectionId, language)

Get collection details

Returns full collection details in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = CollectionApi()
val collectionId : java.math.BigDecimal = 537982 // java.math.BigDecimal | 
val language : kotlin.String = en // kotlin.String | 
try {
    val result : Collection = apiInstance.collectionCollectionIdGet(collectionId, language)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling CollectionApi#collectionCollectionIdGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling CollectionApi#collectionCollectionIdGet")
    e.printStackTrace()
}
```

### Parameters
| **collectionId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **language** | **kotlin.String**|  | [optional] |

### Return type

[**Collection**](Collection.md)

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

