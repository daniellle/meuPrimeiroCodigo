package br.com.ezvida.rst.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UatInstalacaoFisicaAmbienteDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.model.UatInstalacaoFisicaAmbiente;
import fw.core.service.BaseService;

@Stateless
public class UatInstalacaoFisicaAmbienteService extends BaseService {


    private static final long serialVersionUID = 8692359712822744235L;

    @Inject
    private UatInstalacaoFisicaAmbienteDAO uatInstalacaoFisicaAmbienteDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(UatInstalacaoFisicaAmbienteService.class);

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UatInstalacaoFisicaAmbiente> findByCategoria(Long idCategoria, ClienteAuditoria auditoria, DadosFilter dados) {
        LogAuditoria.registrar(LOGGER, auditoria, "Buscando ambiente por id da categoria {}");
        return uatInstalacaoFisicaAmbienteDAO.findByCategoria(idCategoria);
    }
}
