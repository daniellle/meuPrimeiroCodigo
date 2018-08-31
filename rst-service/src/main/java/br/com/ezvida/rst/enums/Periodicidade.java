package br.com.ezvida.rst.enums;

public enum Periodicidade {
	
	// @formatter: off
	ULTIMO_MES("UMES", "Último Mês", 1l), ULTIMO_TRIMESTRE("UTRI", "Último Trimestre", 3l), ULTIMO_SEMESTRE("USEM", "Último Semestre",
			6l), ULTIMO_ANO("UANO", "Último Ano",
					12l), ULTIMOS_5_ANOS("U5AN", "Últimos 5 Anos", 60l), ULTIMOS_10_ANOS("U10A", "Últimos 10 Anos", 120l);
	// @formatter: on

	private String codigo;
	private String descricao;
	private long quantidadeMes;

	private Periodicidade(String codigo, String descricao, long quantidadeMes) {
		this.codigo = codigo;
		this.descricao = descricao;
		this.quantidadeMes = quantidadeMes;
	}
	
	public static Periodicidade getPeriodicidade(String codigo) {
		for (Periodicidade enumType : Periodicidade.values()) {
			if (enumType.codigo.equals(codigo)) {
				return enumType;
			}
		}
		return null;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public long getQuantidadeMes() {
		return quantidadeMes;
	}

}
