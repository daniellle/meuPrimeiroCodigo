package br.com.ezvida.rst.dao.filter;

import br.com.ezvida.rst.model.pagination.Sort;

import java.util.List;

public class ListaPaginada<T> {

	private Long quantidade;
	private List<T> list;

	public ListaPaginada(Long quantidade, List<T> list ) {
		this.quantidade = quantidade;
		this.list = list;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

}
