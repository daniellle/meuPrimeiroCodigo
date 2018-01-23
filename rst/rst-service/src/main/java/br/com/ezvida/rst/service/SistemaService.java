package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.com.ezvida.girst.apiclient.client.SistemaClient;
import br.com.ezvida.girst.apiclient.model.Sistema;
import fw.core.service.BaseService;

@Stateless
public class SistemaService extends BaseService {

    private static final long serialVersionUID = 7316158036302487800L;
    
    @Inject
    private APIClientService apiClientService;
    
    @Inject
    private SistemaClient sistemaClient;
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Sistema> buscarSistemas() {
        return sistemaClient.buscar(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token());
    }

}
