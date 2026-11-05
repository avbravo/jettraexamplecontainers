package io.jettra.examples.store.storage;

import io.jettra.examples.store.driver.JettraDriver;
import io.jettra.examples.store.util.ConsoleColor;
import io.jettra.store.core.StorageMode;
import io.jettra.store.engine.models.DocumentEngine;

import java.util.Map;

/**
 * Comparativa directa entre los 3 modos de almacenamiento soportados en JettraStore:
 * 1. JVM_RAM: Memoria RAM estándar de la JVM (Heap & Stack). Máxima velocidad in-memory.
 * 2. DISK_MEMORY: Motor directo en disco sin pausas de GC usando JettraMemory (LSM Off-Heap FFM).
 * 3. OFF_HEAP: Motor de páginas ranuradas nativas Slotted-Page FFM Panama (JettraStore-OffHeap)
 *    con cero pausas de GC y liberación determinista por ámbito RAII (Arena).
 */
public class StorageModeComparisonExample {

    public static void run(JettraDriver driver) {
        ConsoleColor.printHeader("10. COMPARATIVA DE MODOS: JVM_RAM vs DISK_MEMORY vs OFF_HEAP");

        // 1. Base de datos en modo JVM_RAM
        String dbRam = "benchmark_ram_db";
        driver.getDatabase(dbRam, StorageMode.JVM_RAM);
        StorageMode modeRam = driver.getStorageMode(dbRam);

        ConsoleColor.printSubHeader("1. Base de datos configurada en JVM_RAM");
        ConsoleColor.printInfo("Nombre de BD", dbRam);
        ConsoleColor.printInfo("Modo Activo", modeRam.getCode() + " - " + modeRam.getDescription());

        long startRam = System.nanoTime();
        DocumentEngine ramDocs = driver.getDocumentEngine(dbRam, "transacciones");
        for (int i = 1; i <= 1000; i++) {
            ramDocs.insert("tx_ram_" + i, Map.of(
                    "tx_id", i,
                    "monto", 50.0 + (i * 0.25),
                    "tipo", (i % 2 == 0) ? "DEPOSITO" : "RETIRO",
                    "estado", "PROCESADO"
            ));
        }
        long durationRamNs = System.nanoTime() - startRam;
        double durationRamMs = durationRamNs / 1_000_000.0;
        ConsoleColor.printSuccess("1,000 registros insertados en JVM_RAM en " + String.format("%.2f", durationRamMs) + " ms");
        ConsoleColor.printInfo("Total de documentos en JVM_RAM", ramDocs.count());

        // 2. Base de datos en modo DISK_MEMORY
        String dbDisk = "benchmark_disk_db";
        driver.getDatabase(dbDisk, StorageMode.DISK_MEMORY);
        StorageMode modeDisk = driver.getStorageMode(dbDisk);

        ConsoleColor.printSubHeader("2. Base de datos configurada en DISK_MEMORY");
        ConsoleColor.printInfo("Nombre de BD", dbDisk);
        ConsoleColor.printInfo("Modo Activo", modeDisk.getCode() + " - " + modeDisk.getDescription());

        long startDisk = System.nanoTime();
        DocumentEngine diskDocs = driver.getDocumentEngine(dbDisk, "transacciones");
        for (int i = 1; i <= 1000; i++) {
            diskDocs.insert("tx_disk_" + i, Map.of(
                    "tx_id", i,
                    "monto", 50.0 + (i * 0.25),
                    "tipo", (i % 2 == 0) ? "DEPOSITO" : "RETIRO",
                    "estado", "PERSISTIDO"
            ));
        }
        long durationDiskNs = System.nanoTime() - startDisk;
        double durationDiskMs = durationDiskNs / 1_000_000.0;
        ConsoleColor.printSuccess("1,000 registros insertados en DISK_MEMORY en " + String.format("%.2f", durationDiskMs) + " ms");
        ConsoleColor.printInfo("Total de documentos en DISK_MEMORY", diskDocs.count());

        // 3. Base de datos en modo OFF_HEAP (JettraStore-OffHeap)
        String dbOffHeap = "benchmark_offheap_db";
        driver.getDatabase(dbOffHeap, StorageMode.OFF_HEAP);
        StorageMode modeOffHeap = driver.getStorageMode(dbOffHeap);

        ConsoleColor.printSubHeader("3. Base de datos configurada en OFF_HEAP (Slotted-Page Native)");
        ConsoleColor.printInfo("Nombre de BD", dbOffHeap);
        ConsoleColor.printInfo("Modo Activo", modeOffHeap.getCode() + " - " + modeOffHeap.getDescription());

        long startOffHeap = System.nanoTime();
        DocumentEngine offHeapDocs = driver.getDocumentEngine(dbOffHeap, "transacciones");
        for (int i = 1; i <= 1000; i++) {
            offHeapDocs.insert("tx_offheap_" + i, Map.of(
                    "tx_id", i,
                    "monto", 50.0 + (i * 0.25),
                    "tipo", (i % 2 == 0) ? "DEPOSITO" : "RETIRO",
                    "estado", "NATIVE_OFF_HEAP"
            ));
        }
        long durationOffHeapNs = System.nanoTime() - startOffHeap;
        double durationOffHeapMs = durationOffHeapNs / 1_000_000.0;
        ConsoleColor.printSuccess("1,000 registros insertados en OFF_HEAP en " + String.format("%.2f", durationOffHeapMs) + " ms");
        ConsoleColor.printInfo("Total de documentos en OFF_HEAP", offHeapDocs.count());

        // 4. Conmutación dinámica de modo entre las 3 opciones en tiempo de ejecución
        ConsoleColor.printSubHeader("4. Conmutación Dinámica entre los 3 Modos");
        ConsoleColor.printInfo("Cambiando 'benchmark_ram_db' a DISK_MEMORY...", "");
        driver.setStorageMode(dbRam, StorageMode.DISK_MEMORY);
        ConsoleColor.printSuccess("Modo actualizado dinámicamente a: " + driver.getStorageMode(dbRam).getCode());

        ConsoleColor.printInfo("Cambiando 'benchmark_ram_db' a OFF_HEAP...", "");
        driver.setStorageMode(dbRam, StorageMode.OFF_HEAP);
        ConsoleColor.printSuccess("Modo actualizado dinámicamente a: " + driver.getStorageMode(dbRam).getCode());

        ConsoleColor.printInfo("Cambiando 'benchmark_ram_db' de regreso a JVM_RAM...", "");
        driver.setStorageMode(dbRam, StorageMode.JVM_RAM);
        ConsoleColor.printSuccess("Modo restaurado a: " + driver.getStorageMode(dbRam).getCode());
    }
}
