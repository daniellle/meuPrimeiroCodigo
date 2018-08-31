package br.com.ezvida.rst.producer;

import javax.enterprise.inject.Produces;

import br.com.ezvida.rst.enums.Ambiente;

public class AmbienteProducer {


    @Produces
    private Ambiente produceAmbiente(){
        if(System.getenv("SOLUTIS_DEV_ENV") != null){
			return Ambiente.DESENVOLVIMENTO;
        }
        else{
            return Ambiente.PRODUCAO;
        }
    }
}
