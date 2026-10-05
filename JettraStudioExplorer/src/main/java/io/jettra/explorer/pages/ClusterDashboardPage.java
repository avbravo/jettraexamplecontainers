package io.jettra.explorer.pages;

import io.jettra.explorer.model.ServerNode;
import io.jettra.explorer.service.StoreClusterService;
import io.jettra.flux.widgets.StatCard;
import io.jettra.studio.components.Alert;
import io.jettra.studio.components.FluxWidget;
import io.jettra.studio.components.Label;
import io.jettra.studio.components.ListItem;
import io.jettra.studio.components.ListView;
import io.jettra.studio.core.PageParameters;
import io.jettra.studio.model.Model;
import io.jettra.studio.security.Secured;

import java.util.List;

@Secured(roles = {"ADMIN", "OPERATOR"}, loginUrl = "/login")
public class ClusterDashboardPage extends ExplorerTemplatePage {

    public ClusterDashboardPage() {
        this(new PageParameters());
    }

    public ClusterDashboardPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        setPageTitle("Monitoreo de Nodos & Clúster 3D - JettraStore");
        super.onInitialize();

        StoreClusterService service = StoreClusterService.getInstance();

        add(Alert.success("clusterAlert", "✓ Topología del Clúster sincronizada en tiempo real. 0 fallas detectadas por JettraPolice."));

        // FluxWidgets
        add(FluxWidget.of("fluxActiveNodes", StatCard.of("Nodos Activos", "100%", service.getOnlineNodesCount() + " / " + service.getNodes().size() + " Nodos", true)));
        add(FluxWidget.of("fluxGlobalTps", StatCard.of("Rendimiento Global", "+14.2%", String.format("%,d TPS", service.getTotalTps()), true)));
        add(FluxWidget.of("fluxLatency", StatCard.of("Latencia Media", "-0.2ms", String.format("%.2f ms", service.getAverageLatency()), true)));
        add(FluxWidget.of("fluxCpu", StatCard.of("Carga CPU Loom", "Óptimo", String.format("%.1f%%", service.getAverageCpu()), true)));

        // Nodes Table
        List<ServerNode> nodes = service.getNodes();
        add(new ListView<ServerNode>("nodeRows", Model.of(nodes)) {
            @Override
            protected void populateItem(ListItem<ServerNode> item) {
                ServerNode n = item.getModelObject();
                item.add(new Label("nodeId", n.id()));
                item.add(new Label("nodeName", n.name()));
                item.add(new Label("nodeHostPort", n.host() + ":" + n.port()));
                item.add(new Label("nodeRole", n.role()));
                item.add(new Label("nodeStatus", n.status()));
                item.add(new Label("nodeLatency", String.format("%.2f ms", n.latencyMs())));
                item.add(new Label("nodeCpu", String.format("%.1f%%", n.cpuPercent())));
                item.add(new Label("nodeMemory", n.memoryUsedMb() + " / " + n.memoryTotalMb() + " MB"));
                item.add(new Label("nodeTps", String.format("%,d", n.tps())));
            }
        });
    }
}
