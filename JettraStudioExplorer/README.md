# JettraStudioExplorer & Police 3D 🛡️🌐

**JettraStudioExplorer** es la suite y consola visual unificada de administración, supervisión, telemetría y monitoreo 3D en tiempo real para servidores y clústeres distribuidos **JettraStore**, integrando todas las capacidades avanzadas de **JettraStorePolice3D** con componentes modernos **JettraStudio**, **JettraStoreDriver** y **Hilos Virtuales de Java 25 (Project Loom)**.

---

## 🌟 Opciones y Módulos Integrados

### 1. Mundo 3D Cuántico y Submundos de Nodo (`/police3d`)
- **Renderizado Interactivo WebGL con Three.js**:
  - **🖥️ Servidores / Racks de Nodos 3D**: Nodos del clúster con coordenadas tridimensionales, balizas LED, auras de estado y roles (Leader / Follower).
  - **🏢 Edificios de Sedes y Zonas IP**: Agrupación automática de usuarios por subredes IP (`192.168.1.x` Sede Comercial/POS, `10.0.4.x` Hospital UCI, `172.16.8.x` Sensores IoT/Boyas, `127.0.0.x` DataCenter Central).
  - **👤 Personas y Sesiones en Vivo**: Usuarios caminando entre sedes y servidores ejecutando transacciones (`INSERT`, `SELECT`, `KNN_SEARCH`, `TimeSeries PUSH`) con visualización de pensamientos y latencias exactas.
  - **🐾 Perros Centinelas JettraPolice**: Agentes caninos en órbita física alrededor de sus servidores asignados alertando ante cualquier anomalía.
  - **🚚 Camiones Cuánticos de Datos**: Paquetes de replicación Raft, sincronización de vectores HNSW y compactación SSTable circulando por arterias de red.
- **Expansión al Submundo Interior del Nodo**:
  - Al hacer clic sobre cualquier servidor 3D, el operador accede a la dimensión interior del nodo:
    - **Pedestales de Bases de Datos Alojadas**: Acceso directo e inspección de cada base de datos.
    - **Panel de Recursos Consumidos**: Heap JVM, Memoria Directa Panama Off-Heap (FFM), Hilos Virtuales Loom, MemTable en RAM, archivos SSTables, término/índice WAL y latencia I/O de disco.
    - **Portal Monumental**: Retorno fluido al mundo exterior al pulsar `ESC` o interactuar con el portal.
- **Sistema de Voz y Síntesis Auditiva (`[🔊 VOZ]`)**:
  - Narrador auditivo en tiempo real con Web Speech API / TTS que relata eventos policiales, salud de nodos y conmutaciones de red.
- **Atajos de Teclado**:
  - `K`: Conexiones al Servidor
  - `R`: Reset y Sincronización del Mundo 3D
  - `V`: Activar / Silenciar Voz
  - `ESC`: Salir del Submundo Interior del Nodo

### 2. Centinelas Police & Auditoría en Tiempo Real (`/police`)
- **4 Agentes Centinelas Especializados**:
  - **Heap Sentinel**: Vigila saturación de Heap JVM y Memoria Directa Panama FFM Off-Heap (Zero GC pressure).
  - **Raft Quorum K9**: Vigila latidos de réplicas, desviando tráfico ante fallos y auditando el quorum Raft.
  - **MemTable Purge Dog**: Audita el vaciado de MemTable a archivos inmutables SSTables y compactación en disco.
  - **Security Patrol**: Supervisa autenticación, tokens JWT, permisos granulares y accesos sospechosos.
- **Muro de Incidentes en Tiempo Real**: Historial de auditoría policial con niveles de severidad (`OK`, `INFO`, `WARN`, `CRITICAL`) y acciones correctivas.
- **Conmutador de Topología Multinodo (`cluster.multinode.active`)**:
  - Conmutación en caliente entre modo **Multinodo Distribuido (Raft Consenso)** y modo **Standalone Mononodo**.

### 3. Consola Interactiva de Consultas JettraSQL & JettraQL (`/query`)
- Ejecución de consultas multimodelo en tiempo real (`SELECT`, `WHERE`, `INSERT`, `KNN_SEARCH`, `TimeSeries PUSH`).
- Selector dinámico de Base de Datos y Bucket.
- Medición de rendimiento en microsegundos y visor tabular de resultados con enlace de edición.

### 4. Administrador de Conexiones al Servidor (`/connections`)
- Panel interactivo para conectar directamente con cualquier servidor JettraStore.
- Solicitud de URL (`host:port`), usuario y contraseña.
- **Probar Conexión**: Medición de latencia de red y verificación de estado en línea.
- **Conexión en Caliente**: Conmutación inmediata del clúster activo, sincronizando y cargando todas las bases de datos administradas.

### 5. CRUD Completo de Bases de Datos (`/databases`)
- **CREATE**: Creación en caliente de bases de datos multimodelo.
- **READ**: Carga dinámica directa de todas las bases de datos administradas por el servidor JettraStore.
- **UPDATE**: Renombrado de bases de datos y migración atómica de buckets.
- **DELETE**: Eliminación física y segura de bases de datos en memoria y disco.

### 6. CRUD Completo de Unidades de Registros (`/records`)
- Selector de base de datos y colecciones/buckets multimodelos.
- Inserción reactiva de nuevos documentos y payloads crudos (JSON/BSON).
- Búsqueda instantánea por identificador o coincidencia de texto.
- Edición y actualización de registros existentes.
- Borrado seguro de registros por ID.

### 7. Motores Multimodelos e Índices (`/engines`, `/indexes`)
- Gestión de los 8 motores nativos de JettraStore: Document, Key-Value, Vector, Graph, TimeSeries, Geospatial, Columnar y Java Records.
- Administración y auditoría de índices B-Tree, Hash y Vector HNSW.

### 8. Usuarios, Roles y Seguridad (`/users`)
- Creación y administración de usuarios (`JettraUserAccount`).
- Roles globales (`ADMIN`, `OPERATOR`, `AUDITOR`) y permisos granulares por base de datos (`ADMIN`, `READ_WRITE`, `READ_ONLY`).

### 9. Respaldo y Recuperación (`/backup`)
- Creación de snapshots atómicos `.jbak` en caliente con `JettraAdminClient`.
- Restauración controlada con verificación de consistencia.

---

## 📂 Mapa de Rutas Web

| Ruta | Página | Función Principal | Rol Requerido |
|---|---|---|---|
| `/` o `/login` | `LoginPage` | Autenticación pública | Público (`@NoLoginRequired`) |
| `/police3d` | `Police3DPage` | **Mundo 3D Cuántico, Submundos de Nodo, Voz y Three.js** | Autenticado (`ADMIN`, `OPERATOR`) |
| `/police` | `PoliceMonitorPage` | **Centinelas Police, Muro de Incidentes y Quorum** | Autenticado (`ADMIN`, `OPERATOR`) |
| `/dashboard` | `ClusterDashboardPage` | Telemetría en tiempo real y métricas FluxWidget | Autenticado (`ADMIN`, `OPERATOR`) |
| `/connections` | `ConnectionsPage` | **Administrador de conexiones (URL, user, pass, ping)** | Autenticado (`ADMIN`, `OPERATOR`) |
| `/query` | `QueryConsolePage` | **Consola interactiva JettraSQL / JettraQL** | Autenticado (`ADMIN`, `OPERATOR`) |
| `/databases` | `DatabasesPage` | **CRUD de Bases de Datos (Crear, Listar, Renombrar, Eliminar)** | Autenticado (`ADMIN`, `OPERATOR`) |
| `/records` | `RecordsPage` | **CRUD de Unidades de Registros (Insertar, Filtrar, Editar, Borrar)** | Autenticado (`ADMIN`, `OPERATOR`) |
| `/engines` | `EnginesPage` | Motores Multimodelos (Document, KV, Vector, etc.) | Autenticado (`ADMIN`, `OPERATOR`) |
| `/indexes` | `IndexesPage` | Gestión de Índices (BTree, Hash, Vector HNSW) | Autenticado (`ADMIN`, `OPERATOR`) |
| `/users` | `UserManagerPage` | **Gestión de Usuarios, Roles y Permisos BD** | Administradores (`ADMIN`) |
| `/backup` | `BackupRestorePage` | Copias de seguridad atómicas `.jbak` y restauración | Administradores (`ADMIN`) |

---

## 🚀 Ejecución y Pruebas

### Ejecutar Pruebas Automatizadas
```bash
mvn clean test
```

### Iniciar el Servidor
```bash
mvn exec:java -Dexec.mainClass="io.jettra.explorer.App"
```

Acceso al panel: **http://localhost:8088/**  
- Administrador: `admin` / `admin123`
- Operador: `operator` / `operator123`
