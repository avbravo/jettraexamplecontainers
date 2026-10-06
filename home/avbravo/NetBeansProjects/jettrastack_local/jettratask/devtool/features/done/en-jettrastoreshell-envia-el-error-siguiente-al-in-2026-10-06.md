---
id: "en-jettrastoreshell-envia-el-error-siguiente-al-in-2026-10-06"
status: "done"
priority: "medium"
assignee: null
epic: null
dueDate: null
created: "2026-10-06T20:14:43.053Z"
modified: "2026-10-06T22:52:20.840Z"
completedAt: "2026-10-06T22:52:20.840Z"
labels: []
order: "a0"
---
# En JettraStoreShell envia el error siguiente al ingresar con el login

login admin admin-jettra
se genera este error
[AUTH SUCCESS] Sesión iniciada como 'admin' (Rol Global: SUPER_ADMIN) en 127.0.0.1:9091.

Escriba 'help' o '?' para ver los comandos disponibles, 'menu' para el menú interactivo, o 'exit' para salir.

WARNING: A restricted method in java.lang.System has been called
WARNING: java.lang.System::load has been called by org.jline.nativ.JLineNativeLoader in an unnamed module (file:/home/avbravo/jettra-node/JettraStoreShell-1.0-SNAPSHOT-uber.jar)
WARNING: Use --enable-native-access=ALL-UNNAMED to avoid a warning for callers in this module
WARNING: Restricted methods will be blocked in a future release unless native access is enabled

oct 06, 2026 3:13:28 P. M. org.jline.utils.Log logr
ADVERTENCIA: Failed to load history
java.lang.IllegalArgumentException: Bad history file syntax! The history file `/home/avbravo/.jettra/history.log` may be an older history: please remove it or use a different history file.
	at org.jline.reader.impl.history.DefaultHistory.addHistoryLine(DefaultHistory.java:173)
	at org.jline.reader.impl.history.DefaultHistory.addHistoryLine(DefaultHistory.java:164)
	at org.jline.reader.impl.history.DefaultHistory.lambda$load$0(DefaultHistory.java:85)
	at java.base/java.util.Iterator.forEachRemaining(Iterator.java:133)
	at java.base/java.util.Spliterators$IteratorSpliterator.forEachRemaining(Spliterators.java:1939)
	at java.base/java.util.stream.ReferencePipeline$Head.forEach(ReferencePipeline.java:803)
	at org.jline.reader.impl.history.DefaultHistory.load(DefaultHistory.java:85)
	at org.jline.reader.impl.history.DefaultHistory.attach(DefaultHistory.java:69)
	at org.jline.reader.impl.LineReaderImpl.readLine(LineReaderImpl.java:650)
	at org.jline.reader.impl.LineReaderImpl.readLine(LineReaderImpl.java:512)
	at io.jettra.shell.JettraStoreShellApp.main(JettraStoreShellApp.java:3281)