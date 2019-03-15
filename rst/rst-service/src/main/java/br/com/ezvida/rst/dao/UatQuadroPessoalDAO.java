package br.com.ezvida.rst.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.model.UatQuadroPessoal;
import br.com.ezvida.rst.model.dto.UatQuadroPessoalDTO;
import fw.core.jpa.BaseDAO;

public class UatQuadroPessoalDAO extends BaseDAO<UatQuadroPessoal, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UatInstalacaoFisicaDAO.class);

    @Inject
    public UatQuadroPessoalDAO(EntityManager em) {
        super(em, UatQuadroPessoal.class);
    }

    public List<UatQuadroPessoalDTO> findByUnidade(Long idUnidade) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append("	area.descricao as area_descricao, ");
        sql.append("	tipo_servico.descricao as tipo_servico_descricao, ");
        sql.append("	tipo_profissional.descricao as tipo_profissional_descricao, ");
        sql.append("	quadro_pessoal.quantidade, ");
        sql.append("	quadro_pessoal.id_uat_quadro_pessoal as id_quadro_pessoal, ");
        sql.append("	quadro_pessoal.id_und_atd_trabalhador_fk as id_uat, ");
        sql.append("	tipo_profissional.id_uat_quadro_pessoal_tipo_profissional as tipo_profissional_id ");
        sql.append(" from ");
        sql.append("	uat_quadro_pessoal quadro_pessoal ");
        sql.append(" inner join uat_quadro_pessoal_tipo_profissional tipo_profissional on ");
        sql.append("	quadro_pessoal.id_uat_quadro_pessoal_tipo_profissional_fk = tipo_profissional.id_uat_quadro_pessoal_tipo_profissional ");
        sql.append(" inner join uat_quadro_pessoal_tipo_servico tipo_servico on ");
        sql.append("	tipo_profissional.id_uat_quadro_pessoal_tipo_servico_fk = tipo_servico.id_uat_quadro_pessoal_tipo_servico ");
        sql.append(" inner join uat_quadro_pessoal_area area on ");
        sql.append("	tipo_servico.id_uat_quadro_pessoal_area_fk = area.id_uat_quadro_pessoal_area ");
        sql.append(" where ");
        sql.append("	dt_exclusao is null ");
        sql.append("	and quadro_pessoal.id_und_atd_trabalhador_fk = :idUnidade ");

        Query query = getEm().createNativeQuery(sql.toString());
        query.setParameter("idUnidade", idUnidade);
        List<Object[]> listaInstalacoesFisicaBanco = query.getResultList();

        UatQuadroPessoalDTO indicadorDTO;
        List<UatQuadroPessoalDTO> listaInstalacaoFisicaDTO = new ArrayList<>();
        for (Object[] objects : listaInstalacoesFisicaBanco) {
            indicadorDTO = new UatQuadroPessoalDTO((String) objects[0], (String) objects[1], (String) objects[2], (Integer) objects[3], new Long(((Integer) objects[4]).intValue()), new Long(((Integer) objects[5]).intValue()), new Long(((Integer) objects[6]).intValue()));
            listaInstalacaoFisicaDTO.add(indicadorDTO);
        }
        return listaInstalacaoFisicaDTO;
    }


    public void desativar(Long id) {
        LOGGER.debug("Desativando Instalação Física");
        String sql = "  update uat_quadro_pessoal set dt_exclusao = :data where id_uat_quadro_pessoal = :id";
        Query query = getEm().createNativeQuery(sql);
        query.setParameter("id", id);
        query.setParameter("data", new Date());
        query.executeUpdate();
    }
}
