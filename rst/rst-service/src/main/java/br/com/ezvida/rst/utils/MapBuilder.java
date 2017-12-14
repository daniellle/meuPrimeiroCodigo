package br.com.ezvida.rst.utils;

import java.util.HashMap;
import java.util.Map;

public final class MapBuilder<K, V> {

	private Map<K, V> mapa;

	private MapBuilder(){
		this.mapa = new HashMap<K, V>();
	}
	public static <K, V> MapBuilder<K, V> criar(K chave, V valor){
		MapBuilder<K, V> builder = new MapBuilder<>();
		builder.mapa.put(chave, valor);
		return builder;
	}
	public MapBuilder<K,V> incluir(K chave, V valor){
		this.mapa.put(chave, valor);
		return this;
	}

	public MapBuilder<K,V> remove(K chave){
		this.mapa.remove(chave);
		return this;
	}		

	public Map<K, V> build(){
		return this.mapa;
	}
}

