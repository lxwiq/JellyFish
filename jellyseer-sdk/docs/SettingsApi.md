# SettingsApi

All URIs are relative to *http://localhost:5055/api/v1*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**blacklistGet**](SettingsApi.md#blacklistGet) | **GET** /blacklist | Returns blacklisted items |
| [**settingsAboutGet**](SettingsApi.md#settingsAboutGet) | **GET** /settings/about | Get server stats |
| [**settingsCacheCacheIdFlushPost**](SettingsApi.md#settingsCacheCacheIdFlushPost) | **POST** /settings/cache/{cacheId}/flush | Flush a specific cache |
| [**settingsCacheDnsDnsEntryFlushPost**](SettingsApi.md#settingsCacheDnsDnsEntryFlushPost) | **POST** /settings/cache/dns/{dnsEntry}/flush | Flush a specific DNS cache entry |
| [**settingsCacheGet**](SettingsApi.md#settingsCacheGet) | **GET** /settings/cache | Get a list of active caches |
| [**settingsDiscoverAddPost**](SettingsApi.md#settingsDiscoverAddPost) | **POST** /settings/discover/add | Add a new slider |
| [**settingsDiscoverGet**](SettingsApi.md#settingsDiscoverGet) | **GET** /settings/discover | Get all discover sliders |
| [**settingsDiscoverPost**](SettingsApi.md#settingsDiscoverPost) | **POST** /settings/discover | Batch update all sliders. |
| [**settingsDiscoverResetGet**](SettingsApi.md#settingsDiscoverResetGet) | **GET** /settings/discover/reset | Reset all discover sliders |
| [**settingsDiscoverSliderIdDelete**](SettingsApi.md#settingsDiscoverSliderIdDelete) | **DELETE** /settings/discover/{sliderId} | Delete slider by ID |
| [**settingsDiscoverSliderIdPut**](SettingsApi.md#settingsDiscoverSliderIdPut) | **PUT** /settings/discover/{sliderId} | Update a single slider |
| [**settingsInitializePost**](SettingsApi.md#settingsInitializePost) | **POST** /settings/initialize | Initialize application |
| [**settingsJellyfinGet**](SettingsApi.md#settingsJellyfinGet) | **GET** /settings/jellyfin | Get Jellyfin settings |
| [**settingsJellyfinLibraryGet**](SettingsApi.md#settingsJellyfinLibraryGet) | **GET** /settings/jellyfin/library | Get Jellyfin libraries |
| [**settingsJellyfinPost**](SettingsApi.md#settingsJellyfinPost) | **POST** /settings/jellyfin | Update Jellyfin settings |
| [**settingsJellyfinSyncGet**](SettingsApi.md#settingsJellyfinSyncGet) | **GET** /settings/jellyfin/sync | Get status of full Jellyfin library sync |
| [**settingsJellyfinSyncPost**](SettingsApi.md#settingsJellyfinSyncPost) | **POST** /settings/jellyfin/sync | Start full Jellyfin library sync |
| [**settingsJellyfinUsersGet**](SettingsApi.md#settingsJellyfinUsersGet) | **GET** /settings/jellyfin/users | Get Jellyfin Users |
| [**settingsJobsGet**](SettingsApi.md#settingsJobsGet) | **GET** /settings/jobs | Get scheduled jobs |
| [**settingsJobsJobIdCancelPost**](SettingsApi.md#settingsJobsJobIdCancelPost) | **POST** /settings/jobs/{jobId}/cancel | Cancel a specific job |
| [**settingsJobsJobIdRunPost**](SettingsApi.md#settingsJobsJobIdRunPost) | **POST** /settings/jobs/{jobId}/run | Invoke a specific job |
| [**settingsJobsJobIdSchedulePost**](SettingsApi.md#settingsJobsJobIdSchedulePost) | **POST** /settings/jobs/{jobId}/schedule | Modify job schedule |
| [**settingsLogsGet**](SettingsApi.md#settingsLogsGet) | **GET** /settings/logs | Returns logs |
| [**settingsMainGet**](SettingsApi.md#settingsMainGet) | **GET** /settings/main | Get main settings |
| [**settingsMainPost**](SettingsApi.md#settingsMainPost) | **POST** /settings/main | Update main settings |
| [**settingsMainRegeneratePost**](SettingsApi.md#settingsMainRegeneratePost) | **POST** /settings/main/regenerate | Get main settings with newly-generated API key |
| [**settingsMetadatasGet**](SettingsApi.md#settingsMetadatasGet) | **GET** /settings/metadatas | Get Metadata settings |
| [**settingsMetadatasPut**](SettingsApi.md#settingsMetadatasPut) | **PUT** /settings/metadatas | Update Metadata settings |
| [**settingsMetadatasTestPost**](SettingsApi.md#settingsMetadatasTestPost) | **POST** /settings/metadatas/test | Test Provider configuration |
| [**settingsNetworkGet**](SettingsApi.md#settingsNetworkGet) | **GET** /settings/network | Get network settings |
| [**settingsNetworkPost**](SettingsApi.md#settingsNetworkPost) | **POST** /settings/network | Update network settings |
| [**settingsNotificationsDiscordGet**](SettingsApi.md#settingsNotificationsDiscordGet) | **GET** /settings/notifications/discord | Get Discord notification settings |
| [**settingsNotificationsDiscordPost**](SettingsApi.md#settingsNotificationsDiscordPost) | **POST** /settings/notifications/discord | Update Discord notification settings |
| [**settingsNotificationsDiscordTestPost**](SettingsApi.md#settingsNotificationsDiscordTestPost) | **POST** /settings/notifications/discord/test | Test Discord settings |
| [**settingsNotificationsEmailGet**](SettingsApi.md#settingsNotificationsEmailGet) | **GET** /settings/notifications/email | Get email notification settings |
| [**settingsNotificationsEmailPost**](SettingsApi.md#settingsNotificationsEmailPost) | **POST** /settings/notifications/email | Update email notification settings |
| [**settingsNotificationsEmailTestPost**](SettingsApi.md#settingsNotificationsEmailTestPost) | **POST** /settings/notifications/email/test | Test email settings |
| [**settingsNotificationsGotifyGet**](SettingsApi.md#settingsNotificationsGotifyGet) | **GET** /settings/notifications/gotify | Get Gotify notification settings |
| [**settingsNotificationsGotifyPost**](SettingsApi.md#settingsNotificationsGotifyPost) | **POST** /settings/notifications/gotify | Update Gotify notification settings |
| [**settingsNotificationsGotifyTestPost**](SettingsApi.md#settingsNotificationsGotifyTestPost) | **POST** /settings/notifications/gotify/test | Test Gotify settings |
| [**settingsNotificationsNtfyGet**](SettingsApi.md#settingsNotificationsNtfyGet) | **GET** /settings/notifications/ntfy | Get ntfy.sh notification settings |
| [**settingsNotificationsNtfyPost**](SettingsApi.md#settingsNotificationsNtfyPost) | **POST** /settings/notifications/ntfy | Update ntfy.sh notification settings |
| [**settingsNotificationsNtfyTestPost**](SettingsApi.md#settingsNotificationsNtfyTestPost) | **POST** /settings/notifications/ntfy/test | Test ntfy.sh settings |
| [**settingsNotificationsPushbulletGet**](SettingsApi.md#settingsNotificationsPushbulletGet) | **GET** /settings/notifications/pushbullet | Get Pushbullet notification settings |
| [**settingsNotificationsPushbulletPost**](SettingsApi.md#settingsNotificationsPushbulletPost) | **POST** /settings/notifications/pushbullet | Update Pushbullet notification settings |
| [**settingsNotificationsPushbulletTestPost**](SettingsApi.md#settingsNotificationsPushbulletTestPost) | **POST** /settings/notifications/pushbullet/test | Test Pushbullet settings |
| [**settingsNotificationsPushoverGet**](SettingsApi.md#settingsNotificationsPushoverGet) | **GET** /settings/notifications/pushover | Get Pushover notification settings |
| [**settingsNotificationsPushoverPost**](SettingsApi.md#settingsNotificationsPushoverPost) | **POST** /settings/notifications/pushover | Update Pushover notification settings |
| [**settingsNotificationsPushoverSoundsGet**](SettingsApi.md#settingsNotificationsPushoverSoundsGet) | **GET** /settings/notifications/pushover/sounds | Get Pushover sounds |
| [**settingsNotificationsPushoverTestPost**](SettingsApi.md#settingsNotificationsPushoverTestPost) | **POST** /settings/notifications/pushover/test | Test Pushover settings |
| [**settingsNotificationsSlackGet**](SettingsApi.md#settingsNotificationsSlackGet) | **GET** /settings/notifications/slack | Get Slack notification settings |
| [**settingsNotificationsSlackPost**](SettingsApi.md#settingsNotificationsSlackPost) | **POST** /settings/notifications/slack | Update Slack notification settings |
| [**settingsNotificationsSlackTestPost**](SettingsApi.md#settingsNotificationsSlackTestPost) | **POST** /settings/notifications/slack/test | Test Slack settings |
| [**settingsNotificationsTelegramGet**](SettingsApi.md#settingsNotificationsTelegramGet) | **GET** /settings/notifications/telegram | Get Telegram notification settings |
| [**settingsNotificationsTelegramPost**](SettingsApi.md#settingsNotificationsTelegramPost) | **POST** /settings/notifications/telegram | Update Telegram notification settings |
| [**settingsNotificationsTelegramTestPost**](SettingsApi.md#settingsNotificationsTelegramTestPost) | **POST** /settings/notifications/telegram/test | Test Telegram settings |
| [**settingsNotificationsWebhookGet**](SettingsApi.md#settingsNotificationsWebhookGet) | **GET** /settings/notifications/webhook | Get webhook notification settings |
| [**settingsNotificationsWebhookPost**](SettingsApi.md#settingsNotificationsWebhookPost) | **POST** /settings/notifications/webhook | Update webhook notification settings |
| [**settingsNotificationsWebhookTestPost**](SettingsApi.md#settingsNotificationsWebhookTestPost) | **POST** /settings/notifications/webhook/test | Test webhook settings |
| [**settingsNotificationsWebpushGet**](SettingsApi.md#settingsNotificationsWebpushGet) | **GET** /settings/notifications/webpush | Get Web Push notification settings |
| [**settingsNotificationsWebpushPost**](SettingsApi.md#settingsNotificationsWebpushPost) | **POST** /settings/notifications/webpush | Update Web Push notification settings |
| [**settingsNotificationsWebpushTestPost**](SettingsApi.md#settingsNotificationsWebpushTestPost) | **POST** /settings/notifications/webpush/test | Test Web Push settings |
| [**settingsPlexDevicesServersGet**](SettingsApi.md#settingsPlexDevicesServersGet) | **GET** /settings/plex/devices/servers | Gets the user&#39;s available Plex servers |
| [**settingsPlexGet**](SettingsApi.md#settingsPlexGet) | **GET** /settings/plex | Get Plex settings |
| [**settingsPlexLibraryGet**](SettingsApi.md#settingsPlexLibraryGet) | **GET** /settings/plex/library | Get Plex libraries |
| [**settingsPlexPost**](SettingsApi.md#settingsPlexPost) | **POST** /settings/plex | Update Plex settings |
| [**settingsPlexSyncGet**](SettingsApi.md#settingsPlexSyncGet) | **GET** /settings/plex/sync | Get status of full Plex library scan |
| [**settingsPlexSyncPost**](SettingsApi.md#settingsPlexSyncPost) | **POST** /settings/plex/sync | Start full Plex library scan |
| [**settingsPlexUsersGet**](SettingsApi.md#settingsPlexUsersGet) | **GET** /settings/plex/users | Get Plex users |
| [**settingsPublicGet**](SettingsApi.md#settingsPublicGet) | **GET** /settings/public | Get public settings |
| [**settingsRadarrGet**](SettingsApi.md#settingsRadarrGet) | **GET** /settings/radarr | Get Radarr settings |
| [**settingsRadarrPost**](SettingsApi.md#settingsRadarrPost) | **POST** /settings/radarr | Create Radarr instance |
| [**settingsRadarrRadarrIdDelete**](SettingsApi.md#settingsRadarrRadarrIdDelete) | **DELETE** /settings/radarr/{radarrId} | Delete Radarr instance |
| [**settingsRadarrRadarrIdProfilesGet**](SettingsApi.md#settingsRadarrRadarrIdProfilesGet) | **GET** /settings/radarr/{radarrId}/profiles | Get available Radarr profiles |
| [**settingsRadarrRadarrIdPut**](SettingsApi.md#settingsRadarrRadarrIdPut) | **PUT** /settings/radarr/{radarrId} | Update Radarr instance |
| [**settingsRadarrTestPost**](SettingsApi.md#settingsRadarrTestPost) | **POST** /settings/radarr/test | Test Radarr configuration |
| [**settingsSonarrGet**](SettingsApi.md#settingsSonarrGet) | **GET** /settings/sonarr | Get Sonarr settings |
| [**settingsSonarrPost**](SettingsApi.md#settingsSonarrPost) | **POST** /settings/sonarr | Create Sonarr instance |
| [**settingsSonarrSonarrIdDelete**](SettingsApi.md#settingsSonarrSonarrIdDelete) | **DELETE** /settings/sonarr/{sonarrId} | Delete Sonarr instance |
| [**settingsSonarrSonarrIdPut**](SettingsApi.md#settingsSonarrSonarrIdPut) | **PUT** /settings/sonarr/{sonarrId} | Update Sonarr instance |
| [**settingsSonarrTestPost**](SettingsApi.md#settingsSonarrTestPost) | **POST** /settings/sonarr/test | Test Sonarr configuration |
| [**settingsTautulliGet**](SettingsApi.md#settingsTautulliGet) | **GET** /settings/tautulli | Get Tautulli settings |
| [**settingsTautulliPost**](SettingsApi.md#settingsTautulliPost) | **POST** /settings/tautulli | Update Tautulli settings |


<a id="blacklistGet"></a>
# **blacklistGet**
> BlacklistGet200Response blacklistGet(take, skip, search, filter)

Returns blacklisted items

Returns list of all blacklisted media

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val take : java.math.BigDecimal = 25 // java.math.BigDecimal | 
val skip : java.math.BigDecimal = 0 // java.math.BigDecimal | 
val search : kotlin.String = dune // kotlin.String | 
val filter : kotlin.String = filter_example // kotlin.String | 
try {
    val result : BlacklistGet200Response = apiInstance.blacklistGet(take, skip, search, filter)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#blacklistGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#blacklistGet")
    e.printStackTrace()
}
```

### Parameters
| **take** | **java.math.BigDecimal**|  | [optional] |
| **skip** | **java.math.BigDecimal**|  | [optional] |
| **search** | **kotlin.String**|  | [optional] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **filter** | **kotlin.String**|  | [optional] [default to Filter.manual] [enum: all, manual, blacklistedTags] |

### Return type

[**BlacklistGet200Response**](BlacklistGet200Response.md)

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

<a id="settingsAboutGet"></a>
# **settingsAboutGet**
> SettingsAboutGet200Response settingsAboutGet()

Get server stats

Returns current server stats in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : SettingsAboutGet200Response = apiInstance.settingsAboutGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsAboutGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsAboutGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsAboutGet200Response**](SettingsAboutGet200Response.md)

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

<a id="settingsCacheCacheIdFlushPost"></a>
# **settingsCacheCacheIdFlushPost**
> settingsCacheCacheIdFlushPost(cacheId)

Flush a specific cache

Flushes all data from the cache ID provided

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val cacheId : kotlin.String = cacheId_example // kotlin.String | 
try {
    apiInstance.settingsCacheCacheIdFlushPost(cacheId)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsCacheCacheIdFlushPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsCacheCacheIdFlushPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **cacheId** | **kotlin.String**|  | |

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

<a id="settingsCacheDnsDnsEntryFlushPost"></a>
# **settingsCacheDnsDnsEntryFlushPost**
> settingsCacheDnsDnsEntryFlushPost(dnsEntry)

Flush a specific DNS cache entry

Flushes a specific DNS cache entry

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val dnsEntry : kotlin.String = dnsEntry_example // kotlin.String | 
try {
    apiInstance.settingsCacheDnsDnsEntryFlushPost(dnsEntry)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsCacheDnsDnsEntryFlushPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsCacheDnsDnsEntryFlushPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **dnsEntry** | **kotlin.String**|  | |

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

<a id="settingsCacheGet"></a>
# **settingsCacheGet**
> SettingsCacheGet200Response settingsCacheGet()

Get a list of active caches

Retrieves a list of all active caches and their current stats.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : SettingsCacheGet200Response = apiInstance.settingsCacheGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsCacheGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsCacheGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsCacheGet200Response**](SettingsCacheGet200Response.md)

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

<a id="settingsDiscoverAddPost"></a>
# **settingsDiscoverAddPost**
> DiscoverSlider settingsDiscoverAddPost(settingsDiscoverAddPostRequest)

Add a new slider

Add a single slider and return the newly created slider. Requires the &#x60;ADMIN&#x60; permission. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val settingsDiscoverAddPostRequest : SettingsDiscoverAddPostRequest =  // SettingsDiscoverAddPostRequest | 
try {
    val result : DiscoverSlider = apiInstance.settingsDiscoverAddPost(settingsDiscoverAddPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsDiscoverAddPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsDiscoverAddPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **settingsDiscoverAddPostRequest** | [**SettingsDiscoverAddPostRequest**](SettingsDiscoverAddPostRequest.md)|  | |

### Return type

[**DiscoverSlider**](DiscoverSlider.md)

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

<a id="settingsDiscoverGet"></a>
# **settingsDiscoverGet**
> kotlin.collections.List&lt;DiscoverSlider&gt; settingsDiscoverGet()

Get all discover sliders

Returns all discovery sliders. Built-in and custom made.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : kotlin.collections.List<DiscoverSlider> = apiInstance.settingsDiscoverGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsDiscoverGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsDiscoverGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;DiscoverSlider&gt;**](DiscoverSlider.md)

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

<a id="settingsDiscoverPost"></a>
# **settingsDiscoverPost**
> kotlin.collections.List&lt;DiscoverSlider&gt; settingsDiscoverPost(discoverSlider)

Batch update all sliders.

Batch update all sliders at once. Should also be used for creation. Will only update sliders provided and will not delete any sliders not present in the request. If a slider is missing a required field, it will be ignored. Requires the &#x60;ADMIN&#x60; permission. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val discoverSlider : kotlin.collections.List<DiscoverSlider> =  // kotlin.collections.List<DiscoverSlider> | 
try {
    val result : kotlin.collections.List<DiscoverSlider> = apiInstance.settingsDiscoverPost(discoverSlider)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsDiscoverPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsDiscoverPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **discoverSlider** | [**kotlin.collections.List&lt;DiscoverSlider&gt;**](DiscoverSlider.md)|  | |

### Return type

[**kotlin.collections.List&lt;DiscoverSlider&gt;**](DiscoverSlider.md)

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

<a id="settingsDiscoverResetGet"></a>
# **settingsDiscoverResetGet**
> settingsDiscoverResetGet()

Reset all discover sliders

Resets all discovery sliders to the default values. Requires the &#x60;ADMIN&#x60; permission.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    apiInstance.settingsDiscoverResetGet()
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsDiscoverResetGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsDiscoverResetGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

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

<a id="settingsDiscoverSliderIdDelete"></a>
# **settingsDiscoverSliderIdDelete**
> DiscoverSlider settingsDiscoverSliderIdDelete(sliderId)

Delete slider by ID

Deletes the slider with the provided sliderId. Requires the &#x60;ADMIN&#x60; permission.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val sliderId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
try {
    val result : DiscoverSlider = apiInstance.settingsDiscoverSliderIdDelete(sliderId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsDiscoverSliderIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsDiscoverSliderIdDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sliderId** | **java.math.BigDecimal**|  | |

### Return type

[**DiscoverSlider**](DiscoverSlider.md)

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

<a id="settingsDiscoverSliderIdPut"></a>
# **settingsDiscoverSliderIdPut**
> DiscoverSlider settingsDiscoverSliderIdPut(sliderId, settingsDiscoverSliderIdPutRequest)

Update a single slider

Updates a single slider and return the newly updated slider. Requires the &#x60;ADMIN&#x60; permission. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val sliderId : java.math.BigDecimal = 8.14 // java.math.BigDecimal | 
val settingsDiscoverSliderIdPutRequest : SettingsDiscoverSliderIdPutRequest =  // SettingsDiscoverSliderIdPutRequest | 
try {
    val result : DiscoverSlider = apiInstance.settingsDiscoverSliderIdPut(sliderId, settingsDiscoverSliderIdPutRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsDiscoverSliderIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsDiscoverSliderIdPut")
    e.printStackTrace()
}
```

### Parameters
| **sliderId** | **java.math.BigDecimal**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **settingsDiscoverSliderIdPutRequest** | [**SettingsDiscoverSliderIdPutRequest**](SettingsDiscoverSliderIdPutRequest.md)|  | |

### Return type

[**DiscoverSlider**](DiscoverSlider.md)

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

<a id="settingsInitializePost"></a>
# **settingsInitializePost**
> PublicSettings settingsInitializePost()

Initialize application

Sets the app as initialized, allowing the user to navigate to pages other than the setup page.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : PublicSettings = apiInstance.settingsInitializePost()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsInitializePost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsInitializePost")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**PublicSettings**](PublicSettings.md)

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

<a id="settingsJellyfinGet"></a>
# **settingsJellyfinGet**
> JellyfinSettings settingsJellyfinGet()

Get Jellyfin settings

Retrieves current Jellyfin settings.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : JellyfinSettings = apiInstance.settingsJellyfinGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsJellyfinGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsJellyfinGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**JellyfinSettings**](JellyfinSettings.md)

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

<a id="settingsJellyfinLibraryGet"></a>
# **settingsJellyfinLibraryGet**
> kotlin.collections.List&lt;JellyfinLibrary&gt; settingsJellyfinLibraryGet(sync, enable)

Get Jellyfin libraries

Returns a list of Jellyfin libraries in a JSON array.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val sync : kotlin.String = sync_example // kotlin.String | Syncs the current libraries with the current Jellyfin server
val enable : kotlin.String = enable_example // kotlin.String | Comma separated list of libraries to enable. Any libraries not passed will be disabled!
try {
    val result : kotlin.collections.List<JellyfinLibrary> = apiInstance.settingsJellyfinLibraryGet(sync, enable)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsJellyfinLibraryGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsJellyfinLibraryGet")
    e.printStackTrace()
}
```

### Parameters
| **sync** | **kotlin.String**| Syncs the current libraries with the current Jellyfin server | [optional] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **enable** | **kotlin.String**| Comma separated list of libraries to enable. Any libraries not passed will be disabled! | [optional] |

### Return type

[**kotlin.collections.List&lt;JellyfinLibrary&gt;**](JellyfinLibrary.md)

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

<a id="settingsJellyfinPost"></a>
# **settingsJellyfinPost**
> JellyfinSettings settingsJellyfinPost(jellyfinSettings)

Update Jellyfin settings

Updates Jellyfin settings with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val jellyfinSettings : JellyfinSettings =  // JellyfinSettings | 
try {
    val result : JellyfinSettings = apiInstance.settingsJellyfinPost(jellyfinSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsJellyfinPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsJellyfinPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **jellyfinSettings** | [**JellyfinSettings**](JellyfinSettings.md)|  | |

### Return type

[**JellyfinSettings**](JellyfinSettings.md)

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

<a id="settingsJellyfinSyncGet"></a>
# **settingsJellyfinSyncGet**
> SettingsJellyfinSyncGet200Response settingsJellyfinSyncGet()

Get status of full Jellyfin library sync

Returns sync progress in a JSON array.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : SettingsJellyfinSyncGet200Response = apiInstance.settingsJellyfinSyncGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsJellyfinSyncGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsJellyfinSyncGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsJellyfinSyncGet200Response**](SettingsJellyfinSyncGet200Response.md)

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

<a id="settingsJellyfinSyncPost"></a>
# **settingsJellyfinSyncPost**
> SettingsJellyfinSyncGet200Response settingsJellyfinSyncPost(settingsJellyfinSyncPostRequest)

Start full Jellyfin library sync

Runs a full Jellyfin library sync and returns the progress in a JSON array.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val settingsJellyfinSyncPostRequest : SettingsJellyfinSyncPostRequest =  // SettingsJellyfinSyncPostRequest | 
try {
    val result : SettingsJellyfinSyncGet200Response = apiInstance.settingsJellyfinSyncPost(settingsJellyfinSyncPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsJellyfinSyncPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsJellyfinSyncPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **settingsJellyfinSyncPostRequest** | [**SettingsJellyfinSyncPostRequest**](SettingsJellyfinSyncPostRequest.md)|  | [optional] |

### Return type

[**SettingsJellyfinSyncGet200Response**](SettingsJellyfinSyncGet200Response.md)

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

<a id="settingsJellyfinUsersGet"></a>
# **settingsJellyfinUsersGet**
> kotlin.collections.List&lt;SettingsJellyfinUsersGet200ResponseInner&gt; settingsJellyfinUsersGet()

Get Jellyfin Users

Returns a list of Jellyfin Users in a JSON array.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : kotlin.collections.List<SettingsJellyfinUsersGet200ResponseInner> = apiInstance.settingsJellyfinUsersGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsJellyfinUsersGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsJellyfinUsersGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;SettingsJellyfinUsersGet200ResponseInner&gt;**](SettingsJellyfinUsersGet200ResponseInner.md)

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

<a id="settingsJobsGet"></a>
# **settingsJobsGet**
> kotlin.collections.List&lt;Job&gt; settingsJobsGet()

Get scheduled jobs

Returns list of all scheduled jobs and details about their next execution time in a JSON array.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : kotlin.collections.List<Job> = apiInstance.settingsJobsGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsJobsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsJobsGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;Job&gt;**](Job.md)

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

<a id="settingsJobsJobIdCancelPost"></a>
# **settingsJobsJobIdCancelPost**
> Job settingsJobsJobIdCancelPost(jobId)

Cancel a specific job

Cancels a specific job. Will return the new job status in JSON format.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val jobId : kotlin.String = jobId_example // kotlin.String | 
try {
    val result : Job = apiInstance.settingsJobsJobIdCancelPost(jobId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsJobsJobIdCancelPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsJobsJobIdCancelPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **jobId** | **kotlin.String**|  | |

### Return type

[**Job**](Job.md)

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

<a id="settingsJobsJobIdRunPost"></a>
# **settingsJobsJobIdRunPost**
> Job settingsJobsJobIdRunPost(jobId)

Invoke a specific job

Invokes a specific job to run. Will return the new job status in JSON format.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val jobId : kotlin.String = jobId_example // kotlin.String | 
try {
    val result : Job = apiInstance.settingsJobsJobIdRunPost(jobId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsJobsJobIdRunPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsJobsJobIdRunPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **jobId** | **kotlin.String**|  | |

### Return type

[**Job**](Job.md)

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

<a id="settingsJobsJobIdSchedulePost"></a>
# **settingsJobsJobIdSchedulePost**
> Job settingsJobsJobIdSchedulePost(jobId, settingsJobsJobIdSchedulePostRequest)

Modify job schedule

Re-registers the job with the schedule specified. Will return the job in JSON format.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val jobId : kotlin.String = jobId_example // kotlin.String | 
val settingsJobsJobIdSchedulePostRequest : SettingsJobsJobIdSchedulePostRequest =  // SettingsJobsJobIdSchedulePostRequest | 
try {
    val result : Job = apiInstance.settingsJobsJobIdSchedulePost(jobId, settingsJobsJobIdSchedulePostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsJobsJobIdSchedulePost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsJobsJobIdSchedulePost")
    e.printStackTrace()
}
```

### Parameters
| **jobId** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **settingsJobsJobIdSchedulePostRequest** | [**SettingsJobsJobIdSchedulePostRequest**](SettingsJobsJobIdSchedulePostRequest.md)|  | |

### Return type

[**Job**](Job.md)

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

<a id="settingsLogsGet"></a>
# **settingsLogsGet**
> kotlin.collections.List&lt;SettingsLogsGet200ResponseInner&gt; settingsLogsGet(take, skip, filter, search)

Returns logs

Returns list of all log items and details

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val take : java.math.BigDecimal = 25 // java.math.BigDecimal | 
val skip : java.math.BigDecimal = 0 // java.math.BigDecimal | 
val filter : kotlin.String = filter_example // kotlin.String | 
val search : kotlin.String = plex // kotlin.String | 
try {
    val result : kotlin.collections.List<SettingsLogsGet200ResponseInner> = apiInstance.settingsLogsGet(take, skip, filter, search)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsLogsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsLogsGet")
    e.printStackTrace()
}
```

### Parameters
| **take** | **java.math.BigDecimal**|  | [optional] |
| **skip** | **java.math.BigDecimal**|  | [optional] |
| **filter** | **kotlin.String**|  | [optional] [default to Filter.debug] [enum: debug, info, warn, error] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **search** | **kotlin.String**|  | [optional] |

### Return type

[**kotlin.collections.List&lt;SettingsLogsGet200ResponseInner&gt;**](SettingsLogsGet200ResponseInner.md)

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

<a id="settingsMainGet"></a>
# **settingsMainGet**
> MainSettings settingsMainGet()

Get main settings

Retrieves all main settings in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : MainSettings = apiInstance.settingsMainGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsMainGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsMainGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**MainSettings**](MainSettings.md)

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

<a id="settingsMainPost"></a>
# **settingsMainPost**
> MainSettings settingsMainPost(mainSettings)

Update main settings

Updates main settings with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val mainSettings : MainSettings =  // MainSettings | 
try {
    val result : MainSettings = apiInstance.settingsMainPost(mainSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsMainPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsMainPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **mainSettings** | [**MainSettings**](MainSettings.md)|  | |

### Return type

[**MainSettings**](MainSettings.md)

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

<a id="settingsMainRegeneratePost"></a>
# **settingsMainRegeneratePost**
> MainSettings settingsMainRegeneratePost()

Get main settings with newly-generated API key

Returns main settings in a JSON object, using the new API key.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : MainSettings = apiInstance.settingsMainRegeneratePost()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsMainRegeneratePost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsMainRegeneratePost")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**MainSettings**](MainSettings.md)

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

<a id="settingsMetadatasGet"></a>
# **settingsMetadatasGet**
> MetadataSettings settingsMetadatasGet()

Get Metadata settings

Retrieves current Metadata settings.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : MetadataSettings = apiInstance.settingsMetadatasGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsMetadatasGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsMetadatasGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**MetadataSettings**](MetadataSettings.md)

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

<a id="settingsMetadatasPut"></a>
# **settingsMetadatasPut**
> MetadataSettings settingsMetadatasPut(metadataSettings)

Update Metadata settings

Updates Metadata settings with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val metadataSettings : MetadataSettings =  // MetadataSettings | 
try {
    val result : MetadataSettings = apiInstance.settingsMetadatasPut(metadataSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsMetadatasPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsMetadatasPut")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **metadataSettings** | [**MetadataSettings**](MetadataSettings.md)|  | |

### Return type

[**MetadataSettings**](MetadataSettings.md)

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

<a id="settingsMetadatasTestPost"></a>
# **settingsMetadatasTestPost**
> SettingsMetadatasTestPost200Response settingsMetadatasTestPost(settingsMetadatasTestPostRequest)

Test Provider configuration

Tests if the TVDB configuration is valid. Returns a list of available languages on success.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val settingsMetadatasTestPostRequest : SettingsMetadatasTestPostRequest =  // SettingsMetadatasTestPostRequest | 
try {
    val result : SettingsMetadatasTestPost200Response = apiInstance.settingsMetadatasTestPost(settingsMetadatasTestPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsMetadatasTestPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsMetadatasTestPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **settingsMetadatasTestPostRequest** | [**SettingsMetadatasTestPostRequest**](SettingsMetadatasTestPostRequest.md)|  | |

### Return type

[**SettingsMetadatasTestPost200Response**](SettingsMetadatasTestPost200Response.md)

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

<a id="settingsNetworkGet"></a>
# **settingsNetworkGet**
> MainSettings settingsNetworkGet()

Get network settings

Retrieves all network settings in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : MainSettings = apiInstance.settingsNetworkGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNetworkGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNetworkGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**MainSettings**](MainSettings.md)

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

<a id="settingsNetworkPost"></a>
# **settingsNetworkPost**
> NetworkSettings settingsNetworkPost(networkSettings)

Update network settings

Updates network settings with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val networkSettings : NetworkSettings =  // NetworkSettings | 
try {
    val result : NetworkSettings = apiInstance.settingsNetworkPost(networkSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNetworkPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNetworkPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **networkSettings** | [**NetworkSettings**](NetworkSettings.md)|  | |

### Return type

[**NetworkSettings**](NetworkSettings.md)

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

<a id="settingsNotificationsDiscordGet"></a>
# **settingsNotificationsDiscordGet**
> DiscordSettings settingsNotificationsDiscordGet()

Get Discord notification settings

Returns current Discord notification settings in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : DiscordSettings = apiInstance.settingsNotificationsDiscordGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsDiscordGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsDiscordGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**DiscordSettings**](DiscordSettings.md)

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

<a id="settingsNotificationsDiscordPost"></a>
# **settingsNotificationsDiscordPost**
> DiscordSettings settingsNotificationsDiscordPost(discordSettings)

Update Discord notification settings

Updates Discord notification settings with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val discordSettings : DiscordSettings =  // DiscordSettings | 
try {
    val result : DiscordSettings = apiInstance.settingsNotificationsDiscordPost(discordSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsDiscordPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsDiscordPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **discordSettings** | [**DiscordSettings**](DiscordSettings.md)|  | |

### Return type

[**DiscordSettings**](DiscordSettings.md)

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

<a id="settingsNotificationsDiscordTestPost"></a>
# **settingsNotificationsDiscordTestPost**
> settingsNotificationsDiscordTestPost(discordSettings)

Test Discord settings

Sends a test notification to the Discord agent.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val discordSettings : DiscordSettings =  // DiscordSettings | 
try {
    apiInstance.settingsNotificationsDiscordTestPost(discordSettings)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsDiscordTestPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsDiscordTestPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **discordSettings** | [**DiscordSettings**](DiscordSettings.md)|  | |

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

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="settingsNotificationsEmailGet"></a>
# **settingsNotificationsEmailGet**
> NotificationEmailSettings settingsNotificationsEmailGet()

Get email notification settings

Returns current email notification settings in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : NotificationEmailSettings = apiInstance.settingsNotificationsEmailGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsEmailGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsEmailGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**NotificationEmailSettings**](NotificationEmailSettings.md)

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

<a id="settingsNotificationsEmailPost"></a>
# **settingsNotificationsEmailPost**
> NotificationEmailSettings settingsNotificationsEmailPost(notificationEmailSettings)

Update email notification settings

Updates email notification settings with provided values

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val notificationEmailSettings : NotificationEmailSettings =  // NotificationEmailSettings | 
try {
    val result : NotificationEmailSettings = apiInstance.settingsNotificationsEmailPost(notificationEmailSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsEmailPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsEmailPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **notificationEmailSettings** | [**NotificationEmailSettings**](NotificationEmailSettings.md)|  | |

### Return type

[**NotificationEmailSettings**](NotificationEmailSettings.md)

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

<a id="settingsNotificationsEmailTestPost"></a>
# **settingsNotificationsEmailTestPost**
> settingsNotificationsEmailTestPost(notificationEmailSettings)

Test email settings

Sends a test notification to the email agent.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val notificationEmailSettings : NotificationEmailSettings =  // NotificationEmailSettings | 
try {
    apiInstance.settingsNotificationsEmailTestPost(notificationEmailSettings)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsEmailTestPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsEmailTestPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **notificationEmailSettings** | [**NotificationEmailSettings**](NotificationEmailSettings.md)|  | |

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

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="settingsNotificationsGotifyGet"></a>
# **settingsNotificationsGotifyGet**
> GotifySettings settingsNotificationsGotifyGet()

Get Gotify notification settings

Returns current Gotify notification settings in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : GotifySettings = apiInstance.settingsNotificationsGotifyGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsGotifyGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsGotifyGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**GotifySettings**](GotifySettings.md)

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

<a id="settingsNotificationsGotifyPost"></a>
# **settingsNotificationsGotifyPost**
> GotifySettings settingsNotificationsGotifyPost(gotifySettings)

Update Gotify notification settings

Update Gotify notification settings with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val gotifySettings : GotifySettings =  // GotifySettings | 
try {
    val result : GotifySettings = apiInstance.settingsNotificationsGotifyPost(gotifySettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsGotifyPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsGotifyPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **gotifySettings** | [**GotifySettings**](GotifySettings.md)|  | |

### Return type

[**GotifySettings**](GotifySettings.md)

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

<a id="settingsNotificationsGotifyTestPost"></a>
# **settingsNotificationsGotifyTestPost**
> settingsNotificationsGotifyTestPost(gotifySettings)

Test Gotify settings

Sends a test notification to the Gotify agent.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val gotifySettings : GotifySettings =  // GotifySettings | 
try {
    apiInstance.settingsNotificationsGotifyTestPost(gotifySettings)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsGotifyTestPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsGotifyTestPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **gotifySettings** | [**GotifySettings**](GotifySettings.md)|  | |

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

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="settingsNotificationsNtfyGet"></a>
# **settingsNotificationsNtfyGet**
> NtfySettings settingsNotificationsNtfyGet()

Get ntfy.sh notification settings

Returns current ntfy.sh notification settings in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : NtfySettings = apiInstance.settingsNotificationsNtfyGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsNtfyGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsNtfyGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**NtfySettings**](NtfySettings.md)

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

<a id="settingsNotificationsNtfyPost"></a>
# **settingsNotificationsNtfyPost**
> NtfySettings settingsNotificationsNtfyPost(ntfySettings)

Update ntfy.sh notification settings

Update ntfy.sh notification settings with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val ntfySettings : NtfySettings =  // NtfySettings | 
try {
    val result : NtfySettings = apiInstance.settingsNotificationsNtfyPost(ntfySettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsNtfyPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsNtfyPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **ntfySettings** | [**NtfySettings**](NtfySettings.md)|  | |

### Return type

[**NtfySettings**](NtfySettings.md)

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

<a id="settingsNotificationsNtfyTestPost"></a>
# **settingsNotificationsNtfyTestPost**
> settingsNotificationsNtfyTestPost(ntfySettings)

Test ntfy.sh settings

Sends a test notification to the ntfy.sh agent.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val ntfySettings : NtfySettings =  // NtfySettings | 
try {
    apiInstance.settingsNotificationsNtfyTestPost(ntfySettings)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsNtfyTestPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsNtfyTestPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **ntfySettings** | [**NtfySettings**](NtfySettings.md)|  | |

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

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="settingsNotificationsPushbulletGet"></a>
# **settingsNotificationsPushbulletGet**
> PushbulletSettings settingsNotificationsPushbulletGet()

Get Pushbullet notification settings

Returns current Pushbullet notification settings in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : PushbulletSettings = apiInstance.settingsNotificationsPushbulletGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsPushbulletGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsPushbulletGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**PushbulletSettings**](PushbulletSettings.md)

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

<a id="settingsNotificationsPushbulletPost"></a>
# **settingsNotificationsPushbulletPost**
> PushbulletSettings settingsNotificationsPushbulletPost(pushbulletSettings)

Update Pushbullet notification settings

Update Pushbullet notification settings with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val pushbulletSettings : PushbulletSettings =  // PushbulletSettings | 
try {
    val result : PushbulletSettings = apiInstance.settingsNotificationsPushbulletPost(pushbulletSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsPushbulletPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsPushbulletPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **pushbulletSettings** | [**PushbulletSettings**](PushbulletSettings.md)|  | |

### Return type

[**PushbulletSettings**](PushbulletSettings.md)

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

<a id="settingsNotificationsPushbulletTestPost"></a>
# **settingsNotificationsPushbulletTestPost**
> settingsNotificationsPushbulletTestPost(pushbulletSettings)

Test Pushbullet settings

Sends a test notification to the Pushbullet agent.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val pushbulletSettings : PushbulletSettings =  // PushbulletSettings | 
try {
    apiInstance.settingsNotificationsPushbulletTestPost(pushbulletSettings)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsPushbulletTestPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsPushbulletTestPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **pushbulletSettings** | [**PushbulletSettings**](PushbulletSettings.md)|  | |

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

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="settingsNotificationsPushoverGet"></a>
# **settingsNotificationsPushoverGet**
> PushoverSettings settingsNotificationsPushoverGet()

Get Pushover notification settings

Returns current Pushover notification settings in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : PushoverSettings = apiInstance.settingsNotificationsPushoverGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsPushoverGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsPushoverGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**PushoverSettings**](PushoverSettings.md)

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

<a id="settingsNotificationsPushoverPost"></a>
# **settingsNotificationsPushoverPost**
> PushoverSettings settingsNotificationsPushoverPost(pushoverSettings)

Update Pushover notification settings

Update Pushover notification settings with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val pushoverSettings : PushoverSettings =  // PushoverSettings | 
try {
    val result : PushoverSettings = apiInstance.settingsNotificationsPushoverPost(pushoverSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsPushoverPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsPushoverPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **pushoverSettings** | [**PushoverSettings**](PushoverSettings.md)|  | |

### Return type

[**PushoverSettings**](PushoverSettings.md)

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

<a id="settingsNotificationsPushoverSoundsGet"></a>
# **settingsNotificationsPushoverSoundsGet**
> kotlin.collections.List&lt;SettingsNotificationsPushoverSoundsGet200ResponseInner&gt; settingsNotificationsPushoverSoundsGet(token)

Get Pushover sounds

Returns valid Pushover sound options in a JSON array.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val token : kotlin.String = token_example // kotlin.String | 
try {
    val result : kotlin.collections.List<SettingsNotificationsPushoverSoundsGet200ResponseInner> = apiInstance.settingsNotificationsPushoverSoundsGet(token)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsPushoverSoundsGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsPushoverSoundsGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **token** | **kotlin.String**|  | |

### Return type

[**kotlin.collections.List&lt;SettingsNotificationsPushoverSoundsGet200ResponseInner&gt;**](SettingsNotificationsPushoverSoundsGet200ResponseInner.md)

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

<a id="settingsNotificationsPushoverTestPost"></a>
# **settingsNotificationsPushoverTestPost**
> settingsNotificationsPushoverTestPost(pushoverSettings)

Test Pushover settings

Sends a test notification to the Pushover agent.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val pushoverSettings : PushoverSettings =  // PushoverSettings | 
try {
    apiInstance.settingsNotificationsPushoverTestPost(pushoverSettings)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsPushoverTestPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsPushoverTestPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **pushoverSettings** | [**PushoverSettings**](PushoverSettings.md)|  | |

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

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="settingsNotificationsSlackGet"></a>
# **settingsNotificationsSlackGet**
> SlackSettings settingsNotificationsSlackGet()

Get Slack notification settings

Returns current Slack notification settings in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : SlackSettings = apiInstance.settingsNotificationsSlackGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsSlackGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsSlackGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**SlackSettings**](SlackSettings.md)

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

<a id="settingsNotificationsSlackPost"></a>
# **settingsNotificationsSlackPost**
> SlackSettings settingsNotificationsSlackPost(slackSettings)

Update Slack notification settings

Updates Slack notification settings with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val slackSettings : SlackSettings =  // SlackSettings | 
try {
    val result : SlackSettings = apiInstance.settingsNotificationsSlackPost(slackSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsSlackPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsSlackPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **slackSettings** | [**SlackSettings**](SlackSettings.md)|  | |

### Return type

[**SlackSettings**](SlackSettings.md)

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

<a id="settingsNotificationsSlackTestPost"></a>
# **settingsNotificationsSlackTestPost**
> settingsNotificationsSlackTestPost(slackSettings)

Test Slack settings

Sends a test notification to the Slack agent.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val slackSettings : SlackSettings =  // SlackSettings | 
try {
    apiInstance.settingsNotificationsSlackTestPost(slackSettings)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsSlackTestPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsSlackTestPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **slackSettings** | [**SlackSettings**](SlackSettings.md)|  | |

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

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="settingsNotificationsTelegramGet"></a>
# **settingsNotificationsTelegramGet**
> TelegramSettings settingsNotificationsTelegramGet()

Get Telegram notification settings

Returns current Telegram notification settings in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : TelegramSettings = apiInstance.settingsNotificationsTelegramGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsTelegramGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsTelegramGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**TelegramSettings**](TelegramSettings.md)

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

<a id="settingsNotificationsTelegramPost"></a>
# **settingsNotificationsTelegramPost**
> TelegramSettings settingsNotificationsTelegramPost(telegramSettings)

Update Telegram notification settings

Update Telegram notification settings with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val telegramSettings : TelegramSettings =  // TelegramSettings | 
try {
    val result : TelegramSettings = apiInstance.settingsNotificationsTelegramPost(telegramSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsTelegramPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsTelegramPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **telegramSettings** | [**TelegramSettings**](TelegramSettings.md)|  | |

### Return type

[**TelegramSettings**](TelegramSettings.md)

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

<a id="settingsNotificationsTelegramTestPost"></a>
# **settingsNotificationsTelegramTestPost**
> settingsNotificationsTelegramTestPost(telegramSettings)

Test Telegram settings

Sends a test notification to the Telegram agent.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val telegramSettings : TelegramSettings =  // TelegramSettings | 
try {
    apiInstance.settingsNotificationsTelegramTestPost(telegramSettings)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsTelegramTestPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsTelegramTestPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **telegramSettings** | [**TelegramSettings**](TelegramSettings.md)|  | |

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

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="settingsNotificationsWebhookGet"></a>
# **settingsNotificationsWebhookGet**
> WebhookSettings settingsNotificationsWebhookGet()

Get webhook notification settings

Returns current webhook notification settings in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : WebhookSettings = apiInstance.settingsNotificationsWebhookGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsWebhookGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsWebhookGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**WebhookSettings**](WebhookSettings.md)

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

<a id="settingsNotificationsWebhookPost"></a>
# **settingsNotificationsWebhookPost**
> WebhookSettings settingsNotificationsWebhookPost(webhookSettings)

Update webhook notification settings

Updates webhook notification settings with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val webhookSettings : WebhookSettings =  // WebhookSettings | 
try {
    val result : WebhookSettings = apiInstance.settingsNotificationsWebhookPost(webhookSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsWebhookPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsWebhookPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **webhookSettings** | [**WebhookSettings**](WebhookSettings.md)|  | |

### Return type

[**WebhookSettings**](WebhookSettings.md)

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

<a id="settingsNotificationsWebhookTestPost"></a>
# **settingsNotificationsWebhookTestPost**
> settingsNotificationsWebhookTestPost(webhookSettings)

Test webhook settings

Sends a test notification to the webhook agent.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val webhookSettings : WebhookSettings =  // WebhookSettings | 
try {
    apiInstance.settingsNotificationsWebhookTestPost(webhookSettings)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsWebhookTestPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsWebhookTestPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **webhookSettings** | [**WebhookSettings**](WebhookSettings.md)|  | |

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

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="settingsNotificationsWebpushGet"></a>
# **settingsNotificationsWebpushGet**
> WebPushSettings settingsNotificationsWebpushGet()

Get Web Push notification settings

Returns current Web Push notification settings in a JSON object.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : WebPushSettings = apiInstance.settingsNotificationsWebpushGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsWebpushGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsWebpushGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**WebPushSettings**](WebPushSettings.md)

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

<a id="settingsNotificationsWebpushPost"></a>
# **settingsNotificationsWebpushPost**
> WebPushSettings settingsNotificationsWebpushPost(webPushSettings)

Update Web Push notification settings

Updates Web Push notification settings with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val webPushSettings : WebPushSettings =  // WebPushSettings | 
try {
    val result : WebPushSettings = apiInstance.settingsNotificationsWebpushPost(webPushSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsWebpushPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsWebpushPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **webPushSettings** | [**WebPushSettings**](WebPushSettings.md)|  | |

### Return type

[**WebPushSettings**](WebPushSettings.md)

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

<a id="settingsNotificationsWebpushTestPost"></a>
# **settingsNotificationsWebpushTestPost**
> settingsNotificationsWebpushTestPost(webPushSettings)

Test Web Push settings

Sends a test notification to the Web Push agent.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val webPushSettings : WebPushSettings =  // WebPushSettings | 
try {
    apiInstance.settingsNotificationsWebpushTestPost(webPushSettings)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsNotificationsWebpushTestPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsNotificationsWebpushTestPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **webPushSettings** | [**WebPushSettings**](WebPushSettings.md)|  | |

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

 - **Content-Type**: application/json
 - **Accept**: Not defined

<a id="settingsPlexDevicesServersGet"></a>
# **settingsPlexDevicesServersGet**
> kotlin.collections.List&lt;PlexDevice&gt; settingsPlexDevicesServersGet()

Gets the user&#39;s available Plex servers

Returns a list of available Plex servers and their connectivity state

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : kotlin.collections.List<PlexDevice> = apiInstance.settingsPlexDevicesServersGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsPlexDevicesServersGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsPlexDevicesServersGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;PlexDevice&gt;**](PlexDevice.md)

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

<a id="settingsPlexGet"></a>
# **settingsPlexGet**
> PlexSettings settingsPlexGet()

Get Plex settings

Retrieves current Plex settings.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : PlexSettings = apiInstance.settingsPlexGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsPlexGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsPlexGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**PlexSettings**](PlexSettings.md)

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

<a id="settingsPlexLibraryGet"></a>
# **settingsPlexLibraryGet**
> kotlin.collections.List&lt;PlexLibrary&gt; settingsPlexLibraryGet(sync, enable)

Get Plex libraries

Returns a list of Plex libraries in a JSON array.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val sync : kotlin.String = sync_example // kotlin.String | Syncs the current libraries with the current Plex server
val enable : kotlin.String = enable_example // kotlin.String | Comma separated list of libraries to enable. Any libraries not passed will be disabled!
try {
    val result : kotlin.collections.List<PlexLibrary> = apiInstance.settingsPlexLibraryGet(sync, enable)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsPlexLibraryGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsPlexLibraryGet")
    e.printStackTrace()
}
```

### Parameters
| **sync** | **kotlin.String**| Syncs the current libraries with the current Plex server | [optional] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **enable** | **kotlin.String**| Comma separated list of libraries to enable. Any libraries not passed will be disabled! | [optional] |

### Return type

[**kotlin.collections.List&lt;PlexLibrary&gt;**](PlexLibrary.md)

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

<a id="settingsPlexPost"></a>
# **settingsPlexPost**
> PlexSettings settingsPlexPost(plexSettings)

Update Plex settings

Updates Plex settings with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val plexSettings : PlexSettings =  // PlexSettings | 
try {
    val result : PlexSettings = apiInstance.settingsPlexPost(plexSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsPlexPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsPlexPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **plexSettings** | [**PlexSettings**](PlexSettings.md)|  | |

### Return type

[**PlexSettings**](PlexSettings.md)

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

<a id="settingsPlexSyncGet"></a>
# **settingsPlexSyncGet**
> SettingsPlexSyncGet200Response settingsPlexSyncGet()

Get status of full Plex library scan

Returns scan progress in a JSON array.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : SettingsPlexSyncGet200Response = apiInstance.settingsPlexSyncGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsPlexSyncGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsPlexSyncGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**SettingsPlexSyncGet200Response**](SettingsPlexSyncGet200Response.md)

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

<a id="settingsPlexSyncPost"></a>
# **settingsPlexSyncPost**
> SettingsPlexSyncGet200Response settingsPlexSyncPost(settingsJellyfinSyncPostRequest)

Start full Plex library scan

Runs a full Plex library scan and returns the progress in a JSON array.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val settingsJellyfinSyncPostRequest : SettingsJellyfinSyncPostRequest =  // SettingsJellyfinSyncPostRequest | 
try {
    val result : SettingsPlexSyncGet200Response = apiInstance.settingsPlexSyncPost(settingsJellyfinSyncPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsPlexSyncPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsPlexSyncPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **settingsJellyfinSyncPostRequest** | [**SettingsJellyfinSyncPostRequest**](SettingsJellyfinSyncPostRequest.md)|  | [optional] |

### Return type

[**SettingsPlexSyncGet200Response**](SettingsPlexSyncGet200Response.md)

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

<a id="settingsPlexUsersGet"></a>
# **settingsPlexUsersGet**
> kotlin.collections.List&lt;SettingsPlexUsersGet200ResponseInner&gt; settingsPlexUsersGet()

Get Plex users

Returns a list of Plex users in a JSON array.  Requires the &#x60;MANAGE_USERS&#x60; permission. 

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : kotlin.collections.List<SettingsPlexUsersGet200ResponseInner> = apiInstance.settingsPlexUsersGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsPlexUsersGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsPlexUsersGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;SettingsPlexUsersGet200ResponseInner&gt;**](SettingsPlexUsersGet200ResponseInner.md)

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

<a id="settingsPublicGet"></a>
# **settingsPublicGet**
> PublicSettings settingsPublicGet()

Get public settings

Returns settings that are not protected or sensitive. Mainly used to determine if the application has been configured for the first time.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : PublicSettings = apiInstance.settingsPublicGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsPublicGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsPublicGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**PublicSettings**](PublicSettings.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="settingsRadarrGet"></a>
# **settingsRadarrGet**
> kotlin.collections.List&lt;RadarrSettings&gt; settingsRadarrGet()

Get Radarr settings

Returns all Radarr settings in a JSON array.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : kotlin.collections.List<RadarrSettings> = apiInstance.settingsRadarrGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsRadarrGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsRadarrGet")
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

<a id="settingsRadarrPost"></a>
# **settingsRadarrPost**
> RadarrSettings settingsRadarrPost(radarrSettings)

Create Radarr instance

Creates a new Radarr instance from the request body.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val radarrSettings : RadarrSettings =  // RadarrSettings | 
try {
    val result : RadarrSettings = apiInstance.settingsRadarrPost(radarrSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsRadarrPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsRadarrPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **radarrSettings** | [**RadarrSettings**](RadarrSettings.md)|  | |

### Return type

[**RadarrSettings**](RadarrSettings.md)

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

<a id="settingsRadarrRadarrIdDelete"></a>
# **settingsRadarrRadarrIdDelete**
> RadarrSettings settingsRadarrRadarrIdDelete(radarrId)

Delete Radarr instance

Deletes an existing Radarr instance based on the radarrId parameter.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val radarrId : kotlin.Int = 56 // kotlin.Int | Radarr instance ID
try {
    val result : RadarrSettings = apiInstance.settingsRadarrRadarrIdDelete(radarrId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsRadarrRadarrIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsRadarrRadarrIdDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **radarrId** | **kotlin.Int**| Radarr instance ID | |

### Return type

[**RadarrSettings**](RadarrSettings.md)

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

<a id="settingsRadarrRadarrIdProfilesGet"></a>
# **settingsRadarrRadarrIdProfilesGet**
> kotlin.collections.List&lt;ServiceProfile&gt; settingsRadarrRadarrIdProfilesGet(radarrId)

Get available Radarr profiles

Returns a list of profiles available on the Radarr server instance in a JSON array.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val radarrId : kotlin.Int = 56 // kotlin.Int | Radarr instance ID
try {
    val result : kotlin.collections.List<ServiceProfile> = apiInstance.settingsRadarrRadarrIdProfilesGet(radarrId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsRadarrRadarrIdProfilesGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsRadarrRadarrIdProfilesGet")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **radarrId** | **kotlin.Int**| Radarr instance ID | |

### Return type

[**kotlin.collections.List&lt;ServiceProfile&gt;**](ServiceProfile.md)

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

<a id="settingsRadarrRadarrIdPut"></a>
# **settingsRadarrRadarrIdPut**
> RadarrSettings settingsRadarrRadarrIdPut(radarrId, radarrSettings)

Update Radarr instance

Updates an existing Radarr instance with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val radarrId : kotlin.Int = 56 // kotlin.Int | Radarr instance ID
val radarrSettings : RadarrSettings =  // RadarrSettings | 
try {
    val result : RadarrSettings = apiInstance.settingsRadarrRadarrIdPut(radarrId, radarrSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsRadarrRadarrIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsRadarrRadarrIdPut")
    e.printStackTrace()
}
```

### Parameters
| **radarrId** | **kotlin.Int**| Radarr instance ID | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **radarrSettings** | [**RadarrSettings**](RadarrSettings.md)|  | |

### Return type

[**RadarrSettings**](RadarrSettings.md)

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

<a id="settingsRadarrTestPost"></a>
# **settingsRadarrTestPost**
> SettingsRadarrTestPost200Response settingsRadarrTestPost(settingsRadarrTestPostRequest)

Test Radarr configuration

Tests if the Radarr configuration is valid. Returns profiles and root folders on success.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val settingsRadarrTestPostRequest : SettingsRadarrTestPostRequest =  // SettingsRadarrTestPostRequest | 
try {
    val result : SettingsRadarrTestPost200Response = apiInstance.settingsRadarrTestPost(settingsRadarrTestPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsRadarrTestPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsRadarrTestPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **settingsRadarrTestPostRequest** | [**SettingsRadarrTestPostRequest**](SettingsRadarrTestPostRequest.md)|  | |

### Return type

[**SettingsRadarrTestPost200Response**](SettingsRadarrTestPost200Response.md)

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

<a id="settingsSonarrGet"></a>
# **settingsSonarrGet**
> kotlin.collections.List&lt;SonarrSettings&gt; settingsSonarrGet()

Get Sonarr settings

Returns all Sonarr settings in a JSON array.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : kotlin.collections.List<SonarrSettings> = apiInstance.settingsSonarrGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsSonarrGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsSonarrGet")
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

<a id="settingsSonarrPost"></a>
# **settingsSonarrPost**
> SonarrSettings settingsSonarrPost(sonarrSettings)

Create Sonarr instance

Creates a new Sonarr instance from the request body.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val sonarrSettings : SonarrSettings =  // SonarrSettings | 
try {
    val result : SonarrSettings = apiInstance.settingsSonarrPost(sonarrSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsSonarrPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsSonarrPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sonarrSettings** | [**SonarrSettings**](SonarrSettings.md)|  | |

### Return type

[**SonarrSettings**](SonarrSettings.md)

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

<a id="settingsSonarrSonarrIdDelete"></a>
# **settingsSonarrSonarrIdDelete**
> SonarrSettings settingsSonarrSonarrIdDelete(sonarrId)

Delete Sonarr instance

Deletes an existing Sonarr instance based on the sonarrId parameter.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val sonarrId : kotlin.Int = 56 // kotlin.Int | Sonarr instance ID
try {
    val result : SonarrSettings = apiInstance.settingsSonarrSonarrIdDelete(sonarrId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsSonarrSonarrIdDelete")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsSonarrSonarrIdDelete")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sonarrId** | **kotlin.Int**| Sonarr instance ID | |

### Return type

[**SonarrSettings**](SonarrSettings.md)

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

<a id="settingsSonarrSonarrIdPut"></a>
# **settingsSonarrSonarrIdPut**
> SonarrSettings settingsSonarrSonarrIdPut(sonarrId, sonarrSettings)

Update Sonarr instance

Updates an existing Sonarr instance with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val sonarrId : kotlin.Int = 56 // kotlin.Int | Sonarr instance ID
val sonarrSettings : SonarrSettings =  // SonarrSettings | 
try {
    val result : SonarrSettings = apiInstance.settingsSonarrSonarrIdPut(sonarrId, sonarrSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsSonarrSonarrIdPut")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsSonarrSonarrIdPut")
    e.printStackTrace()
}
```

### Parameters
| **sonarrId** | **kotlin.Int**| Sonarr instance ID | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sonarrSettings** | [**SonarrSettings**](SonarrSettings.md)|  | |

### Return type

[**SonarrSettings**](SonarrSettings.md)

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

<a id="settingsSonarrTestPost"></a>
# **settingsSonarrTestPost**
> SettingsRadarrTestPost200Response settingsSonarrTestPost(settingsSonarrTestPostRequest)

Test Sonarr configuration

Tests if the Sonarr configuration is valid. Returns profiles and root folders on success.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val settingsSonarrTestPostRequest : SettingsSonarrTestPostRequest =  // SettingsSonarrTestPostRequest | 
try {
    val result : SettingsRadarrTestPost200Response = apiInstance.settingsSonarrTestPost(settingsSonarrTestPostRequest)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsSonarrTestPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsSonarrTestPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **settingsSonarrTestPostRequest** | [**SettingsSonarrTestPostRequest**](SettingsSonarrTestPostRequest.md)|  | |

### Return type

[**SettingsRadarrTestPost200Response**](SettingsRadarrTestPost200Response.md)

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

<a id="settingsTautulliGet"></a>
# **settingsTautulliGet**
> TautulliSettings settingsTautulliGet()

Get Tautulli settings

Retrieves current Tautulli settings.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
try {
    val result : TautulliSettings = apiInstance.settingsTautulliGet()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsTautulliGet")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsTautulliGet")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**TautulliSettings**](TautulliSettings.md)

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

<a id="settingsTautulliPost"></a>
# **settingsTautulliPost**
> TautulliSettings settingsTautulliPost(tautulliSettings)

Update Tautulli settings

Updates Tautulli settings with the provided values.

### Example
```kotlin
// Import classes:
//import com.lowiq.jellyfish.jellyseer.infrastructure.*
//import com.lowiq.jellyfish.jellyseer.models.*

val apiInstance = SettingsApi()
val tautulliSettings : TautulliSettings =  // TautulliSettings | 
try {
    val result : TautulliSettings = apiInstance.settingsTautulliPost(tautulliSettings)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SettingsApi#settingsTautulliPost")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SettingsApi#settingsTautulliPost")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **tautulliSettings** | [**TautulliSettings**](TautulliSettings.md)|  | |

### Return type

[**TautulliSettings**](TautulliSettings.md)

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

