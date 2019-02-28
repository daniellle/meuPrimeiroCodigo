package br.com.ezvida.rst.service;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UatQuadroPessoalAreaDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.model.UatQuadroPessoalArea;
import fw.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class UatQuadroPessoalAreaService extends BaseService {

    private static final long serialVersionUID = -642667528948172399L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UatInstalacaoFisicaAmbienteService.class);

    @Inject
    private UatQuadroPessoalAreaDAO uatQuadroPessoalAreaDAO;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UatQuadroPessoalArea> listarTodos(ClienteAuditoria auditoria, DadosFilter dados) {
        LogAuditoria.registrar(LOGGER, auditoria, "Buscando Área Quadro Pessoal");
        return uatQuadroPessoalAreaDAO.pesquisarTodos();
    }

}
