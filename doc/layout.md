# Layout - Reiverr

## Container

**Fichier**: `/src/lib/components/Container.svelte`

### Description
Composant de base pour la gestion du focus et de la navigation D-pad.

### Props principales
- `direction`: 'horizontal' | 'vertical' | 'grid'
- `focusOnMount`: boolean
- `trapFocus`: boolean
- `focusedChild`: boolean
- `class`: string

### Événements
- `clickOrSelect`: Clic ou sélection
- `navigate`: Navigation D-pad
- `enter`: Touche entrée
- `back`: Touche retour
- `mount`: Montage du composant

### Système Selectable
```typescript
import { Selectable, registrars } from '$lib/selectable';

// Enregistrement global
registrars.sidebar.registrar(selectable);
registrars.main.registrar(selectable);
```

---

## Panel

**Fichier**: `/src/lib/components/Panel.svelte`

### Tailles
| Size | Largeur |
|------|---------|
| sm | auto |
| lg | 80% |
| full | 100% |
| dynamic | auto |

### Styles
```css
bg-secondary-800 rounded-2xl
overflow-hidden
```

### Structure
```
┌─────────────────────────┐
│ Header (optionnel)      │
├─────────────────────────┤
│                         │
│ Content (slot)          │
│                         │
└─────────────────────────┘
```

---

## Page Layout

### Structure principale
```
┌────────────────────────────────────────────┐
│ Sidebar │        Main Content              │
│  (96px) │                                  │
│         │  ┌──────────────────────────┐   │
│  ○ User │  │ Hero / Header            │   │
│         │  ├──────────────────────────┤   │
│  ○ Séries│  │                          │   │
│  ○ Films │  │ Content                  │   │
│  ○ Biblio│  │                          │   │
│  ○ Search│  │ Carousel 1               │   │
│         │  │ Carousel 2               │   │
│  ○ Manage│  │ ...                      │   │
│         │  └──────────────────────────┘   │
└────────────────────────────────────────────┘
```

### Styles
```css
/* Main content */
ml-24 /* Offset pour sidebar */
min-h-screen

/* Page container */
px-8 lg:px-16
py-8
```

---

## Grid System

### Grille responsive
```css
grid gap-4
grid-cols-2
sm:grid-cols-3
md:grid-cols-4
lg:grid-cols-5
xl:grid-cols-6
```

### Espacements
| Classe | Valeur |
|--------|--------|
| gap-2 | 8px |
| gap-4 | 16px |
| gap-6 | 24px |
| gap-8 | 32px |

---

## Scroll Container

### Horizontal scroll
```css
flex overflow-x-auto gap-4
scrollbar-hide /* plugin tailwind-scrollbar-hide */
```

### Snap scroll (optionnel)
```css
scroll-snap-type: x mandatory;

/* Items */
scroll-snap-align: start;
```

---

## Breakpoints

| Préfixe | Min-width |
|---------|-----------|
| sm | 640px |
| md | 768px |
| lg | 1024px |
| xl | 1280px |
| 2xl | 1536px |

---

## Équivalent Kotlin

```kotlin
@Composable
fun PageLayout(
    sidebar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        // Sidebar
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(96.dp)
        ) {
            sidebar()
        }

        // Main content
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(horizontal = 32.dp, vertical = 32.dp)
        ) {
            content()
        }
    }
}

@Composable
fun Panel(
    size: PanelSize = PanelSize.SM,
    header: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val widthFraction = when (size) {
        PanelSize.SM -> 0.5f
        PanelSize.LG -> 0.8f
        PanelSize.FULL -> 1f
        PanelSize.DYNAMIC -> null
    }

    Surface(
        modifier = Modifier
            .then(
                if (widthFraction != null)
                    Modifier.fillMaxWidth(widthFraction)
                else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
        color = Secondary800
    ) {
        Column {
            header?.invoke()
            content()
        }
    }
}

enum class PanelSize { SM, LG, FULL, DYNAMIC }

// Grid responsive
@Composable
fun ResponsiveGrid(
    items: List<Any>,
    content: @Composable (Any) -> Unit
) {
    val configuration = LocalConfiguration.current
    val columns = when {
        configuration.screenWidthDp >= 1280 -> 6
        configuration.screenWidthDp >= 1024 -> 5
        configuration.screenWidthDp >= 768 -> 4
        configuration.screenWidthDp >= 640 -> 3
        else -> 2
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { item ->
            content(item)
        }
    }
}
```

---

## Z-index hierarchy

| Élément | Z-index |
|---------|---------|
| Sidebar | 20 |
| Modal Stack | 30 |
| Notification Stack | 50 |

---

## Bonnes pratiques

1. **Sidebar offset**: Toujours appliquer `ml-24` au contenu principal
2. **Padding cohérent**: Utiliser `px-8 lg:px-16` pour les pages
3. **Scroll horizontal**: Toujours `scrollbar-hide` pour l'esthétique
4. **Focus management**: Utiliser Container pour la navigation D-pad
5. **Responsive**: Tester sur tous les breakpoints
