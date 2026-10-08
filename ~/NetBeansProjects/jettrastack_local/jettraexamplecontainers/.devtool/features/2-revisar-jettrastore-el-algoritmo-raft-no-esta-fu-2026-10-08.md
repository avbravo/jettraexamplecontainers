---
id: "2-revisar-jettrastore-el-algoritmo-raft-no-esta-fu-2026-10-08"
status: "in-progress"
priority: "medium"
assignee: null
epic: null
dueDate: null
created: "2026-10-08T20:25:15.001Z"
modified: "2026-10-08T20:25:15.001Z"
completedAt: null
labels: []
order: "a6"
---
# 2. Revisar JettraStore el algoritmo Raft no esta funcionando no distribuye a los demas nodos las bases de datos y sus contenidos de manera automatica, es decir al crear una base de datos se debe distribuir a los demas nodos, si se crea un engine dentro de la base de datos se debe distribuir a los demas nodos, si crean registros o se hacen operaciones sobre ellos se deben distribuir de manera automatica a los demas nodos.

3. En JettraStoreShell el comando cluster live (debe mostrar estos procesos de transferencia)

4. En JettraStoreDriver el comando cluster live (debe mostrar estos procesos de transferencia)

5. En JettraStorePolice3D el comando cluster live (debe mostrar estos procesos de transferencia)