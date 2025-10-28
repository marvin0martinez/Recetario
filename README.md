# Cátedra Recetario 
## En la rama marvin esta el proyecto
**Cátedra Recetario** es una aplicación móvil desarrollada en **Kotlin** para **Android**, cuyo propósito es permitir a los usuarios **visualizar, crear, editar y guardar recetas de cocina**.  
El proyecto fue desarrollado como parte de una **cátedra universitaria**, con el objetivo de aplicar conocimientos en el desarrollo de aplicaciones móviles modernas utilizando **Firebase**, **RecyclerView** y **Material Design**.

---

## Descripción general

La aplicación permite gestionar un conjunto de recetas de cocina en una interfaz sencilla e intuitiva.  
Entre sus principales funcionalidades se encuentran:

 **Visualizar recetas** con su imagen, descripción e ingredientes.  
 **Agregar, editar o eliminar recetas** (solo disponible para usuarios con rol “Chef Master”).  
 **Marcar recetas como favoritas** y consultarlas fácilmente.  
 **Guardar y sincronizar información con Firebase Firestore.**  
 **Cargar imágenes** mediante URL o desde Firebase Storage.  

---

##  Tecnologías utilizadas

| Tecnología | Descripción |
|-------------|-------------|
| **Kotlin** | Lenguaje principal de desarrollo. |
| **Android Studio** | Entorno de desarrollo integrado (IDE). |
| **Firebase Firestore** | Base de datos en la nube para guardar recetas y favoritos. |
| **Glide** | Librería para carga eficiente de imágenes. |
| **RecyclerView** | Listado dinámico y eficiente de recetas. |
| **Material Design** | Diseño moderno, limpio y adaptable. |

---

## Estructura del proyecto
📁 catedrarecetario/
│
├── 📁 .gradle/
├── 📁 .idea/                        # Configuración interna de Android Studio
│
├── 📁 app/
│   ├── 📁 build/                    # Archivos generados automáticamente (no modificar)
│   │
│   ├── 📁 libs/                     # Librerías externas opcionales (.jar)
│   │
│   ├── 📁 src/
│   │   ├── 📁 main/
│   │   │   ├── 📁 java/sv/edu/catedra_recetario/
│   │   │   │   ├── 📄 MainActivity.kt             # Pantalla principal de la app
│   │   │   │   ├── 📄 Receta.kt                  # Data class del modelo Receta
│   │   │   │   ├── 📄 RecetaAdapter.kt           # Adaptador para RecyclerView
│   │   │   │   ├── 📄 RecetaRepository.kt        # Clase que gestiona Firebase Firestore
│   │   │   │   ├── 📄 FavoritosFragment.kt       # Fragmento para mostrar recetas favoritas
│   │   │   │   ├── 📄 HomeFragment.kt            # Fragmento principal con la lista de recetas
│   │   │   │   ├── 📄 PerfilFragment.kt          # Fragmento del perfil del usuario
│   │   │   │   ├── 📄 LoginActivity.kt           # (Opcional) pantalla de login o autenticación
│   │   │   │   └── 📄 utils/                     # Funciones auxiliares o helpers
│   │   │   │
│   │   │   ├── 📁 res/                           # Recursos de interfaz y diseño
│   │   │   │   ├── 📁 drawable/                  # Imágenes, íconos y fondos personalizados
│   │   │   │   │   ├── 📄 ic_favorite.xml
│   │   │   │   │   ├── 📄 ic_favorite_border.xml
│   │   │   │   │   ├── 📄 placeholder_image.xml
│   │   │   │   │   └── 📄 error_image.xml
│   │   │   │   │
│   │   │   │   ├── 📁 layout/                    # Interfaces visuales (archivos XML)
│   │   │   │   │   ├── 📄 activity_main.xml
│   │   │   │   │   ├── 📄 fragment_home.xml
│   │   │   │   │   ├── 📄 fragment_favoritos.xml
│   │   │   │   │   ├── 📄 fragment_perfil.xml
│   │   │   │   │   ├── 📄 item_receta.xml        # Tarjeta individual de una receta
│   │   │   │   │   └── 📄 dialog_add_receta.xml  # Diálogo para agregar recetas
│   │   │   │   │
│   │   │   │   ├── 📁 menu/
│   │   │   │   │   ├── 📄 bottom_nav_menu.xml    # Navegación inferior (Home, Favoritos, Perfil)
│   │   │   │   │   └── 📄 menubutton_nav.xml     # Opciones de acción o menú contextual
│   │   │   │   │
│   │   │   │   ├── 📁 values/
│   │   │   │   │   ├── 📄 colors.xml             # Paleta de colores del tema
│   │   │   │   │   ├── 📄 strings.xml            # Textos y etiquetas de la app
│   │   │   │   │   ├── 📄 styles.xml             # Estilos globales
│   │   │   │   │   └── 📄 themes.xml             # Configuración de temas (Material Design)
│   │   │   │   │
│   │   │   │   ├── 📄 AndroidManifest.xml        # Configuración general de la app
│   │   │   │
│   │   │   └── 📁 test/ & 📁 androidTest/        # Pruebas unitarias e instrumentadas
│   │   │
│   │   ├── 📄 google-services.json               # Configuración de Firebase
│   │
│   ├── 📄 build.gradle                           # Configuración específica del módulo app
│   └── 📄 proguard-rules.pro                     # Reglas para optimización y ofuscación
│
├── 📄 build.gradle                               # Configuración principal del proyecto
├── 📄 settings.gradle                            # Configura los módulos del proyecto
├── 📄 gradlew / gradlew.bat                      # Scripts para compilar desde terminal
├── 📄 gradle.properties                          # Propiedades globales de Gradle
├── 📄 local.properties                           # Ruta del SDK de Android
└── 📄 README.md                                  # Documentación del proyecto (este archivo)


---

## Requisitos previos

Antes de ejecutar el proyecto asegúrate de tener:

**Android Studio Flamingo o superior**
**JDK 11 o superior**
**Dispositivo físico o emulador Android 7.0 (API 24)** o superior
**Conexión a Internet** (para dependencias y Firebase)
**Cuenta de Firebase** (si deseas usar tu propia base de datos)

---
