package com.hulkstore.springboot.web.app.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.hulkstore.springboot.web.app.models.entity.Producto;
import com.hulkstore.springboot.web.app.models.service.IUploadFileService;
import com.hulkstore.springboot.web.app.models.service.ProductoServiceImpl;
import com.hulkstore.springboot.web.app.util.paginator.PageRender;

@Controller
@SessionAttributes("producto")
public class ProductoController {

	@Autowired
	private ProductoServiceImpl productoServiceImpl;

	@Autowired
	private IUploadFileService uploadFileService;

	@Autowired
	private MessageSource messageSource;

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/uploadsP/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;
		try {
			recurso = uploadFileService.load(filename);

		} catch (MalformedURLException e) {

			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

	@GetMapping(value = "/verProducto/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		try {

			Producto producto = productoServiceImpl.findOne(id);
			if (producto == null) {
				flash.addAttribute("error", "El Producto no existe en la base de datos");
				return "redirect:/listar";
			}
			model.put("producto", producto);
			model.put("titulo", "Detalle del Producto: " + producto.getNombre());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "verProducto";
	}

	@RequestMapping(value = { "/listaProductos" }, method = RequestMethod.GET)
	public String listarProducto(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Locale locale) {
		Pageable pageRequest = PageRequest.of(page, 5);
		try {

			Page<Producto> productos = productoServiceImpl.findAll(pageRequest);
			PageRender<Producto> pageRender = new PageRender<>("/listaProductos", productos);
			model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));

			model.addAttribute("productos", productos);
			model.addAttribute("page", pageRender);

		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

		return "listaProductos";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/producto", method = RequestMethod.GET)
	public String crearProducto(Map<String, Object> model) {

		Producto producto = new Producto();
		try {

			model.put("producto", producto);
			model.put("titulo", "Formulario de producto");

		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

		return "producto";
	}

	@RequestMapping(value = "/producto/{id}")
	public String editarP(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Producto producto = null;
		try {

			if (id > 0) {
				producto = productoServiceImpl.findOne(id);
				if (producto == null) {
					flash.addFlashAttribute("error", "El producto no existe en la base de datos");
				}
			} else {
				flash.addFlashAttribute("error", "El ID del producto no puede ser Cero");
				return "redirect:listarProductos";

			}
			model.put("producto", producto);
			model.put("titulo", "Editar cliente");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "producto";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/producto", method = RequestMethod.POST)
	public String guardarP(@Valid @ModelAttribute("producto") Producto producto, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

		try {

			if (result.hasErrors()) {
				model.addAttribute("titulo", "Formulario de producto");
				return "producto";
			}

			if (!foto.isEmpty()) {

				if (producto.getId() != null && producto.getId() > 0 && producto.getFoto() != null
						&& producto.getFoto().length() > 0) {

					uploadFileService.delet(producto.getFoto());
				}

				String uniqueFilename = null;
				try {
					uniqueFilename = uploadFileService.copy(foto);
				} catch (IOException e) {
					e.printStackTrace();
				}

				flash.addFlashAttribute("info", "Has subido correctamente: " + uniqueFilename + "'");
				producto.setFoto(uniqueFilename);
			}

			String mensajeFlash = (producto.getId() != null) ? "Producto editado con exito"
					: "Producto creado con exito!";

			productoServiceImpl.save(producto);

			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);

		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

		return "redirect:listaProductos";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminarP/{id}")
	public String eliminarP(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		try {

			if (id > 0) {
				Producto producto = productoServiceImpl.findOne(id);
				productoServiceImpl.deleteProducto(id);
				flash.addFlashAttribute("info", "Producto eliminado con exito");
				uploadFileService.delet(producto.getFoto());
			}

		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

		return "redirect:/listarProductos";

	}

}
