package br.com.ezvida.rst.dao.filter;

import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class DadosFilter implements Serializable {

    private static final long serialVersionUID = -3468814525094214305L;

    public static final String ADMINISTRADOR = "ADM";
    public static final String DIRETOR_DR = "DIDR";
    public static final String DIRETOR_DN = "DIDN";
    public static final String GESTOR_DR = "GDRA";
    public static final String GESTOR_DR_MASTER = "GDRM";
    public static final String GESTOR_EMPRESA = "GEEM";
    public static final String GESTOR_EMPRESA_MASTER = "GEEMM";
    public static final String GESTOR_SINDICATO = "GESI";
    public static final String TRABALHADOR = "TRA";
    public static final String GESTOR_PARCEIRO = "GEPC";
    public static final String GESTOR_REDE = "GERC";
    public static final String GESTOR_DN = "GDNA";
    public static final String PROF_SAUDE = "PFS";
    public static final String REC_HUMANOS = "RH";
    public static final String SEG_TRABALHO = "ST";
    public static final String SUPERINTENDENTE_DR = "SUDR";
    public static final String MEDICO_TRABALHO_DN = "MTSDN";
    public static final String GESTOR_CONTEUDO_DN = "GCDN";
    public static final String MEDICO_TRABALHO_DR = "MTSDR";
    public static final String GESTOR_COMERCIAL_DR = "GCDR";
    public static final String GESTOR_UNIDADE_SESI = "GUS";


    private boolean administrador;

    private boolean diretoriaDr;

    private boolean diretoriaDn;

    private boolean gestorDr;

    private boolean gestorDn;

    private boolean gestorEmpresa;

    private boolean gestorSindicato;

    private boolean trabalhador;

    private boolean gestorParceiroCredenciado;

    private boolean gestorRedeCredenciada;

    private boolean getorUnidadeSESI;

    private boolean temIdsEmpresa;

    private boolean temIdsSindicato;

    private boolean temIdsParceiro;

    private boolean temIdsRedeCredenciada;

    private boolean temIdsTrabalhador;

    private boolean temIdsUnidadeSESI;

    private Set<String> papeis;

    private Set<Long> idsDepartamentoRegional;

    private Set<Long> idsEmpresa;

    private Set<Long> idsSindicato;

    private Set<Long> idsParceiro;

    private Set<Long> idsRedeCredenciada;

    private Set<Long> idsTrabalhador;

    private Set<Long> idsUnidadeSESI;

    private Set<String> listaPerfisPermitidos;

    public DadosFilter() {

    }

    public DadosFilter(Set<String> papeis, Set<Long> departamentos, Set<Long> empresas, Set<Long> parceiros,
                       Set<Long> redesCredenciadas, Set<Long> sindicatos, Set<Long> idsTrabalhadores, Set<Long> idsUnidadeSESI) {

        this.papeis = papeis;
        this.administrador = contemPapel(ADMINISTRADOR);
        this.diretoriaDr = contemPapel(DIRETOR_DR);
        this.diretoriaDn = contemPapel(DIRETOR_DN);
        this.gestorDr = contemPapeis(GESTOR_DR, GESTOR_DR_MASTER, SUPERINTENDENTE_DR, MEDICO_TRABALHO_DR, GESTOR_COMERCIAL_DR);
        this.gestorDn = contemPapeis(GESTOR_DN, MEDICO_TRABALHO_DN, GESTOR_CONTEUDO_DN);
        this.gestorEmpresa = contemPapeis(GESTOR_EMPRESA, GESTOR_EMPRESA_MASTER, PROF_SAUDE, REC_HUMANOS, SEG_TRABALHO);
        this.gestorParceiroCredenciado = contemPapel(GESTOR_PARCEIRO);
        this.gestorRedeCredenciada = contemPapel(GESTOR_REDE);
        this.gestorSindicato = contemPapel(GESTOR_SINDICATO);
        this.trabalhador = contemPapel(TRABALHADOR);
        this.getorUnidadeSESI = contemPapel(GESTOR_UNIDADE_SESI);

        if (diretoriaDr || gestorDr) {
            this.idsDepartamentoRegional = addHash(departamentos);
        }

        if (gestorEmpresa && !(diretoriaDr || gestorDr)) {
            this.idsEmpresa = addHash(empresas);
        }

        if (getorUnidadeSESI && !(diretoriaDr || gestorDr || gestorEmpresa)) {
            this.idsUnidadeSESI = addHash(idsUnidadeSESI);
        }

        if (trabalhador && !(getorUnidadeSESI || diretoriaDr || gestorDr || gestorEmpresa)) {
            this.idsTrabalhador = addHash(idsTrabalhadores);
        }


        // TODO SO PREEENCHER DE ACORDO COM O PERFIL
        this.idsParceiro = parceiros;
        this.idsRedeCredenciada = redesCredenciadas;
        this.idsSindicato = sindicatos;

        this.temIdsEmpresa = CollectionUtils.isNotEmpty(this.idsEmpresa);
        this.temIdsParceiro = CollectionUtils.isNotEmpty(this.idsParceiro);
        this.temIdsRedeCredenciada = CollectionUtils.isNotEmpty(this.idsRedeCredenciada);
        this.temIdsSindicato = CollectionUtils.isNotEmpty(this.idsSindicato);
        this.temIdsUnidadeSESI = CollectionUtils.isNotEmpty(this.idsUnidadeSESI);

        this.temIdsTrabalhador = CollectionUtils.isNotEmpty(this.idsTrabalhador);

        this.preenchePerfil();

    }

    private Set<Long> addHash(Set<Long> obj) {
        return CollectionUtils.isEmpty(obj) ? Sets.newHashSet(0L) : obj;
    }

    private void preenchePerfil() {
        if (isTrabalhador()) {
            listaPerfisPermitidos = Sets.newHashSet("NA");
        }

        if (isGestorRedeCredenciada()) {
            listaPerfisPermitidos = Sets.newHashSet("NA");
        }

        if (isGestorParceiroCredenciado()) {
            listaPerfisPermitidos = Sets.newHashSet("NA");
        }

        if (isGestorSindicato()) {
            listaPerfisPermitidos = Sets.newHashSet("NA");
        }

        if (isGetorUnidadeSESI()) {
            listaPerfisPermitidos = Sets.newHashSet(GESTOR_UNIDADE_SESI);
        }

        if (isGestorEmpresa()) {
            listaPerfisPermitidos = Sets.newHashSet(GESTOR_SINDICATO, GESTOR_PARCEIRO, GESTOR_REDE);
        }

        if (isGestorDr() || isDiretoriaDr()) {
            listaPerfisPermitidos = Sets.newHashSet(TRABALHADOR, GESTOR_SINDICATO, GESTOR_PARCEIRO, GESTOR_REDE,
                    GESTOR_EMPRESA, GESTOR_EMPRESA_MASTER);
        }

        if (isGestorDn() || isDiretoriaDn()) {
            listaPerfisPermitidos = Sets.newHashSet(TRABALHADOR, GESTOR_SINDICATO, GESTOR_PARCEIRO, GESTOR_REDE,
                    GESTOR_EMPRESA, GESTOR_DR, GESTOR_DR_MASTER, SUPERINTENDENTE_DR, MEDICO_TRABALHO_DR, GESTOR_COMERCIAL_DR, DIRETOR_DR);
        }
    }

    public Set<String> getPapeis() {
        return papeis;
    }

    public void setPapeis(Set<String> papeis) {
        this.papeis = papeis;
    }

    public Set<Long> getIdsDepartamentoRegional() {
        return idsDepartamentoRegional;
    }

    public void setIdsDepartamentoRegional(Set<Long> idsDepartamentoRegional) {
        this.idsDepartamentoRegional = idsDepartamentoRegional;
    }

    public Set<Long> getIdsEmpresa() {
        return idsEmpresa;
    }

    public void setIdsEmpresa(Set<Long> idsEmpresa) {
        this.idsEmpresa = idsEmpresa;
    }

    public Set<Long> getIdsSindicato() {
        return idsSindicato;
    }

    public void setIdsSindicato(Set<Long> idsSindicato) {
        this.idsSindicato = idsSindicato;
    }

    public Set<Long> getIdsParceiro() {
        return idsParceiro;
    }

    public void setIdsParceiro(Set<Long> idsParceiro) {
        this.idsParceiro = idsParceiro;
    }

    public Set<Long> getIdsRedeCredenciada() {
        return idsRedeCredenciada;
    }

    public void setIdsRedeCredenciada(Set<Long> idsRedeCredenciada) {
        this.idsRedeCredenciada = idsRedeCredenciada;
    }

    public Set<Long> getIdsTrabalhador() {
        return idsTrabalhador;
    }

    public void setIdsTrabalhador(Set<Long> idsTrabalhador) {
        this.idsTrabalhador = idsTrabalhador;
    }

    public Set<Long> getIdsUnidadeSESI() {
        return idsUnidadeSESI;
    }

    public void setIdsUnidadeSESI(Set<Long> idsUnidadeSESI) {
        this.idsUnidadeSESI = idsUnidadeSESI;
    }

    public boolean temIdsSindicato() {
        return temIdsSindicato;
    }

    public boolean temIdsDepRegional() {
        return CollectionUtils.isNotEmpty(this.idsDepartamentoRegional);
    }

    public boolean temIdsEmpresa() {
        return temIdsEmpresa;
    }

    public boolean temIdsParceiro() {
        return temIdsParceiro;
    }

    public boolean temIdsRedeCredenciada() {
        return temIdsRedeCredenciada;
    }

    public boolean temIdsTrabalhador() {
        return temIdsTrabalhador;
    }

    public boolean temIdsUnidadeSESI() {
        return temIdsUnidadeSESI;
    }

    public boolean isDiretoriaDr() {
        return diretoriaDr;
    }

    public boolean isDiretoriaDn() {
        return diretoriaDn;
    }

    public boolean isGestorDr() {
        return gestorDr;
    }

    public boolean isGestorDn() {
        return gestorDn;
    }

    public boolean isSuperUsuario() {
        return this.isAdministrador() || this.isGestorDn() | this.isDiretoriaDn();
    }

    public void setGestorDn(boolean gestorDn) {
        this.gestorDn = gestorDn;
    }

    public boolean isGestorEmpresa() {
        return gestorEmpresa;
    }

    public boolean isGestorSindicato() {
        return gestorSindicato;
    }

    public boolean isTrabalhador() {
        return trabalhador;
    }

    public boolean isGestorParceiroCredenciado() {
        return gestorParceiroCredenciado;
    }

    public boolean isGestorRedeCredenciada() {
        return gestorRedeCredenciada;
    }

    public boolean isAdministrador() {
        return administrador;
    }

    public boolean isGetorUnidadeSESI() {
        return getorUnidadeSESI;
    }

    public boolean contemPapel(String papel) {
        return papeis.contains(papel);
    }

    public boolean contemPapeis(String... papeis) {
        Set<Boolean> set = new HashSet<>();
        for (String p : papeis) {
            if (contemPapel(p)) {
                set.add(true);
            } else {
                set.add(false);
            }
        }
        return set.contains(true);
    }

    public Set<String> getListaPerfisPermitidos() {
        return listaPerfisPermitidos;
    }

    public void setListaPerfisPermitidos(Set<String> listaPerfisPermitidos) {
        this.listaPerfisPermitidos = listaPerfisPermitidos;
    }
}