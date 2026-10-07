---
id: "spec-driven-development-prompt-jettrastore-cluster-2026-10-07"
status: "in-progress"
priority: "medium"
assignee: null
epic: null
dueDate: null
created: "2026-10-07T17:21:04.505Z"
modified: "2026-10-07T17:21:04.505Z"
completedAt: null
labels: []
order: "a0"
---
# # Spec-Driven Development Prompt: JettraStore Cluster Distribution, Failover, Real-Time Notifications & Cluster Live Command

## Contexto y Alcance
Trabajando sobre el ecosistema **JettraStore** (`JettraStore`, `JettraStoreShell`, `JettraStoreDriver`, `JettraStorePolice3D`), se requiere solucionar un fallo crítico en la replicación de datos, implementar un mecanismo robusto de elección y notificación de nodo primario (Failover), habilitar un canal de eventos en tiempo real para todos los clientes/módulos y añadir un comando interactivo de supervisión en vivo.

---

## Requerimientos Técnicos

### 1. Corrección de Sincronización y Distribución de Registros (`JettraStore`)
* **Problema actual:** Cuando se crea una base de datos en el nodo primario (`PRIMARY`), este la distribuye correctamente a los nodos secundarios (`SECONDARY`), pero **no está distribuyendo los registros (datos iniciales o transacciones posteriores)** asociados a dicha base de datos.
* **Solución esperada:** Asegurar que el flujo de replicación del nodo primario incluya tanto la estructura/metadatos de creación de la base de datos como el volúmen o flujo continuo de registros hacia todos los nodos secundarios activos del clúster.

### 2. Mecanismo de Failover y Notificaciones de Estado de Nodos (`JettraStore`)
* **Detección de parada:** Si una instancia de JettraStore (ya sea `PRIMARY` o `SECONDARY`) se detiene de forma controlada o pierde conexión, debe notificar inmediatamente a los demás nodos del clúster.
* **Elección automática de Primario:** Si el nodo detenido era el `PRIMARY`, los nodos secundarios restantes deben iniciar un protocolo de elección (o asignación determinista) para que **otro nodo tome el rol de `PRIMARY`**.
* **Propagación del nuevo rol:** El nuevo nodo primario debe notificar de inmediato a los demás nodos del clúster y a los servicios conectados (`JettraStoreShell`, `JettraStoreDriver`, `JettraStorePolice3D`) sobre el cambio de topología y liderazgo.

### 3. Canal de Eventos en Tiempo Real (`JettraStoreShell`, `JettraStoreDriver`, `JettraStorePolice3D`)
* Los módulos `JettraStoreShell`, `JettraStoreDriver` y `JettraStorePolice3D` deben conectarse al clúster y **recibir notificaciones en tiempo real** sobre:
  * Cambios de estado en los nodos (altas, bajas, paradas).
  * Promociones de nodos secundarios a nuevos nodos `PRIMARY`.
  * Eventos de transferencia y replicación de datos.

### 4. Nuevo Comando: `cluster live`
* **Definición:** Añadir un comando interactivo llamado `cluster live`.
* **Funcionalidad:** Muestra información en tiempo real y de forma fluida (streaming de eventos) de lo que está ocurriendo en el clúster (ej. *"enviando archivo X a nodo 2 y nodo 3"*, *"nodo Y promovido a primario"*, *"sincronización de registros completada"*).
* **Alcance de implementación:** Debe estar implementado y disponible en:
  * `JettraStore` (backend de eventos)
  * `JettraStoreShell`
  * `JettraStoreDriver`
  * `JettraStorePolice3D`

---

## Documentación Requerida

Actualizar y documentar exhaustivamente las nuevas funcionalidades, arquitectura de failover, canal en tiempo real y el uso del comando `cluster live` en los siguientes archivos de guía:
1. `JettraStore/guide/book.md`
2. `JettraStoreShell/guide/book.md`
3. `JettraStoreDriver/guide/book.md`
4. `JettraStorePolice3D/guide/book.md`

---

## Criterios de Validación (Definition of Done)
1. Al crear una base de datos y añadir registros en el nodo primario, estos se replican íntegramente en los nodos secundarios.
2. Al detener el nodo primario, un nodo secundario asume el rol de primario de forma transparente y los demás nodos son notificados.
3. `JettraStoreShell`, `JettraStoreDriver` y `JettraStorePolice3D` reciben eventos en tiempo real sin pérdida de mensajes.
4. El comando `cluster live` muestra trazas de actividad en tiempo real en todos los shells/drivers/police aplicables.
5. Los archivos `book.md` de los 4 repositorios reflejan de manera clara y coherente la especificación técnica implementada.