package io.jettra.examples.store.factura;

import io.jettra.examples.store.driver.JettraDriver;
import io.jettra.examples.store.factura.model.DetalleFactura;
import io.jettra.examples.store.factura.model.Factura;
import io.jettra.examples.store.factura.service.FacturaService;
import io.jettra.examples.store.util.ConsoleColor;
import io.jettra.store.core.StorageMode;

import java.util.List;
import java.util.Optional;

/**
 * Ejemplo completo de operaciones CRUD sobre la base de datos 'example_factura_db'
 * utilizando JettraDriver y alternando dinámicamente entre los 3 modos de almacenamiento:
 *   1. JVM_RAM     : Memoria RAM estándar Heap & Stack de la JVM.
 *   2. DISK_MEMORY : Motor de disco directo Off-Heap LSM con JettraMemory (Project Panama FFM).
 *   3. OFF_HEAP    : Motor de páginas ranuradas nativas Slotted-Page FFM con JettraStore-OffHeap.
 */
public class FacturaCrudExample {

    public static void run(JettraDriver driver) {
        ConsoleColor.printHeader("15. CRUD COMPLETO EN 'example_factura_db' CON MODOS RAM, DISK Y OFF-HEAP");

        FacturaService service = new FacturaService(driver);

        // =========================================================================
        // FASE 1: Operaciones en modo JVM_RAM
        // =========================================================================
        ConsoleColor.printSubHeader("FASE 1: Operaciones en Modo JVM_RAM");
        service.setStorageMode(StorageMode.JVM_RAM);
        ConsoleColor.printInfo("Modo Activo", service.getStorageMode().name() + " (" + service.getStorageMode().getDescription() + ")");

        Factura facRam = new Factura();
        facRam.setId("fac_demo_ram_01");
        facRam.setNumero("FAC-2026-RAM-001");
        facRam.setFecha("2026-11-03");
        facRam.setClienteId("cli_ram_100");
        facRam.setClienteNombre("Servicios Cloud Panamá S.A.");
        facRam.setEstado("TIMBRADA");
        facRam.addItem(new DetalleFactura("det_1", "Subscripción Cloud Enterprise 64 Cores", 1, 1250.00));
        facRam.addItem(new DetalleFactura("det_2", "Soporte Platinum 24/7 Virtual Threads", 1, 350.00));

        // Create
        service.create(facRam);
        ConsoleColor.printSuccess("Factura creada en JVM_RAM: " + facRam.getNumero() + " | Total: $" + facRam.getTotal());

        // Read
        Optional<Factura> readRam = service.findById("fac_demo_ram_01");
        readRam.ifPresent(f -> ConsoleColor.printInfo("Lectura verificada [JVM_RAM]", f.getClienteNombre() + " -> $" + f.getTotal()));

        // Update
        facRam.setEstado("PAGADA");
        facRam.addItem(new DetalleFactura("det_3", "Certificado de Cifrado Cuántico", 1, 150.00));
        service.update("fac_demo_ram_01", facRam);
        ConsoleColor.printSuccess("Factura actualizada en JVM_RAM a estado 'PAGADA' | Nuevo Total: $" + facRam.getTotal());

        // =========================================================================
        // FASE 2: Conmutación a DISK_MEMORY (JettraMemory LSM Panama)
        // =========================================================================
        ConsoleColor.printSubHeader("FASE 2: Conmutación a Modo DISK_MEMORY");
        service.setStorageMode(StorageMode.DISK_MEMORY);
        ConsoleColor.printInfo("Modo Activo", service.getStorageMode().name() + " (" + service.getStorageMode().getDescription() + ")");

        Factura facDisk = new Factura();
        facDisk.setId("fac_demo_disk_02");
        facDisk.setNumero("FAC-2026-DISK-002");
        facDisk.setFecha("2026-11-03");
        facDisk.setClienteId("cli_disk_200");
        facDisk.setClienteNombre("Financiera Global del Istmo Inc.");
        facDisk.setEstado("TIMBRADA");
        facDisk.addItem(new DetalleFactura("det_d1", "Licenciamiento JettraStore Cluster 8 Nodos", 2, 4500.00));

        // Create
        service.create(facDisk);
        ConsoleColor.printSuccess("Factura creada en DISK_MEMORY: " + facDisk.getNumero() + " | Total: $" + facDisk.getTotal());

        // Read
        Optional<Factura> readDisk = service.findById("fac_demo_disk_02");
        readDisk.ifPresent(f -> ConsoleColor.printInfo("Lectura verificada [DISK_MEMORY]", f.getClienteNombre() + " -> $" + f.getTotal()));

        // =========================================================================
        // FASE 3: Conmutación a OFF_HEAP (JettraStore-OffHeap Slotted-Page FFM)
        // =========================================================================
        ConsoleColor.printSubHeader("FASE 3: Conmutación a Modo OFF_HEAP (Slotted-Page Native)");
        service.setStorageMode(StorageMode.OFF_HEAP);
        ConsoleColor.printInfo("Modo Activo", service.getStorageMode().name() + " (" + service.getStorageMode().getDescription() + ")");

        Factura facOffHeap = new Factura();
        facOffHeap.setId("fac_demo_offheap_03");
        facOffHeap.setNumero("FAC-2026-OFFHEAP-003");
        facOffHeap.setFecha("2026-11-03");
        facOffHeap.setClienteId("cli_offheap_300");
        facOffHeap.setClienteNombre("Infraestructuras de Alta Velocidad S.A.");
        facOffHeap.setEstado("EMITIDA");
        facOffHeap.addItem(new DetalleFactura("det_o1", "Módulo OffHeap Zero-Copy Slotted-Page Panama", 1, 3800.00));
        facOffHeap.addItem(new DetalleFactura("det_o2", "Consultoría RAII y Memory Arenas", 1, 950.00));

        // Create
        service.create(facOffHeap);
        ConsoleColor.printSuccess("Factura creada en OFF_HEAP: " + facOffHeap.getNumero() + " | Total: $" + facOffHeap.getTotal());

        // Read
        Optional<Factura> readOffHeap = service.findById("fac_demo_offheap_03");
        readOffHeap.ifPresent(f -> ConsoleColor.printInfo("Lectura verificada [OFF_HEAP]", f.getClienteNombre() + " -> $" + f.getTotal()));

        // Update
        facOffHeap.setEstado("TIMBRADA");
        service.update("fac_demo_offheap_03", facOffHeap);
        ConsoleColor.printSuccess("Factura actualizada en OFF_HEAP a estado 'TIMBRADA'");

        // Delete test
        boolean deleted = service.delete("fac_demo_ram_01");
        ConsoleColor.printSuccess("Factura de prueba 'fac_demo_ram_01' eliminada: " + deleted);

        // List & Paginación
        List<Factura> pagina = service.list(1, 5);
        ConsoleColor.printInfo("Listado paginado (Página 1, tamaño 5)", pagina.size() + " facturas obtenidas");
        for (Factura f : pagina) {
            System.out.printf("   * ID: %-20s | No: %-18s | Total: $%10.2f | Estado: %s%n",
                    f.getId(), f.getNumero(), f.getTotal(), f.getEstado());
        }

        ConsoleColor.printSuccess("Total de Facturas en 'example_factura_db': " + service.count());
    }
}

