# Documentation UI - Reiverr

## Vue d'ensemble

Cette documentation présente le design system et les composants UI de **Reiverr**, une application de streaming media construite avec **Svelte** et **TailwindCSS**.

L'objectif de cette documentation est de fournir une référence complète pour reproduire l'interface utilisateur de Reiverr en **Kotlin/Jetpack Compose**.

## Stack technique

- **Framework frontend** : Svelte
- **Framework CSS** : TailwindCSS
- **Icônes** : radix-icons-svelte
- **Cible de reproduction** : Kotlin (Jetpack Compose)

## Structure de la documentation

| Fichier | Description |
|---------|-------------|
| [theme.md](./theme.md) | Palette de couleurs Primary/Secondary (HSL), couleurs spéciales |
| [typography.md](./typography.md) | Police Montserrat, styles h1-h5, body, mode TV |
| [buttons.md](./buttons.md) | Composant Button avec 3 types et variantes |
| [forms.md](./forms.md) | TextField, Toggle, Checkbox, SelectField, SelectButtonGroup |
| [cards.md](./cards.md) | Card, MediaCard, Carousel composants |
| [navigation.md](./navigation.md) | Sidebar, Tab System, PageDots |
| [layout.md](./layout.md) | Container, Panel, Carousel, HeroCarousel |
| [feedback.md](./feedback.md) | Modal, Dialog, Notification, ProgressBar |
| [animations.md](./animations.md) | AnimateScale, transitions fade/fly/flip, durées |

## Résumé des tokens de design

### Couleurs principales

```
Primary (doré/ambre):   hsl(40, 60%, X%)  - Accents, boutons, focus
Secondary (neutre):     hsl(40, 12%, X%)  - Backgrounds, textes
```

### Typographie

- **Police** : Montserrat (font-sans, font-display)
- **Poids** : medium (500), semibold (600)
- **Mode TV** : font-size base 24px (×1.5)

### Dimensions communes

| Élément | Dimension |
|---------|-----------|
| Bouton hauteur | 48px (h-12) |
| Sidebar largeur | 96px (w-24) |
| Icônes | 19px, 24px, 32px |
| Border radius | lg (8px), xl (12px) |
| Border width | 2px |

### Animations

| Type | Durée |
|------|-------|
| Rapide (focus/hover) | 100-200ms |
| Normal (transitions) | 300ms |
| Réorganisation | 500ms |

---

**Version** : 2.2.0
**Dernière mise à jour** : 2025-12-23
