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
        StringBuilder query = new StringBuilder();
        if(listaDeCondicoes.size() == 1) {
            return query.append(listaDeCondicoes.get(0)).toString();
        }

        for (int i = 0; i < listaDeCondicoes.size(); i++) {
            if(i == 0) {
                query.append("(");
                query.append(listaDeCondicoes.get(i));
            } else if(i < listaDeCondicoes.size() - 2) {
                query.append(" and ");
                query.append(listaDeCondicoes.get(0));
            } else {
                query.append(" and ");
                query.append(listaDeCondicoes.get(i));
                query.append(")");
            }
        }
        return query.toString();
    }

    public Map<String, Object> getParametros() {
        return Collections.unmodifiableMap(parametros);
    }
}
