package br.com.ezvida.rst.service;

import br.com.ezvida.rst.dao.TipoProgramaDAO;
import br.com.ezvida.rst.model.TipoPrograma;
import fw.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.List;

public class TipoProgramaService extends BaseService {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TipoProgramaService.class);

    @Inject
    private TipoProgramaDAO tipoProgramaDAO;

    public List<TipoPrograma> buscarTodos(){
        LOGGER.debug("Service - Buscando todos os tipos de programa");

        return tipoProgramaDAO.pesquisarTodos();
    }

}
