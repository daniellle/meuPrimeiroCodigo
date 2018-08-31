package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.com.ezvida.girst.apiclient.client.PerfilClient;
import br.com.ezvida.girst.apiclient.model.Perfil;
import fw.core.service.BaseService;

@Stateless
public class PerfilService extends BaseService {

    private static final long serialVersionUID = -184021763848509L;
    
    @Inject
    private APIClientService apiClientService;
    
    @Inject
    private PerfilClient perfilClient;
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<Perfil> buscarPerfis() {
        return perfilClient.buscar(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token());
    }

}
