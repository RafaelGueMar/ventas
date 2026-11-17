package org.iesvdm.modelo;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteComercial {

    private int clienteId;
    private Comercial comercial;
    private LocalDate fecha;
    private Integer prioridad;
}
