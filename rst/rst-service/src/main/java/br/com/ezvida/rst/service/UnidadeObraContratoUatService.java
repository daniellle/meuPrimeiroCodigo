package br.com.ezvida.rst.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UnidadeObraContratoUatDAO;
import br.com.ezvida.rst.dao.filter.ListaPaginada;
import br.com.ezvida.rst.dao.filter.UnidadeObraContratoUatFilter;
import br.com.ezvida.rst.model.UnidadeObraContratoUat;
import br.com.ezvida.rst.utils.ValidadorUtils;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;

@Stateless
public class UnidadeObraContratoUatService extends BaseService {

	private static final long serialVersionUID = 1L;

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

        unidadeObraContratoUat.setFlagInativo('N');

        unidadeObraContratoUatDAO.salvar(unidadeObraContratoUat);

        return unidadeObraContratoUat;
    }

    public UnidadeObraContratoUat desativar( UnidadeObraContratoUat unidadeObraContratoUat) {
        if ( unidadeObraContratoUat.getId() == null ) {
            throw new BusinessErrorException(getMensagem("app_rst_unidade_obra_contrato_id_invalido",
                    getMensagem("app_rst_unidade_obra_contrato_id_invalido") ) );
        } else if ( unidadeObraContratoUat.getFlagInativo() == null) {
            throw new BusinessErrorException(getMensagem("app_rst_unidade_obra_contrato_flag_inativo_invalido",
                    getMensagem("app_rst_unidade_obra_contrato_flag_inativo_invalido") ) );
        }

        Character flag = unidadeObraContratoUat.getFlagInativo();

        unidadeObraContratoUat = unidadeObraContratoUatDAO.pesquisarPorId(unidadeObraContratoUat.getId());

        if (unidadeObraContratoUat == null) {
            throw new BusinessErrorException(getMensagem("app_rst_unidade_invalida",
                    getMensagem("app_rst_label_unidade_obra")));
        }

        unidadeObraContratoUat.setFlagInativo(flag);
        unidadeObraContratoUat.setDataInativo(new Date());

        unidadeObraContratoUatDAO.salvar(unidadeObraContratoUat);

        return null;
    }

    public UnidadeObraContratoUat ativar( UnidadeObraContratoUat unidadeObraContratoUat){
        if (unidadeObraContratoUat.getId() == null) {
            throw new BusinessErrorException(getMensagem("app_rst_unidade_obra_contrato_id_invalido",
                    getMensagem("app_rst_unidade_obra_contrato_id_invalido") ) );
        } else if (unidadeObraContratoUat.getFlagInativo() == null) {
            throw new BusinessErrorException(getMensagem("app_rst_unidade_obra_contrato_flag_inativo_invalido",
                    getMensagem("app_rst_unidade_obra_contrato_flag_inativo_invalido") ) );
        }

        Integer flag = Integer.parseInt(unidadeObraContratoUat.getFlagInativo().toString());

        if (flag < 1 || flag > 3) {
            throw new BusinessErrorException(getMensagem("app_rst_unidade_obra_contrato_flag_inativo_perfil_invalido",
                    getMensagem("app_rst_unidade_obra_contrato_flag_inativo_perfil_invalido") ) );
        }

        unidadeObraContratoUat = unidadeObraContratoUatDAO.pesquisarPorId(unidadeObraContratoUat.getId());

        if (unidadeObraContratoUat == null) {
            throw new BusinessErrorException(getMensagem("app_rst_unidade_invalida",
                    getMensagem("app_rst_label_unidade_obra")));
        }
        
        if (unidadeObraContratoUat.getFlagInativo() == null) {
            throw new BusinessErrorException(getMensagem("app_rst_unidade_obra_contrato_unidade_ativada",
                    getMensagem("app_rst_unidade_obra_contrato_unidade_ativada") ) );
        }
        Integer flagAtual = Integer.parseInt(unidadeObraContratoUat.getFlagInativo().toString());

         if (flag < flagAtual) {
            throw new BusinessErrorException(getMensagem("app_rst_unidade_obra_contrato_perfil_invalido",
                    getMensagem("app_rst_unidade_obra_contrato_perfil_invalido")));
        }

        unidadeObraContratoUat.setFlagInativo(null);
        unidadeObraContratoUat.setDataInativo(null);

        unidadeObraContratoUatDAO.salvar(unidadeObraContratoUat);

        return null;
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
        }else{
            int diff = getZeroTimeDate(unidadeObraContratoUat.getDataContratoFim() ).compareTo(getZeroTimeDate(unidadeObraContratoUat.getDataContratoInicio() ) );

            if( diff < 0 ){
                throw new BusinessErrorException(getMensagem("app_rst_unidade_obra_contrato_datas_invalido",
                        getMensagem("app_rst_unidade_obra_contrato_datas_invalido") ) );
            }
        }

    }

    private Date getZeroTimeDate(Date fecha) {
        Date res = fecha;
        Calendar calendar = Calendar.getInstance();

        calendar.setTime( fecha );
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        res = calendar.getTime();

        return res;
    }
}
