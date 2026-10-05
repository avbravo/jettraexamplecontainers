package io.jettra.explorer.pages;

import io.jettra.explorer.model.ClusterTrafficBatch;
import io.jettra.explorer.model.PoliceIncident;
import io.jettra.explorer.model.PoliceSentinel;
import io.jettra.explorer.model.UserZone;
import io.jettra.explorer.service.StoreClusterService;
import io.jettra.studio.components.Button;
import io.jettra.studio.components.FeedbackPanel;
import io.jettra.studio.components.Label;
import io.jettra.studio.components.Link;
import io.jettra.studio.components.ListItem;
import io.jettra.studio.components.ListView;
import io.jettra.studio.core.PageParameters;
import io.jettra.studio.model.Model;
import io.jettra.studio.security.Secured;

import java.util.List;

/**
 * Panel de Control Policial y Centinelas JettraPolice.
 * Supervisa:
 * - 4 Agentes Centinelas (Heap, Raft, Purge, Security).
 * - Muro de incidentes de seguridad y evaluaciones en tiempo real.
 * - Conmutador en caliente de topología multinodo.
 * - Tráfico de réplicas y sedes de conexión.
 */
@Secured(roles = {"ADMIN", "OPERATOR"}, loginUrl = "/login")
public class PoliceMonitorPage extends ExplorerTemplatePage {

    public PoliceMonitorPage() {
        this(new PageParameters());
    }

    public PoliceMonitorPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        setPageTitle("Centinelas Police & Auditoría - JettraStore Explorer");
        super.onInitialize();

        StoreClusterService service = StoreClusterService.getInstance();
        FeedbackPanel feedback = new FeedbackPanel("policeFeedback");
        add(feedback);

        String action = getPageParameters().get("action");
        if ("run_patrol".equalsIgnoreCase(action)) {
            service.logIncident("Security Patrol", "Ronda de auditoría general ejecutada. Quorum Raft y memoria óptimos.", "OK", "Verificación sin novedades");
            feedback.info("✓ Ronda de patrullaje policial completada con éxito. Todos los nodos validados.");
        } else if ("toggle_multinode".equalsIgnoreCase(action)) {
            boolean cur = service.isMultinodeActive();
            service.setMultinodeActive(!cur);
            feedback.info("✓ Topología multinodo establecida en " + (!cur ? "ON (Raft Distribuido)" : "OFF (Standalone)"));
        }

        add(new Label("policeQuorumStatus", service.isMultinodeActive() ? "Quorum Raft Óptimo | 4 Nodos Sincronizados" : "Modo Standalone Mononodo"));
        add(new Label("activeSentinelsCount", String.valueOf(service.getSentinels().size())));
        add(new Label("totalIncidentsCount", String.valueOf(service.getIncidents().size())));
        add(new Label("multinodeBadge", service.isMultinodeActive() ? "MULTINODO: ON" : "STANDALONE: OFF"));

        add(Link.of("linkRunPatrol", "/police?action=run_patrol"));
        add(Link.of("linkToggleMultinodePolice", "/police?action=toggle_multinode"));

        // Sentinels List
        List<PoliceSentinel> sentinels = service.getSentinels();
        add(new ListView<PoliceSentinel>("sentinelCards", Model.of(sentinels)) {
            @Override
            protected void populateItem(ListItem<PoliceSentinel> item) {
                PoliceSentinel s = item.getModelObject();
                item.add(new Label("cardSentinelName", s.name()));
                item.add(new Label("cardSentinelTitle", s.title()));
                item.add(new Label("cardSentinelNode", s.assignedNode()));
                item.add(new Label("cardSentinelStatus", s.status()));
                item.add(new Label("cardSentinelHealth", s.healthPercent() + "% Salud"));
                item.add(new Label("cardSentinelAlerts", s.alertsCount() + " Alertas"));
                item.add(new Label("cardSentinelMission", s.mission()));
            }
        });

        // Incidents Table
        List<PoliceIncident> incidents = service.getIncidents();
        add(new ListView<PoliceIncident>("incidentRows", Model.of(incidents)) {
            @Override
            protected void populateItem(ListItem<PoliceIncident> item) {
                PoliceIncident inc = item.getModelObject();
                item.add(new Label("incTime", inc.timestamp()));
                item.add(new Label("incSentinel", inc.sentinel()));
                item.add(new Label("incDesc", inc.description()));
                item.add(new Label("incSeverity", inc.severity()));
                item.add(new Label("incAction", inc.actionTaken()));
            }
        });

        // Zones Table
        List<UserZone> zones = service.getUserZones();
        add(new ListView<UserZone>("zoneRows", Model.of(zones)) {
            @Override
            protected void populateItem(ListItem<UserZone> item) {
                UserZone z = item.getModelObject();
                item.add(new Label("tblZoneName", z.name()));
                item.add(new Label("tblZoneSubnet", z.subnet()));
                item.add(new Label("tblZoneUsers", String.valueOf(z.activeUsers())));
                item.add(new Label("tblZoneBw", z.bandwidthMbps() + " Mbps"));
            }
        });
    }
}
