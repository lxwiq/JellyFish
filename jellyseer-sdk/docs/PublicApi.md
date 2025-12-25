# PublicApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**statusAppdataGet**](PublicApi.md#statusAppdataGet) | **GET** /status/appdata | Get application data volume status |
| [**statusGet**](PublicApi.md#statusGet) | **GET** /status | Get Seerr status |


<a id="statusAppdataGet"></a>
# **statusAppdataGet**
> StatusAppdataGet200Response statusAppdataGet()

Get application data volume status

For Docker installs, returns whether or not the volume mount was configured properly. Always returns true for non-Docker installs.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = PublicApi()
try {
    val result : StatusAppdataGet200Response = apiInstance.statusAppdataGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling PublicApi#statusAppdataGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling PublicApi#statusAppdataGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**StatusAppdataGet200Response**](StatusAppdataGet200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="statusGet"></a>
# **statusGet**
> StatusGet200Response statusGet()

Get Seerr status

Returns the current Seerr status in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = PublicApi()
try {
    val result : StatusGet200Response = apiInstance.statusGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling PublicApi#statusGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling PublicApi#statusGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**StatusGet200Response**](StatusGet200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

