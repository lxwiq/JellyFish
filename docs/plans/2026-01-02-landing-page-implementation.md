# JellyFish Landing Page - Implementation Guide

Guide détaillé pour implémenter la landing page. Suivre dans l'ordre.

---

## 1. Initialisation du projet

### 1.1 Créer le projet Astro

```bash
cd /Users/lolo/AndroidStudioProjects/JellyFish
npm create astro@latest site -- --template minimal --typescript strict
cd site
```

### 1.2 Installer les dépendances

```bash
npm install -D tailwindcss @astrojs/tailwind
npm install lucide-astro
npx astro add tailwind
```

### 1.3 Structure des dossiers

```
site/
├── public/
│   ├── favicon.svg
│   ├── og-image.png              # 1200x630 pour partage social
│   └── screenshots/
│       ├── phone-home.webp
│       ├── phone-player.webp
│       ├── tablet-library.webp
│       └── desktop-app.webp
├── src/
│   ├── components/
│   │   ├── Header.astro
│   │   ├── Hero.astro
│   │   ├── ScreenshotDisplay.astro
│   │   ├── ParticleBackground.astro
│   │   ├── Features.astro
│   │   ├── FeatureCard.astro
│   │   ├── Download.astro
│   │   ├── DownloadButton.astro
│   │   └── Footer.astro
│   ├── layouts/
│   │   └── Layout.astro
│   ├── pages/
│   │   ├── index.astro
│   │   └── privacy-policy.astro
│   └── styles/
│       └── global.css
├── astro.config.mjs
├── tailwind.config.mjs
├── tsconfig.json
└── package.json
```

---

## 2. Configuration Tailwind

### 2.1 tailwind.config.mjs

```javascript
/** @type {import('tailwindcss').Config} */
export default {
  content: ['./src/**/*.{astro,html,js,jsx,md,mdx,svelte,ts,tsx,vue}'],
  theme: {
    extend: {
      colors: {
        jellyfish: {
          bg: '#0D0B14',
          card: '#1C1A27',
          text: '#F8F8FC',
          muted: '#9895A8',
          secondary: '#2A2835',
          ring: '#7A7592',
          glow: '#6B8CAE',
          destructive: '#E54D4D',
        }
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
      },
      animation: {
        'float-slow': 'float 8s ease-in-out infinite',
        'float-medium': 'float 6s ease-in-out infinite',
        'float-fast': 'float 4s ease-in-out infinite',
        'glow-pulse': 'glow 3s ease-in-out infinite alternate',
        'particle': 'particle 15s linear infinite',
      },
      keyframes: {
        float: {
          '0%, 100%': { transform: 'translateY(0px)' },
          '50%': { transform: 'translateY(-20px)' },
        },
        glow: {
          '0%': {
            boxShadow: '0 0 20px rgba(122, 117, 146, 0.2), 0 0 40px rgba(107, 140, 174, 0.1)'
          },
          '100%': {
            boxShadow: '0 0 40px rgba(122, 117, 146, 0.4), 0 0 80px rgba(107, 140, 174, 0.2)'
          },
        },
        particle: {
          '0%': { transform: 'translateY(100vh) scale(0)', opacity: '0' },
          '10%': { opacity: '1' },
          '90%': { opacity: '1' },
          '100%': { transform: 'translateY(-100vh) scale(1)', opacity: '0' },
        }
      },
      backdropBlur: {
        xs: '2px',
      }
    },
  },
  plugins: [],
}
```

### 2.2 src/styles/global.css

```css
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap');

@tailwind base;
@tailwind components;
@tailwind utilities;

@layer base {
  html {
    scroll-behavior: smooth;
  }

  body {
    @apply bg-jellyfish-bg text-jellyfish-text antialiased;
  }

  ::selection {
    @apply bg-jellyfish-ring/30 text-jellyfish-text;
  }
}

@layer components {
  .btn-primary {
    @apply px-6 py-3 bg-jellyfish-text text-jellyfish-bg font-semibold rounded-xl
           hover:bg-jellyfish-text/90 transition-all duration-200
           focus:outline-none focus:ring-2 focus:ring-jellyfish-ring focus:ring-offset-2
           focus:ring-offset-jellyfish-bg;
  }

  .btn-secondary {
    @apply px-6 py-3 bg-jellyfish-secondary text-jellyfish-text font-medium rounded-xl
           border border-white/10 hover:bg-jellyfish-card transition-all duration-200
           focus:outline-none focus:ring-2 focus:ring-jellyfish-ring focus:ring-offset-2
           focus:ring-offset-jellyfish-bg;
  }

  .card {
    @apply bg-jellyfish-card border border-white/10 rounded-2xl p-6
           hover:border-jellyfish-ring/30 transition-all duration-300;
  }

  .glow-effect {
    @apply relative;
  }

  .glow-effect::before {
    content: '';
    @apply absolute inset-0 rounded-2xl opacity-0 transition-opacity duration-300;
    background: radial-gradient(
      circle at center,
      rgba(122, 117, 146, 0.15) 0%,
      transparent 70%
    );
  }

  .glow-effect:hover::before {
    @apply opacity-100;
  }
}

/* Particules bioluminescentes */
.particle {
  @apply absolute rounded-full pointer-events-none;
  background: radial-gradient(
    circle,
    rgba(122, 117, 146, 0.8) 0%,
    rgba(107, 140, 174, 0.4) 50%,
    transparent 70%
  );
}
```

---

## 3. Composants

### 3.1 Layout.astro

```astro
---
import '../styles/global.css';

interface Props {
  title: string;
  description?: string;
}

const {
  title,
  description = "Open-source Jellyfin client for Android, iOS, and Desktop"
} = Astro.props;
---

<!doctype html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="description" content={description} />

    <!-- Open Graph -->
    <meta property="og:title" content={title} />
    <meta property="og:description" content={description} />
    <meta property="og:image" content="/og-image.png" />
    <meta property="og:type" content="website" />

    <!-- Twitter -->
    <meta name="twitter:card" content="summary_large_image" />
    <meta name="twitter:title" content={title} />
    <meta name="twitter:description" content={description} />
    <meta name="twitter:image" content="/og-image.png" />

    <link rel="icon" type="image/svg+xml" href="/favicon.svg" />
    <title>{title}</title>
  </head>
  <body class="min-h-screen">
    <slot />
  </body>
</html>
```

### 3.2 Header.astro

```astro
---
import { Github } from 'lucide-astro';
---

<header class="fixed top-0 left-0 right-0 z-50 bg-jellyfish-bg/80 backdrop-blur-md border-b border-white/5">
  <nav class="max-w-6xl mx-auto px-4 sm:px-6 h-16 flex items-center justify-between">
    <!-- Logo -->
    <a href="/" class="flex items-center gap-3 hover:opacity-80 transition-opacity">
      <img src="/favicon.svg" alt="JellyFish" class="w-8 h-8" />
      <span class="font-semibold text-lg">JellyFish</span>
    </a>

    <!-- Navigation -->
    <div class="flex items-center gap-4">
      <a
        href="https://github.com/lxwiq/JellyFish"
        target="_blank"
        rel="noopener noreferrer"
        class="p-2 text-jellyfish-muted hover:text-jellyfish-text transition-colors"
        aria-label="GitHub"
      >
        <Github class="w-5 h-5" />
      </a>
      <a href="#download" class="btn-primary text-sm">
        Download
      </a>
    </div>
  </nav>
</header>
```

### 3.3 Hero.astro

```astro
---
import ScreenshotDisplay from './ScreenshotDisplay.astro';
import ParticleBackground from './ParticleBackground.astro';
import { ArrowDown, Github } from 'lucide-astro';
---

<section class="relative min-h-screen flex flex-col items-center justify-center px-4 pt-16 overflow-hidden">
  <!-- Particules en arrière-plan -->
  <ParticleBackground />

  <!-- Contenu -->
  <div class="relative z-10 max-w-4xl mx-auto text-center">
    <!-- Badge -->
    <div class="inline-flex items-center gap-2 px-4 py-2 bg-jellyfish-secondary/50 rounded-full border border-white/10 mb-8">
      <span class="w-2 h-2 bg-green-400 rounded-full animate-pulse"></span>
      <span class="text-sm text-jellyfish-muted">Open Source & Privacy First</span>
    </div>

    <!-- Titre -->
    <h1 class="text-4xl sm:text-5xl md:text-6xl font-bold mb-6 bg-gradient-to-b from-jellyfish-text to-jellyfish-muted bg-clip-text text-transparent">
      Your media, everywhere
    </h1>

    <!-- Sous-titre -->
    <p class="text-lg sm:text-xl text-jellyfish-muted max-w-2xl mx-auto mb-10">
      Open-source Jellyfin client for Android, iOS, and Desktop.
      Stream and download your personal media library with no ads,
      no tracking, and no subscriptions.
    </p>

    <!-- CTAs -->
    <div class="flex flex-col sm:flex-row items-center justify-center gap-4 mb-16">
      <a href="#download" class="btn-primary flex items-center gap-2">
        Get Started
        <ArrowDown class="w-4 h-4" />
      </a>
      <a
        href="https://github.com/lxwiq/JellyFish"
        target="_blank"
        rel="noopener noreferrer"
        class="btn-secondary flex items-center gap-2"
      >
        <Github class="w-4 h-4" />
        View on GitHub
      </a>
    </div>
  </div>

  <!-- Screenshots flottants -->
  <ScreenshotDisplay />
</section>
```

### 3.4 ScreenshotDisplay.astro

```astro
---
// Screenshots avec effet bioluminescence
const screenshots = [
  { src: '/screenshots/phone-home.webp', alt: 'Home screen', delay: '0s', duration: 'float-slow' },
  { src: '/screenshots/phone-player.webp', alt: 'Player screen', delay: '1s', duration: 'float-medium' },
  { src: '/screenshots/desktop-app.webp', alt: 'Desktop app', delay: '2s', duration: 'float-fast' },
];
---

<div class="relative z-10 w-full max-w-5xl mx-auto px-4">
  <div class="flex flex-col md:flex-row items-center justify-center gap-8 md:gap-12">

    <!-- Phone 1 -->
    <div
      class="screenshot-container animate-float-slow"
      style="animation-delay: 0s;"
    >
      <div class="screenshot-frame phone">
        <img
          src="/screenshots/phone-home.webp"
          alt="JellyFish home screen"
          class="screenshot-image"
          loading="lazy"
        />
      </div>
    </div>

    <!-- Desktop (centre, plus grand) -->
    <div
      class="screenshot-container animate-float-medium hidden md:block"
      style="animation-delay: 0.5s;"
    >
      <div class="screenshot-frame desktop">
        <img
          src="/screenshots/desktop-app.webp"
          alt="JellyFish desktop app"
          class="screenshot-image"
          loading="lazy"
        />
      </div>
    </div>

    <!-- Phone 2 -->
    <div
      class="screenshot-container animate-float-fast"
      style="animation-delay: 1s;"
    >
      <div class="screenshot-frame phone">
        <img
          src="/screenshots/phone-player.webp"
          alt="JellyFish player screen"
          class="screenshot-image"
          loading="lazy"
        />
      </div>
    </div>

  </div>
</div>

<style>
  .screenshot-container {
    @apply relative;
  }

  .screenshot-frame {
    @apply relative rounded-2xl overflow-hidden;
    background: linear-gradient(
      145deg,
      rgba(28, 26, 39, 0.8) 0%,
      rgba(13, 11, 20, 0.9) 100%
    );
    border: 1px solid rgba(255, 255, 255, 0.1);
  }

  .screenshot-frame::before {
    content: '';
    @apply absolute inset-0 rounded-2xl opacity-50 transition-opacity duration-500;
    background: radial-gradient(
      ellipse at center,
      rgba(122, 117, 146, 0.3) 0%,
      rgba(107, 140, 174, 0.1) 40%,
      transparent 70%
    );
    filter: blur(20px);
    z-index: -1;
    transform: scale(1.2);
  }

  .screenshot-container:hover .screenshot-frame::before {
    @apply opacity-100;
  }

  .screenshot-frame.phone {
    @apply w-48 sm:w-56;
    aspect-ratio: 9/19;
    padding: 8px;
  }

  .screenshot-frame.desktop {
    @apply w-80 lg:w-96;
    aspect-ratio: 16/10;
    padding: 12px;
  }

  .screenshot-image {
    @apply w-full h-full object-cover rounded-xl;
  }

  /* Glow animation au hover */
  .screenshot-container:hover .screenshot-frame {
    box-shadow:
      0 0 40px rgba(122, 117, 146, 0.3),
      0 0 80px rgba(107, 140, 174, 0.15),
      inset 0 0 20px rgba(122, 117, 146, 0.1);
  }
</style>
```

### 3.5 ParticleBackground.astro

```astro
---
// Génère des particules aléatoires pour l'effet bioluminescence
const particleCount = 30;
const particles = Array.from({ length: particleCount }, (_, i) => ({
  id: i,
  size: Math.random() * 4 + 2, // 2-6px
  left: Math.random() * 100,
  delay: Math.random() * 15,
  duration: Math.random() * 10 + 10, // 10-20s
  opacity: Math.random() * 0.5 + 0.2, // 0.2-0.7
}));
---

<div class="absolute inset-0 overflow-hidden pointer-events-none" aria-hidden="true">
  {particles.map(p => (
    <div
      class="particle animate-particle"
      style={`
        width: ${p.size}px;
        height: ${p.size}px;
        left: ${p.left}%;
        animation-delay: ${p.delay}s;
        animation-duration: ${p.duration}s;
        opacity: ${p.opacity};
      `}
    />
  ))}

  <!-- Gradient overlay pour fondu -->
  <div class="absolute inset-0 bg-gradient-to-b from-jellyfish-bg via-transparent to-jellyfish-bg"></div>
</div>

<style>
  .particle {
    position: absolute;
    bottom: -10px;
    border-radius: 50%;
    background: radial-gradient(
      circle,
      rgba(122, 117, 146, 0.8) 0%,
      rgba(107, 140, 174, 0.4) 40%,
      transparent 70%
    );
    filter: blur(1px);
  }

  @keyframes particle {
    0% {
      transform: translateY(0) scale(0);
      opacity: 0;
    }
    10% {
      opacity: var(--particle-opacity, 0.5);
      transform: scale(1);
    }
    90% {
      opacity: var(--particle-opacity, 0.5);
    }
    100% {
      transform: translateY(-100vh) scale(0.5);
      opacity: 0;
    }
  }
</style>
```

### 3.6 Features.astro

```astro
---
import FeatureCard from './FeatureCard.astro';
import { Code2, WifiOff, Smartphone, ShieldCheck } from 'lucide-astro';

const features = [
  {
    icon: 'Code2',
    title: 'Open Source',
    description: '100% open source under GPL-3.0. Audit the code, contribute, or fork it.',
  },
  {
    icon: 'WifiOff',
    title: 'Offline Ready',
    description: 'Download media for offline playback. Watch anywhere, anytime.',
  },
  {
    icon: 'Smartphone',
    title: 'Cross-Platform',
    description: 'One app for Android, iOS, and Desktop. Your library everywhere.',
  },
  {
    icon: 'ShieldCheck',
    title: 'Privacy First',
    description: 'No ads, no tracking, no data collection. Your media stays yours.',
  },
];
---

<section id="features" class="py-24 px-4">
  <div class="max-w-6xl mx-auto">
    <div class="text-center mb-16">
      <h2 class="text-3xl sm:text-4xl font-bold mb-4">
        Why JellyFish?
      </h2>
      <p class="text-jellyfish-muted max-w-2xl mx-auto">
        A modern, privacy-respecting way to enjoy your media collection.
      </p>
    </div>

    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
      {features.map((feature, index) => (
        <FeatureCard
          icon={feature.icon}
          title={feature.title}
          description={feature.description}
          delay={index * 100}
        />
      ))}
    </div>
  </div>
</section>
```

### 3.7 FeatureCard.astro

```astro
---
import { Code2, WifiOff, Smartphone, ShieldCheck } from 'lucide-astro';

interface Props {
  icon: string;
  title: string;
  description: string;
  delay?: number;
}

const { icon, title, description, delay = 0 } = Astro.props;

const icons = {
  Code2,
  WifiOff,
  Smartphone,
  ShieldCheck,
};

const IconComponent = icons[icon as keyof typeof icons];
---

<div
  class="card glow-effect group"
  style={`animation-delay: ${delay}ms;`}
>
  <div class="w-12 h-12 bg-jellyfish-secondary rounded-xl flex items-center justify-center mb-4 group-hover:bg-jellyfish-ring/20 transition-colors">
    <IconComponent class="w-6 h-6 text-jellyfish-ring" />
  </div>
  <h3 class="text-lg font-semibold mb-2">{title}</h3>
  <p class="text-jellyfish-muted text-sm">{description}</p>
</div>
```

### 3.8 Download.astro

```astro
---
import DownloadButton from './DownloadButton.astro';

const platforms = [
  {
    name: 'Google Play',
    icon: 'play-store',
    href: '#', // Remplacer par le vrai lien
    available: true,
  },
  {
    name: 'App Store',
    icon: 'app-store',
    href: '#',
    available: false,
  },
  {
    name: 'Windows',
    icon: 'windows',
    href: '#',
    available: false,
  },
  {
    name: 'macOS',
    icon: 'apple',
    href: '#',
    available: false,
  },
  {
    name: 'Linux',
    icon: 'linux',
    href: '#',
    available: false,
  },
  {
    name: 'GitHub',
    icon: 'github',
    href: 'https://github.com/lxwiq/JellyFish/releases',
    available: true,
    secondary: true,
  },
];
---

<section id="download" class="py-24 px-4 bg-jellyfish-card/30">
  <div class="max-w-4xl mx-auto text-center">
    <h2 class="text-3xl sm:text-4xl font-bold mb-4">
      Download JellyFish
    </h2>
    <p class="text-jellyfish-muted mb-12 max-w-xl mx-auto">
      Available on multiple platforms. Choose yours and start streaming.
    </p>

    <!-- Mobile & Stores -->
    <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 max-w-md mx-auto mb-8">
      {platforms.filter(p => ['Google Play', 'App Store'].includes(p.name)).map(platform => (
        <DownloadButton {...platform} />
      ))}
    </div>

    <!-- Desktop -->
    <div class="grid grid-cols-1 sm:grid-cols-3 gap-4 max-w-lg mx-auto mb-8">
      {platforms.filter(p => ['Windows', 'macOS', 'Linux'].includes(p.name)).map(platform => (
        <DownloadButton {...platform} />
      ))}
    </div>

    <!-- GitHub -->
    <div class="pt-6 border-t border-white/10">
      <p class="text-jellyfish-muted text-sm mb-4">
        Or download directly from GitHub
      </p>
      {platforms.filter(p => p.name === 'GitHub').map(platform => (
        <DownloadButton {...platform} />
      ))}
    </div>
  </div>
</section>
```

### 3.9 DownloadButton.astro

```astro
---
interface Props {
  name: string;
  icon: string;
  href: string;
  available: boolean;
  secondary?: boolean;
}

const { name, icon, href, available, secondary = false } = Astro.props;
---

{available ? (
  <a
    href={href}
    target="_blank"
    rel="noopener noreferrer"
    class:list={[
      "flex items-center justify-center gap-3 px-6 py-4 rounded-xl font-medium transition-all duration-200",
      secondary
        ? "bg-jellyfish-secondary hover:bg-jellyfish-card border border-white/10"
        : "bg-jellyfish-text text-jellyfish-bg hover:bg-jellyfish-text/90"
    ]}
  >
    <img src={`/icons/${icon}.svg`} alt="" class="w-6 h-6" />
    <span>{name}</span>
  </a>
) : (
  <div class="flex items-center justify-center gap-3 px-6 py-4 rounded-xl bg-jellyfish-secondary/50 border border-white/5 opacity-60 cursor-not-allowed">
    <img src={`/icons/${icon}.svg`} alt="" class="w-6 h-6 opacity-50" />
    <div class="text-left">
      <span class="block text-sm">{name}</span>
      <span class="block text-xs text-jellyfish-muted">Coming Soon</span>
    </div>
  </div>
)}
```

### 3.10 Footer.astro

```astro
---
import { Github, Heart } from 'lucide-astro';
---

<footer class="py-12 px-4 border-t border-white/5">
  <div class="max-w-6xl mx-auto">
    <div class="flex flex-col md:flex-row items-center justify-between gap-6">

      <!-- Logo & Info -->
      <div class="text-center md:text-left">
        <div class="flex items-center justify-center md:justify-start gap-2 mb-2">
          <img src="/favicon.svg" alt="JellyFish" class="w-6 h-6" />
          <span class="font-semibold">JellyFish</span>
        </div>
        <p class="text-jellyfish-muted text-sm">
          Open-source Jellyfin client
        </p>
        <p class="text-jellyfish-muted text-xs mt-1">
          Licensed under GPL-3.0
        </p>
      </div>

      <!-- Links -->
      <div class="flex flex-wrap items-center justify-center gap-6 text-sm">
        <a href="/privacy-policy" class="text-jellyfish-muted hover:text-jellyfish-text transition-colors">
          Privacy Policy
        </a>
        <a
          href="https://github.com/lxwiq/JellyFish"
          target="_blank"
          rel="noopener noreferrer"
          class="text-jellyfish-muted hover:text-jellyfish-text transition-colors flex items-center gap-1"
        >
          <Github class="w-4 h-4" />
          GitHub
        </a>
        <a
          href="https://jellyfin.org"
          target="_blank"
          rel="noopener noreferrer"
          class="text-jellyfish-muted hover:text-jellyfish-text transition-colors"
        >
          Jellyfin
        </a>
      </div>

    </div>

    <!-- Disclaimer -->
    <div class="mt-8 pt-6 border-t border-white/5 text-center">
      <p class="text-jellyfish-muted text-xs">
        JellyFish is not affiliated with Jellyfin. Jellyfin is a trademark of Jellyfin Contributors.
      </p>
      <p class="text-jellyfish-muted text-xs mt-2 flex items-center justify-center gap-1">
        Made with <Heart class="w-3 h-3 text-jellyfish-destructive" /> by
        <a href="https://github.com/lxwiq" target="_blank" rel="noopener noreferrer" class="hover:text-jellyfish-text">
          lxwiq
        </a>
      </p>
    </div>
  </div>
</footer>
```

---

## 4. Pages

### 4.1 index.astro

```astro
---
import Layout from '../layouts/Layout.astro';
import Header from '../components/Header.astro';
import Hero from '../components/Hero.astro';
import Features from '../components/Features.astro';
import Download from '../components/Download.astro';
import Footer from '../components/Footer.astro';
---

<Layout title="JellyFish - Open Source Jellyfin Client">
  <Header />
  <main>
    <Hero />
    <Features />
    <Download />
  </main>
  <Footer />
</Layout>
```

### 4.2 privacy-policy.astro

```astro
---
import Layout from '../layouts/Layout.astro';
import Header from '../components/Header.astro';
import Footer from '../components/Footer.astro';

// Importer le contenu markdown
import { Content } from '../../site-docs/privacy-policy.md';
// OU copier le contenu directement
---

<Layout title="Privacy Policy - JellyFish">
  <Header />
  <main class="pt-24 pb-16 px-4">
    <article class="max-w-3xl mx-auto prose prose-invert prose-jellyfish">
      <h1>Privacy Policy</h1>

      <!-- Contenu de la privacy policy -->
      <!-- Copier le contenu de site-docs/privacy-policy.md ici -->
      <!-- OU utiliser un composant Markdown d'Astro -->

    </article>
  </main>
  <Footer />
</Layout>

<style is:global>
  .prose-jellyfish {
    --tw-prose-body: theme('colors.jellyfish.text');
    --tw-prose-headings: theme('colors.jellyfish.text');
    --tw-prose-links: theme('colors.jellyfish.ring');
    --tw-prose-bold: theme('colors.jellyfish.text');
    --tw-prose-counters: theme('colors.jellyfish.muted');
    --tw-prose-bullets: theme('colors.jellyfish.muted');
    --tw-prose-hr: theme('colors.jellyfish.secondary');
    --tw-prose-quotes: theme('colors.jellyfish.muted');
    --tw-prose-code: theme('colors.jellyfish.text');
    --tw-prose-pre-bg: theme('colors.jellyfish.card');
  }
</style>
```

---

## 5. Assets requis

### 5.1 Icônes de plateformes

Créer les fichiers SVG dans `public/icons/`:

- `play-store.svg` - Logo Google Play
- `app-store.svg` - Logo Apple App Store
- `windows.svg` - Logo Windows
- `apple.svg` - Logo Apple
- `linux.svg` - Logo Linux (Tux)
- `github.svg` - Logo GitHub

### 5.2 Screenshots

Placer dans `public/screenshots/`:

| Fichier | Dimensions | Description |
|---------|------------|-------------|
| `phone-home.webp` | 1080x2340 | Écran d'accueil mobile |
| `phone-player.webp` | 1080x2340 | Lecteur vidéo mobile |
| `desktop-app.webp` | 1920x1200 | App desktop complète |

**Optimisation :** Convertir en WebP, qualité 85%, max 200KB par image.

### 5.3 Favicon

Exporter le logo jellyfish en SVG dans `public/favicon.svg`.

### 5.4 Open Graph

Créer `public/og-image.png` (1200x630) avec :
- Logo JellyFish centré
- Tagline "Your media, everywhere"
- Fond `#0D0B14`

---

## 6. Configuration Astro

### astro.config.mjs

```javascript
import { defineConfig } from 'astro/config';
import tailwind from '@astrojs/tailwind';

export default defineConfig({
  integrations: [tailwind()],
  site: 'https://jellyfish-app.vercel.app', // Remplacer par ton domaine
  output: 'static',
  build: {
    assets: 'assets',
  },
});
```

---

## 7. Déploiement Vercel

### 7.1 Fichier vercel.json (optionnel)

```json
{
  "buildCommand": "npm run build",
  "outputDirectory": "dist",
  "framework": "astro"
}
```

### 7.2 Commandes

```bash
# Build local
npm run build

# Preview local
npm run preview

# Déployer (après avoir connecté à Vercel)
npx vercel
```

---

## 8. Checklist finale

### Setup
- [ ] Créer projet Astro
- [ ] Configurer Tailwind avec couleurs JellyFish
- [ ] Installer lucide-astro

### Composants
- [ ] Layout.astro
- [ ] Header.astro
- [ ] Hero.astro
- [ ] ScreenshotDisplay.astro
- [ ] ParticleBackground.astro
- [ ] Features.astro
- [ ] FeatureCard.astro
- [ ] Download.astro
- [ ] DownloadButton.astro
- [ ] Footer.astro

### Pages
- [ ] index.astro
- [ ] privacy-policy.astro

### Assets
- [ ] Favicon SVG
- [ ] Screenshots (phone x2, desktop x1)
- [ ] Icônes plateformes (6 SVG)
- [ ] Open Graph image

### Déploiement
- [ ] Connecter repo à Vercel
- [ ] Configurer domaine
- [ ] Tester URLs (/, /privacy-policy)

---

*Guide d'implémentation pour JellyFish Landing Page*
*Janvier 2, 2026*
