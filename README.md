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
catedrarecetario/
├── app/
│ ├── java/sv/edu/catedra_recetario/ # Código fuente principal (Activities, Models, Adapters, Repository)
│ ├── res/
│ │ ├── layout/ # Archivos XML de diseño de interfaz
│ │ ├── drawable/ # Iconos e imágenes del proyecto
│ │ ├── menu/ # Configuración del BottomNavigation y menús
│ │ └── values/ # Strings, colores y temas
│ └── AndroidManifest.xml # Configuración general de la aplicación
└── build.gradle # Configuración de Gradle
---

## Requisitos previos

Antes de ejecutar el proyecto asegúrate de tener:

**Android Studio Flamingo o superior**
**JDK 11 o superior**
**Dispositivo físico o emulador Android 7.0 (API 24)** o superior
**Conexión a Internet** (para dependencias y Firebase)
**Cuenta de Firebase** (si deseas usar tu propia base de datos)

---
