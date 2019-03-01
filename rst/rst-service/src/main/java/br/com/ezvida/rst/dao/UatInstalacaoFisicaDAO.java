package br.com.ezvida.rst.dao;

import br.com.ezvida.rst.model.UatInstalacaoFisica;
import br.com.ezvida.rst.model.dto.UatInstalacaoFisicaDTO;
import fw.core.jpa.BaseDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UatInstalacaoFisicaDAO extends BaseDAO<UatInstalacaoFisica, Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UatInstalacaoFisicaDAO.class);

    @Inject
    public UatInstalacaoFisicaDAO(EntityManager em) {
        super(em, UatInstalacaoFisica.class);
    }


    public List<UatInstalacaoFisicaDTO> findByUnidade(Long idUnidade) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select ");
        sql.append("	inst_fis.id_uat_instalacao_fisica as id_instalacao_fisica, ");
        sql.append("	cat.descricao as categoria, ");
        sql.append("	amb.descricao as ambiente, ");
        sql.append("	inst_fis.quantidade , ");
        sql.append("	inst_fis.area , ");
        sql.append("    inst_fis.id_und_atd_trabalhador_fk as id_unidade ");
        sql.append(" from ");
        sql.append("	uat_instalacao_fisica inst_fis ");
        sql.append(" inner join uat_instalacao_fisica_ambiente amb on ");
        sql.append("	inst_fis.id_uat_instalacao_fisica_ambiente_fk = amb.id_uat_instalacao_fisica_ambiente ");
        sql.append(" inner join uat_instalacao_fisica_categoria cat on ");
        sql.append("	amb.id_uat_instalacao_fisica_categoria_fk = cat.id_uat_instalacao_fisica_categoria ");
        sql.append(" where ");
        sql.append("	inst_fis.id_und_atd_trabalhador_fk = :idUnidade ");
        sql.append(" and dt_exclusao is null ");
        Query query = getEm().createNativeQuery(sql.toString());
        query.setParameter("idUnidade", idUnidade);
        List<Object[]> listaInstalacoesFisicaBanco = query.getResultList();

        UatInstalacaoFisicaDTO indicadorDTO;
        List<UatInstalacaoFisicaDTO> listaInstalacaoFisicaDTO = new ArrayList<>();
        for (Object[] objects : listaInstalacoesFisicaBanco) {
            indicadorDTO = new UatInstalacaoFisicaDTO(new Long(((Integer) objects[0]).intValue()), (String) objects[1], (String) objects[2], (Integer) objects[3], (BigDecimal) objects[4], new Long(((Integer) objects[5]).intValue()));
            listaInstalacaoFisicaDTO.add(indicadorDTO);
        }
        return listaInstalacaoFisicaDTO;
    }

    public void desativar(Long id) {
        LOGGER.debug("Inativando Instalação Física");
        String sql = "  update uat_instalacao_fisica set dt_exclusao = :data where id_uat_instalacao_fisica = :id";
        Query query = getEm().createNativeQuery(sql);
        query.setParameter("id", id);
        query.setParameter("data", new Date());
        query.executeUpdate();
    }

}
