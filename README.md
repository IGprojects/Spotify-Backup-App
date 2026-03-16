# 🎵 Spotify Backup · Gestión de Playlists

**Spotify Backup** es una herramienta integral diseñada para que nunca pierdas tu biblioteca musical. Utilizando la API oficial de Spotify, esta aplicación permite extraer, salvaguardar y restaurar tus playlists favoritas de forma local, ofreciendo una capa extra de seguridad para tus listas de reproducción.

---

## 🚀 Tecnologías y Herramientas

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-FF0000?style=for-the-badge&logo=java&logoColor=white)
![Spotify API](https://img.shields.io/badge/Spotify%20API-1DB954?style=for-the-badge&logo=spotify&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)

---

## 🛠️ Características Principales

* **Extracción Automática:** Obtén todas tus playlists públicas simplemente con tu User ID.
* **Backups Locales:** Crea archivos de respaldo de tus listas para evitar pérdidas accidentales.
* **Restauración:** Recupera tus playlists directamente en tu cuenta.
* **Multiplataforma:** Interfaz adaptada tanto para entornos de escritorio como para dispositivos móviles.

---

## 🔑 Cómo obtener tu Spotify User ID

Para utilizar la aplicación, necesitas identificar tu cuenta. Puedes hacerlo de dos formas:
1.  **Vía Web:** Inicia sesión en [Spotify Profile](https://www.spotify.com/es/account/overview/) y tu ID aparecerá en la URL o el perfil.
2.  **Vía App:** Ve a tu Perfil de Usuario -> Compartir -> Copiar enlace al perfil. El ID es el código alfanumérico que aparece en el link.

> [!NOTE]
> La aplicación solo puede realizar copias de seguridad de las **Playlists configuradas como Públicas**.

---

## 📸 Capturas de Pantalla

### 🖥️ Versión Desktop (Escritorio)
| Principal | Selección |
| :---: | :---: |
| ![Desktop 1](https://github.com/IGprojects/Spotify-Backup-App/blob/main/Assets/Captura1.png) | ![Desktop 2](https://github.com/IGprojects/Spotify-Backup-App/blob/main/Assets/Captura2.png) |

| Listado de Canciones | Proceso de Backup |
| :---: | :---: |
| ![Desktop 3](https://github.com/IGprojects/Spotify-Backup-App/blob/main/Assets/Captura3.png) | ![Desktop 4](https://github.com/IGprojects/Spotify-Backup-App/blob/main/Assets/Captura4.png) |

### 📱 Versión Smartphone (Móvil)
| Login Mobile | Dashboard | Perfil |
| :---: | :---: | :---: |
| ![Mobile 1](https://github.com/IGprojects/Spotify-Backup-App/blob/main/Assets/Captura5.png) | ![Mobile 2](https://github.com/IGprojects/Spotify-Backup-App/blob/main/Assets/Captura6.png) | ![Mobile 3](https://github.com/IGprojects/Spotify-Backup-App/blob/main/Assets/Captura7.png) |

---

## 📦 Guía de Despliegue y Distribución

Si deseas compilar y empaquetar la aplicación por tu cuenta, sigue estos pasos:

### ☕ 1. Generar archivo .JAR
[📺 Ver Video Tutorial](https://www.youtube.com/watch?v=EyYb0GmtEX4)
1. Crea un *launcher* que ejecute el `App.main` en la clase principal.
2. Asegúrate de tener actualizado el `pom.xml` con las dependencias necesarias.
3. En IntelliJ, pulsa `Ctrl + Ctrl` y ejecuta:
   * `mvn clean`
   * `mvn install`
4. El archivo se generará en la carpeta `/target`.

### 🪟 2. Convertir a ejecutable .EXE
[📺 Ver Video Tutorial](https://www.youtube.com/watch?v=NRptmWyrvvo)
1. Utiliza la herramienta **Launch4j**.
2. Selecciona tu archivo JAR y asigna un icono `.ico`.
3. En la pestaña **JRE**, establece la versión mínima de Java compatible.
4. Genera el `.exe` en la ruta deseada.

### 💿 3. Crear instalador .MSI
[📺 Ver Video Tutorial](https://www.youtube.com/watch?v=LIk0Z0THuGQ)
1. Descarga e instala [MSI Wrapper](https://www.mediafire.com/file/sjhfgeow8pykuqw/MSI_Wrapper_10_0_51_0.msi/file).
2. Carga tu archivo `.exe`.
3. Genera el ID automático y completa la información de contacto (opcional).
4. Pulsa **Build** para obtener tu instalador listo para Windows.

---

**Desarrollado por [IGprojects](https://github.com/IGprojects)**
