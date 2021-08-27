package com.hulkstore.springboot.web.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.hulkstore.springboot.web.app.models.entity.Producto;

public interface IProductoService {
	
    public List<Producto> findAll();
	
	public void save (Producto producto);
	
	public Producto findOne(Long id);
	
	public void deleteProducto(Long id);
	
	public Page<Producto> findAll(Pageable pageable);

}
