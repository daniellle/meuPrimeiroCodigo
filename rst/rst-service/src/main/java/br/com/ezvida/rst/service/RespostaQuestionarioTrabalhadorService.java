package br.com.ezvida.rst.service;

import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.RespostaQuestionarioTrabalhadorDAO;
import br.com.ezvida.rst.enums.Classificacao;
import br.com.ezvida.rst.model.QuestionarioTrabalhador;
import br.com.ezvida.rst.model.RespostaQuestionarioTrabalhador;
import br.com.ezvida.rst.model.dto.ClassificacaoDTO;
import br.com.ezvida.rst.model.dto.ResultadoQuestionarioDTO;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

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
	
	public ResultadoQuestionarioDTO getResultadoQuestionario(Long idQuestionarioTrabalhador,ClienteAuditoria auditoria) {
		LOGGER.debug("Pesquisando EmpresaTrabalhador por id");
		if (idQuestionarioTrabalhador == null) {
			throw new BusinessErrorException("Id de consulta está nulo.");
		}
		LogAuditoria.registrar(LOGGER, auditoria, "Pesquisa de Resposta por id: " + idQuestionarioTrabalhador);
		
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
		
		if (classificacaoTrabalhador.equals(Classificacao.BAIXO_RISCO)) {
			classificacaoDTO.setUrl("assets/img/baixorisco.svg");
		} else if (classificacaoTrabalhador.equals(Classificacao.MEDIO_RISCO)) {
			classificacaoDTO.setUrl("assets/img/mediorisco.svg");
		} else if (classificacaoTrabalhador.equals(Classificacao.MEDIO_ALTO)) {
			classificacaoDTO.setUrl("assets/img/medioalto.svg");
		} else if (classificacaoTrabalhador.equals(Classificacao.ALTO_RISCO)) {
			classificacaoDTO.setUrl("assets/img/altorisco.svg");
		}
		
		resultadoQuestionarioDTO.setClassificacao(classificacaoDTO);
		
		return resultadoQuestionarioDTO;
	}
	
}
