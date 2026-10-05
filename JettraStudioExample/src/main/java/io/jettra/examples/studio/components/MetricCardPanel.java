package io.jettra.examples.studio.components;

import io.jettra.studio.components.Label;
import io.jettra.studio.components.Panel;
import io.jettra.studio.model.Model;

/**
 * Reusable MetricCardPanel demonstrating JettraStudio's modular Panel system.
 */
public class MetricCardPanel extends Panel {

    public MetricCardPanel(String id, String title, String value, String changePercent, String icon) {
        super(id);
        add(new Label("metricIcon", icon));
        add(new Label("metricTitle", title));
        add(new Label("metricValue", value));
        add(new Label("metricChange", changePercent));
    }
}
