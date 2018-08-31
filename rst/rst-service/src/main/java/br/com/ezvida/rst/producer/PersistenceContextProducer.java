package br.com.ezvida.rst.producer;

import fw.core.jpa.H2Database;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

public class PersistenceContextProducer {

    @PersistenceContext(unitName = "rst", name = "rst")
    private EntityManager em;

    @PersistenceUnit(unitName = "rst", name = "rst")
    private EntityManagerFactory emf;

    @Default
    @Produces
    public EntityManager getConexaoPadrao() {
        return em;
    }

    @Produces
    @H2Database
    @RequestScoped
    public EntityManager criarConexaoNaoGerenciada() {
        return emf.createEntityManager();
    }

    public void close(@Disposes @H2Database EntityManager em) {
        if (em != null) {
            em.close();
        }
    }

}