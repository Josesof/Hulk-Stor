package com.hulkstore.springboot.web.app.models.dao;

import java.util.List;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.hulkstore.springboot.web.app.models.entity.Producto;

public interface IProductoDao extends  PagingAndSortingRepository<Producto, Long>{

	//Esta consulta no se hace a nivel de base de datos, se realiza a nivel de objeto entidad
	//la p se utiliza como parametro
	@Query("select p from Producto p where p.nombre like %?1%")
	public List<Producto> finByNombre(String term);


}
