package br.com.ezvida.rst.producer;

import br.com.ezvida.rst.anotacoes.Preferencial;
import br.com.ezvida.rst.anotacoes.Prod;
import br.com.ezvida.rst.enums.Ambiente;
import br.com.ezvida.rst.service.UsuarioService;
import br.com.ezvida.rst.service.UsuarioServiceDev;

import javax.enterprise.inject.Produces;

public class UsuarioProducer {


    @Produces
    @Preferencial
    public UsuarioService produceUsuarioService(Ambiente ambiente, @Prod UsuarioService service){
        if(ambiente == Ambiente.DESENVOLVIMENTO){
            return new UsuarioServiceDev();
        }else{
            return service;
        }

    }
}
