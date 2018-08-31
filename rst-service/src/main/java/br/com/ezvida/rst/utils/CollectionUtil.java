package br.com.ezvida.rst.utils;

import java.util.List;

import com.google.common.collect.Lists;

public class CollectionUtil {

	public static List<Long> getIds(String ids) {
		String[] array = ids.split(",");
		List<Long> novaLista = Lists.newArrayList();
		for (String string : array) {
			novaLista.add(Long.valueOf(string));
		}

		return novaLista;
	}
}
