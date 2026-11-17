package org.iesvdm.dao;

import java.time.LocalDate;
import java.util.List;

import org.iesvdm.modelo.ClienteComercial;
import org.iesvdm.modelo.Comercial;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ClienteComercialDAOImpl implements ClienteComercialDAO {

    private final JdbcTemplate jdbcTemplate;

    ClienteComercialDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ClienteComercial> findByClienteId(int clienteId) {
        List<ClienteComercial> vinculos = jdbcTemplate.query("""
                SELECT chc.cliente_id, chc.fecha, chc.prioridad,
                       c.id, c.nombre, c.apellido1, c.apellido2, c.comisión
                FROM cliente_has_comercial chc
                JOIN comercial c ON c.id = chc.comercial_id
                WHERE chc.cliente_id = ?
                ORDER BY chc.prioridad, c.apellido1, c.nombre
                """, (rs, rowNum) -> new ClienteComercial(
                        rs.getInt("cliente_id"),
                        new Comercial(
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("apellido1"),
                                rs.getString("apellido2"),
                                rs.getObject("comisión", Float.class)),
                        rs.getObject("fecha", LocalDate.class),
                        rs.getInt("prioridad")),
                clienteId);

        log.info("Devueltos {} vínculos de comerciales para el cliente {}.", vinculos.size(), clienteId);
        return vinculos;
    }

    @Override
    public void create(int clienteId, int comercialId, LocalDate fecha, int prioridad) {
        jdbcTemplate.update("""
                INSERT INTO cliente_has_comercial (cliente_id, comercial_id, fecha, prioridad)
                VALUES (?, ?, ?, ?)
                """, clienteId, comercialId, fecha, prioridad);
    }

    @Override
    public void delete(int clienteId, int comercialId) {
        int rows = jdbcTemplate.update("""
                DELETE FROM cliente_has_comercial
                WHERE cliente_id = ? AND comercial_id = ?
                """, clienteId, comercialId);
        log.info("Eliminado {} vínculo del cliente {} con el comercial {}.", rows, clienteId, comercialId);
    }
}
