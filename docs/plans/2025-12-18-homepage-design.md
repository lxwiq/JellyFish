# Homepage Design

## Overview

Mobile-first dashboard layout with sidebar navigation and activity feed.

## Layout

```
┌──────────────────────────┐
│ 🐟 JellyFish    [Avatar] │  <- Header
├──┬───────────────────────┤
│🏠│                       │
│🔍│  Activity Feed        │
│⭐│  (vertical scroll)    │
│⬇️│                       │
│⚙️│                       │
└──┴───────────────────────┘
 56dp
```

### Sidebar (NavigationRail)
- Width: 56dp
- Icons only, no labels
- Items:
  - Home (filled house icon)
  - Search (magnifying glass)
  - Favorites (star)
  - Downloads (arrow down)
  - Settings (gear)
- Active state: white icon + subtle background `#27272A`

### Header
- Height: 56dp
- Left: App logo/name "JellyFish"
- Right: User avatar (circular, 32dp)
- Avatar tap: dropdown menu (logout, switch server)

### Activity Feed
- Vertical LazyColumn
- Card items for each activity type
- Pull-to-refresh

## Color Palette (shadcn-dark, monochrome)

| Token | Value | Usage |
|-------|-------|-------|
| background | `#09090B` | Main background |
| surface | `#18181B` | Cards, elevated surfaces |
| border | `#27272A` | Card borders, dividers |
| textPrimary | `#FAFAFA` | Headlines, primary text |
| textSecondary | `#A1A1AA` | Subtitles, metadata |
| textTertiary | `#71717A` | Timestamps, hints |
| accent | `#FAFAFA` | Active states (on dark bg) |

## Activity Feed Items

### Types
1. **Media Added** - New movie/episode/album added to library
2. **Continue Watching** - Resume playback items
3. **Recently Favorited** - Items added to favorites
4. **Recently Played** - Music/media played recently

### Card Structure
```
┌─────────────────────────────────┐
│ [Poster]  Title                 │
│  60x90    Type • Relative time  │
│           Progress bar (if any) │
└─────────────────────────────────┘
```

- Poster: 60x90dp, rounded corners 4dp
- Border: 1dp `#27272A`
- Corner radius: 8dp
- Padding: 12dp
- No shadow (flat design)

### Typography
- Title: Medium 16sp, `#FAFAFA`
- Subtitle: Regular 14sp, `#A1A1AA`
- Timestamp: Regular 12sp, `#71717A`

## Components to Create

1. **AppScaffold** - Main layout with sidebar + content
2. **NavigationRail** - Sidebar navigation
3. **AppHeader** - Top header with logo and avatar
4. **ActivityFeedItem** - Card for feed items
5. **UserAvatar** - Circular avatar with dropdown

## Data Requirements

From Jellyfin API:
- `Items/Latest` - Recently added items
- `Items/Resume` - Continue watching
- `Users/{userId}/Items` with filters for favorites
- User info for avatar

## Navigation

Sidebar destinations:
- Home → Activity Feed (this design)
- Search → Search screen (future)
- Favorites → Favorites list (future)
- Downloads → Downloads manager (future)
- Settings → Settings screen (future)
