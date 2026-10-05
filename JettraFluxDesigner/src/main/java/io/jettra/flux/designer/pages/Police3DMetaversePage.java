package io.jettra.flux.designer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.widgets.RawHtml;

import java.util.Map;

@Page(path = "/metaverse")
public class Police3DMetaversePage extends TemplatePage {

    @Override
    protected String getTitle() {
        return "JettraFlux Designer • Metaverso JettraPolice 3D";
    }

    @Override
    protected Widget buildCenter(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        String action = params.get("action");
        if ("addToCanvas".equalsIgnoreCase(action)) {
            sessionState.addWidgetToCanvas("Police3DCanvas", null);
            try {
                exchange.getResponseHeaders().set("Location", "/designer");
                exchange.sendResponseHeaders(302, -1);
                return RawHtml.of("Redirecting...");
            } catch (Exception ignored) {}
        }

        StringBuilder sb = new StringBuilder();

        sb.append("<script src='https://cdnjs.cloudflare.com/ajax/libs/three.js/r128/three.min.js'></script>");

        sb.append("<div style='display:flex; justify-content:space-between; align-items:center; margin-bottom:18px; flex-wrap:wrap; gap:12px;'>")
          .append("  <div>")
          .append("    <h2 style='margin:0; font-size:22px; font-weight:800; color:#fff;'><i class='fas fa-cube' style='color:#00d4ff; margin-right:8px;'></i> Componente Metaverso JettraPolice 3D</h2>")
          .append("    <p style='margin:4px 0 0; color:#94a3b8; font-size:13px;'>Diseño 3D de alta fidelidad basado en JettraStorePolice3D para incrustar en cualquier interfaz reactiva JettraFlux</p>")
          .append("  </div>")
          .append("  <div style='display:flex; gap:10px;'>")
          .append("    <a href='?action=addToCanvas' class='btn-cyber' style='background:#10b981;'><i class='fas fa-plus'></i> Incrustar en Lienzo del Diseñador</a>")
          .append("  </div>")
          .append("</div>");

        // 3D Canvas Preview
        sb.append("<div style='width:100%; height:500px; background:#040711; border-radius:12px; border:1px solid #1e293b; overflow:hidden; position:relative; box-shadow:0 8px 30px rgba(0,0,0,0.6); margin-bottom:20px;'>")
          .append("  <canvas id='metaverseCanvas' style='width:100%; height:100%; display:block;'></canvas>")
          .append("  <div style='position:absolute; bottom:16px; left:16px; background:rgba(15,23,42,0.85); backdrop-filter:blur(8px); padding:10px 16px; border-radius:8px; border:1px solid rgba(0,212,255,0.3); font-size:12px; color:#f8fafc;'>")
          .append("    <div>• <b>Racks Cibernéticos</b>: Telemetría de Servidores y Nodos Raft</div>")
          .append("    <div>• <b>Edificios de Zonas</b>: Redes y Subredes IP</div>")
          .append("    <div>• <b>Camiones de Datos</b>: Flujos de transacciones y replicación</div>")
          .append("  </div>")
          .append("</div>");

        // Feature Explanation Cards
        sb.append("<div style='display:grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap:18px;'>")
          .append("<div class='designer-card'>")
          .append("  <h4 style='color:#00d4ff; margin:0 0 8px;'><i class='fas fa-code'></i> Generación Automática</h4>")
          .append("  <p style='color:#94a3b8; font-size:13px; margin:0;'>Al arrastrar este widget, JettraFluxDesigner inyecta automáticamente el pipeline WebGL y los callbacks de eventos hacia JettraStoreDriver.</p>")
          .append("</div>")
          .append("<div class='designer-card'>")
          .append("  <h4 style='color:#22c55e; margin:0 0 8px;'><i class='fas fa-bolt'></i> Cero Dependencias Pesadas</h4>")
          .append("  <p style='color:#94a3b8; font-size:13px; margin:0;'>Utiliza Three.js estándar sin sobrecarga ni bibliotecas invasivas, compatible con cualquier navegador moderno.</p>")
          .append("</div>")
          .append("<div class='designer-card'>")
          .append("  <h4 style='color:#eab308; margin:0 0 8px;'><i class='fas fa-shield-alt'></i> Telemetría en Tiempo Real</h4>")
          .append("  <p style='color:#94a3b8; font-size:13px; margin:0;'>Monitorea la saturación de Memoria Directa Panama y los Hilos Virtuales Loom directamente en 3D.</p>")
          .append("</div>")
          .append("</div>");

        // Embedded Script for Three.js
        sb.append("""
            <script>
            (function() {
                let canvas = document.getElementById('metaverseCanvas');
                if (!canvas) return;

                let scene = new THREE.Scene();
                scene.background = new THREE.Color(0x040711);
                scene.fog = new THREE.FogExp2(0x040711, 0.02);

                let camera = new THREE.PerspectiveCamera(45, canvas.clientWidth / canvas.clientHeight, 0.1, 1000);
                camera.position.set(0, 24, 38);
                camera.lookAt(0, 0, 0);

                let renderer = new THREE.WebGLRenderer({ canvas: canvas, antialias: true });
                renderer.setSize(canvas.clientWidth, canvas.clientHeight);
                renderer.setPixelRatio(window.devicePixelRatio);

                let ambient = new THREE.AmbientLight(0xffffff, 0.7);
                scene.add(ambient);
                let dirLight = new THREE.DirectionalLight(0x00d4ff, 0.9);
                dirLight.position.set(10, 30, 10);
                scene.add(dirLight);

                let grid = new THREE.GridHelper(80, 40, 0x00d4ff, 0x1e293b);
                grid.position.y = -0.1;
                scene.add(grid);

                // Add sample servers
                let group = new THREE.Group();
                for (let i = 0; i < 4; i++) {
                    let box = new THREE.Mesh(
                        new THREE.BoxGeometry(3, 6, 3),
                        new THREE.MeshStandardMaterial({ color: 0x0f172a, emissive: 0x00d4ff, emissiveIntensity: 0.3 })
                    );
                    box.position.set(-9 + i * 6, 3, -6);
                    group.add(box);
                }
                scene.add(group);

                let t = 0;
                function animate() {
                    requestAnimationFrame(animate);
                    t += 0.01;
                    group.rotation.y = Math.sin(t * 0.5) * 0.2;
                    renderer.render(scene, camera);
                }
                animate();

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
