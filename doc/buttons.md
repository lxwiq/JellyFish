# Boutons - Reiverr

## Composant Button

**Fichier**: `/src/lib/components/Button.svelte`

## Types de boutons

| Type | Background | Usage |
|------|------------|-------|
| `primary` | `bg-secondary-800` | Actions principales |
| `secondary` | border uniquement | Actions secondaires |
| `primary-dark` | `bg-primary-900` | Sur fond clair |

## Props

| Prop | Type | Défaut | Description |
|------|------|--------|-------------|
| `disabled` | boolean | false | Désactive le bouton |
| `focusOnMount` | boolean | false | Focus au montage |
| `type` | string | 'primary' | Type de style |
| `confirmDanger` | boolean | false | Demande confirmation |
| `action` | function | null | Action async |
| `secondaryAction` | function | null | Action secondaire (menu) |
| `icon` | ComponentType | - | Icône avant le texte |
| `iconAfter` | ComponentType | - | Icône après le texte |

## Dimensions

- **Hauteur**: 48px (`h-12`)
- **Padding horizontal**: 24px (`px-6`)
- **Border radius**: 12px (`rounded-xl`)
- **Icônes**: 19px

## Styles CSS

### Bouton primary
```css
h-12 flex-1 flex items-center
rounded-xl px-6
bg-secondary-800 selectable
cursor-pointer
```

### Bouton secondary
```css
border-2 p-1 hover:border-primary-500
/* Focus: border-primary-500 */
/* Inner: bg-primary-500 text-secondary-950 */
```

### État désactivé
```css
cursor-not-allowed pointer-events-none opacity-40
```

### État confirmDanger
```css
!border-red-500
!bg-red-500
```

## Action secondaire

Quand `secondaryAction` est défini, le bouton est divisé:
- Partie gauche: action principale (`rounded-l-xl`)
- Séparateur: `w-0.5 bg-secondary-700`
- Partie droite: icône DotsVertical (`rounded-r-xl`)

## Équivalent Kotlin

```kotlin
enum class ButtonType { PRIMARY, SECONDARY, PRIMARY_DARK }

@Composable
fun ReiverButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.PRIMARY,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    confirmDanger: Boolean = false
) {
    var armed by remember { mutableStateOf(false) }
    val isFocused = /* focus state */

    val backgroundColor = when (type) {
        ButtonType.PRIMARY -> Color(0xFF1F2937) // secondary-800
        ButtonType.PRIMARY_DARK -> Color(0xFF1a1614) // primary-900
        ButtonType.SECONDARY -> Color.Transparent
    }

    val borderColor = when {
        confirmDanger && armed -> Color(0xFFEF4444) // red-500
        type == ButtonType.SECONDARY && isFocused -> Primary500
        else -> Color.Transparent
    }

    Surface(
        modifier = modifier
            .height(48.dp)
            .clickable(enabled = enabled) {
                if (confirmDanger && !armed) {
                    armed = true
                } else {
                    onClick()
                    armed = false
                }
            }
            .alpha(if (enabled) 1f else 0.4f),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        border = BorderStroke(2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            icon?.let {
                Icon(it, null, Modifier.size(19.dp).padding(end = 8.dp))
            }
            Text(text, fontWeight = FontWeight.Medium)
        }
    }
}
```

## Utilisation

```svelte
<!-- Bouton simple -->
<Button>Confirmer</Button>

<!-- Avec icône -->
<Button icon={Play}>Lecture</Button>

<!-- Action async -->
<Button action={() => api.save()}>Sauvegarder</Button>

<!-- Confirmation danger -->
<Button confirmDanger action={() => api.delete()}>Supprimer</Button>

<!-- Avec action secondaire -->
<Button secondaryAction={() => showMenu()}>Options</Button>
```
