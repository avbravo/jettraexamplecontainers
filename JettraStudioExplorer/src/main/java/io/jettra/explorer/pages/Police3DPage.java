package io.jettra.explorer.pages;

import io.jettra.explorer.model.ClusterTrafficBatch;
import io.jettra.explorer.model.LiveUserSession;
import io.jettra.explorer.model.PoliceSentinel;
import io.jettra.explorer.model.ServerNode;
import io.jettra.explorer.model.UserZone;
import io.jettra.explorer.service.StoreClusterService;
import io.jettra.studio.components.Button;
import io.jettra.studio.components.FeedbackPanel;
import io.jettra.studio.components.Label;
import io.jettra.studio.components.Link;
import io.jettra.studio.components.ListItem;
import io.jettra.studio.components.ListView;
import io.jettra.studio.core.PageParameters;
import io.jettra.studio.security.Secured;

import java.util.List;

/**
 * Visualizador Interactivo 3D en Tiempo Real de JettraStorePolice3D.
 * Renderiza la ciudad cibernética con:
 * - Servidores/Racks 3D (Nodos y su submundo interior al hacer clic).
 * - Edificios 3D (Zonas y Sedes por subred IP).
 * - Personas 3D (Sesiones en vivo y consultas activas con pensamientos).
 * - Perros Centinelas 3D (Heap Sentinel, Raft Quorum K9, MemTable Purge Dog, Security Patrol).
 * - Camiones Cuánticos (Tráfico de transacciones y réplicas Raft).
 * - Sistema de Voz Narrador y Atajos de Teclado.
 */
@Secured(roles = {"ADMIN", "OPERATOR"}, loginUrl = "/login")
public class Police3DPage extends ExplorerTemplatePage {

    private String selectedNodeId = "node-01";

    public Police3DPage() {
        this(new PageParameters());
    }

    public Police3DPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        setPageTitle("Monitor 3D en Tiempo Real - JettraStorePolice3D");
        super.onInitialize();

        StoreClusterService service = StoreClusterService.getInstance();
        FeedbackPanel feedback = new FeedbackPanel("police3dFeedback");
        add(feedback);

        String pNode = getPageParameters().get("node");
        if (pNode != null && !pNode.isBlank()) {
            this.selectedNodeId = pNode.trim();
        }

        String action = getPageParameters().get("action");
        if ("toggle_multinode".equalsIgnoreCase(action)) {
            boolean current = service.isMultinodeActive();
            service.setMultinodeActive(!current);
            feedback.info("✓ Topología multinodo conmutada a " + (!current ? "ON (Raft Distribuido)" : "OFF (Standalone Mononodo)"));
        } else if ("reset_world".equalsIgnoreCase(action)) {
            feedback.info("✓ Entorno 3D, telemetría y balizas sincronizadas con el servidor JettraStore.");
        }

        // Subworld Internal Telemetry for Selected Node
        var nodeTelemetry = service.getNodeInternalTelemetry(selectedNodeId);
        add(new Label("nodeSubworldTitle", "Dimensión Interior: " + nodeTelemetry.nodeName()));
        add(new Label("nodeSubworldHost", nodeTelemetry.host() + ":" + nodeTelemetry.port() + " (" + nodeTelemetry.role() + ")"));
        add(new Label("nodeHeapFmt", String.format("%.1f MB / %.1f MB", nodeTelemetry.heapUsedMb(), nodeTelemetry.heapMaxMb())));
        add(new Label("nodeDirectMemFmt", String.format("%.1f MB / %.1f MB", nodeTelemetry.directMemoryUsedMb(), nodeTelemetry.directMemoryLimitMb())));
        add(new Label("nodeLoomThreads", String.valueOf(nodeTelemetry.loomVirtualThreads())));
        add(new Label("nodeMemTableFmt", String.format("%.1f MB (WAL Term #%d)", nodeTelemetry.memTableMb(), nodeTelemetry.walTerm())));
        add(new Label("nodeSstablesCount", String.valueOf(nodeTelemetry.sstableFiles()) + " archivos SSTables"));
        add(new Label("nodeDiskLatency", String.format("%.2f ms", nodeTelemetry.ioDiskLatencyMs())));
        add(new Label("multinodeStatusBadge", service.isMultinodeActive() ? "MULTINODO: ON (Raft Quorum)" : "STANDALONE: OFF"));

        add(Link.of("linkToggleMultinode", "/police3d?action=toggle_multinode&node=" + selectedNodeId));
        add(Link.of("linkResetWorld", "/police3d?action=reset_world&node=" + selectedNodeId));

        // Subworld Hosted Databases List
        List<String> dbs = nodeTelemetry.hostedDatabases();
        add(new ListView<String>("nodeDbRows", dbs) {
            @Override
            protected void populateItem(ListItem<String> item) {
                String d = item.getModelObject();
                item.add(new Label("nodeDbName", d));
                item.add(Link.of("linkInspectDb", "/records?db=" + d));
            }
        });

        // 3D Legend: Active Nodes
        List<ServerNode> nodes = service.getNodes();
        add(new ListView<ServerNode>("legendNodes", nodes) {
            @Override
            protected void populateItem(ListItem<ServerNode> item) {
                ServerNode n = item.getModelObject();
                item.add(new Label("legendNodeName", n.name()));
                item.add(new Label("legendNodeRole", n.role()));
                item.add(new Label("legendNodeLatency", n.latencyMs() + " ms"));
                item.add(Link.of("linkEnterSubworld", "/police3d?node=" + n.id()));
            }
        });

        // 3D Legend: Sentinels
        List<PoliceSentinel> sentinels = service.getSentinels();
        add(new ListView<PoliceSentinel>("legendSentinels", sentinels) {
            @Override
            protected void populateItem(ListItem<PoliceSentinel> item) {
                PoliceSentinel s = item.getModelObject();
                item.add(new Label("sentinelName", s.name()));
                item.add(new Label("sentinelTitle", s.title()));
                item.add(new Label("sentinelNode", s.assignedNode()));
                item.add(new Label("sentinelHealth", s.healthPercent() + "%"));
            }
        });

        // 3D Legend: Live Sessions (Personas)
        List<LiveUserSession> sessions = service.getLiveSessions();
        add(new ListView<LiveUserSession>("legendSessions", sessions) {
            @Override
            protected void populateItem(ListItem<LiveUserSession> item) {
                LiveUserSession s = item.getModelObject();
                item.add(new Label("sessionUser", s.username()));
                item.add(new Label("sessionIp", s.ipAddress()));
                item.add(new Label("sessionDb", s.targetDatabase()));
                item.add(new Label("sessionQuery", s.activeQuery()));
                item.add(new Label("sessionLatency", s.latencyMs() + " ms"));
            }
        });

        // 3D Legend: Zones (Edificios)
        List<UserZone> zones = service.getUserZones();
        add(new ListView<UserZone>("legendZones", zones) {
            @Override
            protected void populateItem(ListItem<UserZone> item) {
                UserZone z = item.getModelObject();
                item.add(new Label("zoneName", z.name()));
                item.add(new Label("zoneSubnet", z.subnet()));
                item.add(new Label("zoneUsers", String.valueOf(z.activeUsers())));
                item.add(new Label("zoneBandwidth", z.bandwidthMbps() + " Mbps"));
            }
        });

        // 3D Legend: Traffic (Camiones)
        List<ClusterTrafficBatch> traffic = service.getClusterTraffic();
        add(new ListView<ClusterTrafficBatch>("legendTraffic", traffic) {
            @Override
            protected void populateItem(ListItem<ClusterTrafficBatch> item) {
                ClusterTrafficBatch t = item.getModelObject();
                item.add(new Label("trafficBatch", t.batchId()));
                item.add(new Label("trafficRoute", t.sourceNode() + " ➔ " + t.targetNode()));
                item.add(new Label("trafficType", t.trafficType()));
                item.add(new Label("trafficRate", t.transferRate()));
            }
        });
    }
}
