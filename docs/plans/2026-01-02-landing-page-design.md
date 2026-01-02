# JellyFish Landing Page Design

**Date:** January 2, 2026
**Status:** Approved
**Tech Stack:** Astro + Vercel
**Language:** English only

---

## Overview

A simple, elegant landing page for JellyFish that matches the app's dark theme and serves as the official presence for App Store and Play Store requirements.

### Goals

1. Showcase the app with a striking "bioluminescence" visual effect
2. Provide download links for all platforms
3. Host the privacy policy (required for stores)
4. Maintain visual consistency with the app's Shadcn/ui dark theme

---

## Color Palette

Extracted from `composeApp/src/commonMain/kotlin/com/lowiq/jellyfish/presentation/theme/Colors.kt`:

| Token | Hex | Usage |
|-------|-----|-------|
| `background` | `#0D0B14` | Page background |
| `card` | `#1C1A27` | Cards, surfaces |
| `foreground` | `#F8F8FC` | Primary text |
| `mutedForeground` | `#9895A8` | Secondary text |
| `secondary` | `#2A2835` | Buttons, accents |
| `ring` | `#7A7592` | Glow effects |
| `destructive` | `#E54D4D` | Error states |

### Bioluminescence Effect

- Subtle glow using `#7A7592` with blue tints (`#6B8CAE`)
- Floating particles in background (CSS/canvas)
- Screenshots with soft drop shadows and hover glow

---

## Page Structure

```
┌─────────────────────────────────────────────────────┐
│  HEADER (sticky, blur background)                   │
│  [Logo] JellyFish              [GitHub] [Download]  │
├─────────────────────────────────────────────────────┤
│                                                     │
│  HERO SECTION                                       │
│                                                     │
│  "Your media, everywhere"                           │
│  Open-source Jellyfin client for Android,           │
│  iOS, and Desktop                                   │
│                                                     │
│  [Get Started]  [View on GitHub]                    │
│                                                     │
│  ┌─────┐      ┌─────┐      ┌─────┐                 │
│  │     │      │     │      │     │                 │
│  │ 📱  │      │ 📱  │      │ 💻  │                 │
│  │     │      │     │      │     │                 │
│  └─────┘      └─────┘      └─────┘                 │
│   Phone       Tablet       Desktop                  │
│                                                     │
│  ✨ Floating luminescent particles ✨               │
│                                                     │
├─────────────────────────────────────────────────────┤
│                                                     │
│  FEATURES (4 cards in grid)                         │
│                                                     │
│  ┌──────────┐  ┌──────────┐                        │
│  │ 🔓       │  │ 📴       │                        │
│  │ Open     │  │ Offline  │                        │
│  │ Source   │  │ Ready    │                        │
│  └──────────┘  └──────────┘                        │
│  ┌──────────┐  ┌──────────┐                        │
│  │ 📱💻     │  │ 🔒       │                        │
│  │ Cross-   │  │ Privacy  │                        │
│  │ Platform │  │ First    │                        │
│  └──────────┘  └──────────┘                        │
│                                                     │
├─────────────────────────────────────────────────────┤
│                                                     │
│  DOWNLOAD SECTION                                   │
│                                                     │
│  "Download JellyFish"                               │
│                                                     │
│  [Google Play]     [App Store]                      │
│   Available         Coming Soon                     │
│                                                     │
│  [Windows]  [macOS]  [Linux]   [GitHub]            │
│  Coming Soon Coming Soon Coming Soon  Releases     │
│                                                     │
├─────────────────────────────────────────────────────┤
│                                                     │
│  FOOTER                                             │
│                                                     │
│  JellyFish - Open source Jellyfin client            │
│  Licensed under GPL-3.0                             │
│                                                     │
│  [Privacy Policy]  [GitHub]  [Jellyfin]            │
│                                                     │
│  Not affiliated with Jellyfin.                      │
│  Jellyfin is a trademark of Jellyfin Contributors.  │
│                                                     │
└─────────────────────────────────────────────────────┘
```

---

## Section Details

### 1. Header

- **Position:** Sticky top, `backdrop-filter: blur(12px)`
- **Background:** `#0D0B14` with 80% opacity
- **Content:**
  - Left: Jellyfish logo (32px) + "JellyFish" text
  - Right: GitHub icon link + "Download" CTA button
- **Height:** 64px

### 2. Hero Section

**Headline:**
```
Your media, everywhere
```

**Subheadline:**
```
Open-source Jellyfin client for Android, iOS, and Desktop.
Stream and download your personal media library with no ads,
no tracking, and no subscriptions.
```

**CTA Buttons:**
- Primary: "Get Started" → scrolls to download section
- Secondary: "View on GitHub" → external link

**Screenshots Display (Bioluminescence):**
- 3 device mockups: Phone, Tablet, Desktop
- Floating animation: subtle Y-axis oscillation (CSS keyframes)
- Glow effect: `box-shadow` with `#7A7592` and blur
- On hover: increased glow intensity
- Background: animated particles (CSS or lightweight canvas)

### 3. Features Section

Four feature cards in 2x2 grid (responsive to 1 column on mobile):

| Icon | Title | Description |
|------|-------|-------------|
| 🔓 | Open Source | 100% open source under GPL-3.0. Audit the code, contribute, or fork it. |
| 📴 | Offline Ready | Download media for offline playback. Watch anywhere, anytime. |
| 📱💻 | Cross-Platform | One app for Android, iOS, and Desktop. Your library everywhere. |
| 🔒 | Privacy First | No ads, no tracking, no data collection. Your media stays yours. |

**Card Style:**
- Background: `#1C1A27`
- Border: 1px `rgba(255,255,255,0.1)`
- Border-radius: 16px
- Padding: 24px
- Hover: subtle glow effect

### 4. Download Section

**Headline:**
```
Download JellyFish
```

**Platform Buttons:**

| Platform | Status | Link |
|----------|--------|------|
| Google Play | Available | Play Store URL |
| App Store | Coming Soon | Disabled |
| Windows | Coming Soon | Disabled |
| macOS | Coming Soon | Disabled |
| Linux | Coming Soon | Disabled |
| GitHub Releases | Available | GitHub releases URL |

**Button Style:**
- Available: `#2A2835` background, full opacity, clickable
- Coming Soon: `#2A2835` background, 50% opacity, "Coming Soon" badge

### 5. Footer

- Background: `#0D0B14` (same as page)
- Top border: 1px `rgba(255,255,255,0.1)`
- Content:
  - App name and tagline
  - License info (GPL-3.0)
  - Links: Privacy Policy, GitHub, Jellyfin official site
  - Trademark disclaimer

---

## Pages

### `/` (Index)
Main landing page with all sections above.

### `/privacy-policy`
Privacy policy page. Content from `site-docs/privacy-policy.md`, styled to match site theme.

---

## Technical Specifications

### Stack
- **Framework:** Astro 4.x
- **Styling:** Tailwind CSS
- **Animations:** CSS keyframes + Intersection Observer
- **Icons:** Lucide icons (consistent with app)
- **Hosting:** Vercel

### Project Structure
```
site/
├── public/
│   ├── favicon.ico
│   ├── jellyfish-logo.svg
│   └── screenshots/
│       ├── phone-1.png
│       ├── phone-2.png
│       ├── tablet-1.png
│       └── desktop-1.png
├── src/
│   ├── components/
│   │   ├── Header.astro
│   │   ├── Hero.astro
│   │   ├── Features.astro
│   │   ├── Download.astro
│   │   ├── Footer.astro
│   │   ├── ScreenshotDisplay.astro
│   │   └── ParticleBackground.astro
│   ├── layouts/
│   │   └── Layout.astro
│   ├── pages/
│   │   ├── index.astro
│   │   └── privacy-policy.astro
│   └── styles/
│       └── global.css
├── astro.config.mjs
├── tailwind.config.mjs
└── package.json
```

### Tailwind Config
```javascript
// tailwind.config.mjs
export default {
  content: ['./src/**/*.{astro,html,js,jsx,md,mdx,svelte,ts,tsx,vue}'],
  theme: {
    extend: {
      colors: {
        jellyfish: {
          background: '#0D0B14',
          card: '#1C1A27',
          foreground: '#F8F8FC',
          muted: '#9895A8',
          secondary: '#2A2835',
          ring: '#7A7592',
          destructive: '#E54D4D',
        }
      },
      animation: {
        'float': 'float 6s ease-in-out infinite',
        'glow': 'glow 2s ease-in-out infinite alternate',
      },
      keyframes: {
        float: {
          '0%, 100%': { transform: 'translateY(0px)' },
          '50%': { transform: 'translateY(-20px)' },
        },
        glow: {
          '0%': { boxShadow: '0 0 20px rgba(122, 117, 146, 0.3)' },
          '100%': { boxShadow: '0 0 40px rgba(122, 117, 146, 0.6)' },
        }
      }
    },
  },
  plugins: [],
}
```

---

## Responsive Breakpoints

| Breakpoint | Layout Changes |
|------------|----------------|
| `< 640px` (mobile) | Single column, stacked screenshots, hamburger menu |
| `640px - 1024px` (tablet) | 2-column features, 2 screenshots |
| `> 1024px` (desktop) | Full layout, 3 screenshots, all features visible |

---

## Performance Targets

- **Lighthouse Score:** 95+ on all metrics
- **First Contentful Paint:** < 1.5s
- **Total Page Size:** < 500KB (excluding screenshots)
- **Screenshots:** WebP format, lazy loaded, optimized

---

## Deployment

### Vercel Setup

1. Connect GitHub repository to Vercel
2. Set build command: `npm run build`
3. Set output directory: `dist`
4. Configure custom domain (optional): `jellyfish.app` or similar

### GitHub Actions (Alternative)

```yaml
# .github/workflows/deploy-site.yml
name: Deploy Site
on:
  push:
    branches: [main]
    paths: ['site/**']

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-node@v4
        with:
          node-version: 20
      - run: cd site && npm ci && npm run build
      - uses: amondnet/vercel-action@v25
        with:
          vercel-token: ${{ secrets.VERCEL_TOKEN }}
          vercel-org-id: ${{ secrets.VERCEL_ORG_ID }}
          vercel-project-id: ${{ secrets.VERCEL_PROJECT_ID }}
          working-directory: site
```

---

## Assets Required

### Screenshots Needed
- [ ] Phone screenshot 1: Home/Library view
- [ ] Phone screenshot 2: Player view
- [ ] Tablet screenshot: Library grid view
- [ ] Desktop screenshot: Full app view

### Graphics
- [x] App icon (already exists: `app-icon.png`)
- [ ] SVG version of logo for web
- [ ] Open Graph image (1200x630) for social sharing
- [ ] Favicon set (16x16, 32x32, 180x180)

---

## Content Checklist

- [ ] Final headline copy
- [ ] Feature descriptions
- [ ] Download button states and links
- [ ] Privacy policy (use existing from `site-docs/privacy-policy.md`)
- [ ] Footer legal text
- [ ] Meta descriptions for SEO

---

## Implementation Phases

### Phase 1: Setup
- Initialize Astro project
- Configure Tailwind with JellyFish colors
- Set up project structure

### Phase 2: Core Components
- Build Header, Footer, Layout
- Implement Hero section with basic screenshots
- Create Features grid
- Build Download section

### Phase 3: Bioluminescence Effect
- Add floating animation to screenshots
- Implement particle background
- Add glow effects and hover states

### Phase 4: Pages & Content
- Integrate privacy policy page
- Add final copy and content
- Optimize images

### Phase 5: Deploy
- Connect to Vercel
- Configure domain
- Test and launch

---

*Design document for JellyFish landing page*
*Approved: January 2, 2026*
