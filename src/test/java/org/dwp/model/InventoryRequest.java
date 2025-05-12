package org.dwp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class representing an inventory operation request (buy/sell).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
    private String productId;
    private int quantity;
}