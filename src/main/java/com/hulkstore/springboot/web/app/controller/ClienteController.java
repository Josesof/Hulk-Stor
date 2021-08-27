package com.hulkstore.springboot.web.app.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.hulkstore.springboot.web.app.models.entity.Cliente;
import com.hulkstore.springboot.web.app.models.service.IClienteService;
import com.hulkstore.springboot.web.app.models.service.IUploadFileService;
import com.hulkstore.springboot.web.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadFileService uploadFileService;

	@Autowired
	private MessageSource messageSource;

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/uploads/{filename:.+}")
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

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		try {

			Cliente cliente = clienteService.fetchByIdWithFacturas(id);
			if (cliente == null) {
				flash.addAttribute("error", "El cliente no existe en la base de datos");
				return "redirect:/listar";
			}
			model.put("cliente", cliente);
			model.put("titulo", "Detalle del cliente: " + cliente.getNombre());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "ver";
	}

	@RequestMapping(value = { "", "/listar" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model, Locale locale) {
		Pageable pageRequest = PageRequest.of(page, 5);

		try {
			Page<Cliente> clientes = clienteService.findAll(pageRequest);
			PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
			model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));
			model.addAttribute("clientes", clientes);
			model.addAttribute("page", pageRender);

		} catch (Exception e) {

			System.out.println(e.getMessage());
		}

		return "listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		try {

			model.put("cliente", cliente);
			model.put("titulo", "Formulario de cliente");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "form";
	}

	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;
		try {

			if (id > 0) {
				cliente = clienteService.findOne(id);
				if (cliente == null) {
					flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
				}
			} else {
				flash.addFlashAttribute("error", "El ID del cliente no puede ser Cero");
				return "redirect:listar";

			}
			model.put("cliente", cliente);
			model.put("titulo", "Editar cliente");

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "form";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid @ModelAttribute("cliente") Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

		try {

			if (result.hasErrors()) {
				model.addAttribute("titulo", "Formulario de cliente");
				return "form";
			}

			if (!foto.isEmpty()) {

				if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
						&& cliente.getFoto().length() > 0) {

					uploadFileService.delet(cliente.getFoto());
				}

				String uniqueFilename = null;
				try {
					uniqueFilename = uploadFileService.copy(foto);
				} catch (IOException e) {

					e.printStackTrace();
				}

				flash.addFlashAttribute("info", "Has subido correctamente: " + uniqueFilename + "'");

				cliente.setFoto(uniqueFilename);
			}

			String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con exito" : "Cliente creado con exito!";
			clienteService.save(cliente);

			status.setComplete();
			flash.addFlashAttribute("success", mensajeFlash);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "redirect:listar";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/eliminarProducto/{id}")
	public String eliminarProducto(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		try {

			if (id > 0) {
				Cliente cliente = clienteService.findOne(id);

				clienteService.delete(id);
				flash.addFlashAttribute("info", "Cliente eliminado con exito");

				uploadFileService.delet(cliente.getFoto());

			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return "redirect:/listar";
	}

	@SuppressWarnings("unused")
	private boolean hasRole(String role) {

		try {
			SecurityContext contex = SecurityContextHolder.getContext();

			if (contex == null) {
				return false;
			}

			Authentication auth = contex.getAuthentication();
			if (auth == null) {
				return false;
			}

			Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

			return authorities.contains(new SimpleGrantedAuthority(role));

		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}

	}

}
