package com.hulkstore.springboot.web.app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.hulkstore.springboot.web.app.models.entity.Cliente;
import com.hulkstore.springboot.web.app.models.entity.Factura;
import com.hulkstore.springboot.web.app.models.service.ClienteServiceImpl;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {
	
	@MockBean
	ClienteServiceImpl clienteServiceImpl;
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@Test
	void buscarFacturaId() throws Exception {
		
		Cliente cliente = new Cliente();
		Factura factura = new Factura();
		factura.setId(1L);
		List<Factura> listaFactura = new ArrayList<>();
		listaFactura.add(factura);
		
		cliente.setNombre("Julio");
		cliente.setApellido("Marin");
		cliente.setEmail("julio@gmail.com");
		cliente.setFoto("hjdhhfhf");
		cliente.setCreatAt(new Date());
		cliente.setFacturas(listaFactura);
		

	    when(clienteServiceImpl.fetchByIdWithFacturas(factura.getId())).thenReturn((Cliente) cliente);
		
		assertEquals(cliente, clienteServiceImpl.fetchByIdWithFacturas(factura.getId()));
	}
	
	@Test
	void ListarCliente() throws Exception {
		
		Cliente cliente = new Cliente();
		Factura factura = new Factura();
		factura.setId(1L);
		List<Cliente> listaCliente = new ArrayList<>();
		
		
		cliente.setNombre("Julio");
		cliente.setApellido("Marin");
		cliente.setEmail("julio@gmail.com");
		cliente.setFoto("hjdhhfhf");
		cliente.setCreatAt(new Date());
		listaCliente.add(cliente);
		
    
	    
		
		assertEquals(listaCliente, clienteServiceImpl.findAll());
	}

}
