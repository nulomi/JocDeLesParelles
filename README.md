# Joc de Parelles (Memoria) - MVVM en Jetpack Compose

Este proyecto es un juego de memoria (también conocido como "Joc de Parelles" o *Memory*) desarrollado para Android utilizando **Jetpack Compose** y siguiendo el patrón de arquitectura **MVVM** (Model-View-ViewModel).

El objetivo del juego es encontrar las 3 parejas de cartas (6 cartas en total) que se encuentran ocultas y desordenadas en un tablero.

## 🏗️ Arquitectura MVVM

El proyecto está dividido en tres capas principales para mantener el código limpio, escalable y fácil de testear:

1. **Model (ModelGame.kt):** Contiene los datos puros y la lógica de negocio del juego. No sabe nada de la interfaz gráfica.
2. **ViewModel (GameViewModel.kt):** Actúa como puente entre el Modelo y la Vista. Contiene el estado de la UI y gestiona los eventos (clics del usuario), además de manejar la asincronía.
3. **View (MainActivity.kt y GameScreen.kt):** Es la interfaz de usuario hecha con Jetpack Compose. Observa los cambios del ViewModel y dibuja la pantalla.

---

## 📂 Explicación de las Clases y el Código

### 1. El Modelo (`ModelGame.kt`)
Aquí definimos las estructuras de datos y las reglas del juego:

*   **`CardModel`**: Representa una carta individual. Guarda su `id`, su `posición`, la imagen que tiene por delante (`frontImageRes`) y dos estados booleanos: `isFlipped` (si está girada) e `isMatched` (si ya se ha encontrado su pareja).
    *   La propiedad `currentImageRes` decide qué imagen mostrar: si está girada o emparejada muestra su imagen, si no, muestra la carta boca abajo (`R.drawable.cartagirada`).
*   **`BoardModel`**: Representa el tablero entero. Contiene la lista de cartas y un booleano `isGameOver`.
    *   **`createInitialBoard()`**: Crea el tablero inicial. Coge las 3 imágenes (`lobo`, `mano`, `perro`), las duplica para tener las 6 cartas, las baraja con `.shuffled()` y las coloca en el tablero.
    *   **`onCardClick()`**: Gira la carta seleccionada.
    *   **`checkMatches()`**: Comprueba si las dos cartas giradas son iguales. Si lo son, las marca como emparejadas. Si no, las vuelve a girar.

### 2. El ViewModel (`GameViewModel.kt`)
Se encarga de gestionar el estado y la lógica de la aplicación:

*   Utiliza **`MutableStateFlow`** para guardar el estado del tablero y **`StateFlow`** para exponerlo de forma segura a la vista. Esto permite que la UI se actualice automáticamente cuando el estado cambia.
*   **`onCardClick(position)`**: Recibe el clic del usuario. Si ya hay 2 cartas giradas, ignora el clic. Si se giran 2 cartas, usa **`viewModelScope.launch`** y **`delay(1000L)`** para esperar 1 segundo antes de comprobar si hacen pareja, permitiendo al usuario ver qué carta ha levantado antes de que se vuelvan a tapar.
*   **`restartGame()`**: Reinicia el estado del tablero llamando al método de fábrica del modelo.

### 3. La Vista (`MainActivity.kt` y `GameScreen.kt`)
*   **`MainActivity`**: Es el punto de entrada de la app. Inicializa el `GameViewModel` y configura el `Scaffold` básico.
*   **`GameScreen`**: Es el composable principal. Recoge el estado del ViewModel con `collectAsState()`. Utiliza un `Box` para poner tu imagen de fondo (`R.drawable.fondo`) y encima un `Column` con el `LazyVerticalGrid` que dibuja las cartas en una cuadrícula de 3 columnas.
*   **`CardItemView`**: Es el diseño visual de cada carta. Usa un `Card` de Material3. Cuando se hace clic en él, llama al ViewModel. La imagen usa `ContentScale.Fit` para que no se recorte.

---

## ❓ ¿Por qué el paquete se llama `org.proven.projecte.nuria1.prova3enratlla`?

En Android, el **nombre del paquete** (package) es el identificador único de tu aplicación en el sistema y en la Google Play Store.

He utilizado `org.proven.projecte.nuria1.prova3enratlla` por las siguientes razones:

1. **Convención inversa de dominio:** Es el estándar en Java y Kotlin. Se empieza por el dominio de arriba hacia abajo (`org` -> `proven` -> `projecte`).
2. **Identificación del autor/centro:** `nuria1` identifica claramente quién ha creado el proyecto (tus iniciales o tu nombre).
3. **Identificación del proyecto:** `prova3enratlla` es el nombre de tu proyecto. Aunque el juego sea de cartas (Parelles), el nombre del paquete hace referencia al nombre con el que creaste el proyecto en Android Studio.
4. **Evitar conflictos:** Si subes tu app a Google Play, no puede existir otra app en el mundo con ese mismo nombre de paquete. Al usar tu nombre (`nuria1`), te aseguras de que sea único.

**💡 Nota importante:** Todos los archivos `.kt` de tu proyecto **deben** empezar con la misma línea `package org.proven.projecte.nuria1.prova3enratlla`. Además, esta ruta debe coincidir exactamente con el `namespace` y el `applicationId` definidos en el archivo `build.gradle.kts` de tu módulo `app`.

---

## 🎨 Recursos Utilizados (Drawables)
Para que el juego funcione correctamente, la carpeta `res/drawable` debe contener las siguientes imágenes:
*   `fondo`: Imagen de fondo de la pantalla.
*   `cartagirada`: Imagen del dorso de la carta (boca abajo).
*   `lobo`: Imagen de la carta del lobo.
*   `mano`: Imagen de la carta de la mano.
*   `perro`: Imagen de la carta del perro.
