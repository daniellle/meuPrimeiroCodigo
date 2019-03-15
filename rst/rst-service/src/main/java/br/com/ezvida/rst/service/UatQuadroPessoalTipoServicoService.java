package br.com.ezvida.rst.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UatQuadroPessoalTipoServicoDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.model.UatQuadroPessoalTipoServico;
import fw.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class UatQuadroPessoalTipoServicoService extends BaseService {

    private static final long serialVersionUID = 8796981225922092479L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UatQuadroPessoalTipoServicoService.class);

    @Inject
    private UatQuadroPessoalTipoServicoDAO uatQuadroPessoalTipoServicoDAO;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UatQuadroPessoalTipoServico> findByArea(Long idArea, ClienteAuditoria auditoria, DadosFilter dados) {
        LogAuditoria.registrar(LOGGER, auditoria, "Buscando tipo serviço por área Quadro Pessoal");
        return this.uatQuadroPessoalTipoServicoDAO.findByArea(idArea);
    }
}
