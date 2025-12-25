# Navigation - Reiverr

## Sidebar

**Fichier**: `/src/lib/components/Sidebar/Sidebar.svelte`

### Dimensions
- Largeur: 96px (`w-24`)
- Position: fixed, left, inset-y-0
- Z-index: 20
- Padding: 32px vertical (`py-8`)

### Items de navigation

| Route | Icône | Label |
|-------|-------|-------|
| /users | Person | {user.name} |
| / | Laptop | Series |
| /movies | CardStack | Movies |
| /library | Bookmark | Library |
| /search | MagnifyingGlass | Search |
| /manage | Gear | Manage |

Icônes depuis `radix-icons-svelte`, taille 32px (`w-8 h-8`).

### États visuels

```css
/* Normal */
text-stone-300

/* Hover */
hover:text-primary-500

/* Focus/Selected */
text-primary-500

/* Active (page courante) */
/* Indicateur DotFilled 19px à gauche */
```

### Expansion au hover

```css
/* Gradient de fond */
min-w-[40rem] w-[25vw]
bg-gradient-to-r from-secondary-900 to-transparent
group-hover:opacity-100

/* Labels */
absolute left-20
transition-opacity
group-hover:opacity-100
```

### Équivalent Kotlin

```kotlin
enum class NavigationTab(val route: String, val icon: ImageVector) {
    USERS("/users", Icons.Default.Person),
    SERIES("/", Icons.Default.Tv),
    MOVIES("/movies", Icons.Default.Movie),
    LIBRARY("/library", Icons.Default.Bookmark),
    SEARCH("/search", Icons.Default.Search),
    MANAGE("/manage", Icons.Default.Settings)
}

@Composable
fun NavigationSidebar(
    selectedTab: NavigationTab,
    activeTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(96.dp)
    ) {
        // Gradient animé
        AnimatedVisibility(visible = isExpanded) {
            Box(
                modifier = Modifier
                    .width(400.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Secondary900, Color.Transparent)
                        )
                    )
            )
        }

        Column(
            modifier = Modifier.padding(vertical = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Items...
        }
    }
}
```

---

## Tab System

**Fichiers**:
- `/src/lib/components/Tab/Tab.svelte`
- `/src/lib/components/Tab/TabContainer.svelte`

### Props Tab
- `tab`: number - Index de l'onglet
- `openTab`: Writable<number> - Store de l'onglet actif
- `size`: 'hug' | 'stretch'
- `direction`: 'horizontal' | 'vertical'

### Animation

```css
/* Actif */
opacity-100 translate-x-0

/* Inactif horizontal */
opacity-0 pointer-events-none
translate-x-[-40px] /* si openTab >= index */
translate-x-[40px]  /* si openTab < index */

/* Inactif vertical */
translate-y-[-40px] /* si openTab >= index */
translate-y-[40px]  /* si openTab < index */
```

### Équivalent Kotlin

```kotlin
@Composable
fun <T> TabContainer(
    selectedTab: T,
    tabs: List<T>,
    direction: TabDirection = TabDirection.Horizontal,
    content: @Composable (T) -> Unit
) {
    Box {
        tabs.forEachIndexed { index, tab ->
            AnimatedVisibility(
                visible = tab == selectedTab,
                enter = slideIn(direction, index) + fadeIn(),
                exit = slideOut(direction, index) + fadeOut()
            )
        }
    }
}
```

---

## PageDots

**Fichier**: `/src/lib/components/HeroShowcase/PageDots.svelte`

### Props
- `index`: number - Page courante
- `length`: number - Nombre total
- `onJump`: (index) => void

### Dimensions
- Point: 8px (`w-2 h-2`)
- XL: 10px (`xl:w-2.5 xl:h-2.5`)
- Padding: 6px (`p-1.5`)
- XL padding: 10px (`xl:p-2.5`)

### États
```css
/* Actif */
opacity-50

/* Inactif */
opacity-20

/* Hover */
hover:scale-125 hover:opacity-50
transition-transform
```

### Équivalent Kotlin

```kotlin
@Composable
fun PageDots(
    currentPage: Int,
    pageCount: Int,
    onPageSelected: (Int) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .alpha(if (index == currentPage) 0.5f else 0.2f)
                    .background(Color.White, CircleShape)
                    .clickable { onPageSelected(index) }
            )
        }
    }
}
```
