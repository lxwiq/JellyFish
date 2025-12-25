# Thème et Couleurs - Reiverr

## Palette PRIMARY (Tons Dorés/Ambrés)

Base HSL: `hsl(40, 60%, X%)` pour tons clairs, saturation réduite pour tons sombres.

| Nuance | HSL | Utilisation |
|--------|-----|-------------|
| 50 | `hsl(40, 60%, 95%)` | Arrière-plans très clairs |
| 100 | `hsl(40, 60%, 90%)` | États hover |
| 200 | `hsl(40, 60%, 80%)` | Bordures douces |
| 300 | `hsl(40, 60%, 70%)` | Accents moyens |
| 400 | `hsl(40, 60%, 65%)` | **Couleur principale** |
| 500 | `hsl(40, 60%, 55%)` | Hover sur primaire |
| 600 | `hsl(40, 30%, 24%)` | Textes sur fond clair |
| 700 | `hsl(40, 30%, 18%)` | Bordures foncées |
| 800 | `hsl(40, 20%, 12%)` | Arrière-plans sombres |
| 900 | `hsl(40, 20%, 8%)` | Ultra-sombres |
| 950 | `hsl(40, 20%, 4%)` | Noir profond |

## Palette SECONDARY (Tons Neutres Chauds)

Base HSL: `hsl(40, 12%, X%)` avec saturation très faible.

| Nuance | HSL | Utilisation |
|--------|-----|-------------|
| 50 | `hsl(40, 12%, 95%)` | Surfaces élevées |
| 100 | `hsl(40, 12%, 90%)` | Cartes |
| 200 | `hsl(40, 12%, 80%)` | Séparateurs |
| 300 | `hsl(40, 12%, 70%)` | Textes désactivés |
| 400 | `hsl(40, 12%, 65%)` | Textes faible importance |
| 500 | `hsl(40, 12%, 55%)` | Textes secondaires |
| 600 | `hsl(40, 8%, 30%)` | Textes sur fond clair |
| 700 | `hsl(40, 8%, 20%)` | Sections sombres |
| 800 | `hsl(40, 8%, 12%)` | **Arrière-plan principal** |
| 900 | `hsl(40, 8%, 7%)` | Overlays |
| 950 | `hsl(40, 8%, 4%)` | Noir chaud |

## Couleurs Spéciales

| Nom | Valeur | Utilisation |
|-----|--------|-------------|
| darken | `#07050166` | Overlay sombre 40% |
| lighten | `#fde68a20` | Overlay clair 12% |
| highlight-foreground | `#f6c304` | Éléments actifs |
| highlight-background | `#161517` | Fond focus |

## Équivalent Kotlin

```kotlin
object ReiverColors {
    object Primary {
        val shade400 = Color.hsl(40f, 0.60f, 0.65f) // Main
        val shade500 = Color.hsl(40f, 0.60f, 0.55f)
        val shade800 = Color.hsl(40f, 0.20f, 0.12f)
        val shade900 = Color.hsl(40f, 0.20f, 0.08f)
    }

    object Secondary {
        val shade100 = Color.hsl(40f, 0.12f, 0.90f) // Titres
        val shade300 = Color.hsl(40f, 0.12f, 0.70f) // Body text
        val shade700 = Color.hsl(40f, 0.08f, 0.20f)
        val shade800 = Color.hsl(40f, 0.08f, 0.12f) // Main BG
        val shade900 = Color.hsl(40f, 0.08f, 0.07f)
    }

    val highlightForeground = Color(0xFFF6C304)
    val highlightBackground = Color(0xFF161517)
}
```

## Guide d'Utilisation

### Arrière-plans (Dark Mode)
1. Base: `secondary-950/900`
2. Élevé niveau 1: `secondary-800` (cartes)
3. Élevé niveau 2: `secondary-700` (interactifs)

### Textes (Dark Mode)
1. Primaire: `primary-100/200` (titres)
2. Secondaire: `secondary-300/400` (corps)
3. Tertiaire: `secondary-500` (labels)
4. Désactivé: `secondary-600`

### Éléments Interactifs
- Boutons primaires: `primary-400`, hover `primary-300`
- Focus: border `highlight-foreground`
