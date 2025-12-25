# Typographie - Reiverr

## Police principale

**Montserrat** avec fallback sans-serif.

```javascript
fontFamily: {
  sans: ['Montserrat', 'sans-serif'],
  display: ['Montserrat', 'system', 'sans-serif']
}
```

## Styles de titres

### h1 - Titre principal
```css
.h1 {
  font-weight: 600; /* semibold */
  font-size: 2.25rem; /* 36px */
  color: hsl(40, 12%, 90%); /* secondary-100 */
  letter-spacing: 0.05em; /* tracking-wider */
}
```

### h2 - Titre secondaire
```css
.h2 {
  font-weight: 600;
  font-size: 1.875rem; /* 30px */
  color: hsl(40, 12%, 90%);
}
```

### h3 - Titre tertiaire
```css
.h3 {
  font-weight: 600;
  font-size: 1.5rem; /* 24px */
  color: hsl(40, 12%, 90%);
}
```

### h4 - Titre quaternaire
```css
.h4 {
  font-weight: 600;
  font-size: 1.25rem; /* 20px */
  color: hsl(40, 12%, 90%);
}
```

### h5 - Titre niveau 5
```css
.h5 {
  font-weight: 500; /* medium */
  font-size: 1.125rem; /* 18px */
  color: hsl(40, 12%, 90%);
}
```

## Texte courant

### body
```css
.body {
  font-weight: 500;
  font-size: 1.125rem; /* 18px */
  color: hsl(40, 12%, 70%); /* secondary-300 */
}
```

### h-ghost (texte fantôme)
```css
.h-ghost {
  font-weight: 600;
  font-size: 1.5rem; /* 24px */
  color: hsl(40, 12%, 55%); /* secondary-500 */
}
```

### error
```css
.error {
  color: rgb(239, 68, 68); /* red-500 */
}
```

## Mode TV

Pour les écrans TV, taille de base augmentée:

```css
@media tv {
  html { font-size: 24px; }
}
```

| Style | Normal | TV (×1.5) |
|-------|--------|-----------|
| h1 | 36px | 54px |
| h2 | 30px | 45px |
| h3 | 24px | 36px |
| h4 | 20px | 30px |
| h5 | 18px | 27px |
| body | 18px | 27px |

## Équivalent Kotlin

```kotlin
object ReiverTypography {
    val h1 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        color = Color(0xFFE8E7E6), // secondary-100
        letterSpacing = 0.05.em
    )

    val h2 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp,
        color = Color(0xFFE8E7E6)
    )

    val h3 = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        color = Color(0xFFE8E7E6)
    )

    val body = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        color = Color(0xFFB3B0AD) // secondary-300
    )

    val error = TextStyle(
        color = Color(0xFFEF4444) // red-500
    )
}

// Mode TV
val scaleFactor = if (isTvMode) 1.5f else 1f
```
