package org.iesvdm.servicios;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.iesvdm.dao.ComercialDAO;
import org.iesvdm.dao.PedidoDAO;
import org.iesvdm.dto.ClienteTotalPedidosDTO;
import org.iesvdm.dto.EstadisticasPedidosDTO;
import org.iesvdm.modelo.Comercial;
import org.iesvdm.modelo.PedidoDetalle;
import org.springframework.stereotype.Service;

@Service
public class ComercialService {

	private final ComercialDAO comercialDAO;
	private final PedidoDAO pedidoDAO;

	public ComercialService(ComercialDAO comercialDAO, PedidoDAO pedidoDAO) {
		this.comercialDAO = comercialDAO;
		this.pedidoDAO = pedidoDAO;
	}

	public List<Comercial> listAll() {
		return comercialDAO.getAll();
	}

	public List<PedidoDetalle> listPedidosByComercial(int comercialId) {
		return pedidoDAO.findByComercialId(comercialId);
	}

	public List<ClienteTotalPedidosDTO> calcularTotalesPorCliente(List<PedidoDetalle> pedidos) {
		Map<Integer, ClienteTotalPedidosDTO> totalesPorCliente = new LinkedHashMap<>();
		for (PedidoDetalle pedido : pedidos) {
			ClienteTotalPedidosDTO cliente = totalesPorCliente.computeIfAbsent(
					pedido.getClienteId(),
					id -> new ClienteTotalPedidosDTO(id, pedido.getClienteNombre(), 0.0));
			cliente.setTotalPedidos(cliente.getTotalPedidos() + pedido.getTotal());
		}
		return new ArrayList<>(totalesPorCliente.values()).stream()
				.sorted(Comparator.comparingDouble(ClienteTotalPedidosDTO::getTotalPedidos).reversed()
						.thenComparing(ClienteTotalPedidosDTO::getClienteNombre,
								Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)))
				.toList();
	}

	public EstadisticasPedidosDTO calcularEstadisticasPedidos(List<PedidoDetalle> pedidos) {
		long totalPedidos = pedidos.size();
		double mediaPedidos = pedidos.stream()
				.mapToDouble(PedidoDetalle::getTotal)
				.average()
				.orElse(0.0);
		double maximoPedido = pedidos.stream()
				.mapToDouble(PedidoDetalle::getTotal)
				.max()
				.orElse(0.0);
		double minimoPedido = pedidos.stream()
				.mapToDouble(PedidoDetalle::getTotal)
				.min()
				.orElse(0.0);
		return new EstadisticasPedidosDTO(totalPedidos, mediaPedidos, maximoPedido, minimoPedido);
	}

	public Comercial one(Integer id) {
		Optional<Comercial> comercial = comercialDAO.find(id);
		return comercial.orElse(null);
	}

	public void newComercial(Comercial comercial) {
		comercialDAO.create(comercial);
	}

	public void replaceComercial(Comercial comercial) {
		comercialDAO.update(comercial);
	}

	public void deleteComercial(int id) {
		comercialDAO.delete(id);
	}
}