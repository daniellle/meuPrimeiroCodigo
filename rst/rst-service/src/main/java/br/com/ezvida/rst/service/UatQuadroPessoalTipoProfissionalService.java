
package br.com.ezvida.rst.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UatQuadroPessoalTipoProfissionalDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.model.UatQuadroPessoalTipoProfissional;
import fw.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class UatQuadroPessoalTipoProfissionalService extends BaseService {

    private static final long serialVersionUID = 6919743145644039309L;

    private static final Logger LOGGER = LoggerFactory.getLogger(UatQuadroPessoalTipoProfissionalService.class);

    @Inject
    private UatQuadroPessoalTipoProfissionalDAO uatQuadroPessoalTipoProfissionalDAO;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<UatQuadroPessoalTipoProfissional> findByTipoServico(Long idTipoServico, ClienteAuditoria auditoria, DadosFilter dados) {
        LogAuditoria.registrar(LOGGER, auditoria, "Buscando Tipo Profissional Quadro Pessoal");
        return this.uatQuadroPessoalTipoProfissionalDAO.findByTipoServico(idTipoServico);
    }

}
