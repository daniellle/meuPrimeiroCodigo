package br.com.ezvida.rst.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dashboard implements Serializable {

    private static final long serialVersionUID = -5420490970292889913L;

    private final String titulo;

    private final String painel;

    private final String cor;

    private final String tipo;

    private final double valor;

    private final List<String> legendas = new ArrayList<>();

    private final List<Double> series = new ArrayList<>();

    public Dashboard(String titulo, String painel, String cor, String tipo, double valor) {
        this.titulo = titulo;
        this.painel = painel;
        this.cor = cor;
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getPainel() {
        return painel;
    }

    public String getCor() {
        return cor;
    }

    public String getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public List<String> getLegendas() {
        return legendas;
    }

    public List<Double> getSeries() {
        return series;
    }

    public Dashboard adicionar(String... legendas){

        Arrays.stream(legendas).forEach(legenda -> getLegendas().add(legenda));

        return this;

    }

    public Dashboard adicionar(Double... series) {

        Arrays.stream(series).forEach(serie -> getSeries().add(serie));

        return this;

    }

}