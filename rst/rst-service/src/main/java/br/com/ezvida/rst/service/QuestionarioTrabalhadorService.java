package br.com.ezvida.rst.service;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.*;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.QuestionarioTrabalhadorFilter;
import br.com.ezvida.rst.model.*;
import br.com.ezvida.rst.model.dto.IndicadorDTO;
import br.com.ezvida.rst.service.excpetions.RegistroNaoEncontradoException;
import fw.core.exception.BusinessErrorException;
import fw.core.exception.NotFoundException;
import fw.core.service.BaseService;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class QuestionarioTrabalhadorService extends BaseService {

    private static final long serialVersionUID = 8092379126782496057L;

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionarioTrabalhadorService.class);

    private static final String QUESTIONARIO_TRABALHADOR = "QuestionarioTrabalhador";

    @Inject
    private QuestionarioTrabalhadorDAO questionarioTrabalhadorDAO;

    @Inject
    private QuestionarioDAO questionarioDAO;

    @Inject
    private RespostaQuestionarioTrabalhadorDAO respostaQuestionarioTrabalhadorDAO;

    @Inject
    private RespostaQuestionarioDAO respostaQuestionarioDAO;

    @Inject
    private ClassificacaoPontuacaoDAO classificacaoPontuacaoDAO;

    @Inject
    private TrabalhadorDAO trabalhadorDAO;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public QuestionarioTrabalhador buscarPorId(Long id, ClienteAuditoria auditoria) {
        if (id == null) {
            throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
        }

        QuestionarioTrabalhador questionarioTrabalhador = questionarioTrabalhadorDAO.pesquisarPorId(id);
        if (questionarioTrabalhador == null) {
            throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
        }

        LogAuditoria.registrar(LOGGER, auditoria, "Pesquisa de QuestionarioTrabalhador por id: " + id);

        return questionarioTrabalhador;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean getUltimoRegistro(Long idTrabalhador) {
        QuestionarioTrabalhador questionarioTrabalhador = questionarioTrabalhadorDAO.getUltimoRegisto(idTrabalhador);
        if (questionarioTrabalhador != null) {
            Days dias = Days.daysBetween(new DateTime(questionarioTrabalhador.getDataQuestionarioTrabalhador()),
                    new DateTime());
            if (dias.getDays() > questionarioTrabalhador.getQuestionario().getPeriodicidade().getQuantidadeDias()) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Boolean getUltimoRegistro(String cpf) {
        Trabalhador trabalhador = trabalhadorDAO.pesquisarPorCpf(cpf);
        if (trabalhador == null) {
            throw new NotFoundException("Trabalhador não encontrado");
        }
        return getUltimoRegistro(trabalhador.getId());
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public QuestionarioTrabalhador buscarPorIdCompleto(Long id, ClienteAuditoria auditoria) {
        if (id == null) {
            throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
        }

        QuestionarioTrabalhador questionarioTrabalhador = questionarioTrabalhadorDAO.buscarPorIdCompleto(id);
        if (questionarioTrabalhador == null) {
            throw new RegistroNaoEncontradoException(getMensagem("app_rst_nenhum_registro_encontrado"));
        }

        if (auditoria != null) {
            LogAuditoria.registrar(LOGGER, auditoria, "Pesquisa de QuestionarioTrabalhador por id: " + id);
        }
        return questionarioTrabalhador;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ListaPaginada<QuestionarioTrabalhador> pesquisaPaginada(
            QuestionarioTrabalhadorFilter questionarioTrabalhadorFilter, ClienteAuditoria auditoria) {
        if (auditoria != null) {
            LogAuditoria.registrar(LOGGER, auditoria, "Pesquisando QuestionarioTrabalhadorFilter por filtro.",
                    questionarioTrabalhadorFilter);
        }
        return questionarioTrabalhadorDAO.pesquisarPaginado(questionarioTrabalhadorFilter);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public ListaPaginada<QuestionarioTrabalhador> pesquisaPaginada(String cpf, Integer pagina, Integer qtdRegistro, ClienteAuditoria auditoria) {
        Trabalhador trabalhador = trabalhadorDAO.pesquisarPorCpf(cpf);
        if (trabalhador == null) {
            throw new RegistroNaoEncontradoException("Trabalhador não encontrado");
        }
        QuestionarioTrabalhadorFilter questFilter = new QuestionarioTrabalhadorFilter();
        questFilter.setId(trabalhador.getId());
        questFilter.setPagina(pagina);
        questFilter.setQuantidadeRegistro(qtdRegistro);
        return this.pesquisaPaginada(questFilter, auditoria);

    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public int calcularPontuacao(QuestionarioTrabalhador questionarioTrabalhador) {
        int pontuacaoTotal = 0;
        List<Long> idsRespostasQuestionario = new ArrayList<Long>();
        for (RespostaQuestionarioTrabalhador respostaQuestionarioTrabalhador : questionarioTrabalhador
                .getListaRespostaQuestionarioTrabalhador()) {
            idsRespostasQuestionario.add(respostaQuestionarioTrabalhador.getRespostaQuestionario().getId());
        }
        List<IndicadorDTO> listaIndicadores = respostaQuestionarioTrabalhadorDAO
                .getListaIndicadorePorIdPerguntaQuestionario(idsRespostasQuestionario);
        for (IndicadorDTO indicadorDTO : listaIndicadores) {
            if (!indicadorDTO.getAprovado()) {
                pontuacaoTotal++;
            }
        }
        return pontuacaoTotal;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public ClassificacaoPontuacao calcularClassificacao(int pontuacaoTotal) {
        return classificacaoPontuacaoDAO.buscarClassificacaoPorPontuacao(pontuacaoTotal);
    }

    public void atualizaPerguntas(QuestionarioTrabalhador questionarioTrabalhador) {
        for (RespostaQuestionarioTrabalhador respostaQuestionarioTrabalhador : questionarioTrabalhador
                .getListaRespostaQuestionarioTrabalhador()) {
            respostaQuestionarioTrabalhador.getRespostaQuestionario().getId();
            RespostaQuestionario respostaQuestionario = respostaQuestionarioDAO
                    .pesquisarPorId(respostaQuestionarioTrabalhador.getRespostaQuestionario().getId());
            respostaQuestionarioTrabalhador.setRespostaQuestionario(respostaQuestionario);
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public QuestionarioTrabalhador salvar(QuestionarioTrabalhador questionarioTrabalhador, ClienteAuditoria auditoria) {

        if (questionarioTrabalhador == null) {
            throw new BusinessErrorException(getMensagem("app_rst_parametro_nulo"));
        }
        validar(questionarioTrabalhador);

        String descricaoAuditoria = "Cadastro de " + QUESTIONARIO_TRABALHADOR + ": ";
        if (questionarioTrabalhador.getId() != null) {
            descricaoAuditoria = "Alteração no cadastro de " + QUESTIONARIO_TRABALHADOR + ": ";
        }

        atualizaPerguntas(questionarioTrabalhador);

        int pontuacaoTotal = calcularPontuacao(questionarioTrabalhador);

        ClassificacaoPontuacao classificacaoPontuacao = calcularClassificacao(pontuacaoTotal);
        questionarioTrabalhador.setClassificacaoPontuacao(classificacaoPontuacao);
        questionarioTrabalhador.setQuantidadePonto(pontuacaoTotal);

        questionarioTrabalhador.setTrabalhador(getTrabalhador(questionarioTrabalhador));

        questionarioTrabalhadorDAO.salvar(questionarioTrabalhador);

        for (RespostaQuestionarioTrabalhador respostaQuestionarioTrabalhador : questionarioTrabalhador
                .getListaRespostaQuestionarioTrabalhador()) {
            respostaQuestionarioTrabalhador.setQuestionarioTrabalhador(questionarioTrabalhador);
            respostaQuestionarioTrabalhadorDAO.salvar(respostaQuestionarioTrabalhador);
        }

        if (auditoria != null) {
            LogAuditoria.registrar(LOGGER, auditoria, descricaoAuditoria, questionarioTrabalhador);
        }
        return questionarioTrabalhador;
    }

    private Trabalhador getTrabalhador(QuestionarioTrabalhador questionarioTrabalhador) {
        Trabalhador trabalhador = trabalhadorDAO.pesquisarPorId(questionarioTrabalhador.getTrabalhador().getId());
        if (trabalhador == null) {
            throw new RegistroNaoEncontradoException("Trabalhador não encontrado");
        }
        return trabalhador;
    }

    private void validar(QuestionarioTrabalhador questionarioTrabalhador) {
        if (questionarioTrabalhador.getQuestionario().getId() != null) {
            Questionario question = questionarioDAO.pesquisarPorId(questionarioTrabalhador.getQuestionario().getId());
            if (question == null) {
                throw new RegistroNaoEncontradoException("Questionário não encontrado");
            } else {
                questionarioTrabalhador.setQuestionario(question);
            }

        }
        if (questionarioTrabalhador.getTrabalhador().getId() != null) {
            if (!this.getUltimoRegistro(questionarioTrabalhador.getTrabalhador().getId())) {
                throw new BusinessErrorException("Você já preencheu o seu IGEV do período. Aguarde o próximo ciclo.");
            }
        }
    }

}
