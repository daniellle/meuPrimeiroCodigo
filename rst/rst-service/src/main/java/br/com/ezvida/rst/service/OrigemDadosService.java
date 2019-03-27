package br.com.ezvida.rst.service;

import br.com.ezvida.rst.dao.OrigemDadosDAO;
import br.com.ezvida.rst.model.OrigemDados;
import fw.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.math.BigInteger;
import java.util.List;

@Stateless
public class OrigemDadosService extends BaseService {

    private static final long serialVersionUID = -2455135657709852828L;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrigemDadosService.class);

    @Inject
    private OrigemDadosDAO origemDadosDAO;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<OrigemDados> listarTodos() {
        LOGGER.debug("Listando Origens de Dados");
        return origemDadosDAO.pesquisarTodos();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public BigInteger countByDescricao(String descricao){
        return origemDadosDAO.countByDescricao(descricao);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<OrigemDados> findOrigemDadosToContrato(){
        LOGGER.debug("Listando origens de dados para combo origem do contrato");
        return this.origemDadosDAO.findOrigemDadosToContrato();
    }
}
