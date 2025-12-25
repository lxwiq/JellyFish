# Feedback UI - Reiverr

## Modal

**Fichier**: `/src/lib/components/Modal/Modal.svelte`

### Caractéristiques
- Position: `fixed inset-0`
- Focus: `focusOnMount`, `trapFocus`
- Fermeture: événement `back` ou `modalStack.closeTopmost()`

---

## ModalStack

**Fichier**: `/src/lib/components/Modal/ModalStack.svelte`

### Caractéristiques
- Z-index: 30
- Groupes: masque les modals du même groupe
- Body: `overflow: hidden` quand actif

### Usage
```javascript
modalStack.open({
  component: MyModal,
  props: { title: "Mon Modal" },
  group: 'main'
});
```

---

## Dialog

**Fichier**: `/src/lib/components/Dialog/Dialog.svelte`

### Props
- `size`: 'sm' | 'full' | 'lg' | 'dynamic'

### Styles
```css
/* Backdrop */
bg-primary-900/75

/* Centrage */
flex items-center justify-center
py-20 px-32
```

### Animation
- Transition: `fade` 100ms

---

## ConfirmDialog

**Fichier**: `/src/lib/components/Dialog/ConfirmDialog.svelte`

### Props
- `modalId`: symbol
- `header`: string
- `body`: string
- `confirm`: () => Promise
- `cancel`: () => Promise

### Structure
```
┌─────────────────┐
│ Header (h3)     │
│ Body (body)     │
│                 │
│ [Confirm]       │
│ [Cancel]        │
└─────────────────┘
```

---

## SelectDialog

**Fichier**: `/src/lib/components/Dialog/SelectDialog.svelte`

### Props
- `title`: string
- `subtitle`: string
- `options`: string[]
- `selectedOption`: string
- `handleSelectOption`: (option) => void

---

## Notification

**Fichier**: `/src/lib/components/Notifications/Notification.svelte`

### Props
- `title`: string
- `body`: string
- `type`: 'info' | 'warning' | 'error'
- `duration`: number (défaut: 5000)
- `persistent`: boolean

### Styles
```css
bg-primary-800 rounded-xl shadow-xl
w-80 /* 320px */
px-6 py-4
```

---

## NotificationStack

**Fichier**: `/src/lib/components/Notifications/NotificationStack.svelte`

### Caractéristiques
- Position: `fixed top-8 right-8 z-50`
- Limite: 5 notifications max
- Ordre: plus récentes en haut

### Animations
```svelte
in:fly|global={{ duration: 150, x: 50 }}
out:fade|global={{ duration: 150 }}
animate:flip={{ duration: 500 }}
```

---

## ProgressBar

**Fichier**: `/src/lib/components/ProgressBar.svelte`

### Props
- `progress`: number (0 à 1)

### Styles
```css
/* Conteneur */
bg-zinc-200 bg-opacity-20 rounded-full h-1

/* Barre */
bg-zinc-200 bg-opacity-80
```

### Animation
```css
transition: max-width;
delay: 200ms;
duration: 500ms;
```

### Équivalent Kotlin

```kotlin
@Composable
fun ProgressBar(progress: Float) {
    var animatedProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(progress) {
        delay(200)
        animatedProgress = progress
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(Color.White.copy(alpha = 0.2f), CircleShape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(animateFloatAsState(animatedProgress).value)
                .background(Color.White.copy(alpha = 0.8f))
        )
    }
}
```

---

## Récapitulatif animations

| Composant | Entrée | Sortie | Durée |
|-----------|--------|--------|-------|
| Dialog | fade | fade | 100ms |
| Notification | fly x:50 | fade | 150ms |
| NotificationStack | flip | - | 500ms |
| ProgressBar | - | - | 500ms + 200ms delay |
