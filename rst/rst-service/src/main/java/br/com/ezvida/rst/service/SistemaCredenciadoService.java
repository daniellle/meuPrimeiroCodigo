package br.com.ezvida.rst.service;

import br.com.ezvida.girst.apiclient.client.SistemaCredenciadoClient;
import br.com.ezvida.girst.apiclient.model.ListaPaginada;
import br.com.ezvida.girst.apiclient.model.SistemaCredenciado;
import br.com.ezvida.girst.apiclient.model.filter.SistemaCredenciadoFilter;
import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.enums.EntidadeEnum;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @Inject
    private EmpresaService empresaService;

    @Inject
    private OrigemDadosService origemDadosService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SistemaCredenciadoService.class);

    public String cadastrar(SistemaCredenciado sistemaCredenciado, DadosFilter dados
            , ClienteAuditoria auditoria) {

        LogAuditoria.registrar(LOGGER, auditoria, "Cadastro de Sistema  Credenciado:" + sistemaCredenciado);
        if (valido(sistemaCredenciado)) {
            sistemaCredenciado.setCnpj(StringUtil.removeCaracteresEspeciais(sistemaCredenciado.getCnpj()));
            sistemaCredenciado.setTelefoneResponsavel(StringUtil.removeCaracteresEspeciais(sistemaCredenciado.getTelefoneResponsavel()));
            validarSistemaEntidade(sistemaCredenciado);
            return sistemaCredenciadoClient.cadastrar(apiClientService.getURL(), sistemaCredenciado, apiClientService.getOAuthToken().getAccess_token());
        } else {
            LOGGER.debug("Sistema Credenciado com dados inválidos.");
            throw new BusinessException("Sistema Credenciado com dados inválidos.");
        }
    }

    public String alterar(SistemaCredenciado sistemaCredenciado, DadosFilter dados
            , ClienteAuditoria auditoria) {

        LogAuditoria.registrar(LOGGER, auditoria, "Alteracao de Sistema  Credenciado:" + sistemaCredenciado);
        sistemaCredenciado.setCnpj(StringUtil.removeCaracteresEspeciais(sistemaCredenciado.getCnpj()));
        sistemaCredenciado.setTelefoneResponsavel(StringUtil.removeCaracteresEspeciais(sistemaCredenciado.getTelefoneResponsavel()));
        return sistemaCredenciadoClient.alterar(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), sistemaCredenciado);
    }

    public String ativarDesativar(SistemaCredenciado sistemaCredenciado, DadosFilter dados
            , ClienteAuditoria auditoria) {

        LogAuditoria.registrar(LOGGER, auditoria, "Ativar Desativar Sistema Credenciado:" + sistemaCredenciado);
        sistemaCredenciado.setCnpj(StringUtil.removeCaracteresEspeciais(sistemaCredenciado.getCnpj()));
        return sistemaCredenciadoClient.ativarDesativar(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), sistemaCredenciado);
    }

    public String resetarClientSecret(SistemaCredenciado sistemaCredenciado) {
        sistemaCredenciado.setCnpj(StringUtil.removeCaracteresEspeciais(sistemaCredenciado.getCnpj()));
        return sistemaCredenciadoClient.resetarClientSecret(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), sistemaCredenciado);
    }


    public SistemaCredenciado findById(String id, DadosFilter dados
            , ClienteAuditoria auditoria) {
        LogAuditoria.registrar(LOGGER, auditoria, "Buscar por id:" + id);
        SistemaCredenciado sistemaCredenciado = sistemaCredenciadoClient.buscarPorId(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), id);
        return sistemaCredenciado;
    }

    public ListaPaginada<SistemaCredenciado> pesquisarPaginado(SistemaCredenciadoFilter filter, DadosFilter dados
            , ClienteAuditoria auditoria) {

        LogAuditoria.registrar(LOGGER, auditoria, "Pesquisando sistema credenciado: " + filter);
        if (filter.getCnpj() != null) {
            filter.setCnpj(StringUtil.removeCaracteresEspeciais(filter.getCnpj()));
        }
        return sistemaCredenciadoClient.pesquisar(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), filter);
    }

    private boolean valido(SistemaCredenciado sistemaCredenciado) {
        return sistemaCredenciado != null &&
                ValidadorUtils.isValidCNPJ(StringUtil.removeCaracteresEspeciais(sistemaCredenciado.getCnpj())) &&
                StringUtils.isNotBlank(sistemaCredenciado.getEmailResponsavel()) &&
                StringUtils.isNotBlank(sistemaCredenciado.getNomeResponsavel()) &&
                sistemaCredenciado.getEntidade() != null &&
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


    public void validarSistemaEntidade(SistemaCredenciado sistemaCredenciado) {
        BigInteger result = origemDadosService.countByDescricao(sistemaCredenciado.getSistema());
        boolean empty = result.compareTo(BigInteger.ZERO) == 0;
        if (!EntidadeEnum.EMPRESA.getValue().equals(sistemaCredenciado.getEntidade())) {
            if (empty) {
                throw new BusinessException(getMensagem("app_rst_sistema_invalido_origem_dados"));
            }
        } else {
            if (!empty) {
                throw new BusinessException(getMensagem("app_rst_sistema_invalido_origem_dados"));
            }
        }
    }

}
