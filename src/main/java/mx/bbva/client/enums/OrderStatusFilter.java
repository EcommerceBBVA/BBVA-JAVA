/*
 * COPYRIGHT © 2012-2017. BANCOMER.
 * PATENT PENDING. ALL RIGHTS RESERVED.
 * BANCOMER IS A REGISTERED TRADEMARK OF BANCOMER INC.
 *
 * This software is confidential and proprietary information of BANCOMER INC.
 * You shall not disclose such Confidential Information and shall use it only
 * in accordance with the company policy.
 */
package mx.bbva.client.enums;

/**
 * Estatus disponibles para filtrar transacciones
 *
 */
public enum OrderStatusFilter {

    COMPLETED("completed", "Completada"),

    CHARGEBACK_PENDING("chargeback_pending", "Contracargo en disputa"),

    CHARGEBACK_ACCEPTED("chargeback_accepted", "Contracargo aceptado"),

    CHARGEBACK_ADJUSTMENT("chargeback_adjustment", "Contracargo Rechazado"),

    REFUNDED("refunded", "Reembolsada"),

    CHARGE_PENDING("charge_pending", "Esperando pago"),

    IN_PROGRESS("in_progress", "Pendiente"),

    CANCELLED("cancelled", "Cancelada"),

    FAILED("failed", "Fallida"),

    ;

    private String value;
    private String description;

    OrderStatusFilter(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static OrderStatusFilter getByValue(final String value) {
        OrderStatusFilter status = null;
        for (OrderStatusFilter statusTemp : OrderStatusFilter.values()) {
            if (statusTemp.getValue().equals(value)) {
                status = statusTemp;
                break;
            }
        }
        return status;
    }

    public String getValue() {
        return this.value;
    }

    public String getDescription() {
        return this.description;
    }

}
