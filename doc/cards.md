# Cards - Reiverr

## Card de base

**Fichier**: `/src/lib/components/Card.svelte`

### Structure
```
┌──────────────────────┐
│      Image           │
│    (LazyImg)         │
├──────────────────────┤
│ Titre                │
│ Sous-titre           │
└──────────────────────┘
```

### Dimensions
- Largeur: variable
- Ratio image: 2:3 (poster) ou 16:9 (backdrop)
- Border radius: 12px (`rounded-xl`)

### Styles
```css
bg-secondary-800 rounded-xl overflow-hidden
selectable cursor-pointer
```

### États
```css
/* Focus */
border-primary-500 scale-105

/* Hover */
hover:border-primary-500
```

---

## MediaCard

Variante avec overlay d'informations.

### Structure
```
┌──────────────────────┐
│      Image           │
│                      │
│  ┌────────────────┐  │
│  │ ▶ Progress Bar │  │
│  │ Titre          │  │
│  │ Année • Rating │  │
│  └────────────────┘  │
└──────────────────────┘
```

### Overlay
```css
absolute inset-x-0 bottom-0
bg-gradient-to-t from-secondary-900 to-transparent
p-4
```

### Badge (optionnel)
```css
absolute top-2 right-2
bg-primary-500 text-secondary-950
rounded-full px-2 py-0.5 text-xs font-semibold
```

---

## CardGrid

**Fichier**: `/src/lib/components/CardGrid.svelte`

### Structure
```css
grid gap-4
grid-cols-2 /* mobile */
sm:grid-cols-3
md:grid-cols-4
lg:grid-cols-5
xl:grid-cols-6
```

### Props
- `items`: array - Liste des éléments
- `cardWidth`: number - Largeur des cartes

---

## Carousel

**Fichier**: `/src/lib/components/Carousel/Carousel.svelte`

### Structure
```
┌─────────────────────────────────────────┐
│ Titre                            Voir + │
├─────────────────────────────────────────┤
│ ◀ │ Card │ Card │ Card │ Card │ ▶      │
└─────────────────────────────────────────┘
```

### Styles
```css
/* Conteneur */
flex overflow-x-auto gap-4 scrollbar-hide

/* Navigation */
absolute inset-y-0 left-0/right-0
flex items-center
bg-gradient-to-r from-secondary-900/80 to-transparent
```

### Props
- `title`: string
- `items`: array
- `onSeeAll`: function

---

## HeroCarousel

**Fichier**: `/src/lib/components/HeroShowcase/`

### Structure
```
┌─────────────────────────────────────────┐
│                                         │
│           Image Backdrop                │
│                                         │
│  ┌─────────────────────────────────┐   │
│  │ Logo ou Titre                   │   │
│  │ Description...                  │   │
│  │ [Lecture] [+ Liste]             │   │
│  └─────────────────────────────────┘   │
│                                         │
│             • • ○ • •                   │
└─────────────────────────────────────────┘
```

### Dimensions
- Hauteur: 70vh min
- Overlay: gradient du bas

### Composants
- `HeroShowcase.svelte`: Conteneur principal
- `HeroCard.svelte`: Slide individuel
- `PageDots.svelte`: Indicateurs

---

## Équivalent Kotlin

```kotlin
@Composable
fun MediaCard(
    title: String,
    subtitle: String,
    imageUrl: String,
    progress: Float? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        if (isFocused) 1.05f else 1f
    )

    Card(
        modifier = modifier
            .scale(scale)
            .focusable()
            .onFocusChanged { isFocused = it.isFocused }
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        border = if (isFocused) BorderStroke(2.dp, Primary500) else null
    ) {
        Box {
            AsyncImage(
                model = imageUrl,
                contentScale = ContentScale.Crop,
                modifier = Modifier.aspectRatio(2f/3f)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Secondary900)
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    progress?.let {
                        LinearProgressIndicator(
                            progress = it,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                    Text(title, style = Typography.h5)
                    Text(subtitle, style = Typography.body)
                }
            }
        }
    }
}

@Composable
fun Carousel(
    title: String,
    items: List<Any>,
    onSeeAll: (() -> Unit)? = null,
    content: @Composable (Any) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, style = Typography.h3)
            onSeeAll?.let {
                TextButton(onClick = it) {
                    Text("Voir plus")
                }
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                content(item)
            }
        }
    }
}
```

---

## Bonnes pratiques

1. **Images**: Toujours utiliser LazyImg pour le lazy loading
2. **Focus**: Implémenter scale + bordure pour feedback visuel
3. **Navigation D-pad**: Support Container/Selectable
4. **Performance**: Limiter le nombre d'items visibles
5. **Placeholders**: Afficher skeletons pendant le chargement
