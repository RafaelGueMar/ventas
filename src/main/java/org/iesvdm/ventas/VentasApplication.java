package org.iesvdm.ventas;


import java.util.Optional;

import org.iesvdm.dao.ClienteDAO;
import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.modelo.Cliente;
import org.iesvdm.modelo.Comercial;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner; //interfaz que permite ejecutar código al iniciar la aplicación Spring Boot. Se implementa el método run().

@SpringBootApplication(scanBasePackages = "org.iesvdm") // indica cual es la clase que inicia la aplicación Spring Boot. Esta anotación
						// es un atajo de @Configuration, @EnableAutoConfiguration y @ComponentScan.
@Slf4j
public class VentasApplication implements CommandLineRunner {

	@Autowired
	private ClienteDAO clienteDAO;

	@Autowired
	private ComercialDAO comercialDAO;

	public static void main(String[] args) {
		SpringApplication.run(VentasApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("***********");
		log.info("Prueba de arranque de la aplicación Spring Boot");
		log.info("***********");

		clienteDAO.getAll().forEach(c -> log.info("Cliente: {}", c));

		int id = 1;
		Optional<Cliente> cliente = clienteDAO.find(id);
		if (cliente.isPresent()) {
			log.info("Cliente con id {}: {}", id, cliente.get());

			String nombreOld = cliente.get().getNombre();

			cliente.get().setNombre("Jose M");

			clienteDAO.update(cliente.get());

			log.info("Cliente con id {} actualizado: {}", id, clienteDAO.find(id).get());
			
			clienteDAO.getAll().forEach(client -> log.info("Cliente: {}", client));

			cliente.get().setNombre(nombreOld);

			clienteDAO.update(cliente.get());
			
			log.info("Cliente con id {} restaurado: {}", id, clienteDAO.find(id).get());
		} else {
			log.info("No se encontró cliente con id {}", id);
		}

		log.info("Prueba CRUD de ComercialDAO");
		Comercial comercial = new Comercial(0, "Prueba", "DAO", "Spring", 0.1f);
		comercialDAO.create(comercial);
		int comercialId = comercial.getId();
		log.info("Comercial creado: {}", comercialDAO.find(comercialId).orElseThrow());

		comercial.setComision(0.12f);
		comercialDAO.update(comercial);
		log.info("Comercial actualizado: {}", comercialDAO.find(comercialId).orElseThrow());
		comercialDAO.getAll().forEach(c -> log.info("Comercial: {}", c));

		comercialDAO.delete(comercialId);
		if (comercialDAO.find(comercialId).isPresent()) {
			throw new IllegalStateException("No se pudo eliminar el comercial de prueba");
		}
		log.info("Comercial {} eliminado correctamente.", comercialId);

	}
}
