package br.com.ezvida.rst.enums;

public interface CodeableEnum {
	
	public CodeableEnum getByCodigo(String codigo);
	
	public CodeableEnum getByCodigoJson(String codigoJson);
	
	public String getCodigo();
	
	public String getCodigoJson();
	

}
