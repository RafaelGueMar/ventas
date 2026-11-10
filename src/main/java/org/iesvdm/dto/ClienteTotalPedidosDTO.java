package org.iesvdm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClienteTotalPedidosDTO {

    private Integer clienteId;
    private String clienteNombre;
    private double totalPedidos;
}
