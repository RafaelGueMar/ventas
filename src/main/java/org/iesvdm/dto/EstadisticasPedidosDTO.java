package org.iesvdm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstadisticasPedidosDTO {

    private long totalPedidos;
    private double mediaPedidos;
    private double maximoPedido;
    private double minimoPedido;
}
