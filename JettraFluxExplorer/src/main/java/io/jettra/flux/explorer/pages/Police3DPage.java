package io.jettra.flux.explorer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.widgets.RawHtml;
import io.jettra.flux.explorer.model.*;

import java.util.List;
import java.util.Map;

@Page(path = "/police3d")
public class Police3DPage extends TemplatePage {

    @Override
    protected String getTitle() {
        return "JettraFlux Explorer • Monitor 3D en Tiempo Real";
    }

    @Override
    protected Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        String selectedNodeId = params.getOrDefault("node", "node-01");
        var telemetry = clusterService.getNodeInternalTelemetry(selectedNodeId);

        List<ServerNode> nodes = clusterService.getNodes();
        List<PoliceSentinel> sentinels = clusterService.getSentinels();
        List<LiveUserSession> sessions = clusterService.getLiveSessions();
        List<UserZone> zones = clusterService.getUserZones();
        List<ClusterTrafficBatch> traffic = clusterService.getClusterTraffic();

        StringBuilder sb = new StringBuilder();

        // Include Three.js via CDN
        sb.append("<script src='https://cdnjs.cloudflare.com/ajax/libs/three.js/r128/three.min.js'></script>");

        // Header & Controls
        sb.append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:16px; flex-wrap:wrap; gap:12px;'>")
          .append("  <div>")
          .append("    <h2 style='margin:0; font-size:22px; font-weight:800; color:#fff;'>JettraStorePolice3D • Metaverso Cibernético</h2>")
          .append("    <p style='margin:4px 0 0; color:#94a3b8; font-size:13px;'>Visualizador 3D: Racks de Nodos, Zonas IP, Sesiones en Tiempo Real, Perros Centinela y Camiones Raft</p>")
          .append("  </div>")
          .append("  <div style='display:flex; gap:10px;'>")
          .append("    <button onclick='resetCamera()' class='btn-cyber'><i class='fas fa-compass'></i> Reajustar Cámara</button>")
          .append("    <a href='?action=toggle_multinode&node=").append(selectedNodeId).append("' class='btn-cyber' style='background:#059669;'><i class='fas fa-network-wired'></i> Alternar Multinodo</a>")
          .append("  </div>")
          .append("</div>");

        // 3D Canvas Container
        sb.append("<div style='position:relative; width:100%; height:520px; background:#040711; border-radius:12px; border:1px solid #1e293b; overflow:hidden; box-shadow:0 8px 30px rgba(0,0,0,0.6); margin-bottom:24px;'>")
          .append("  <canvas id='police3dCanvas' style='width:100%; height:100%; display:block;'></canvas>")
          .append("  <div style='position:absolute; top:16px; left:16px; background:rgba(15,23,42,0.85); backdrop-filter:blur(8px); padding:10px 16px; border-radius:8px; border:1px solid rgba(0,212,255,0.3); font-size:12px; color:#f8fafc; pointer-events:none;'>")
          .append("    <div style='font-weight:700; color:#00d4ff;'><i class='fas fa-cube'></i> CONTROLES INTERACTIVOS</div>")
          .append("    <div>• Clic en Racks / Nodos para entrar a su Submundo</div>")
          .append("    <div>• Arrastrar botón izquierdo: Rotar Ciudad 3D</div>")
          .append("    <div>• Rueda del ratón: Zoom In / Out</div>")
          .append("  </div>")
          .append("  <div id='hoverTooltip' style='position:absolute; display:none; background:rgba(2,6,23,0.9); border:1px solid #00d4ff; border-radius:6px; padding:6px 12px; font-size:12px; color:#fff; pointer-events:none; z-index:100;'></div>")
          .append("</div>");

        // Subworld Internal Telemetry Panel
        sb.append("<div class='explorer-card' style='border-top:3px solid #00d4ff;'>")
          .append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:14px;'>")
          .append("  <h3 style='margin:0; font-size:16px; font-weight:700; color:#fff;'><i class='fas fa-door-open' style='color:#00d4ff; margin-right:8px;'></i> Dimensión Interior del Nodo: <span style='color:#00d4ff;'>").append(telemetry.nodeName()).append("</span></h3>")
          .append("  <span class='cyber-badge-online'>").append(telemetry.role()).append(" • ").append(telemetry.host()).append(":").append(telemetry.port()).append("</span>")
          .append("</div>")
          .append("<div style='display:grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap:14px; margin-bottom:16px;'>")
          .append("  <div style='background:#090d16; padding:12px; border-radius:8px; border:1px solid #1e293b;'>")
          .append("    <div style='font-size:11px; color:#94a3b8;'>HEAP JVM</div>")
          .append("    <div style='font-size:16px; font-weight:800; color:#38bdf8;'>").append(String.format("%.1f", telemetry.heapUsedMb())).append(" / ").append(String.format("%.1f", telemetry.heapMaxMb())).append(" MB</div>")
          .append("  </div>")
          .append("  <div style='background:#090d16; padding:12px; border-radius:8px; border:1px solid #1e293b;'>")
          .append("    <div style='font-size:11px; color:#94a3b8;'>DIRECT OFF-HEAP (PANAMA)</div>")
          .append("    <div style='font-size:16px; font-weight:800; color:#22c55e;'>").append(String.format("%.1f", telemetry.directMemoryUsedMb())).append(" / ").append(String.format("%.1f", telemetry.directMemoryLimitMb())).append(" MB</div>")
          .append("  </div>")
          .append("  <div style='background:#090d16; padding:12px; border-radius:8px; border:1px solid #1e293b;'>")
          .append("    <div style='font-size:11px; color:#94a3b8;'>VIRTUAL THREADS LOOM</div>")
          .append("    <div style='font-size:16px; font-weight:800; color:#eab308;'>").append(telemetry.loomVirtualThreads()).append(" hilos</div>")
          .append("  </div>")
          .append("  <div style='background:#090d16; padding:12px; border-radius:8px; border:1px solid #1e293b;'>")
          .append("    <div style='font-size:11px; color:#94a3b8;'>MEMTABLE (WAL TERM)</div>")
          .append("    <div style='font-size:16px; font-weight:800; color:#ec4899;'>").append(String.format("%.1f", telemetry.memTableMb())).append(" MB (Term #").append(telemetry.walTerm()).append(")</div>")
          .append("  </div>")
          .append("  <div style='background:#090d16; padding:12px; border-radius:8px; border:1px solid #1e293b;'>")
          .append("    <div style='font-size:11px; color:#94a3b8;'>SSTABLE COMPACTADAS</div>")
          .append("    <div style='font-size:16px; font-weight:800; color:#a855f7;'>").append(telemetry.sstableFiles()).append(" archivos</div>")
          .append("  </div>")
          .append("</div>")

          .append("<div style='font-size:13px; font-weight:700; color:#cbd5e1; margin-bottom:8px;'>Bases de Datos alojadas en este nodo:</div>")
          .append("<div style='display:flex; flex-wrap:wrap; gap:8px;'>");
        for (String db : telemetry.hostedDatabases()) {
            sb.append("<a href='/records?db=").append(db).append("' class='btn-cyber' style='font-size:12px; padding:4px 10px; background:#1e293b;'><i class='fas fa-database' style='color:#00d4ff;'></i> ").append(db).append("</a>");
        }
        sb.append("</div></div>");

        // Two Column Grid: Sentinels & Traffic
        sb.append("<div style='display:grid; grid-template-columns: 1fr 1fr; gap:20px; margin-bottom:24px;'>")
          .append("<div class='explorer-card'>")
          .append("  <h4 style='margin:0 0 12px; font-size:15px; color:#fff;'><i class='fas fa-shield-alt' style='color:#22c55e;'></i> Perros Centinela (JettraGuard K9)</h4>")
          .append("  <table class='explorer-table'>")
          .append("  <thead><tr><th>Nombre</th><th>Nodo Asignado</th><th>Salud</th></tr></thead><tbody>");
        for (PoliceSentinel s : sentinels) {
            sb.append("<tr>")
              .append("<td><b style='color:").append(s.badgeColor()).append(";'>").append(s.name()).append("</b><br/><span style='font-size:11px; color:#64748b;'>").append(s.title()).append("</span></td>")
              .append("<td>").append(s.assignedNode()).append("</td>")
              .append("<td><span class='cyber-badge-online'>").append(s.healthPercent()).append("%</span></td>")
              .append("</tr>");
        }
        sb.append("  </tbody></table>")
          .append("</div>")

          .append("<div class='explorer-card'>")
          .append("  <h4 style='margin:0 0 12px; font-size:15px; color:#fff;'><i class='fas fa-truck' style='color:#38bdf8;'></i> Lotes de Tráfico y Réplicas (Camiones Raft)</h4>")
          .append("  <table class='explorer-table'>")
          .append("  <thead><tr><th>Lote</th><th>Ruta Nodos</th><th>Tipo Carga</th><th>Velocidad</th></tr></thead><tbody>");
        for (ClusterTrafficBatch t : traffic) {
            sb.append("<tr>")
              .append("<td><code>").append(t.batchId()).append("</code></td>")
              .append("<td>").append(t.sourceNode()).append(" ➔ ").append(t.targetNode()).append("</td>")
              .append("<td>").append(t.trafficType()).append("</td>")
              .append("<td><span class='cyber-badge-info'>").append(t.transferRate()).append("</span></td>")
              .append("</tr>");
        }
        sb.append("  </tbody></table>")
          .append("</div>")
          .append("</div>");

        // Two Column Grid: Zones & Sessions
        sb.append("<div style='display:grid; grid-template-columns: 1fr 1fr; gap:20px;'>")
          .append("<div class='explorer-card'>")
          .append("  <h4 style='margin:0 0 12px; font-size:15px; color:#fff;'><i class='fas fa-building' style='color:#eab308;'></i> Zonas / Sedes (Edificios IP 3D)</h4>")
          .append("  <table class='explorer-table'>")
          .append("  <thead><tr><th>Sede / Zona</th><th>Subred IP</th><th>Usuarios</th><th>Ancho Banda</th></tr></thead><tbody>");
        for (UserZone z : zones) {
            sb.append("<tr>")
              .append("<td><b style='color:").append(z.colorHex()).append(";'>").append(z.name()).append("</b></td>")
              .append("<td>").append(z.subnet()).append("</td>")
              .append("<td>").append(z.activeUsers()).append("</td>")
              .append("<td>").append(z.bandwidthMbps()).append(" Mbps</td>")
              .append("</tr>");
        }
        sb.append("  </tbody></table>")
          .append("</div>")

          .append("<div class='explorer-card'>")
          .append("  <h4 style='margin:0 0 12px; font-size:15px; color:#fff;'><i class='fas fa-user-astronaut' style='color:#ec4899;'></i> Sesiones en Vivo (Personas 3D)</h4>")
          .append("  <table class='explorer-table'>")
          .append("  <thead><tr><th>Usuario / IP</th><th>Base Datos</th><th>Consulta Activa</th><th>Latencia</th></tr></thead><tbody>");
        for (LiveUserSession s : sessions) {
            sb.append("<tr>")
              .append("<td><b>").append(s.username()).append("</b><br/><span style='font-size:11px; color:#64748b;'>").append(s.ipAddress()).append("</span></td>")
              .append("<td><span class='cyber-badge-info'>").append(s.targetDatabase()).append("</span></td>")
              .append("<td><code style='font-size:11px;'>").append(s.activeQuery()).append("</code></td>")
              .append("<td>").append(s.latencyMs()).append(" ms</td>")
              .append("</tr>");
        }
        sb.append("  </tbody></table>")
          .append("</div>")
          .append("</div>");

        // Embedded Three.js WebGL Engine
        sb.append("""
            <script>
            (function() {
                let canvas = document.getElementById('police3dCanvas');
                if (!canvas) return;

                let scene = new THREE.Scene();
                scene.background = new THREE.Color(0x040711);
                scene.fog = new THREE.FogExp2(0x040711, 0.015);

                let camera = new THREE.PerspectiveCamera(45, canvas.clientWidth / canvas.clientHeight, 0.1, 1000);
                camera.position.set(0, 32, 50);
                camera.lookAt(0, 0, 0);

                let renderer = new THREE.WebGLRenderer({ canvas: canvas, antialias: true });
                renderer.setSize(canvas.clientWidth, canvas.clientHeight);
                renderer.setPixelRatio(window.devicePixelRatio);

                let ambient = new THREE.AmbientLight(0xffffff, 0.6);
                scene.add(ambient);
                let dirLight = new THREE.DirectionalLight(0x00d4ff, 0.8);
                dirLight.position.set(20, 40, 20);
                scene.add(dirLight);

                let grid = new THREE.GridHelper(100, 50, 0x00d4ff, 0x1e293b);
                grid.position.y = -0.1;
                scene.add(grid);

                let nodesGroup = new THREE.Group();
                let buildingsGroup = new THREE.Group();
                let trucksGroup = new THREE.Group();
                let peopleGroup = new THREE.Group();
                let dogsGroup = new THREE.Group();

                scene.add(nodesGroup);
                scene.add(buildingsGroup);
                scene.add(trucksGroup);
                scene.add(peopleGroup);
                scene.add(dogsGroup);

                // 1. Server Nodes (Racks)
                let nodeCoords = [
                    { id: "node-01", name: "node-01 (Leader)", x: -14, z: -10, color: 0x00d4ff },
                    { id: "node-02", name: "node-02 (Follower)", x: -4, z: -18, color: 0x22c55e },
                    { id: "node-03", name: "node-03 (Follower)", x: 6, z: -18, color: 0x22c55e },
                    { id: "node-04", name: "node-04 (Follower)", x: 16, z: -10, color: 0x22c55e }
                ];

                let clickableMeshes = [];
                nodeCoords.forEach(n => {
                    let geo = new THREE.BoxGeometry(3.5, 7.0, 3.5);
                    let mat = new THREE.MeshStandardMaterial({
                        color: 0x0f172a,
                        emissive: n.color,
                        emissiveIntensity: 0.3,
                        metalness: 0.8,
                        roughness: 0.2
                    });
                    let mesh = new THREE.Mesh(geo, mat);
                    mesh.position.set(n.x, 3.5, n.z);
                    mesh.userData = { id: n.id, name: n.name, type: "NODE" };
                    nodesGroup.add(mesh);
                    clickableMeshes.push(mesh);

                    let ringGeo = new THREE.RingGeometry(2.5, 3.2, 32);
                    let ringMat = new THREE.MeshBasicMaterial({ color: n.color, side: THREE.DoubleSide });
                    let ring = new THREE.Mesh(ringGeo, ringMat);
                    ring.rotation.x = Math.PI / 2;
                    ring.position.set(n.x, 0.05, n.z);
                    nodesGroup.add(ring);
                });

                // 2. Zone Buildings
                let buildingCoords = [
                    { name: "Sede Comercial (192.168.1.x)", x: -24, z: 12, h: 12, color: 0x38bdf8 },
                    { name: "Complejo Hospitalario (10.0.4.x)", x: -10, z: 16, h: 16, color: 0x22c55e },
                    { name: "Red IoT & Boyas (172.16.8.x)", x: 10, z: 16, h: 9, color: 0xeab308 },
                    { name: "DataCenter Central (127.0.0.1)", x: 24, z: 12, h: 14, color: 0xec4899 }
                ];
                buildingCoords.forEach(b => {
                    let bGeo = new THREE.BoxGeometry(5.0, b.h, 5.0);
                    let bMat = new THREE.MeshStandardMaterial({ color: b.color, wireframe: true });
                    let bMesh = new THREE.Mesh(bGeo, bMat);
                    bMesh.position.set(b.x, b.h / 2, b.z);
                    buildingsGroup.add(bMesh);
                });

                // 3. Sentinels (Police Dogs)
                let dogColors = [0x22c55e, 0x00d4ff, 0xffd700, 0x38bdf8];
                for (let i = 0; i < 4; i++) {
                    let dGeo = new THREE.ConeGeometry(0.8, 2.0, 16);
                    let dMat = new THREE.MeshBasicMaterial({ color: dogColors[i] });
                    let dog = new THREE.Mesh(dGeo, dMat);
                    dog.position.set(-12 + i * 8, 1, -5);
                    dogsGroup.add(dog);
                }

                // 4. Traffic Trucks (Moving cubes)
                let truckGeo = new THREE.BoxGeometry(1.6, 1.0, 2.8);
                let truckMat = new THREE.MeshStandardMaterial({ color: 0x38bdf8, emissive: 0x00d4ff, emissiveIntensity: 0.4 });
                let truck1 = new THREE.Mesh(truckGeo, truckMat);
                let truck2 = new THREE.Mesh(truckGeo, truckMat);
                trucksGroup.add(truck1);
                trucksGroup.add(truck2);

                // Animation loop
                let t = 0;
                function animate() {
                    requestAnimationFrame(animate);
                    t += 0.02;

                    // Move trucks along paths
                    truck1.position.x = -14 + Math.sin(t) * 10;
                    truck1.position.z = -14 + Math.cos(t) * 4;
                    truck1.position.y = 0.5;

                    truck2.position.x = 4 + Math.cos(t * 1.2) * 8;
                    truck2.position.z = -14 + Math.sin(t * 1.2) * 4;
                    truck2.position.y = 0.5;

                    // Slight bobbing of dogs
                    dogsGroup.children.forEach((d, idx) => {
                        d.position.y = 1 + Math.sin(t * 2 + idx) * 0.15;
                    });

                    renderer.render(scene, camera);
                }
                animate();

                // Mouse interaction for rotation and clicking
                let isDragging = false;
                let prevMouse = { x: 0, y: 0 };
                let angle = 0;

                canvas.addEventListener('mousedown', e => {
                    isDragging = true;
                    prevMouse = { x: e.clientX, y: e.clientY };
                });
                window.addEventListener('mouseup', () => isDragging = false);

                canvas.addEventListener('mousemove', e => {
                    if (isDragging) {
                        let deltaX = e.clientX - prevMouse.x;
                        angle += deltaX * 0.005;
                        camera.position.x = Math.sin(angle) * 55;
                        camera.position.z = Math.cos(angle) * 55;
                        camera.lookAt(0, 0, 0);
                        prevMouse = { x: e.clientX, y: e.clientY };
                    }
                });

                canvas.addEventListener('click', e => {
                    let rect = canvas.getBoundingClientRect();
                    let mouse = new THREE.Vector2();
                    mouse.x = ((e.clientX - rect.left) / canvas.clientWidth) * 2 - 1;
                    mouse.y = -((e.clientY - rect.top) / canvas.clientHeight) * 2 + 1;

                    let raycaster = new THREE.Raycaster();
                    raycaster.setFromCamera(mouse, camera);
                    let intersects = raycaster.intersectObjects(clickableMeshes);
                    if (intersects.length > 0) {
                        let obj = intersects[0].object;
                        if (obj.userData && obj.userData.id) {
                            window.location.href = '?node=' + obj.userData.id;
                        }
                    }
                });

                window.resetCamera = function() {
                    camera.position.set(0, 32, 50);
                    camera.lookAt(0, 0, 0);
                    angle = 0;
                };

                window.addEventListener('resize', () => {
                    if (!canvas) return;
                    camera.aspect = canvas.clientWidth / canvas.clientHeight;
                    camera.updateProjectionMatrix();
                    renderer.setSize(canvas.clientWidth, canvas.clientHeight);
                });
            })();
            </script>
            """);

        return RawHtml.of(sb.toString());
    }
}
