package br.com.ezvida.rst.producer;

import javax.enterprise.inject.Produces;

import br.com.ezvida.rst.anotacoes.Preferencial;
import br.com.ezvida.rst.anotacoes.Prod;
import br.com.ezvida.rst.enums.Ambiente;
import br.com.ezvida.rst.service.UsuarioService;

public class UsuarioProducer {


    @Produces
    @Preferencial
    public UsuarioService produceUsuarioService(Ambiente ambiente, @Prod UsuarioService service){
        if(ambiente == Ambiente.DESENVOLVIMENTO){
			// return new UsuarioServiceDev();
			return service;
        }else{
            return service;
        }

    }
}
