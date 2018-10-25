package br.com.ezvida.rst.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.ezvida.girst.apiclient.model.Credencial;
import br.com.ezvida.girst.apiclient.model.Token;
import fw.core.service.BaseService;

@Stateless
public class APIClientService extends BaseService {

	private static final long serialVersionUID = 4803857682047598134L;
	
	@Inject
	private ParametroService parametroService;
	
	public Credencial getCredencial() {
        Credencial credencial = new Credencial();
		credencial.setUsuario(parametroService.getUsuarioApiGirst());
		credencial.setSenha(parametroService.getSenhaApiGirst());
        
        return credencial;
	}
	
	public Token getOAuthToken() {
		Token oauthToken = new Token();
		oauthToken.setAccess_token(parametroService.getAccessToken());
		oauthToken.setRefresh_token(parametroService.getRefreshToken());
		oauthToken.setToken_type(parametroService.getTokenType());
        return oauthToken;
	}

	public String getURL() {
		 return parametroService.getUrlApiGirst();
	}

	public String getSistema() {
		return parametroService.getCodigoSistemaCadastro();
	}

}
