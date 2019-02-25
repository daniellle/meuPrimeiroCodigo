package br.com.ezvida.rst.filtrosbusca;

import java.util.*;

public class Filtro {

    private final List<String> listaDeCondicoes;
    private final Map<String, Object> parametros;

    public Filtro() {
        this.listaDeCondicoes = new ArrayList<>();
        this.parametros = new HashMap<>();
    }

    public void adicionaRestricao(String condicao, String parametro, Object valor) {
        this.listaDeCondicoes.add(condicao);
        this.parametros.put(parametro, valor);
    }

    public void adicionaRestricao(String condicao) {
        this.listaDeCondicoes.add(condicao);
    }

    public String getQuery() {
        // TODO
        return "";
    }

    public Map<String, Object> getParametros() {
        return Collections.unmodifiableMap(parametros);
    }
}
