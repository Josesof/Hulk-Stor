package com.hulkstore.springboot.web.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;
	private Page<T> page;
	private int totalPaginas;
	private int numElementoPorPagina;
	private int paginaActual;
	private List<PageItem> paginas;

	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.paginas = new ArrayList<PageItem>();

		numElementoPorPagina = page.getSize();
		totalPaginas = page.getTotalPages();
		paginaActual = page.getNumber() + 1;
		int desde;
		int hasta;

		if (totalPaginas <= numElementoPorPagina) {
			desde = 1;
			hasta = totalPaginas;
		} else {
			if (paginaActual <= numElementoPorPagina / 2) {
				desde = 1;
				hasta = numElementoPorPagina;
			} else if (paginaActual >= totalPaginas - numElementoPorPagina / 2) {
				desde = totalPaginas - numElementoPorPagina + 1;
				hasta = numElementoPorPagina;
			} else {
				desde = paginaActual - numElementoPorPagina / 2;
				hasta = numElementoPorPagina;
			}
		}

		for (int i = 0; i < hasta; i++) {
			paginas.add(new PageItem(desde + i, paginaActual == desde + i));
		}
	}

	public String getUrl() {
		return url;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public int getPaginaActual() {
		return paginaActual;
	}

	public List<PageItem> getPaginas() {
		return paginas;
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}

	public boolean isHasPrevious() {
		return page.hasPrevious();
	}
}
