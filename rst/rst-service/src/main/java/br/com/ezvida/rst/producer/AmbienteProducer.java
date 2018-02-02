package br.com.ezvida.rst.producer;

import br.com.ezvida.rst.enums.Ambiente;

import javax.enterprise.inject.Produces;

public class AmbienteProducer {


    @Produces
    private Ambiente produceAmbiente(){
        if(System.getenv("SOLUTIS_DEV_ENV") != null){
            return Ambiente.DESENVOLVIMENTO;
        }else{
            return Ambiente.PRODUCAO;
        }
    }
}
