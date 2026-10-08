---
id: "0-en-jettrastore-cuando-se-detiene-el-nodo-primary-2026-10-08"
status: "review"
priority: "medium"
assignee: null
epic: null
dueDate: null
created: "2026-10-08T18:32:32.278Z"
modified: "2026-10-08T20:10:16.381Z"
completedAt: null
labels: []
order: "a2"
---
# 0] En JettraStore cuando se detiene el nodo PRIMARY envia a consola el mensaje [broadcastFrameWithQuorum] ⚡ Consenso Dinámico ajustado: 2 de 3 nodos configurados disponibles. Acks: 2/2 (quórum requerido: 2, resultado: true)
[JettraStoreServer] 👑 ¡NODO LOCAL 'node-02' PROMOVIDO A NUEVO PRIMARY TRAS CAÍDA DE 'node-01'!

lo que es correcto , pero al ejecutar desde JettraShell el comando cluster-distributed info

=======================================================================================================================
                                   JETTRASTORE CLUSTER DATABASE DISTRIBUTION INFO                                       
========================================================================================================================
+----------+----------------------+-------+-----------+----------+---------------+--------------------------------------+
| Nodo ID  | Dirección IP         | Puerto| Rol       | Estado   | Cantidad BDs  | Bases de Datos                       |
+----------+----------------------+-------+-----------+----------+---------------+--------------------------------------+
| node-01  | 127.0.0.2            | 9091  | PRIMARY   | RUNNING  | 2             | default_db, mydb                     |
| node-02  | 127.0.0.3            | 9092  | SECONDARY | RUNNING  | 2             | default_db, mydb                     |
| node-03  | 127.0.0.4            | 9093  | SECONDARY | RUNNING  | 0             | (0 bases de datos)                   |

pero muestra como el nodo-01 este aun activo y lo sigue marcando como PRIMARY, debes corregis esto