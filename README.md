# 🏥 Portal de Monitoreo de Sistemas - Hospital San Rafael

Este proyecto consiste en un portal web técnico y público que permite visualizar en tiempo real el estado de disponibilidad, salud de infraestructura (RAM/Disco) y eventos de más de 21 sistemas críticos de información del Hospital San Rafael, tomando inspiración funcional del portal oficial de status de OpenAI (status.openai.com).

## 🔗 URL del Repositorio GitHub

Repositorio Público: https://github.com/Adylasouza/sanrafael-status.git

##  📝 Descripción del Proyecto

El sistema soluciona la necesidad del Hospital San Rafael de contar con un panel centralizado de auditoría y monitoreo de infraestructura tecnológica. El núcleo del sistema realiza consultas automáticas periódicas (mediante tareas programadas) a los endpoints /health de los diversos microservicios del hospital (simulados a través de contenedores Docker en los puertos 8081 a 8085), registrando de forma persistente el uso de memoria RAM, disco duro y eventos bajo tres condiciones de severidad: CONDITION_ACTIVE, CONDITION_WARNING y CONDITION_ERROR.

## Audiencia Objetivo:

Público General y Personal Médico: Acceden sin credenciales para validar la estabilidad de las plataformas (HU-16).

Administradores de TI (Ingenieros de Soporte): Inician sesión para registrar nuevos sistemas, reportar incidentes manuales y auditar la infraestructura (HU-17, HU-18).

## 🚀 Pasos para Ejecutar y Desplegar el Proyecto

1. Iniciar los Servidores de Prueba (Docker)

Asegúrese de tener Docker instalado y ejecutándose en su computadora. En su terminal (CMD), ejecute el siguiente comando para iniciar las APIs de simulación del hospital:

docker run --name servicios_health_containers -p 8081:8081 -p 8082:8082 -p 8083:8083 -p 8084:8084 -p 8085:8085 harrysoler/rnghealthapis


2. Ejecutar la Aplicación Spring Boot (Backend)

Abra una terminal dentro de la carpeta raíz de este proyecto y ejecute los siguientes comandos de Gradle:

En Windows (CMD / PowerShell):

gradlew.bat bootRun


En Linux / macOS:

./gradlew bootRun


3. Acceder al Sistema

Abra su navegador de preferencia e ingrese a las siguientes URLs locales:

Dashboard Público de Monitoreo: http://localhost:8090/

Formulario de Acceso Administrativo: http://localhost:8090/auth/login

Credenciales de Administrador (Fines de Evaluación):

Usuario: admin

Contraseña: 123

## 📂 Arquitectura y Estructura del Proyecto

El proyecto está diseñado bajo los fundamentos de la Arquitectura Hexagonal (Ports & Adapters). El núcleo o corazón del negocio (core) se encuentra completamente libre de dependencias de frameworks externos como Spring, comunicándose a través de interfaces (ports) que son implementadas por adaptadores externos (adapters).


```text
src/
├── main/
│   ├── java/
│   │   └── dev/adylaoliveira/sanrafael/
│   │       ├── StatusApplication.java
│   │       ├── adapter/
│   │       │   ├── api/rest/
│   │       │   │   ├── HealthEventResponse.java
│   │       │   │   ├── HealthResponse.java
│   │       │   │   └── RestHealthReportGateway.java
│   │       │   ├── config/
│   │       │   │   └── DataLoader.java
│   │       │   ├── controller/
│   │       │   │   ├── AuthController.java
│   │       │   │   ├── DashboardController.java
│   │       │   │   ├── ProductController.java
│   │       │   │   └── ViewController.java
│   │       │   ├── repository/inmemory/
│   │       │   │   └── InMemoryProductRepository.java
│   │       │   └── scheduler/
│   │       │       └── DailyHealthCheckScheduler.java
│   │       ├── core/
│   │       │   ├── constant/
│   │       │   │   └── Condition.java
│   │       │   ├── dto/
│   │       │   │   ├── CreateProductDTO.java
│   │       │   │   ├── ProductDetailDTO.java
│   │       │   │   ├── ProductListResponseDTO.java
│   │       │   │   ├── ProductResponseDTO.java
│   │       │   │   ├── ProductStatusDTO.java
│   │       │   │   ├── ReportDTO.java
│   │       │   │   └── SystemStatusDTO.java
│   │       │   ├── entity/
│   │       │   │   ├── Event.java
│   │       │   │   ├── Product.java
│   │       │   │   ├── Report.java
│   │       │   │   └── User.java
│   │       │   ├── exception/
│   │       │   │   ├── GlobalExceptionHandler.java
│   │       │   │   └── NotFoundException.java
│   │       │   ├── port/
│   │       │   │   ├── HealthReportGateway.java
│   │       │   │   └── ProductRepository.java
│   │       │   └── security/
│   │       │       ├── JwtFilter.java
│   │       │       ├── JwtService.java
│   │       │       └── SecurityConfig.java
│   │       └── service/
│   │           ├── ProductService.java
│   │           └── StatusRequestService.java
│   └── resources/
│       ├── application.properties
│       └── templates/
│           ├── admin.html
│           ├── dashboard.html
│           └── login.html
└── test/
    └── java/
        └── dev/adylaoliveira/sanrafael/
            └── SanrafaelStatusApplicationTests.java
```

📊 Tabla de Endpoints y Direcciones del Sistema

El backend expone las siguientes rutas y métodos para interactuar con la interfaz del usuario y los servicios externos:

Funcionalidad / Pantalla

Ruta Mapeada

Método HTTP

Acceso / Seguridad

Descripción

Página de Inicio

/ o /dashboard

GET

Público

Renderiza la interfaz gráfica interactiva tipo Single Page Application (dashboard.html).

API de Sincronización

/status

GET

Público

Retorna el JSON estructurado en tiempo real con el estado global calculado, métricas e histórico.

Pantalla de Login

/auth/login

GET

Público

Renderiza el formulario visual para que el administrador ingrese credenciales.

Autenticación API

/auth/login

POST

Público

Recibe usuario y contraseña. Genera y retorna el Token JWT al cliente si los datos son válidos.

Crear Productos

/products

POST

Autenticado (JWT)

Registra un nuevo servicio en el panel de monitoreo técnico del hospital.

Detalles Producto

/products/{productId}

GET

Público

Retorna la ficha técnica de un sistema específico con sus reportes de RAM, Disco y Eventos.

)

🗺️ Diagrama de Arquitectura (Ports & Adapters)


<img width="4096" height="1094" alt="dLRTSXet5BxdAJJqeaiQc3GpwKN6muH06jDfR0_OpMNdM3eiYhNIvaYBGpjvgZv2NwnQX9O5hUCwNAoq-tr7vpi_adXZ7P2h2jMpTrANG52mDMXnJz8vr6m5oc8BAumsV4scm0JY2gnz2_mk9rDfCJNA4FlXurgwb6L88EqPBr-zUFNBorFAXCZSFyIm3WcDsvOuJHnULLGgx2R_QhGBj2" src="https://github.com/user-attachments/assets/fd9853d3-7d40-4599-934e-aa0b78b15511" />


🗄️ Modelo Entidad/Relación (Base de Datos)


<img width="1150" height="369" alt="image" src="https://github.com/user-attachments/assets/6948c819-3cc4-4591-899f-b3426339c7f5" />


##🔌 Endpoints o Peticiones Disponibles (Capa Controller)

Todas las rutas operativas mapeadas y expuestas en los controladores del proyecto (AuthController, DashboardController, ProductController y ViewController) se detallan en la siguiente tabla técnica:

<img width="514" height="759" alt="image" src="https://github.com/user-attachments/assets/7428a3b7-0be2-4d0a-a06e-3e9fa8e3d0cd" />

<img width="512" height="504" alt="image" src="https://github.com/user-attachments/assets/c62b6b8d-ec2d-4c31-9bdd-f90dfd44e0f4" />


## 🛠️ Stack de Tecnologías y Respuestas Orientadoras

El sistema del Hospital San Rafael fue construido seleccionando tecnologías robustas que garantizan los requerimientos de seguridad, concurrencia y despliegue rápido exigidos en la industria.

Java 21 (LTS): Lenguaje de programación principal de la aplicación. Su implementación nos permite utilizar de forma nativa los Java Records, asegurando la inmutabilidad de los modelos de dominio (Product, Report, Event), evitando efectos secundarios y optimizando el consumo de hilos de ejecución.

Spring Boot 3.x: Framework empresarial que actúa como base tecnológica para gestionar la inyección de dependencias, el ciclo de vida de los componentes del backend y las configuraciones del servidor embebido.

Spring Security: Módulo de protección configurado de manera Stateless (Sin estado). Intercepta cada petición al servidor mediante un filtro personalizado para validar firmas digitales, aislando las rutas públicas de las acciones restringidas para el rol administrador.

JSON Web Tokens (JWT): Estándar de la industria utilizado para transmitir de forma segura la identidad del usuario a través de un token criptográfico compacto firmado con el algoritmo HMAC-SHA256.

Thymeleaf: Motor de plantillas que procesa y renderiza los componentes visuales HTML del lado del servidor de manera dinámica e integrada con los datos del backend.

Tailwind CSS & Material Symbols: Tecnologías de diseño implementadas directamente en el frontend para estructurar una interfaz gráfica moderna, responsiva, limpia y de nivel profesional.

Gradle: Sistema avanzado de automatización de compilación y gestión de dependencias del proyecto.

🖼️ Capturas de Pantalla UI/UX

(Durante la entrega en formato PDF de este archivo, tome capturas de pantalla de la ejecución real de su navegador en localhost y adjúntelas en esta sección).

Dashboard Principal: Muestra la tarjeta del Hospital con "Todos los sistemas operativos" en verde brillante y el listado interactivo con barras horizontales de progreso históricas de cada uno de los 21 servicios del hospital.

Detalles del Sistema Clínico: Al pulsar sobre "Historia Clínica Electrónica" se despliega la métrica de uso de memoria RAM, Disco Duro e historial de auditoría cronológico vertical.

Interfaz de Acceso Administrativo: Formulario de login limpio con seguridad de validación instantánea.

🧠 Análisis y Reflexión Personal

Retos más grandes en el desarrollo:

El desafío más complejo consistió en conectar las puntas de la Arquitetura Hexagonal de forma limpia. Dado que el núcleo del negocio (core) no puede conocer ni tener dependencias físicas de frameworks externos, desacoplar el filtro de seguridad de JWT (JwtFilter) y coordinar el agendador de tareas automático en segundo plano (@Scheduled de Spring) para que inyecte reportes de salud sin violar los contratos de las interfaces del dominio representó un gran reto de abstracción y diseño orientado a objetos.

Aprendizajes técnicos más valiosos:

El aprendizaje más significativo fue comprender que la infraestructura debe adaptarse al negocio, y no al revés. Diseñar entidades inmutables mediante Records de Java, abstraer los servicios externos mediante el uso de puertos y adaptadores (Ports & Adapters) y descubrir cómo mantener la persistencia en memoria de forma Thread-safe (utilizando un ConcurrentHashMap robusto frente a accesos simultáneos) nos brindó bases sólidas de ingeniería de software para desarrollar sistemas de alto rendimiento y fáciles de testear.
