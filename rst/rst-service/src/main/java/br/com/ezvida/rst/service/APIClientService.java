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
		//oauthToken.setAccess_token(parametroService.getAccessToken());
		oauthToken.setRefresh_token(parametroService.getRefreshToken());
		oauthToken.setToken_type(parametroService.getTokenType());

		//TODO: REMOVER QUANDO FOR PUBLICAR
		oauthToken.setAccess_token("eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJjYWRhc3RybyIsImtpZCI6Im1haW4iLCJpc3MiOiJnaXJzdCIsInBhcGVpcyI6WyJSRURFU1lTIl0sInRva2VuVHlwZSI6IkFDQ0VTU19UT0tFTiJ9.QGBu9ZdN59RSmY8lFHTWVVIPoBGpKbMG6KhvUU1FksONQqbGljaa75DlRC-k27n7VUPba_cg_eHhi7HGvryNddL6_Z-WBsGFd2fD1c98gDtwULUJfxRNiOz94Qf3keI7LH4AlHm9i2E0BDfd5Fa2DkiyGJ76EggkMu88NLfRoiOxXicfPKPzTTMR2moFSE-91BbyQNPNW7mDoqi88ABDY0-ch2aT6-Pamgvr6VlBO3SZAjkUbL4fMMRF60FTMNeQWUZEV4ymPkf57wPVCevXyv-FQjDRn-ubalex8mSCvxM_y5CsboJ5ikvRtt_Lho1qG5KDMLOXLFiAtNjYCdZlmQ");
		return oauthToken;
	}

	public String getURL() {
		/*return parametroService.getUrlApiGirst();*/

		//TODO: REMOVER QUANDO FOR PUBLICAR
		return "http://localhost:8080/girst/api/v1/";
	}

	public String getSistema() {
		return parametroService.getCodigoSistemaCadastro();
	}

}
