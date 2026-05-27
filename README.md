🏥 Portal de Monitoreo de Sistemas - Hospital San Rafael

Este proyecto consiste en un portal web técnico y público que permite visualizar en tiempo real el estado de disponibilidad, salud de infraestructura (RAM/Disco) y eventos de más de 21 sistemas críticos de información del Hospital San Rafael, tomando inspiración funcional del portal oficial de status de OpenAI (status.openai.com).

🔗 URL del Repositorio GitHub

Repositorio Público: https://github.com/Adylasouza/sanrafael-status

📝 Descripción del Proyecto

El sistema soluciona la necesidad del Hospital San Rafael de contar con un panel centralizado de auditoría y monitoreo de infraestructura tecnológica. El núcleo del sistema realiza consultas automáticas periódicas (mediante tareas programadas) a los endpoints /health de los diversos microservicios del hospital (simulados a través de contenedores Docker en los puertos 8081 a 8085), registrando de forma persistente el uso de memoria RAM, disco duro y eventos bajo tres condiciones de severidad: CONDITION_ACTIVE, CONDITION_WARNING y CONDITION_ERROR.

Audiencia Objetivo:

Público General y Personal Médico: Acceden sin credenciales para validar la estabilidad de las plataformas (HU-16).

Administradores de TI (Ingenieros de Soporte): Inician sesión para registrar nuevos sistemas, reportar incidentes manuales y auditar la infraestructura (HU-17, HU-18).

🚀 Pasos para Ejecutar y Desplegar el Proyecto

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

📂 Arquitectura y Estructura del Proyecto

El proyecto está diseñado bajo los fundamentos de la Arquitectura Hexagonal (Ports & Adapters). El núcleo o corazón del negocio (core) se encuentra completamente libre de dependencias de frameworks externos como Spring, comunicándose a través de interfaces (ports) que son implementadas por adaptadores externos (adapters).

src/
└── main/
├── java/
│   └── dev/adylaoliveira/sanrafael/
│       ├── adapter/                      # CAPA ADAPTERS (Detalles de Infraestructura)
│       │   ├── api/rest/                 # Llamados HTTP externos (Docker APIs)
│       │   ├── config/                   # Configuración del Spring Data Loader
│       │   ├── controller/               # Controladores REST API y Vista (Thymeleaf)
│       │   └── repository/inmemory/      # Repositorio thread-safe en memoria (ConcurrentHashMap)
│       ├── core/                         # CAPA CORE (Reglas de Negocio Puras)
│       │   ├── constant/                 # Enums compartidos de condición (Condition)
│       │   ├── dto/                      # Objetos de Transferencia de Datos
│       │   ├── entity/                   # Entidades de dominio (Records inmutables: Product, Report, Event)
│       │   ├── exception/                # Manejo unificado de excepciones
│       │   ├── port/                     # Interfaces definidoras de contratos (Gateways y Repositorios)
│       │   └── security/                 # Seguridad stateless basada en tokens JWT (JwtFilter, JwtService)
│       └── service/                      # Orquestador del Dominio (ProductService, StatusRequestService)
│           └── StatusRequestService.java # Agendador automático periódico (@Scheduled)
└── resources/
├── templates/                        # Vistas HTML dinámicas servidas mediante Thymeleaf
│   ├── dashboard.html                # Interfaz de una sola página de cara al público
│   └── login.html                    # Acceso del Administrador controlado por JWT
└── application.properties            # Configuración de puerto del servidor (8090)


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

🛠️ Stack Tecnológico y Respuestas Orientadoras

Preguntas Orientadoras del Profesor:

¿Qué versión de Java estás utilizando?

Estamos utilizando Java 21, lo cual nos permite hacer uso avanzado de características modernas como los Records de Java para la inmutabilidad de datos y la optimización de memoria.

¿Qué dependencias de Gradle estás utilizando?

Las dependencias están configuradas a través del gestor Gradle. Se emplean los módulos nativos de Spring Boot para Web, Thymeleaf, Security y la biblioteca oficial de control de JWT.

¿En qué versión se encuentra cada una de estas dependencias?

El listado de versiones exactas definidas en nuestro archivo build.gradle es:

org.springframework.boot:spring-boot-starter-thymeleaf (Versión 4.0.5)

org.springframework.boot:spring-boot-starter-webmvc (Versión 4.0.5)

org.springframework.boot:spring-boot-starter-security (Versión 4.0.5)

com.fasterxml.jackson.core:jackson-databind (Versión 2.15.2 implícita)

io.jsonwebtoken:jjwt-api, jjwt-impl, jjwt-jackson (Versión 0.11.5)

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