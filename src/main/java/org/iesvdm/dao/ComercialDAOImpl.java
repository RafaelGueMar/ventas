package org.iesvdm.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import org.iesvdm.modelo.Comercial;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ComercialDAOImpl implements ComercialDAO {

	private final JdbcTemplate jdbcTemplate;

	ComercialDAOImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public synchronized void create(Comercial comercial) {
		String sqlInsert = """
				INSERT INTO comercial (nombre, apellido1, apellido2, comisión)
				VALUES (?, ?, ?, ?)
				""";

		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rows = jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sqlInsert, new String[] { "id" });
			ps.setString(1, comercial.getNombre());
			ps.setString(2, comercial.getApellido1());
			ps.setString(3, comercial.getApellido2());
			ps.setFloat(4, comercial.getComision());
			return ps;
		}, keyHolder);

		comercial.setId(keyHolder.getKey().intValue());
		log.info("Insertados {} registros de comercial.", rows);
	}

	@Override
	public List<Comercial> getAll() {
		List<Comercial> comerciales = jdbcTemplate.query(
				"SELECT * FROM comercial",
				(rs, rowNum) -> new Comercial(
						rs.getInt("id"),
						rs.getString("nombre"),
						rs.getString("apellido1"),
						rs.getString("apellido2"),
						rs.getFloat("comisión")));

		log.info("Devueltos {} comerciales.", comerciales.size());
		return comerciales;
	}

	@Override
	public Optional<Comercial> find(int id) {
		List<Comercial> comerciales = jdbcTemplate.query(
				"SELECT * FROM comercial WHERE id = ?",
				(rs, rowNum) -> new Comercial(
						rs.getInt("id"),
						rs.getString("nombre"),
						rs.getString("apellido1"),
						rs.getString("apellido2"),
						rs.getFloat("comisión")),
				id);

		return comerciales.stream().findFirst();
	}

	@Override
	public void update(Comercial comercial) {
		int rows = jdbcTemplate.update("""
				UPDATE comercial SET
					nombre = ?,
					apellido1 = ?,
					apellido2 = ?,
					comisión = ?
				WHERE id = ?
				""", comercial.getNombre(), comercial.getApellido1(), comercial.getApellido2(),
				comercial.getComision(), comercial.getId());

		log.info("Update de Comercial con {} registros actualizados.", rows);
	}

	@Override
	public void delete(int id) {
		int rows = jdbcTemplate.update("DELETE FROM comercial WHERE id = ?", id);
		log.info("Delete de Comercial con {} registros eliminados.", rows);
	}
}