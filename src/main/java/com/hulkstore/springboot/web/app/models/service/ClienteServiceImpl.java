package com.hulkstore.springboot.web.app.models.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hulkstore.springboot.web.app.models.dao.IClienteDao;
import com.hulkstore.springboot.web.app.models.dao.IFacturaDao;
import com.hulkstore.springboot.web.app.models.dao.IProductoDao;
import com.hulkstore.springboot.web.app.models.entity.Cliente;
import com.hulkstore.springboot.web.app.models.entity.Factura;
import com.hulkstore.springboot.web.app.models.entity.Producto;

@Service
public class ClienteServiceImpl implements IClienteService {

	@Autowired
	private IClienteDao clienteDao;
	
	@Autowired
	private IProductoDao productoDao;
	
	@Autowired
	private IFacturaDao facturaDao;

	@Transactional()
	@Override
	public List<Cliente> findAll() {
		// TODO Auto-generated method stub
		return (List<Cliente>) clienteDao.findAll();
	}

	@Transactional()
	@Override
	public void save(Cliente cliente) {
		clienteDao.save(cliente);

	}

	@Transactional()
	@Override
	public Cliente findOne(Long id) {
		// TODO Auto-generated method stub
		return clienteDao.findById(id).orElse(null);
	}
	
	@Transactional()
	@Override
	public Cliente fetchByIdWithFacturas(Long id) {
		
		return clienteDao.fetchByIdWithFacturas(id);
	}

	@Transactional()
	@Override
	public void delete(Long id) {
		clienteDao.deleteById(id);

	}

	@Transactional()
	@Override
	public Page<Cliente> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return clienteDao.findAll(pageable);
	}

	@Override
	public List<Producto> finByNombre(String term) {
		// TODO Auto-generated method stub
		return productoDao.finByNombre(term);
	}


	@Transactional()
	@Override
	public void saveFactura(Factura factura) {
		facturaDao.save(factura);
		
	}

	@Transactional()
	@Override
	public Producto findProductoById(Long id) {
		// TODO Auto-generated method stub
		return productoDao.findById(id).orElse(null);
	}

	@Transactional()
	@Override
	public Factura findFacturaById(Long id) {
		// TODO Auto-generated method stub
		return facturaDao.findById(id).orElse(null);
	}

	@Transactional()
	@Override
	public void deleteFactura(Long id) {
		facturaDao.deleteById(id);
		
	}

	@Transactional()
	@Override
	public Factura fetchByIdWhithClienteWhithItemFacturaWhithProducto(Long id) {
		// TODO Auto-generated method stub
		return facturaDao.fetchByIdWhithClienteWhithItemFacturaWhithProducto(id);
	}

	
}
