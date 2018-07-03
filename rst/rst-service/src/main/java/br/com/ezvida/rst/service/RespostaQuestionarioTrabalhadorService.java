package br.com.ezvida.rst.service;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.RespostaQuestionarioTrabalhadorDAO;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.enums.Classificacao;
import br.com.ezvida.rst.model.QuestionarioTrabalhador;
import br.com.ezvida.rst.model.RespostaQuestionarioTrabalhador;
import br.com.ezvida.rst.model.dto.ClassificacaoDTO;
import br.com.ezvida.rst.model.dto.ResultadoQuestionarioDTO;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.Set;

@Stateless
public class RespostaQuestionarioTrabalhadorService extends BaseService {

    private static final long serialVersionUID = -5833872931747412049L;

    private static final Logger LOGGER = LoggerFactory.getLogger(EmpresaTrabalhadorService.class);

    @Inject
    private RespostaQuestionarioTrabalhadorDAO respostaQuestionarioTrabalhadorDAO;

    @Inject
    private QuestionarioTrabalhadorService questionarioTrabalhadorService;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Set<RespostaQuestionarioTrabalhador> pesquisarPorId(Long idQuestionarioTrabalhador,
                                                               ClienteAuditoria auditoria) {
        LOGGER.debug("Pesquisando EmpresaTrabalhador por id");
        if (idQuestionarioTrabalhador == null) {
            throw new BusinessErrorException("Id de consulta está nulo.");
        }
        LogAuditoria.registrar(LOGGER, auditoria, "Pesquisa de Resposta por id: " + idQuestionarioTrabalhador);
        return respostaQuestionarioTrabalhadorDAO.pesquisarPorQuestionarioTrabalhador(idQuestionarioTrabalhador);
    }

    public ResultadoQuestionarioDTO getResultadoQuestionario(Long idQuestionarioTrabalhador, ClienteAuditoria auditoria) {
        return getResultadoQuestionarioDTO(idQuestionarioTrabalhador, auditoria, false);
    }

    private ResultadoQuestionarioDTO getResultadoQuestionarioDTO(Long idQuestionarioTrabalhador, ClienteAuditoria auditoria, Boolean mobile) {
        LOGGER.debug("Pesquisando resultado do questionario");
        if (idQuestionarioTrabalhador == null) {
            throw new BusinessErrorException("Id de consulta está nulo.");
        }
        if (auditoria != null) {
            LogAuditoria.registrar(LOGGER, auditoria, "Pesquisa de Resposta por id: " + idQuestionarioTrabalhador);
        }
        ResultadoQuestionarioDTO resultadoQuestionarioDTO = respostaQuestionarioTrabalhadorDAO.getResultadoQuestionario(idQuestionarioTrabalhador);

        QuestionarioTrabalhador questionarioTrabalhador = questionarioTrabalhadorService.buscarPorIdCompleto(idQuestionarioTrabalhador, auditoria);
        resultadoQuestionarioDTO.setDescricaoQuestionario(questionarioTrabalhador.getQuestionario().getDescricao());
        resultadoQuestionarioDTO.setTituloQuestionario(questionarioTrabalhador.getQuestionario().getNome());

        ClassificacaoDTO classificacaoDTO = new ClassificacaoDTO();

        int quantidadePontos = questionarioTrabalhador.getQuantidadePonto() != null ?
                questionarioTrabalhador.getQuantidadePonto() : 0;
        Classificacao classificacaoTrabalhador = questionarioTrabalhador.getClassificacaoPontuacao()
                .getCodigoClassificacao(quantidadePontos);

        classificacaoDTO.setClassificacao(questionarioTrabalhador.getClassificacaoPontuacao().getDescricao());
        classificacaoDTO.setMensagem(questionarioTrabalhador.getClassificacaoPontuacao().getMensagem());

        getUrlClassificacao(classificacaoDTO, classificacaoTrabalhador, mobile);

        resultadoQuestionarioDTO.setClassificacao(classificacaoDTO);

        return resultadoQuestionarioDTO;
    }

    private void getUrlClassificacao(ClassificacaoDTO classificacaoDTO, Classificacao classificacaoTrabalhador, Boolean mobile) {
        if (!mobile) {
            if (classificacaoTrabalhador.equals(Classificacao.BAIXO_RISCO)) {
                classificacaoDTO.setUrl("assets/img/baixorisco.svg");
            } else if (classificacaoTrabalhador.equals(Classificacao.MEDIO_RISCO)) {
                classificacaoDTO.setUrl("assets/img/mediorisco.svg");
            } else if (classificacaoTrabalhador.equals(Classificacao.MEDIO_ALTO)) {
                classificacaoDTO.setUrl("assets/img/medioalto.svg");
            } else if (classificacaoTrabalhador.equals(Classificacao.ALTO_RISCO)) {
                classificacaoDTO.setUrl("assets/img/altorisco.svg");
            }
        } else {
            switch (classificacaoTrabalhador) {
                case BAIXO_RISCO:
                    classificacaoDTO.setUrl(Classificacao.BAIXO_RISCO.toString());
                    break;
                case MEDIO_RISCO:
                    classificacaoDTO.setUrl(Classificacao.MEDIO_RISCO.toString());
                    break;
                case MEDIO_ALTO:
                    classificacaoDTO.setUrl(Classificacao.MEDIO_ALTO.toString());
                    break;
                case ALTO_RISCO:
                    classificacaoDTO.setUrl(Classificacao.ALTO_RISCO.toString());
                    break;
            }
        }
    }

    public ResultadoQuestionarioDTO getResultadoQuestionario(String cpfTrabalhador, ClienteAuditoria auditoria) {
        ListaPaginada<QuestionarioTrabalhador> paginada = questionarioTrabalhadorService.pesquisaPaginada(cpfTrabalhador, 1, 1, auditoria);
        if (paginada == null || paginada.getList().isEmpty() || paginada.getList().get(0).getId() == null) {
            throw new RegistroNaoEncontradoException("Resultado não encontrado");
        }
        return this.getResultadoQuestionarioDTO(paginada.getList().get(0).getId(), auditoria, true);
    }

}
