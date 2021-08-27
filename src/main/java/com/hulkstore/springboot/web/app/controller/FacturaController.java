package com.hulkstore.springboot.web.app.controller;

import com.hulkstore.springboot.web.app.models.entity.Cliente;
import com.hulkstore.springboot.web.app.models.entity.Factura;
import com.hulkstore.springboot.web.app.models.entity.ItemFactura;
import com.hulkstore.springboot.web.app.models.entity.Producto;
import com.hulkstore.springboot.web.app.models.service.IClienteService;
import com.hulkstore.springboot.web.app.models.service.ProductoServiceImpl;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private ProductoServiceImpl productoServiceImpl;

	@GetMapping("/form/{clienteId}")
	public String crear(@PathVariable(value = "clienteId") Long clienteId, Map<String, Object> model,
			RedirectAttributes flash, SessionStatus status) {
		try {

			Cliente cliente = clienteService.findOne(clienteId);
			if (cliente == null) {
				flash.addAttribute("error", "El cliente no existe en la base de datos");
				return "redirect:/listar";
			}
			Factura factura = new Factura();
			factura.setCliente(cliente);
			status.setComplete();
			model.put("factura", factura);
			model.put("titulo", "Crear factura");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "factura/form";
	}

	@GetMapping("/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash,
			SessionStatus status) {

		try {

			Factura factura = clienteService.fetchByIdWhithClienteWhithItemFacturaWhithProducto(id);

			if (factura == null) {
				flash.addFlashAttribute("error", "La factura no existe en la base de datos");
				return "redirect:/listar";
			}

			status.setComplete();
			model.addAttribute("factura", factura);
			model.addAttribute("titulo", "Factura : ".concat(factura.getDescripcion()));

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "factura/ver";
	}

	@GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
	public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
		try {

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return clienteService.finByNombre(term);
	}

	@PostMapping("/form")
	public String guardar(@Valid Factura factura, BindingResult result, Model model,
			@RequestParam(name = "item_id[]", required = false) Long[] itemId,
			@RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, RedirectAttributes flash,
			SessionStatus status) {
		try {

			if (result.hasErrors()) {
				model.addAttribute("titulo", "Crear Factura");
				return "factura/form";
			}

			if (itemId == null || itemId.length == 0) {
				model.addAttribute("titulo", "Crear Factura");
				model.addAttribute("error", "La factura no puede no tener lineas!");
				return "factura/form";
			}

			for (int i = 0; i < itemId.length; i++) {
				Producto producto = clienteService.findProductoById(itemId[i]);

				ItemFactura linea = new ItemFactura();
				linea.setCantidad(cantidad[i]);
				linea.setProducto(producto);
				factura.addItemFactura(linea);
				if (!factura.getItems().isEmpty()) {
					producto = productoServiceImpl.findOne(producto.getId());
					int cantidadM = producto.getCantidad() - cantidad[i];
					producto.setCantidad(cantidadM);

					productoServiceImpl.save(producto);
				}
			}
			Cliente cliente = clienteService.findOne(factura.getCliente().getId());
			factura.setCliente(cliente);

			clienteService.saveFactura(factura);

			status.setComplete();
			flash.addFlashAttribute("success", "Factura creada con exito");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "redirect:/ver/" + factura.getCliente().getId();
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		try {

			Factura factura = clienteService.findFacturaById(id);
			if (factura != null) {

				clienteService.deleteFactura(id);
				flash.addFlashAttribute("success", "Factura eliminado con exito");

				return "redirect:/ver/" + factura.getCliente().getId();
			}
			flash.addFlashAttribute("error", "La factura no esxiste en la base de datos");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "redirect:/listar";
	}

}
