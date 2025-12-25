# Formulaires - Reiverr

## TextField

**Fichier**: `/src/lib/components/TextField.svelte`

### Props
- `value`: string - Valeur du champ
- `placeholder`: string - Texte d'indication
- `type`: HTMLInputTypeAttribute - Type d'input
- `isValid`: Promise<boolean> | boolean - Validation

### Styles
```css
/* Label */
text-secondary-300 font-medium tracking-wide text-sm mb-1

/* Input */
bg-primary-900 px-6 py-2 rounded-lg
```

### Icônes de validation
- Loading: Spinner
- Valid: Check (✓)
- Invalid: Cross1 (✗)

---

## Toggle

**Fichier**: `/src/lib/components/Toggle.svelte`

### Dimensions
- Largeur: 52px (`w-[3.25rem]`)
- Hauteur: 28px (`h-7`)
- Cercle: 24px (`h-6 w-6`)

### États
```css
/* Non coché */
bg-secondary-700

/* Coché */
peer-checked:bg-primary-500
peer-checked:after:translate-x-full
```

### Équivalent Kotlin
```kotlin
Switch(
    checked = checked,
    onCheckedChange = onCheckedChange,
    colors = SwitchDefaults.colors(
        checkedTrackColor = Primary500,
        uncheckedTrackColor = Secondary700
    )
)
```

---

## Checkbox

**Fichier**: `/src/lib/components/Checkbox.svelte`

### Dimensions
- Conteneur: 36px × 36px (`w-9 h-9`)
- Icône: 24px
- Border: 2px, rounded-xl

### États
```css
/* Non coché */
border-transparent

/* Coché */
border-secondary-200

/* Focus + coché */
focus-within:border-primary-500
focus-within:bg-primary-500
```

---

## SelectField

**Fichier**: `/src/lib/components/SelectField.svelte`

### Props
- `color`: 'secondary' | 'primary'
- `value`: string
- `disabled`: boolean
- `action`: async function

### Styles
```css
flex items-center justify-between
rounded-xl px-6 py-2.5 font-medium
border-2 border-transparent
focus:border-primary-500 hover:border-primary-500
```

### Icône
ArrowRight avec animation au focus:
```css
text-primary-500 translate-x-0.5 scale-110
```

---

## SelectItem

**Fichier**: `/src/lib/components/SelectItem.svelte`

```css
flex items-center justify-between
bg-primary-900 rounded-xl px-6 py-2.5
font-medium border-2 border-transparent
focus:border-primary-500 hover:border-primary-500
cursor-pointer
```

Affiche Check (24px) si `selected = true`.

---

## SelectButtonGroup

**Fichier**: `/src/lib/components/SelectButtonGroup.svelte`

### Props
```typescript
type Option = {
  label: string;
  value?: string;
  disabled?: boolean;
};
```
- `options`: Option[]
- `selected`: string
- `name`: string (titre)
- `style`: 'primary' | 'secondary'

### Structure
```css
/* Titre */
font-semibold text-sm text-secondary-400 mb-2

/* Conteneur */
rounded-xl bg-primary-900

/* Séparateur */
h-[1px] w-full bg-secondary-700

/* Item */
h-12 px-6 py-3 border-2
rounded-t-xl /* premier */
rounded-b-xl /* dernier */
```

---

## IconToggle

**Fichier**: `/src/lib/components/IconToggle.svelte`

### Dimensions
- Taille: 44px × 44px (`h-11 w-11`)
- Icône: 19px

### Styles
```css
bg-primary-900 flex items-center justify-center
rounded-lg cursor-pointer
```

Classes `selected`/`unselected` selon focus.

---

## Couleurs communes

```css
bg-primary-900: #1F2937
bg-primary-500: #8B5CF6
bg-secondary-700: #374151
text-secondary-300: #D1D5DB
text-secondary-400: #9CA3AF
border-primary-500: #8B5CF6
```
