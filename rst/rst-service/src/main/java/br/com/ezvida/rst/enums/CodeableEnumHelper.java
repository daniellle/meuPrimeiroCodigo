package br.com.ezvida.rst.enums;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CodeableEnumHelper {
	
	private CodeableEnumHelper() { }

	public static <E extends CodeableEnum> Map<String,E> mapByCodigo(E[] values) {
		Map<String,E> map = new HashMap<String,E>();
		for (E e : values) {
			map.put(e.getCodigo().toLowerCase(Locale.ROOT), e);
		}
		return map;
	}

	public static <E extends CodeableEnum> Map<String,E> mapByCodigoJson(E[] values) {
		Map<String,E> map = new HashMap<String,E>();
		for (E e : values) {
			map.put(e.getCodigoJson().toLowerCase(Locale.ROOT), e);
		}
		return map;
	}
 
	public static <E extends CodeableEnum> E getByCodigoJson(String codigoJson, E[] values) {
		for (E e : values) {
			if (e.getCodigoJson().equalsIgnoreCase(codigoJson)) {
				return e;
			}
		}
		return null;
	}

}
