package io.jettra.examples.studio.model;

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
        return String.format("$%.2f", price);
    }

    public String getStatusBadge() {
        return active ? "ACTIVO" : "INACTIVO";
    }
}
