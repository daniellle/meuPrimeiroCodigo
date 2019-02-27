package br.com.ezvida.rst.service;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UatInstalacaoFisicaDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.model.UatInstalacaoFisica;
import br.com.ezvida.rst.model.dto.UatInstalacaoFisicaDTO;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Stateless
public class UatInstalacaoFisicaService extends BaseService {

    private static final long serialVersionUID = -8017625552821005037L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UatInstalacaoFisicaService.class);

    @Inject
    private UatInstalacaoFisicaDAO uatInstalacaoFisicaDAO;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<UatInstalacaoFisica> salvar(List<UatInstalacaoFisica> listInstalacoesFisicas, ClienteAuditoria auditoria, DadosFilter dados) {
        LogAuditoria.registrar(LOGGER, auditoria, "Salvando Instalação Física");
        List<UatInstalacaoFisica> listaSalvar = new ArrayList<>();
        if (listInstalacoesFisicas != null && !listInstalacoesFisicas.isEmpty()) {
            for (UatInstalacaoFisica uatInstalacaoFisica : listInstalacoesFisicas) {
                if (uatInstalacaoFisica.getArea() != null &&
                        uatInstalacaoFisica.getQuantidade() != null &&
                        uatInstalacaoFisica.getUnidadeAtendimentoTrabalhador() != null &&
                        uatInstalacaoFisica.getUnidadeAtendimentoTrabalhador().getId() != null) {
                    //TODO: Posteriormente Validar origem de dados
                    listaSalvar.add(uatInstalacaoFisica);
                }
            }
            if (!listaSalvar.isEmpty()) {
                for (UatInstalacaoFisica uatInstalacaoFisica : listaSalvar) {
                    uatInstalacaoFisica.setDataCriacao(new Date());
                    this.uatInstalacaoFisicaDAO.salvar(uatInstalacaoFisica);
                }
                return listaSalvar;
            } else {
                throw new BusinessErrorException(getMensagem("app_rst_instalacao_fisica_obrigatoria"));
            }
        } else {
            throw new BusinessErrorException(getMensagem("app_rst_instalacao_fisica_obrigatoria"));
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<String, List<UatInstalacaoFisicaDTO>> findByUnidade(Long idUnidade, ClienteAuditoria auditoria, DadosFilter dados) {
        LogAuditoria.registrar(LOGGER, auditoria, "Buscando Instalações Físicas por id da unidade sesi " + idUnidade);
        Map<String, List<UatInstalacaoFisicaDTO>> instalacoesFisicasAGG = null;
        List<UatInstalacaoFisicaDTO> listaInstalacoes = this.uatInstalacaoFisicaDAO.findByUnidade(idUnidade);
        if (listaInstalacoes != null && !listaInstalacoes.isEmpty()) {
            instalacoesFisicasAGG = listaInstalacoes.stream().collect(Collectors.groupingBy(UatInstalacaoFisicaDTO::getDescricaoCategoria));
        }
        return instalacoesFisicasAGG;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void desativar(Long id, ClienteAuditoria auditoria, DadosFilter dados) {
        LogAuditoria.registrar(LOGGER, auditoria, "Desativando Instalação Física id: " + id);
        this.uatInstalacaoFisicaDAO.desativar(id);
    }

}
