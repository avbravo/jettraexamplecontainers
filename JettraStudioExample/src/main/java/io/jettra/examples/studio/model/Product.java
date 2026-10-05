package io.jettra.examples.studio.model;

import java.util.Locale;

/**
 * Java 25 Record representing a Product in the catalog.
 */
public record Product(
    String id,
    String name,
    String category,
    int stock,
    double price,
    boolean active
) {
    public String getFormattedPrice() {
        return String.format(Locale.US, "$%.2f", price);
    }

    public String getStatusBadge() {
        return active ? "ACTIVO" : "INACTIVO";
    }
}
