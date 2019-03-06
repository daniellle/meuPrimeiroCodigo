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
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class UatInstalacaoFisicaService extends BaseService {

    private static final long serialVersionUID = -8017625552821005037L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UatInstalacaoFisicaService.class);

    @Inject
    private UatInstalacaoFisicaDAO uatInstalacaoFisicaDAO;

    @Inject
    private ValidationService validationService;

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<UatInstalacaoFisica> salvar(List<UatInstalacaoFisica> listInstalacoesFisicas, ClienteAuditoria auditoria, DadosFilter dados) {
        LogAuditoria.registrar(LOGGER, auditoria, "Salvando Instalação Física");
        List<UatInstalacaoFisica> listaSalvar = new ArrayList<>();
        if (listInstalacoesFisicas != null && !listInstalacoesFisicas.isEmpty()) {
            for (UatInstalacaoFisica uatInstalacaoFisica : listInstalacoesFisicas) {
                if (uatInstalacaoFisica.getArea() != null &&
                        uatInstalacaoFisica.getQuantidade() != null &&
                        uatInstalacaoFisica.getUnidadeAtendimentoTrabalhador() != null &&
                        uatInstalacaoFisica.getUnidadeAtendimentoTrabalhador().getId() != null &&
                        uatInstalacaoFisica.getUatInstalacaoFisicaAmbiente() != null &&
                        uatInstalacaoFisica.getUatInstalacaoFisicaAmbiente().getId() != null) {
                    listaSalvar.add(uatInstalacaoFisica);
                }
            }
            if (!listaSalvar.isEmpty()) {
                for (UatInstalacaoFisica uatInstalacaoFisica : listaSalvar) {
                    uatInstalacaoFisica.setDataCriacao(new Date());
                    if (validationService.validarFiltroDadosGestaoUnidadeSesi(dados, uatInstalacaoFisica.getUnidadeAtendimentoTrabalhador().getId())) {
                        try {
                            this.uatInstalacaoFisicaDAO.salvar(uatInstalacaoFisica);
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage());
                            throw new BusinessErrorException(getMensagem("app_validacao_error"));
                        }
                    } else {
                        String mensagem = getMensagem("app_rst_instalacao_fisica_nao_autorizado");
                        LOGGER.error(mensagem);
                        throw new BusinessErrorException(mensagem);
                    }
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
        Map<String, List<UatInstalacaoFisicaDTO>> instalacoesFisicasAGG = null;
        if (validationService.validarFiltroDadosGestaoUnidadeSesi(dados, idUnidade)) {
            LogAuditoria.registrar(LOGGER, auditoria, "Buscando Instalações Físicas por id da unidade sesi " + idUnidade);
            List<UatInstalacaoFisicaDTO> listaInstalacoes = this.uatInstalacaoFisicaDAO.findByUnidade(idUnidade);
            if (listaInstalacoes != null && !listaInstalacoes.isEmpty()) {
                instalacoesFisicasAGG = listaInstalacoes.stream().collect(Collectors.groupingBy(UatInstalacaoFisicaDTO::getDescricaoCategoria));
            }
        }
        return instalacoesFisicasAGG;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void desativar(Long id, Long idUnidade, ClienteAuditoria auditoria, DadosFilter dados) {
        LogAuditoria.registrar(LOGGER, auditoria, "Desativando Instalação Física id: " + id);
        if (validationService.validarFiltroDadosGestaoUnidadeSesi(dados, idUnidade)) {
            try {
                this.uatInstalacaoFisicaDAO.desativar(id);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                throw new BusinessErrorException(getMensagem("app_validacao_error"));
            }
        } else {
            LOGGER.error("app_rst_instalacao_fisica_nao_desativar_autorizado");
            throw new BusinessErrorException(getMensagem("app_rst_instalacao_fisica_nao_desativar_autorizado"));
        }
    }

}
