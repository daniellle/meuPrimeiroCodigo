package br.com.ezvida.rst.service;

import br.com.ezvida.girst.apiclient.client.SistemaCredenciadoClient;
import br.com.ezvida.girst.apiclient.model.ListaPaginada;
import br.com.ezvida.girst.apiclient.model.SistemaCredenciado;
import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.SistemaCredenciadoFilter;
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
import java.util.*;
import java.util.stream.Collectors;

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

        Map<String, String> filtro = new HashMap<>();

        if (filter.getPagina() != null) {
            filtro.put("pagina", filter.getPagina().toString());
        }

        if (filter.getQuantidadeRegistro() != null) {
            filtro.put("quantidadeRegistros", filter.getQuantidadeRegistro().toString());
        }

        if (StringUtils.isNotBlank(filter.getCnpj())) {
            filtro.put("cnpj", StringUtil.removeCaracteresEspeciais(filter.getCnpj()));
        }

        if (StringUtils.isNotBlank(filter.getNomeResponsavel())) {
            filtro.put("nomeResponsavel", filter.getNomeResponsavel());
        }

        if (StringUtils.isNotBlank(filter.getSistema())) {
            filtro.put("sistema", filter.getSistema());
        }

        if (filter.getBloqueado() != null) {
            filtro.put("bloqueado", filter.getBloqueado().toString());
        }

        ListaPaginada<SistemaCredenciado> listaPaginada = sistemaCredenciadoClient.pesquisar(apiClientService.getURL(), apiClientService.getOAuthToken().getAccess_token(), filtro);
        ListaPaginada<SistemaCredenciado> listaRetorno = new ListaPaginada<>(0l, new ArrayList<SistemaCredenciado>());
        Set<SistemaCredenciado> set = new HashSet<>();
        if (listaPaginada != null && !listaPaginada.getList().isEmpty()) {
            if (dados.isAdministrador() || dados.isGestorDn()) {
                return listaPaginada;
            } else if (dados.isGestorDr()) {
                List<String> listCNPJDR = empresaService.findCNPJByIdsDepartamentoRegional(dados.getIdsDepartamentoRegional());
                listCNPJDR.forEach(c -> {
                    Optional<SistemaCredenciado> optionalSistemaCredenciado = listaPaginada.getList().stream().filter(p -> c.equals(p.getCnpj())).findFirst();
                    if (optionalSistemaCredenciado.isPresent()) {
                        set.add(optionalSistemaCredenciado.get());
                    }
                });
            } else if (dados.isGetorUnidadeSESI()) {
                List<String> listCNPJUnidadeSesi = empresaService.findCNPJByIdsUnidadeSesi(dados.getIdsUnidadeSESI());
                listCNPJUnidadeSesi.forEach(c -> {
                    Optional<SistemaCredenciado> optionalSistemaCredenciado = listaPaginada.getList().stream().filter(p -> c.equals(p.getCnpj())).findFirst();
                    if (optionalSistemaCredenciado.isPresent()) {
                        set.add(optionalSistemaCredenciado.get());
                    }
                });
            }
        }

        listaRetorno.getList().addAll(set);
        listaRetorno.setQuantidade(new Long(listaRetorno.getList().size()));
        return listaRetorno;
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
