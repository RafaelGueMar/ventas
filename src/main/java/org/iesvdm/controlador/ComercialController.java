package org.iesvdm.controlador;

import org.iesvdm.modelo.Comercial;
import org.iesvdm.servicios.ComercialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ComercialController {

	@Autowired
	private ComercialService comercialService;

	@GetMapping("/comerciales")
	public String listar(Model model) {
		model.addAttribute("Comerciales", comercialService.listAll());
		return "comerciales";
	}

	@GetMapping("/comerciales/{id}")
	public String detalle(Model model, @PathVariable Integer id) {
		Comercial comercial = comercialService.one(id);
		if (comercial == null) {
			return "redirect:/comerciales";
		}
		model.addAttribute("comercial", comercial);
		model.addAttribute("pedidos", comercialService.listPedidosByComercial(id));
		return "detalle-comercial";
	}

	@GetMapping("/comerciales/crear")
	public String crear(Model model) {
		model.addAttribute("comercial", new Comercial());
		model.addAttribute("formAction", "/comerciales/crear");
		model.addAttribute("titulo", "Crear comercial");
		return "form-comercial";
	}

	@PostMapping("/comerciales/crear")
	public String submitCrear(@ModelAttribute("comercial") Comercial comercial) {
		comercialService.newComercial(comercial);
		return "redirect:/comerciales";
	}

	@GetMapping("/comerciales/editar/{id}")
	public String editar(Model model, @PathVariable Integer id) {
		Comercial comercial = comercialService.one(id);
		if (comercial == null) {
			return "redirect:/comerciales";
		}
		model.addAttribute("comercial", comercial);
		model.addAttribute("formAction", "/comerciales/editar/" + id);
		model.addAttribute("titulo", "Editar comercial");
		return "form-comercial";
	}

	@PostMapping("/comerciales/editar/{id}")
	public String submitEditar(@PathVariable Integer id, @ModelAttribute("comercial") Comercial comercial) {
		comercial.setId(id);
		comercialService.replaceComercial(comercial);
		return "redirect:/comerciales";
	}

	@PostMapping("/comerciales/borrar/{id}")
	public String submitBorrar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		try {
			comercialService.deleteComercial(id);
			redirectAttributes.addFlashAttribute("mensaje", "Comercial eliminado correctamente.");
			redirectAttributes.addFlashAttribute("tipoMensaje", "success");
		} catch (DataIntegrityViolationException e) {
			redirectAttributes.addFlashAttribute("mensaje",
					"No se puede eliminar este comercial porque tiene pedidos asociados.");
			redirectAttributes.addFlashAttribute("tipoMensaje", "warning");
		}
		return "redirect:/comerciales";
	}
}