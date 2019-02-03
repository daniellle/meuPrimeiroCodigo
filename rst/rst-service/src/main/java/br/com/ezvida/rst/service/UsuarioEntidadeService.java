package br.com.ezvida.rst.service;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UsuarioEntidadeDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.TrabalhadorFilter;
import br.com.ezvida.rst.dao.filter.UsuarioEntidadeFilter;
import br.com.ezvida.rst.model.Trabalhador;
import br.com.ezvida.rst.model.UsuarioEntidade;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class UsuarioEntidadeService extends BaseService {

    private static final long serialVersionUID = 1666720223703103019L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioEntidadeService.class);

    private static final String USUARIO_ENTIDADE = "USUARIO ENTIDADE";

    @Inject
    private UsuarioEntidadeDAO usuarioEntidadeDAO;

    @Inject
    private TrabalhadorService trabalhadorService;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UsuarioEntidade> pesquisarPorCPF(String cpf, ClienteAuditoria auditoria) {
        LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de Usuario Entidade por cpf: " + cpf);
        return usuarioEntidadeDAO.pesquisarPorCPF(cpf, true);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UsuarioEntidade> pesquisarPorCPF(String cpf) {
        return usuarioEntidadeDAO.pesquisarPorCPF(cpf, true);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ListaPaginada<UsuarioEntidade> pesquisarEmpresa(UsuarioEntidadeFilter filtro, ClienteAuditoria auditoria) {
        LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de Empresas em Usuario Entidade por filtro: ", filtro);
        return usuarioEntidadeDAO.pesquisarEmpresa(filtro);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UsuarioEntidade> pesquisarTodasEmpresasAssociadas(String cpf) {
        return usuarioEntidadeDAO.pesquisarTodasEmpresasAssociadas(cpf);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UsuarioEntidade> pesquisarTodosDepartamentosAssociadas(String cpf) {
        return usuarioEntidadeDAO.pesquisarTodosDepartamentosAssociadas(cpf);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ListaPaginada<UsuarioEntidade> pesquisarSindicato(UsuarioEntidadeFilter filtro, ClienteAuditoria auditoria) {
        LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de Sindicato em Usuario Entidade por filtro: ", filtro);
        return usuarioEntidadeDAO.pesquisarSindicato(filtro);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ListaPaginada<UsuarioEntidade> pesquisarDepartamentoRegional(UsuarioEntidadeFilter filtro,
                                                                        ClienteAuditoria auditoria) {
        LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de Departamento Regional em Usuario Entidade por filtro: ",
                filtro);
        return usuarioEntidadeDAO.pesquisarDepartamentoRegional(filtro);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ListaPaginada<UsuarioEntidade> pesquisarUnidadeSESI(UsuarioEntidadeFilter filtro,
                                                               ClienteAuditoria auditoria) {
        LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de Unidade SESI em Usuario Entidade por filtro: ",
                filtro);
        return usuarioEntidadeDAO.pesquisarUnidadeSESI(filtro);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void salvar(UsuarioEntidade usuarioEntidade, ClienteAuditoria auditoria) {
        String descricaoAuditoria = "Cadastro de " + USUARIO_ENTIDADE + ": ";
        if (usuarioEntidade.getId() != null) {
            descricaoAuditoria = "Alteração no cadastro de " + USUARIO_ENTIDADE + ": ";
        }
        LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, usuarioEntidade);
        usuarioEntidadeDAO.salvar(usuarioEntidade);
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<UsuarioEntidade> salvar(List<UsuarioEntidade> list, ClienteAuditoria auditoria) {
        boolean sucess = false;
        List<UsuarioEntidade> lista = list;
        try {
            for (UsuarioEntidade usuarioEntidade : list) {
                if (usuarioEntidadeDAO.verificandoExistenciaUsuarioEntidade(usuarioEntidade) == null) {
                    salvar(usuarioEntidade, auditoria);
                 return  lista = CollectionUtils.isNotEmpty(list) ? list : Lists.newArrayList();
                }
            }
        } catch(Exception e){
            throw new BusinessErrorException(getMensagem("app_rst_generic_itens_not_add"));
        }
        return lista;
//        if (CollectionUtils.isNotEmpty(list)) {
//            for (UsuarioEntidade usuarioEntidade : list) {
//                if (usuarioEntidadeDAO.verificandoExistenciaUsuarioEntidade(usuarioEntidade) == null) {
//                    salvar(usuarioEntidade, auditoria);
//                    sucess = true;
//                }
//            }
//
//        }
//        if (sucess) {
//            return CollectionUtils.isNotEmpty(list) ? list : Lists.newArrayList();
//        } else {
//            throw new BusinessErrorException(getMensagem("app_rst_generic_itens_not_add"));
//        }

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Object desativarUsuarioEntidade(UsuarioEntidade usuarioEntidade, ClienteAuditoria auditoria) {
        if (usuarioEntidade.getId() != null) {
            usuarioEntidade = usuarioEntidadeDAO.pesquisarPorId(usuarioEntidade.getId());
            if (usuarioEntidade.getDataExclusao() == null) {
                usuarioEntidade.setDataExclusao(new Date());
                salvar(usuarioEntidade, auditoria);
            }
        }
        return usuarioEntidade;
    }

    @TransactionAttribute (TransactionAttributeType.REQUIRED)
    public Object alterarUsuarioEntidade(UsuarioEntidade usuarioEntidade, ClienteAuditoria auditoria){
        if(usuarioEntidade.getId() != null){
            usuarioEntidade = usuarioEntidadeDAO.pesquisarPorId(usuarioEntidade.getId());
            if(usuarioEntidade.getDataAlteracao() == null ){
                usuarioEntidade.setDataAlteracao(new Date());
                salvar(usuarioEntidade, auditoria);
            }
        }
        return usuarioEntidade;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UsuarioEntidade> validarAcessoAoUsuario(String cpf, DadosFilter dadosFilter, ClienteAuditoria auditoria) {

        List<UsuarioEntidade> usuarioEntidade = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(usuarioEntidadeDAO.pesquisarPorCPF(cpf, false))) {
            if (dadosFilter.temIdsDepRegional()) {
                usuarioEntidade.addAll(usuarioEntidadeDAO.pesquisarPorDepartRegionalEmEmpresa(cpf, dadosFilter));
                usuarioEntidade.addAll(usuarioEntidadeDAO.pesquisarPorDepartRegional(cpf, dadosFilter));

                if (usuarioEntidade.isEmpty()) {
                    throw new BusinessErrorException(getMensagem("app_rst_usuario_acesso_negado"));
                }
            }
        } else {
            if (trabalhadorService.pesquisarPorCPF(cpf) != null) {
                TrabalhadorFilter trabalhadorFilter = new TrabalhadorFilter();
                trabalhadorFilter.setCpf(cpf);
                trabalhadorFilter.setAplicarDadosFilter(true);
                ListaPaginada<Trabalhador> paginado = trabalhadorService.pesquisarPaginado(trabalhadorFilter, auditoria, dadosFilter);

                if (paginado != null && CollectionUtils.isEmpty(paginado.getList())) {
                    throw new BusinessErrorException(getMensagem("app_rst_usuario_acesso_negado"));
                }
            }
        }

        return usuarioEntidade;

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UsuarioEntidade> buscarUsuariosEntidade(String cpf) {

        List<UsuarioEntidade> usuarioEntidade = Lists.newArrayList();
        usuarioEntidade.addAll(usuarioEntidadeDAO.pesquisarPorCPF(cpf, true));

        return usuarioEntidade;

    }



    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Collection<String> buscarUsuarioEntidadedoUsuarioLogado(String cpf, DadosFilter dadosFilter) {

        Set<String> cpfs = Sets.newHashSet();

        if (dadosFilter.temIdsDepRegional() || dadosFilter.temIdsUnidadeSESI()) {
            cpfs.addAll(usuarioEntidadeDAO.pesquisarPorDepartRegional(cpf, dadosFilter).stream().map(ue -> ue.getCpf()).collect(Collectors.toList()));
            cpfs.addAll(usuarioEntidadeDAO.pesquisarPorDepartRegionalEmEmpresa(cpf, dadosFilter).stream().map(ue -> ue.getCpf())
                    .collect(Collectors.toList()));
        }

        if (dadosFilter.temIdsEmpresa() || dadosFilter.temIdsDepRegional() || dadosFilter.temIdsUnidadeSESI()) {

            cpfs.addAll(usuarioEntidadeDAO.pesquisarPorDepartRegionalEmSindicato(cpf, dadosFilter).stream().map(ue -> ue.getCpf())
                    .collect(Collectors.toList()));

            TrabalhadorFilter trabalhadorFilter = new TrabalhadorFilter();
            trabalhadorFilter.setAplicarDadosFilter(true);
            ListaPaginada<Trabalhador> paginado = trabalhadorService.pesquisarPaginado(trabalhadorFilter, dadosFilter);

            if (paginado != null) {
                cpfs.addAll(paginado.getList().stream().map(ue -> ue.getCpf()).collect(Collectors.toList()));
            }
        }

        return cpfs;
    }
}
