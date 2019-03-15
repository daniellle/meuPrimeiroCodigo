package br.com.ezvida.rst.service;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UatQuadroPessoalDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.model.UatQuadroPessoal;
import br.com.ezvida.rst.model.dto.UatQuadroPessoalDTO;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class UatQuadroPessoalService extends BaseService {

    private static final long serialVersionUID = 7822160195519908385L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UatQuadroPessoalService.class);

    @Inject
    private UatQuadroPessoalDAO uatQuadroPessoalDAO;

    @Inject
    private ValidationService validationService;


    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public List<UatQuadroPessoal> salvar(List<UatQuadroPessoal> listQuadrosPessoais, ClienteAuditoria auditoria, DadosFilter dados) {
        LogAuditoria.registrar(LOGGER, auditoria, "Salvando Instalação Física");
        List<UatQuadroPessoal> listaSalvar = new ArrayList<>();
        if (listQuadrosPessoais != null && !listQuadrosPessoais.isEmpty()) {
            for (UatQuadroPessoal quadroPessoal : listQuadrosPessoais) {
                if (quadroPessoal.getQuantidade() != null &&
                        quadroPessoal.getUnidadeAtendimentoTrabalhador() != null &&
                        quadroPessoal.getUnidadeAtendimentoTrabalhador().getId() != null &&
                        quadroPessoal.getUatQuadroPessoalTipoProfissional() != null &&
                        quadroPessoal.getUatQuadroPessoalTipoProfissional().getId() != null) {
                    listaSalvar.add(quadroPessoal);
                }
            }

            if (!listaSalvar.isEmpty()) {
                this.validarQuadroPessoalJaCadastrado(listaSalvar);
                for (UatQuadroPessoal uatQuadroPessoal : listaSalvar) {
                    uatQuadroPessoal.setDataCriacao(new Date());
                    if (validationService.validarFiltroDadosGestaoUnidadeSesi(dados, uatQuadroPessoal.getUnidadeAtendimentoTrabalhador().getId())) {
                        try {
                            this.uatQuadroPessoalDAO.salvar(uatQuadroPessoal);
                        } catch (Exception e) {
                            LOGGER.error(e.getMessage());
                            throw new BusinessErrorException(getMensagem("app_validacao_error"));
                        }
                    } else {
                        String mensagem = getMensagem("app_rst_quadro_pessoal_nao_autorizado");
                        LOGGER.error(mensagem);
                        throw new BusinessErrorException(mensagem);
                    }
                }
                return listaSalvar;
            } else {
                throw new BusinessErrorException(getMensagem("app_rst_quadro_pessoal_obrigatorio"));
            }
        } else {
            throw new BusinessErrorException(getMensagem("app_rst_quadro_pessoal_obrigatorio"));
        }
    }


    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<String, List<UatQuadroPessoalDTO>> findByUnidade(Long idUnidade, ClienteAuditoria auditoria, DadosFilter dados) {
        Map<String, List<UatQuadroPessoalDTO>> listQuadrosPessoaisAgg = null;
        if (validationService.validarFiltroDadosGestaoUnidadeSesi(dados, idUnidade) || dados.isCallCenter()) {
            LogAuditoria.registrar(LOGGER, auditoria, "Buscando Quadros Pessoais por id da unidade sesi " + idUnidade);
            List<UatQuadroPessoalDTO> listaQuadrosPessoais = this.uatQuadroPessoalDAO.findByUnidade(idUnidade);
            if (listaQuadrosPessoais != null && !listaQuadrosPessoais.isEmpty()) {
                listQuadrosPessoaisAgg = listaQuadrosPessoais.stream().collect(Collectors.groupingBy(UatQuadroPessoalDTO::getDescricaoArea));
            }
        }
        return listQuadrosPessoaisAgg;
    }


    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void desativar(Long id, Long idUnidade, ClienteAuditoria auditoria, DadosFilter dados) {
        LogAuditoria.registrar(LOGGER, auditoria, "Desativando Quadro Pessoal id: " + id);
        if (validationService.validarFiltroDadosGestaoUnidadeSesi(dados, idUnidade)) {
            try {
                this.uatQuadroPessoalDAO.desativar(id);
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
                throw new BusinessErrorException(getMensagem("app_validacao_error"));
            }
        } else {
            LOGGER.error("app_rst_quadro_pessoal_nao_desativar_autorizado");
            throw new BusinessErrorException(getMensagem("app_rst_quadro_pessoal_nao_desativar_autorizado"));
        }
    }

    private void validarQuadroPessoalJaCadastrado(List<UatQuadroPessoal> listUatQuadroPessoal) {
        Long idUat = listUatQuadroPessoal.get(0).getUnidadeAtendimentoTrabalhador().getId();
        List<UatQuadroPessoalDTO> quadrosPessoaisCadastradosUnidade = this.uatQuadroPessoalDAO.findByUnidade(idUat);
        if (quadrosPessoaisCadastradosUnidade != null && !quadrosPessoaisCadastradosUnidade.isEmpty()) {
            listUatQuadroPessoal.forEach(q -> {
                Optional<UatQuadroPessoalDTO> quadroPessoal = quadrosPessoaisCadastradosUnidade.stream().filter(d ->
                        d.getIdTipoProfissional().equals(q.getUatQuadroPessoalTipoProfissional().getId())
                ).findFirst();
                if (quadroPessoal.isPresent()) {
                    throw new BusinessErrorException(getMensagem("app_rst_quadro_pessoal_duplicado", quadroPessoal.get().getDescricaoTipoProfissional()));
                }
            });
        }
    }
}
