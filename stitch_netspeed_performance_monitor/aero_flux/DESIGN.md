# Design System Specification: The Kinetic Stream

## 1. Overview & Creative North Star
The Creative North Star for this design system is **"The Kinetic Stream."** 

In an era of cluttered utility apps, this system moves beyond mere functionalism into a high-end, editorial experience. It treats network data not as static numbers, but as a fluid, living pulse. We reject the "standard dashboard" aesthetic in favor of a sophisticated, high-performance instrument. By utilizing intentional asymmetry, deep tonal layering, and "breathing" data visualizations, we create an environment that feels both authoritative and ethereal. 

This system breaks the "template" look by favoring negative space and typographic scale over rigid grid lines, ensuring the user's focus is always on the velocity of their digital life.

---

## 2. Colors & Tonal Depth
The palette is a curated spectrum of deep midnight blues and high-velocity teals, engineered to evoke a sense of professional-grade technology.

### The Color Logic
- **Primary (`#00dfc1`)**: Our "Speed Teal." This is the pulse of the system. Use it sparingly for active data streams, primary actions, and critical status indicators.
- **Surface Hierarchy (`surface_container` tiers)**: This system relies on "Nesting" rather than "Sectioning."
    - **Surface (`#11131d`)**: The canvas.
    - **Surface Container Low (`#191b26`)**: Secondary content areas.
    - **Surface Container High (`#282934`)**: Interactive cards or elevated modules.

### The "No-Line" Rule
**Explicit Instruction:** Designers are prohibited from using 1px solid borders to define sections. Layout boundaries must be achieved through background color shifts. For example, a `surface_container_low` card sitting on a `surface` background provides enough contrast to define a boundary without the visual "noise" of a stroke.

### The Glass & Gradient Rule
To achieve a signature high-end feel, floating elements (like speed overlays or mobile navigation) should utilize **Glassmorphism**.
- **Implementation**: Combine a semi-transparent `surface_container_highest` (approx. 60-80% opacity) with a `backdrop-filter: blur(20px)`.
- **Signature Textures**: Use subtle linear gradients for large-scale data visualizations, transitioning from `primary` (`#00dfc1`) to `primary_container` (`#00a38d`) to add dimension and "soul" to the speed curves.

---

## 3. Typography
Our typography is a dialogue between geometric precision and utilitarian clarity.

- **Display & Headlines (Manrope)**: We use Manrope for its modern, technical geometry. **Display-lg (3.5rem)** should be used for the primary speed readout. Its wide apertures and clean lines make "100 Mbps" feel like a luxury statement.
- **Body & Labels (Inter)**: We use Inter for all technical data, labels, and instructional text. Inter’s tall x-height ensures that even at **label-sm (0.6875rem)**, network protocols and IP addresses remain perfectly legible.

**Hierarchy as Identity:** 
Use extreme contrast in scale. A massive `display-lg` speed number paired with a tiny, all-caps `label-md` "DOWNLOAD" creates an editorial, high-tech rhythm that standard "Goldilocks" sizing (where everything is medium-sized) lacks.

---

## 4. Elevation & Depth
In this design system, depth is a function of light and layering, not drop shadows.

### The Layering Principle
Hierarchy is achieved by "stacking" the surface tiers.
- **Base**: `surface`
- **Section**: `surface_container_low`
- **Interactive Element**: `surface_container_high`
This creates a soft, natural lift that mimics stacked sheets of fine, dark paper.

### Ambient Shadows
When a component must float (e.g., a modal or a floating action button), use **Ambient Shadows**.
- **Specification**: Shadows must be extra-diffused. Use a blur value of 30px–60px with an opacity of 4%–8%. 
- **Shadow Tint**: The shadow color should be a darker version of the `background` (`#0c0e18`), never pure black, to ensure the depth feels integrated into the deep blue environment.

### The "Ghost Border" Fallback
If accessibility requirements demand a container boundary, use a **Ghost Border**.
- **Specification**: `outline_variant` (`#414754`) at 15% opacity. This provides a "suggestion" of a container without breaking the fluid aesthetic.

---

## 5. Components

### Buttons: The Kinetic Trigger
- **Primary**: Solid `primary` (`#00dfc1`) with `on_primary` text. Use `rounded-md` (0.75rem) for a modern, soft touch.
- **Secondary**: `secondary_container` background with `on_secondary_container` text. 
- **Interaction**: On hover, apply a subtle glow using a box-shadow tinted with the `primary` color (15% opacity).

### Cards & Data Modules
- **Rule**: No divider lines.
- **Separation**: Use `2rem` of vertical whitespace (from the spacing scale) or a shift from `surface_container_lowest` to `surface_container_low`.
- **Rounding**: Use `rounded-xl` (1.5rem) for main dashboard cards to give them a premium, friendly feel.

### Data Visualization (The Pulse)
- **Line Graphs**: Use a 3px stroke width for the primary data line. Use a gradient fill beneath the line (Primary to Transparent) to create a "wave" effect.
- **Gauges**: Use the `tertiary` (`#00daf8`) color for upload speeds to differentiate from download speeds, maintaining a cohesive "cool" palette.

### Input Fields
- **Style**: Use `surface_container_highest` as the fill. 
- **Active State**: Instead of a thick border, use a 2px `primary` bottom-bar or a subtle glow effect to indicate focus.

---

## 6. Do's and Don'ts

### Do:
- **Do** embrace white space. If a layout feels "empty," it is likely working.
- **Do** use `manrope` for any number that changes dynamically.
- **Do** use `primary_fixed` for small highlights or badges to ensure they "pop" against the deep background.

### Don't:
- **Don't** use 1px solid lines to separate list items. Use tonal shifts or generous padding.
- **Don't** use standard "Material Blue." Stick strictly to the `secondary` (`#a5c8ff`) and `tertiary` (`#00daf8`) ranges for a custom feel.
- **Don't** use sharp 0px corners. This system is "fluid"; even the most technical data should be housed in containers with at least a `sm` (0.25rem) radius.
- **Don't** use high-contrast white text (`#ffffff`) on the deep blue background. Use `on_surface` (`#e1e1f0`) to reduce eye strain and maintain the premium tonal range.