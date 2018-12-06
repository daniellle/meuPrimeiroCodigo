package br.com.ezvida.rst.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;

public class VidasAtivasDAO {

    @Inject
    EntityManager em;

    public BigInteger quantidadeVidasAtivas(){
        StringBuilder sql = new StringBuilder();
        sql.append(" select  count(distinct t.id_trabalhador) ")
                .append(" from und_obra o ")
                .append(" inner join emp_lotacao l on o.id_und_obra = l.id_und_obra_fk ")
                .append(" inner join emp_trab_lotacao l2 on l.id_empresa_lotacao = l2.id_emp_lotacao_fk ")
                .append(" inner join emp_trabalhador et on l2.id_empr_trabalhador_fk = et.id_emp_trabalhador ")
                .append(" inner join trabalhador t on et.id_trabalhador_fk = t.id_trabalhador ")
                .append(" where (((o.dt_contrato_ini is not null or o.dt_contrato_ini > now()) and o.dt_contrato_fim > now()) ")
                .append(" or (o.dt_contrato_ini < now() and o.dt_contrato_fim > now())) ")
                .append(" and (o.fl_inativo = 'N') ")
                .append(" and ((l2.dt_desligamento is null or l2.dt_desligamento > now()) and (l2.fl_inativo = 'N' or l2.fl_inativo is null))");

        Query query =  em.createQuery(sql.toString());
        return (BigInteger)query.getSingleResult();
    }

}
