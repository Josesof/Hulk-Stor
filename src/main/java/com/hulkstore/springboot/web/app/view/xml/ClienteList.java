package com.hulkstore.springboot.web.app.view.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.hulkstore.springboot.web.app.models.entity.Cliente;


@XmlRootElement(name="clientes")
public class ClienteList {
	
	@XmlElement(name="cliente")
	public List<Cliente> clientes;

	public ClienteList() {
		
		// TODO Auto-generated constructor stub
	}

	public ClienteList(List<Cliente> clientes) {
		
		this.clientes = clientes;
	}
	
	

	public List<Cliente> getClientes() {
		return clientes;
	}
	
	

}
