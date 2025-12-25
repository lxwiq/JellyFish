# Animations - Reiverr

## AnimateScale

**Fichier**: `/src/lib/components/AnimateScale.svelte`

### Comportement
Applique `scale-105` au focus/hover avec transition 200ms.

```svelte
<div
  style="transition: transform 200ms;"
  class={classNames('relative', {
    'scale-105': hasFocus,
    'hover:scale-105': enabled
  })}
>
  <slot />
</div>
```

### Équivalent Kotlin
```kotlin
val scale by animateFloatAsState(
    targetValue = if (hasFocus) 1.05f else 1f,
    animationSpec = tween(200)
)
Box(modifier = Modifier.scale(scale)) { content() }
```

---

## Transitions de bordure

**Fichier**: `/src/app.css`

### Classes selectable
```css
.selectable {
  @apply border-2 border-transparent transition-colors;
  @apply hover:border-primary-500;
  @apply focus:border-primary-500;
}

.selected {
  @apply border-2 border-primary-500;
}

.unselected {
  @apply border-2 border-transparent;
}
```

---

## LazyImg - Fade des images

**Fichier**: `/src/lib/components/LazyImg.svelte`

```css
transition-opacity duration-300
opacity-0 /* non chargé */
opacity-100 /* chargé */
```

### Équivalent Kotlin
```kotlin
val alpha by animateFloatAsState(
    targetValue = if (loaded) 1f else 0f,
    animationSpec = tween(300)
)
AsyncImage(modifier = Modifier.alpha(alpha))
```

---

## Placeholder pulse

**Fichier**: `/src/app.css`

```css
.placeholder {
  @apply bg-zinc-700 bg-opacity-40 animate-pulse;
}
```

Animation `pulse` de Tailwind: opacité 1 → 0.5 → 1 en 2s, infini.

### Équivalent Kotlin
```kotlin
val alpha by rememberInfiniteTransition().animateFloat(
    initialValue = 1f,
    targetValue = 0.5f,
    animationSpec = infiniteRepeatable(
        animation = tween(1000),
        repeatMode = RepeatMode.Reverse
    )
)
```

---

## Timer animation

**Fichier**: `/tailwind.config.js`

```javascript
keyframes: {
  timer: {
    '0%': { width: '0%' },
    '100%': { width: '100%' }
  }
},
animation: {
  timer: 'timer 1s linear'
}
```

---

## Transitions Svelte

### fade
```svelte
import { fade } from 'svelte/transition';
<div transition:fade={{ duration: 100 }}>
```

### fly
```svelte
import { fly } from 'svelte/transition';
<div in:fly={{ duration: 150, x: 50 }}>
```

### flip
```svelte
import { flip } from 'svelte/animate';
{#each items as item (item.id)}
  <div animate:flip={{ duration: 500 }}>
{/each}
```

---

## Tableau récapitulatif

| Animation | Durée | Propriété | Usage |
|-----------|-------|-----------|-------|
| AnimateScale | 200ms | transform | Focus/hover |
| transition-colors | 150ms | border-color | Sélection |
| LazyImg fade | 300ms | opacity | Images |
| Pulse | 2000ms | opacity | Placeholders |
| Timer | 1000ms | width | Progress |
| Dialog fade | 100ms | opacity | Modales |
| Notification fly | 150ms | transform+opacity | Toasts |
| Flip | 500ms | transform | Réorganisation |

---

## Correspondances Compose

| Tailwind/Svelte | Compose |
|-----------------|---------|
| `scale-105` | `Modifier.scale(1.05f)` |
| `transition-colors` | `animateColorAsState()` |
| `opacity-0/100` | `Modifier.alpha()` |
| `animate-pulse` | `infiniteRepeatable()` |
| `fade` | `fadeIn()/fadeOut()` |
| `fly` | `slideIn*() + fadeIn()` |
| `flip` | `animateItemPlacement()` |

---

## Bonnes pratiques

1. **Désactivation**: Respecter `useCssTransitions` setting
2. **Performance**: Animer `transform` et `opacity` (GPU)
3. **Accessibilité**: Respecter `prefers-reduced-motion`
4. **Durées cohérentes**:
   - Rapide: 100-200ms
   - Normal: 300ms
   - Lent: 500ms+
