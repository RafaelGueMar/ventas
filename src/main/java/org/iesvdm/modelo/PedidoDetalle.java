package org.iesvdm.modelo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Datos de lectura de un pedido junto con los nombres de sus relaciones.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDetalle {

    private int id;
    private double total;
    private LocalDate fecha;
    private Integer clienteId;
    private Integer comercialId;
    private String clienteNombre;
    private String comercialNombre;
}
