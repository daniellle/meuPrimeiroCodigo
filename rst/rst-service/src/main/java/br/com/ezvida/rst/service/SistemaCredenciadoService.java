package br.com.ezvida.rst.service;

import br.com.ezvida.girst.apiclient.client.CredencialClient;
import br.com.ezvida.girst.apiclient.client.SistemaCredenciadoClient;
import br.com.ezvida.girst.apiclient.model.SistemaCredenciado;
import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.utils.StringUtil;
import br.com.ezvida.rst.utils.ValidadorUtils;
import fw.core.exception.BusinessException;
import fw.core.service.BaseService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.math.BigInteger;
import java.util.Set;

@Stateless
public class SistemaCredenciadoService extends BaseService {

    @Inject
    private APIClientService apiClientService;

    @Inject
    private SistemaCredenciadoClient sistemaCredenciadoClient;

    @Inject
    private DepartamentoRegionalService departamentoRegionalService;

    @Inject
    private UnidadeAtendimentoTrabalhadorService unidadeAtendimentoTrabalhadorService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SistemaCredenciadoService.class);

    public String cadastrar(SistemaCredenciado sistemaCredenciado, DadosFilter dados
            , ClienteAuditoria auditoria) {

        LogAuditoria.registrar(LOGGER, auditoria, "Cadastro de Sistema  Credenciado:" + sistemaCredenciado);
        sistemaCredenciado.setCnpj(StringUtil.removeCaracteresEspeciais(sistemaCredenciado.getCnpj()));
        sistemaCredenciado.setTelefoneResponsavel(StringUtil.removeCaracteresEspeciais(sistemaCredenciado.getTelefoneResponsavel()));
        if (valido(sistemaCredenciado)) {
            if (validarFiltroDados(dados, auditoria, sistemaCredenciado)) {
                return sistemaCredenciadoClient.cadastrar(apiClientService.getURL(), sistemaCredenciado, apiClientService.getOAuthToken().getAccess_token());
            } else {
                throw new BusinessException("Não é possível cadastrar sistema credenciado para o CNPJ informado.");
            }
        } else {
            LOGGER.debug("Sistema Credenciado com dados inválidos.");
            throw new BusinessException("Sistema Credenciado com dados inválidos.");
        }
    }

    private boolean valido(SistemaCredenciado sistemaCredenciado) {
        return sistemaCredenciado != null &&
                ValidadorUtils.isValidCNPJ(sistemaCredenciado.getCnpj()) &&
                StringUtils.isNotBlank(sistemaCredenciado.getEmailResponsavel()) &&
                StringUtils.isNotBlank(sistemaCredenciado.getNomeResponsavel()) &&
                StringUtils.isNotBlank(sistemaCredenciado.getSistema()) &&
                ValidadorUtils.isValidEmail(sistemaCredenciado.getEmailResponsavel()) &&
                validarLengthTelefone(sistemaCredenciado.getTelefoneResponsavel());
    }

    private boolean validarLengthTelefone(String telefone) {
        boolean result = true;
        if (telefone != null) {
            StringUtil.removeCaracteresEspeciais(telefone);
            result = telefone.matches("^[0-9]{10,11}$");
        }
        return result;
    }

    public boolean validarFiltroDados(DadosFilter dados, ClienteAuditoria auditoria, SistemaCredenciado sistemaCredenciado) {
        boolean next = false;
        if (dados.isAdministrador() || dados.isGestorDn()) {
            next = true;
        } else if (dados.isGestorDr()) {
            Set<Long> listIdDepartamentosRegionais = dados.getIdsDepartamentoRegional();
            if (listIdDepartamentosRegionais != null && !listIdDepartamentosRegionais.isEmpty() && departamentoRegionalService.countByIdsAndCNPJ(listIdDepartamentosRegionais, StringUtil.removeCaracteresEspeciais(sistemaCredenciado.getCnpj())).compareTo(BigInteger.ZERO) > 0) {
                next = true;
            }
        } else if (dados.isGetorUnidadeSESI()) {
            Set<Long> listIdUnidades = dados.getIdsUnidadeSESI();
            if (listIdUnidades != null && !listIdUnidades.isEmpty() && unidadeAtendimentoTrabalhadorService.countByListIdAndCNPJ(listIdUnidades, StringUtil.removeCaracteresEspeciais(sistemaCredenciado.getCnpj())).compareTo(BigInteger.ZERO) > 0) {
                next = true;
            }
        }
        return next;
    }

}
