package org.iesvdm.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Pedido;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PedidoDAOImpl implements PedidoDAO {

    private final JdbcTemplate jdbcTemplate;

    private static final String PEDIDO_DETALLE_SELECT = """
            SELECT p.id, p.total, p.fecha, p.id_cliente, p.id_comercial,
                   CONCAT_WS(' ', c.nombre, c.apellido1, c.apellido2) AS cliente_nombre,
                   CONCAT_WS(' ', co.nombre, co.apellido1, co.apellido2) AS comercial_nombre
            FROM pedido p
            JOIN cliente c ON c.id = p.id_cliente
            JOIN comercial co ON co.id = p.id_comercial
            """;

    PedidoDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public synchronized void create(Pedido pedido) {
        String sqlInsert = """
                INSERT INTO pedido (total, fecha, id_cliente, id_comercial)
                VALUES (?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] {"id"});
            ps.setDouble(1, pedido.getTotal());
            ps.setObject(2, pedido.getFecha());
            ps.setObject(3, pedido.getClienteId());
            ps.setObject(4, pedido.getComercialId());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() == null) {
            throw new IllegalStateException("No se pudo obtener el id del pedido creado");
        }
        pedido.setId(keyHolder.getKey().intValue());
        log.info("Insertados {} pedidos.", rows);
    }

    @Override
    public List<Pedido> getAll() {
        List<Pedido> pedidos = jdbcTemplate.query(
                "SELECT id, total, fecha, id_cliente, id_comercial FROM pedido ORDER BY id",
                (rs, rowNum) -> new Pedido(
                        rs.getInt("id"),
                        rs.getDouble("total"),
                        rs.getObject("fecha", java.time.LocalDate.class),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_comercial")));

        log.info("Devueltos {} pedidos.", pedidos.size());
        return pedidos;
    }

    @Override
    public Optional<Pedido> find(int id) {
        List<Pedido> pedidos = jdbcTemplate.query(
                "SELECT id, total, fecha, id_cliente, id_comercial FROM pedido WHERE id = ?",
                (rs, rowNum) -> new Pedido(
                        rs.getInt("id"),
                        rs.getDouble("total"),
                        rs.getObject("fecha", java.time.LocalDate.class),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_comercial")),
                id);

        return pedidos.stream().findFirst();
    }

    @Override
    public List<PedidoDetalle> getAllDetalles() {
        List<PedidoDetalle> pedidos = jdbcTemplate.query(
                PEDIDO_DETALLE_SELECT + " ORDER BY p.id",
                (rs, rowNum) -> mapPedidoDetalle(rs));

        log.info("Devueltos {} pedidos con nombres relacionados.", pedidos.size());
        return pedidos;
    }

    @Override
    public Optional<PedidoDetalle> findDetalle(int id) {
        List<PedidoDetalle> pedidos = jdbcTemplate.query(
                PEDIDO_DETALLE_SELECT + " WHERE p.id = ?",
                (rs, rowNum) -> mapPedidoDetalle(rs),
                id);

        return pedidos.stream().findFirst();
    }

    private PedidoDetalle mapPedidoDetalle(java.sql.ResultSet rs) throws java.sql.SQLException {
        return new PedidoDetalle(
                rs.getInt("id"),
                rs.getDouble("total"),
                rs.getObject("fecha", java.time.LocalDate.class),
                rs.getInt("id_cliente"),
                rs.getInt("id_comercial"),
                rs.getString("cliente_nombre"),
                rs.getString("comercial_nombre"));
    }

    @Override
    public void update(Pedido pedido) {
        int rows = jdbcTemplate.update("""
                UPDATE pedido SET
                    total = ?,
                    fecha = ?,
                    id_cliente = ?,
                    id_comercial = ?
                WHERE id = ?
                """, pedido.getTotal(), pedido.getFecha(), pedido.getClienteId(), pedido.getComercialId(),
                pedido.getId());

        log.info("Update de Pedido con {} registros actualizados.", rows);
    }

    @Override
    public void delete(int id) {
        int rows = jdbcTemplate.update("DELETE FROM pedido WHERE id = ?", id);
        log.info("Delete de Pedido con {} registros eliminados.", rows);
    }
}
