package com.hulkstore.springboot.web.app.models.service;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.hulkstore.springboot.web.app.models.dao.IProductoDao;
import com.hulkstore.springboot.web.app.models.entity.Producto;

@Service
public class ProductoServiceImpl  implements IProductoService{
	
	@Autowired
	private IProductoDao productoDao;

	@Override
	public List<Producto> findAll() {
		
		return (List<Producto>) productoDao.findAll();
	}

	@Override
	public void save(Producto producto) {
		productoDao.save(producto);
		
	}

	@Override
	public Producto findOne(Long id) {
		
		return productoDao.findById(id).orElse(null);
	}

	@Override
	public void deleteProducto(Long id) {
		
		productoDao.deleteById(id);
	}

	@Override
	public Page<Producto> findAll(Pageable pageable) {
		
		return productoDao.findAll(pageable);
	}
	


}
