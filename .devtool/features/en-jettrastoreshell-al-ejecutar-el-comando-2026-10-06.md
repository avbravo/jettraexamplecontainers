---
id: "en-jettrastoreshell-al-ejecutar-el-comando-2026-10-06"
status: "in-progress"
priority: "medium"
assignee: null
epic: null
dueDate: null
created: "2026-10-07T01:40:36.805Z"
modified: "2026-10-07T01:40:36.805Z"
completedAt: null
labels: []
order: "a4"
---
# En JettraStoreShell Al ejecutar el comando

show nodes muestra

                      JETTRASTORE RAFT CLUSTER TOPOLOGY                                   

============================================================================================== +----------+----------------------+-------+-----------+------------+----------+--------------+ | Nodo ID | Dirección IP | Puerto| Rol | Estado Raft| Estado | Offload Bytes| +----------+----------------------+-------+-----------+------------+----------+--------------+ | node-01 | 192.168.60.243 | 9091 | PRIMARY | LEADER | RUNNING | 0 | | node-02 | 192.168.60.246 | 9091 | SECONDARY | FOLLOWER | RUNNING | 0 | | node-03 | 192.168.1.103 | 9091 | SECONDARY | FOLLOWER | RUNNING | 0 | +----------+----------------------+-------+-----------+------------+----------+--------------+ Modo Multinodo (cluster.multinode.active): ON (Algoritmo de consenso y distribución de datos ACTIVO) Total: 3 nodo(s) registrados en el anillo dinámico. Quórum: Activo (Consenso distribuido).

y el estado debe ser obtenido en tiempo real ya que los dos nodos secundarios en el ejemplo que realizado estan detenidos y los marca como RUNNING debe indicar el estado actual de los nodos