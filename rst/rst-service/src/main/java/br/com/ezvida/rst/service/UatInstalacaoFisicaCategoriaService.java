package br.com.ezvida.rst.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.TrabalhadorDAO;
import br.com.ezvida.rst.dao.UatInstalacaoFisicaCategoriaDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.model.UatInstalacaoFisicaCategoria;
import fw.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class UatInstalacaoFisicaCategoriaService extends BaseService {

    private static final long serialVersionUID = 3803409672517581214L;

    @Inject
    private UatInstalacaoFisicaCategoriaDAO uatInstalacaoFisicaCategoriaDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(TrabalhadorDAO.class);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UatInstalacaoFisicaCategoria> listarTodos(ClienteAuditoria auditoria, DadosFilter dados) {
        LogAuditoria.registrar(LOGGER, auditoria, "Listar todas Periodicidade");
        return uatInstalacaoFisicaCategoriaDAO.pesquisarTodos();
    }
}
