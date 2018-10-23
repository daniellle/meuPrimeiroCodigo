package br.com.ezvida.rst.service;

import br.com.ezvida.girst.apiclient.client.SistemaCredenciadoClient;
import br.com.ezvida.girst.apiclient.model.ListaPaginada;
import br.com.ezvida.girst.apiclient.model.SistemaCredenciado;
import br.com.ezvida.girst.apiclient.model.filter.SistemaCredenciadoFilter;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(SistemaCredenciadoService.class);

    public String cadastrar(SistemaCredenciado sistemaCredenciado, DadosFilter dados
            , ClienteAuditoria auditoria) {

        LogAuditoria.registrar(LOGGER, auditoria, "Cadastro de Sistema  Credenciado:" + sistemaCredenciado);
        if (valido(sistemaCredenciado)) {
            sistemaCredenciado.setCnpj(StringUtil.removeCaracteresEspeciais(sistemaCredenciado.getCnpj()));
            sistemaCredenciado.setTelefoneResponsavel(StringUtil.removeCaracteresEspeciais(sistemaCredenciado.getTelefoneResponsavel()));
            if (validarFiltroDados(dados, auditoria, sistemaCredenciado)) {
                return sistemaCredenciadoClient.cadastrar(apiClientService.getURL(), sistemaCredenciado, apiClientService.getOAuthToken().getAccess_token());
            } else {
                LOGGER.debug("Não é possível cadastrar sistema credenciado para o CNPJ informado.");
                throw new BusinessException("Não é possível cadastrar sistema credenciado para o CNPJ informado.");
            }
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
        if (validarFiltroDados(dados, auditoria, sistemaCredenciado)) {
            return sistemaCredenciadoClient.alterar(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), sistemaCredenciado);
        } else {
            LOGGER.debug("Não é possível alterar sistema credenciado.");
            throw new BusinessException("Não é possível alterar sistema credenciado.");
        }
    }

    public String ativarDesativar(SistemaCredenciado sistemaCredenciado, DadosFilter dados
            , ClienteAuditoria auditoria) {

        LogAuditoria.registrar(LOGGER, auditoria, "Ativar Desativar Sistema Credenciado:" + sistemaCredenciado);
        sistemaCredenciado.setCnpj(StringUtil.removeCaracteresEspeciais(sistemaCredenciado.getCnpj()));
        if (validarFiltroDados(dados, auditoria, sistemaCredenciado)) {
            return sistemaCredenciadoClient.ativarDesativar(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), sistemaCredenciado);
        } else {
            LOGGER.debug("Não é possível modificar status do sistema credenciado.");
            throw new BusinessException("Não é possível modificar status do sistema credenciado.");
        }
    }

    public String resetarClientSecret(SistemaCredenciado sistemaCredenciado) {
        sistemaCredenciado.setCnpj(StringUtil.removeCaracteresEspeciais(sistemaCredenciado.getCnpj()));
        return sistemaCredenciadoClient.resetarClientSecret(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), sistemaCredenciado);
    }


    public SistemaCredenciado findById(String id, DadosFilter dados
            , ClienteAuditoria auditoria) {
        LogAuditoria.registrar(LOGGER, auditoria, "Buscar por id:" + id);
        SistemaCredenciado sistemaCredenciado = sistemaCredenciadoClient.buscarPorId(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), id);
        if (!validarFiltroDados(dados, auditoria, sistemaCredenciado)) {
            LOGGER.debug("Sistema Credenciado não encontrado");
            sistemaCredenciado = null;
        }
        return sistemaCredenciado;
    }

    public ListaPaginada<SistemaCredenciado> pesquisarPaginado(SistemaCredenciadoFilter filter, DadosFilter dados
            , ClienteAuditoria auditoria) {

        LogAuditoria.registrar(LOGGER, auditoria, "Pesquisando sistema credenciado: " + filter);
        ListaPaginada<SistemaCredenciado> listaPaginada = new ListaPaginada<>(0l, new ArrayList<>());
        if (filter.getCnpj() != null) {
            filter.setCnpj(StringUtil.removeCaracteresEspeciais(filter.getCnpj()));
        }

        filter.setListCNPJ(new ArrayList<>());
        if (dados.isAdministrador() || dados.isGestorDn()) {
            return sistemaCredenciadoClient.pesquisar(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), filter);
        } else if (dados.isGestorDr()) {
            if (dados.getIdsDepartamentoRegional() != null && !dados.getIdsDepartamentoRegional().isEmpty()) {
                List<String> listCNPJDR = empresaService.findCNPJByIdsDepartamentoRegional(dados.getIdsDepartamentoRegional() != null ? dados.getIdsDepartamentoRegional() : new HashSet<>());
                filter.getListCNPJ().addAll(listCNPJDR);
            }
        } else if (dados.isGetorUnidadeSESI()) {
            if (dados.getIdsUnidadeSESI() != null && !dados.getIdsUnidadeSESI().isEmpty()) {
                List<String> listCNPJUnidadeSesi = empresaService.findCNPJByIdsUnidadeSesi(dados.getIdsUnidadeSESI() != null ? dados.getIdsUnidadeSESI() : new HashSet<>());
                filter.getListCNPJ().addAll(listCNPJUnidadeSesi);
            }
        }

        if (filter.getListCNPJ() != null && !filter.getListCNPJ().isEmpty()) {
            listaPaginada = sistemaCredenciadoClient.pesquisar(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), filter);
        }

        return listaPaginada;
    }

    private boolean valido(SistemaCredenciado sistemaCredenciado) {
        return sistemaCredenciado != null &&
                ValidadorUtils.isValidCNPJ(StringUtil.removeCaracteresEspeciais(sistemaCredenciado.getCnpj())) &&
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
        if (sistemaCredenciado.getCnpj() != null) {
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
        }
        return next;
    }

}
