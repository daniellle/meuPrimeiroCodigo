package br.com.ezvida.rst.service;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.GrupoPerguntaQuestionarioDAO;
import br.com.ezvida.rst.dao.PerguntaQuestionarioDAO;
import br.com.ezvida.rst.dao.QuestionarioDAO;
import br.com.ezvida.rst.dao.TrabalhadorDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.PerguntaQuestionarioFilter;
import br.com.ezvida.rst.enums.StatusQuestionario;
import br.com.ezvida.rst.model.PerguntaQuestionario;
import br.com.ezvida.rst.model.Questionario;
import br.com.ezvida.rst.model.RespostaQuestionario;
import br.com.ezvida.rst.model.Trabalhador;
import br.com.ezvida.rst.model.dto.GrupoPerguntaQuestionarioDTO;
import br.com.ezvida.rst.model.dto.PerguntaQuestionarioDTO;
import br.com.ezvida.rst.model.dto.QuestionarioDTO;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import com.google.common.collect.Sets;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;
import fw.security.exception.UnauthorizedException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.*;

@Stateless
public class PerguntaQuestionarioService extends BaseService {

    private static final long serialVersionUID = 8993384051604605060L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PerguntaQuestionarioService.class);

    @Inject
    private PerguntaQuestionarioDAO perguntaQuestionarioDAO;

    @Inject
    private QuestionarioDAO questionarioDAO;

    @Inject
    private GrupoPerguntaQuestionarioDAO grupoPerguntaQuestionarioDAO;

    @Inject
    private RespostaQuestionarioService respostaQuestionarioService;

    @Inject
    private TrabalhadorDAO trabalhadorDAO;

    @Inject
    private QuestionarioTrabalhadorService questionarioTrabalhadorService;


    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ListaPaginada<PerguntaQuestionario> perguntaQuestionario(PerguntaQuestionarioFilter perguntaFilter,
                                                                    ClienteAuditoria auditoria, DadosFilter dados) {
        LogAuditoria.registrar(LOGGER, auditoria,
                "pesquisa de perguntas associadas ao questionario " + perguntaFilter.getIdQuestionario(), perguntaFilter);
        return perguntaQuestionarioDAO.perguntaQuestionario(perguntaFilter);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<PerguntaQuestionario> buscarPerguntaQuestionario(PerguntaQuestionarioFilter perguntaFilter,
                                                                 ClienteAuditoria auditoria) {
        if (auditoria != null) {
            LogAuditoria.registrar(LOGGER, auditoria,
                    "pesquisa de perguntas associadas ao questionario " + perguntaFilter.getIdQuestionario(), perguntaFilter);
        }
        return perguntaQuestionarioDAO.buscarPerguntaQuestionario(perguntaFilter);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public QuestionarioDTO montarQuestionario(ClienteAuditoria auditoria) {

        QuestionarioDTO questionarioDTO = perguntaQuestionarioDAO.obterQuestionario();

        if (questionarioDTO != null) {
            int numeracao = 1;
            questionarioDTO.setListaGrupoPerguntaQuestionario(perguntaQuestionarioDAO.pesquisarGrupoPerguntaQuestionario(questionarioDTO.getId()));
            for (GrupoPerguntaQuestionarioDTO grupoPerguntaQuestionarioDTO : questionarioDTO.getListaGrupoPerguntaQuestionario()) {
                grupoPerguntaQuestionarioDTO.setListaPerguntaQuestionarioDTO(
                        perguntaQuestionarioDAO.pesquisarPerguntaQuestionarioDTO(grupoPerguntaQuestionarioDTO.getId(), questionarioDTO.getId()));
                for (PerguntaQuestionarioDTO perguntaQuestionarioDTO : grupoPerguntaQuestionarioDTO.getListaPerguntaQuestionarioDTO()) {
                    perguntaQuestionarioDTO.setNumeracao(String.format("%02d", numeracao));
                    numeracao++;
                }
            }
        }

        return questionarioDTO;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Map<String, Object> gerarQuestionario(String login, ClienteAuditoria auditoria) {
        Map<String, Object> mapTrabaQuest = new HashMap<>();
        if (StringUtils.isNotEmpty(login)) {
            Trabalhador trabalhador = trabalhadorDAO.pesquisarPorCpf(login);
            if(trabalhador == null){
                throw new RegistroNaoEncontradoException("Trabalhador não encontrado");
            }
            Boolean podeResponder = questionarioTrabalhadorService.isAllowedAnswerQuestionarioIgev(trabalhador.getId());
            if (podeResponder) {
                QuestionarioDTO quetionario = this.montarQuestionario(auditoria);
                mapTrabaQuest.put("questionario", quetionario);
                mapTrabaQuest.put("trabalhador", trabalhador);
            } else {
                throw new BusinessErrorException("Você já preencheu o seu IGEV do período. Aguarde o próximo ciclo.");
            }
        }
        return mapTrabaQuest;
    }

    private Questionario clonaraQuestionario(PerguntaQuestionario perguntaQuestionario, boolean remover)
            throws CloneNotSupportedException {
        Questionario questionarioClone = (Questionario) ObjectUtils.clone(perguntaQuestionario.getQuestionario());
        questionarioClone.setId(null);
        questionarioClone.setVersao(null);
        questionarioClone.setStatus(StatusQuestionario.EDICAO);
        questionarioDAO.salvar(questionarioClone);

        questionarioClone.setListaPerguntaQuestionario(clonarPerguntasRespostas(perguntaQuestionario, questionarioClone, remover));
        return questionarioClone;
    }

    private Set<PerguntaQuestionario> clonarPerguntasRespostas(PerguntaQuestionario perguntaQuestionario, Questionario questionarioClone, boolean remover)
            throws CloneNotSupportedException {
        PerguntaQuestionarioFilter perguntaFilter = new PerguntaQuestionarioFilter();
        perguntaFilter.setIdQuestionario(perguntaQuestionario.getQuestionario().getId());

        Set<PerguntaQuestionario> perguntasExistentes = Sets.newHashSet(perguntaQuestionarioDAO.buscarPerguntaQuestionario(perguntaFilter));

        Set<PerguntaQuestionario> clonePerguntas = Sets.newHashSet();

        Long idAlterando = null;
        if (perguntaQuestionario.getId() != null) {
            idAlterando = perguntaQuestionario.getId();
        }

        if (!remover) {
            perguntaQuestionario.setQuestionario(questionarioClone);
            perguntaQuestionario.setId(null);
            clonePerguntas.add(perguntaQuestionario);
        }

        for (PerguntaQuestionario pergunta : perguntasExistentes) {
            if (!pergunta.getId().equals(idAlterando)) {
                PerguntaQuestionario perguntaClone = (PerguntaQuestionario) pergunta.clone();
                perguntaClone.setId(null);
                perguntaClone.setQuestionario(questionarioClone);

                Set<RespostaQuestionario> cloneResposta = Sets.newHashSet();
                for (RespostaQuestionario resposta : pergunta.getRespostaQuestionarios()) {
                    RespostaQuestionario respostaClone = (RespostaQuestionario) resposta.clone();
                    respostaClone.setId(null);
                    respostaClone.setPerguntaQuestionario(pergunta);
                    cloneResposta.add(respostaClone);
                }

                perguntaClone.setRespostaQuestionarios(cloneResposta);
                clonePerguntas.add(perguntaClone);
            }
        }

        return clonePerguntas;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public PerguntaQuestionario associarPerguntaQuestionario(PerguntaQuestionario perguntaQuestionario,
                                                             ClienteAuditoria auditoria) {
        validar(perguntaQuestionario);
        Questionario questionario = questionarioDAO.pesquisarPorId(perguntaQuestionario.getQuestionario().getId());
        if (questionario.getStatus().equals(StatusQuestionario.PUBLICADO) ||
                questionario.getStatus().equals(StatusQuestionario.DESATIVADO)) {
            try {
                Questionario questionarioClone = clonaraQuestionario(perguntaQuestionario, false);
                Set<PerguntaQuestionario> listaPerguntasClone = questionarioClone.getListaPerguntaQuestionario();
                for (PerguntaQuestionario pergunta : listaPerguntasClone) {
                    grupoPerguntaQuestionarioDAO.salvar(pergunta.getGrupoPergunta());
                    perguntaQuestionarioDAO.salvar(pergunta);
                    respostaQuestionarioService.salvarRespostaQuestionarioEmLote(pergunta.getRespostaQuestionarios(), pergunta);
                }
            } catch (CloneNotSupportedException e) {
                LOGGER.error(e.getMessage());
            }
        } else {
            grupoPerguntaQuestionarioDAO.salvar(perguntaQuestionario.getGrupoPergunta());
            perguntaQuestionarioDAO.salvar(perguntaQuestionario);
            respostaQuestionarioService.salvarRespostaQuestionarioEmLote(perguntaQuestionario.getRespostaQuestionarios(), perguntaQuestionario);
        }

        perguntaQuestionario.getQuestionario().setListaPerguntaQuestionario(null); // Para não dar erro de recursividade na auditoria nem no json de retorno
        if (auditoria != null) {
            LogAuditoria.registrar(LOGGER, auditoria, "Associar pergunta ao questionário", perguntaQuestionario);
        }

        return perguntaQuestionario;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Questionario desativarPerguntaQuestionario(PerguntaQuestionario perguntaQuestionario, ClienteAuditoria auditoria) {
        Questionario questionarioRetorno = new Questionario();
        Questionario questionario = questionarioDAO.pesquisarPorId(perguntaQuestionario.getQuestionario().getId());
        if (perguntaQuestionario.getId() != null) {
            perguntaQuestionario = pesquisarPorId(perguntaQuestionario.getId(), auditoria);
            if (questionario.getStatus().equals(StatusQuestionario.PUBLICADO)
                    || questionario.getStatus().equals(StatusQuestionario.DESATIVADO)) {
                try {
                    perguntaQuestionario.getQuestionario().setListaPerguntaQuestionario(null); // para evitar: Found shared references to a collection
                    questionarioRetorno = clonaraQuestionario(perguntaQuestionario, true);
                    Set<PerguntaQuestionario> listaPerguntasClone = questionarioRetorno.getListaPerguntaQuestionario();
                    for (PerguntaQuestionario pergunta : listaPerguntasClone) {
                        grupoPerguntaQuestionarioDAO.salvar(pergunta.getGrupoPergunta());
                        perguntaQuestionarioDAO.salvar(pergunta);
                        respostaQuestionarioService.salvarRespostaQuestionarioEmLote(pergunta.getRespostaQuestionarios(), pergunta);
                    }

                    String descricaoAuditoria = "Desativação de pergunta do questionário: ";
                    questionarioRetorno.setListaPerguntaQuestionario(null);
                    LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, questionarioRetorno);
                } catch (CloneNotSupportedException e) {
                    LOGGER.error(e.getMessage());
                }
            } else {
                if (perguntaQuestionario.getDataExclusao() == null) {
                    perguntaQuestionario.setDataExclusao(new Date());
                    perguntaQuestionario.getRespostaQuestionarios().clear();
                    respostaQuestionarioService.salvarRespostaQuestionarioEmLote(perguntaQuestionario.getRespostaQuestionarios(), perguntaQuestionario);
                    perguntaQuestionarioDAO.salvar(perguntaQuestionario);
                    questionarioRetorno = perguntaQuestionario.getQuestionario();
                    String descricaoAuditoria = "Desativação de pergunta do questionário: ";
                    LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, questionarioRetorno);
                }
            }
        }
        return questionarioRetorno;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public PerguntaQuestionario pesquisarPorId(Long id, ClienteAuditoria auditoria) {

        if (id == null) {
            throw new UnauthorizedException(getMensagem("app_rst_id_consulta_nulo"));
        }

        PerguntaQuestionario perguntaQuestionario = perguntaQuestionarioDAO.pesquisarPorId(id);

        if (perguntaQuestionario == null) {
            throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
        }
        LogAuditoria.registrar(LOGGER, auditoria, "pesquisa de pergunta por id: " + id);
        return perguntaQuestionario;
    }

    private void validar(PerguntaQuestionario perguntaQuestionario) {
        PerguntaQuestionarioFilter filter = new PerguntaQuestionarioFilter();
        filter.setIdQuestionario(perguntaQuestionario.getQuestionario().getId());
        List<PerguntaQuestionario> perguntas = perguntaQuestionarioDAO.buscarPerguntaQuestionario(filter);

        if (CollectionUtils.isNotEmpty(perguntas)) {
            for (PerguntaQuestionario pergunta : perguntas) {
                if (perguntaQuestionario.getGrupoPergunta().getId().equals(pergunta.getGrupoPergunta().getId())
                        && perguntaQuestionario.getPergunta().getId().equals(pergunta.getPergunta().getId())
                        && (perguntaQuestionario.getIndicadorQuestionario() != null && pergunta.getIndicadorQuestionario() != null)
                        && perguntaQuestionario.getIndicadorQuestionario().getId().equals(pergunta.getIndicadorQuestionario().getId())
                        && !perguntaQuestionario.getId().equals(pergunta.getId())) {
                    throw new BusinessErrorException(getMensagem("app_rst_pergunta_adicionada"));
                }

                if (perguntaQuestionario.getId() != null
                        && !perguntaQuestionario.getId().equals(pergunta.getId())
                        && perguntaQuestionario.getGrupoPergunta().getId().equals(pergunta.getGrupoPergunta().getId())) {
                    if (!perguntaQuestionario.getOrdemGrupo().equals(pergunta.getOrdemGrupo())) {
                        throw new BusinessErrorException(getMensagem("app_rst_mesmo_grupo_pergunta_com_ordem_diferente",
                                pergunta.getGrupoPergunta().getDescricao(), pergunta.getOrdemGrupo()));
                    }
                } else {
                    if (perguntaQuestionario.getGrupoPergunta().getId().equals(pergunta.getGrupoPergunta().getId())
                            && !perguntaQuestionario.getOrdemGrupo().equals(pergunta.getOrdemGrupo())) {
                        throw new BusinessErrorException(getMensagem("app_rst_mesmo_grupo_pergunta_com_ordem_diferente",
                                pergunta.getGrupoPergunta().getDescricao(), pergunta.getOrdemGrupo()));
                    }
                }

                if (perguntaQuestionario.getId() != null
                        && !perguntaQuestionario.getId().equals(pergunta.getId())
                        && !perguntaQuestionario.getGrupoPergunta().getId().equals(pergunta.getGrupoPergunta().getId())) {
                    if (perguntaQuestionario.getOrdemGrupo().equals(pergunta.getOrdemGrupo())) {
                        throw new BusinessErrorException(getMensagem("app_rst_grupo_pergunta_diferente_com_ordem_iguais",
                                pergunta.getOrdemGrupo()));
                    }
                } else {
                    if (!perguntaQuestionario.getGrupoPergunta().getId().equals(pergunta.getGrupoPergunta().getId())
                            && perguntaQuestionario.getOrdemGrupo().equals(pergunta.getOrdemGrupo())) {
                        throw new BusinessErrorException(getMensagem("app_rst_grupo_pergunta_diferente_com_ordem_iguais",
                                pergunta.getOrdemGrupo()));
                    }
                }

                if (perguntaQuestionario.getId() != null
                        && !perguntaQuestionario.getId().equals(pergunta.getId())
                        && perguntaQuestionario.getGrupoPergunta().getId().equals(pergunta.getGrupoPergunta().getId())
                        && perguntaQuestionario.getOrdemPergunta().equals(pergunta.getOrdemPergunta())) {
                    throw new BusinessErrorException(getMensagem("app_rst_pergunta_mesma_ordem"));
                } else {
                    if (perguntaQuestionario.getGrupoPergunta().getId().equals(pergunta.getGrupoPergunta().getId())
                            && perguntaQuestionario.getOrdemPergunta().equals(pergunta.getOrdemPergunta())) {
                        throw new BusinessErrorException(getMensagem("app_rst_pergunta_mesma_ordem"));
                    }
                }
            }
        }
    }

}
