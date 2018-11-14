package br.com.ezvida.rst.service;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UnidadeObraContratoUatDAO;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.UnidadeObraContratoUatFilter;
import br.com.ezvida.rst.model.UnidadeObra;
import br.com.ezvida.rst.model.UnidadeObraContratoUat;
import br.com.ezvida.rst.utils.ValidadorUtils;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class UnidadeObraContratoUatService extends BaseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnidadeObraContratoUatService.class);

    @Inject
    private UnidadeObraContratoUatDAO unidadeObraContratoUatDAO;

    public List<UnidadeObraContratoUat> validarPorEmpresa(String cnpj){
        LOGGER.debug("Validando Unidade Obra...");
        List<UnidadeObraContratoUat> unidadeObraContratoUats;

        if( cnpj != null ) {
            cnpj = cnpj.replace(".","").replace("-","").replace("/","");
            if (ValidadorUtils.isValidCNPJ(cnpj)) {
                unidadeObraContratoUats = unidadeObraContratoUatDAO.validar(cnpj);
                if (unidadeObraContratoUats == null || unidadeObraContratoUats.size() == 0) {
                    throw new BusinessErrorException(getMensagem("app_rst_unidade_invalida",
                            getMensagem("app_rst_label_unidade_obra")));
                }
            } else {
                throw new BusinessErrorException(getMensagem("app_rst_unidade_cnpj_invalida",
                        getMensagem("app_rst_label_cnpj")));
            }
        }else{
            throw new BusinessErrorException(getMensagem("app_rst_unidade_cnpj_invalida",
                    getMensagem("app_rst_label_cnpj")));
        }
        return unidadeObraContratoUats;
    }


    public ListaPaginada<UnidadeObraContratoUat> pesquisarPaginado(UnidadeObraContratoUatFilter unidadeObraContratoUatFilter, ClienteAuditoria auditoria, Long empresaId){

        return unidadeObraContratoUatDAO.pesquisarPaginado(unidadeObraContratoUatFilter, empresaId);

    }

    public UnidadeObraContratoUat salvar( UnidadeObraContratoUat unidadeObraContratoUat){

        validarContrato(unidadeObraContratoUat);

        unidadeObraContratoUat.setFlagInativo("N".charAt(0));

        unidadeObraContratoUatDAO.salvar(unidadeObraContratoUat);

        return unidadeObraContratoUat;
    }

    private void validarContrato(UnidadeObraContratoUat unidadeObraContratoUat){

        if( unidadeObraContratoUat.getUnidadeObra() == null || unidadeObraContratoUat.getUnidadeObra().getId() == null ) {
            throw new BusinessErrorException(getMensagem("app_rst_unidade_obra_contrato_unidade_obra_invalido",
                    getMensagem("app_rst_unidade_obra_contrato_unidade_obra_invalido") ) );
        }else if( unidadeObraContratoUat.getTipoPrograma() == null || unidadeObraContratoUat.getTipoPrograma().getId() == null ){
            throw new BusinessErrorException(getMensagem("app_rst_unidade_obra_contrato_programa_invalido",
                    getMensagem("app_rst_unidade_obra_contrato_programa_invalido") ) );
        }else if( unidadeObraContratoUat.getUnidadeAtendimentoTrabalhador() == null || unidadeObraContratoUat.getUnidadeAtendimentoTrabalhador().getId() == null ){
            throw new BusinessErrorException(getMensagem("app_rst_unidade_obra_contrato_unidade_sesi_invalido",
                    getMensagem("app_rst_unidade_obra_contrato_unidade_sesi_invalido") ) );
        }else if( unidadeObraContratoUat.getDataContratoInicio() == null ){
            throw new BusinessErrorException(getMensagem("app_rst_unidade_obra_contrato_inicio_contrato_invalido",
                    getMensagem("app_rst_unidade_obra_contrato_inicio_contrato_invalido") ) );
        }else if( unidadeObraContratoUat.getDataContratoFim() == null ){
            throw new BusinessErrorException(getMensagem("app_rst_unidade_obra_contrato_fim_contrato_invalido",
                    getMensagem("app_rst_unidade_obra_contrato_fim_contrato_invalido") ) );
        }else if( unidadeObraContratoUat.getAnoVigencia() == null ){
            throw new BusinessErrorException(getMensagem("app_rst_unidade_obra_contrato_ano_vigencia_invalido",
                    getMensagem("app_rst_unidade_obra_contrato_ano_vigencia_invalido") ) );
        }

    }
}
