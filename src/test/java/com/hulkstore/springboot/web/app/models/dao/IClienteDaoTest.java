
package com.hulkstore.springboot.web.app.models.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import com.hulkstore.springboot.web.app.models.entity.Cliente;
import com.hulkstore.springboot.web.app.models.entity.Factura;
import com.hulkstore.springboot.web.app.models.service.ClienteServiceImpl;

@SpringBootTest
public class IClienteDaoTest {

	@MockBean
	private IClienteDao iClienteDao;
	
	@Autowired
	ClienteServiceImpl clienteServiceImpl;
	
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
	
}
