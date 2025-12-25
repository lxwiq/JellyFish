# OverrideruleApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**overrideRuleGet**](OverrideruleApi.md#overrideRuleGet) | **GET** /overrideRule | Get override rules |
| [**overrideRulePost**](OverrideruleApi.md#overrideRulePost) | **POST** /overrideRule | Create override rule |
| [**overrideRuleRuleIdDelete**](OverrideruleApi.md#overrideRuleRuleIdDelete) | **DELETE** /overrideRule/{ruleId} | Delete override rule by ID |
| [**overrideRuleRuleIdPut**](OverrideruleApi.md#overrideRuleRuleIdPut) | **PUT** /overrideRule/{ruleId} | Update override rule |


<a id="overrideRuleGet"></a>
# **overrideRuleGet**
> kotlin.collections.List&lt;OverrideRule&gt; overrideRuleGet()

Get override rules

Returns a list of all override rules with their conditions and settings

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = OverrideruleApi()
try {
    val result : kotlin.collections.List<OverrideRule> = apiInstance.overrideRuleGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OverrideruleApi#overrideRuleGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OverrideruleApi#overrideRuleGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;OverrideRule&gt;**](OverrideRule.md)

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

<a id="overrideRulePost"></a>
# **overrideRulePost**
> kotlin.collections.List&lt;OverrideRule&gt; overrideRulePost()

Create override rule

Creates a new Override Rule from the request body.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = OverrideruleApi()
try {
    val result : kotlin.collections.List<OverrideRule> = apiInstance.overrideRulePost()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OverrideruleApi#overrideRulePost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OverrideruleApi#overrideRulePost")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;OverrideRule&gt;**](OverrideRule.md)

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

<a id="overrideRuleRuleIdDelete"></a>
# **overrideRuleRuleIdDelete**
> OverrideRule overrideRuleRuleIdDelete(ruleId)

Delete override rule by ID

Deletes the override rule with the provided ruleId.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = OverrideruleApi()
val ruleId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
try {
    val result : OverrideRule = apiInstance.overrideRuleRuleIdDelete(ruleId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OverrideruleApi#overrideRuleRuleIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OverrideruleApi#overrideRuleRuleIdDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **ruleId** | **java.math.BigDecimal**|  | |

### Return type

[**OverrideRule**](OverrideRule.md)

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

<a id="overrideRuleRuleIdPut"></a>
# **overrideRuleRuleIdPut**
> kotlin.collections.List&lt;OverrideRule&gt; overrideRuleRuleIdPut(ruleId)

Update override rule

Updates an Override Rule from the request body.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = OverrideruleApi()
val ruleId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
try {
    val result : kotlin.collections.List<OverrideRule> = apiInstance.overrideRuleRuleIdPut(ruleId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling OverrideruleApi#overrideRuleRuleIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling OverrideruleApi#overrideRuleRuleIdPut")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **ruleId** | **java.math.BigDecimal**|  | |

### Return type

[**kotlin.collections.List&lt;OverrideRule&gt;**](OverrideRule.md)

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

