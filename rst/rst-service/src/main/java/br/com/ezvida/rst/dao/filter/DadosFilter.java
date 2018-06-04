package br.com.ezvida.rst.dao.filter;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.google.common.collect.Sets;

public class DadosFilter implements Serializable {

	private static final long serialVersionUID = -3468814525094214305L;

	public static final String ADMINISTRADOR = "ADM";
	public static final String DIRETOR_DR = "DIDR";
	public static final String DIRETOR_DN = "DIDN";
	public static final String GESTOR_DR = "GDRA";
	public static final String GESTOR_EMPRESA = "GEEM";
	public static final String GESTOR_SINDICATO = "GESI";
	public static final String TRABALHADOR = "TRA";
	public static final String GESTOR_PARCEIRO = "GEPC";
	public static final String GESTOR_REDE = "GERC";
	public static final String GESTOR_DN = "GDNA";
	public static final String PROF_SAUDE = "PFS";

	private boolean administrador;

	private boolean diretoriaDr;

	private boolean diretoriaDn;

	private boolean gestorDr;

	private boolean gestorDn;

	private boolean gestorEmpresa;

	private boolean profissionalSaude;

	private boolean gestorSindicato;

	private boolean trabalhador;

	private boolean gestorParceiroCredenciado;

	private boolean gestorRedeCredenciada;

	private boolean temIdsDepartamentoRegional;

	private boolean temIdsEmpresa;

	private boolean temIdsSindicato;

	private boolean temIdsParceiro;

	private boolean temIdsRedeCredenciada;

	private boolean temIdsTrabalhador;

	private Set<String> papeis;

	private Set<Long> idsDepartamentoRegional;

	private Set<Long> idsEmpresa;

	private Set<Long> idsSindicato;

	private Set<Long> idsParceiro;

	private Set<Long> idsRedeCredenciada;

	private Set<Long> idsTrabalhador;

	private Set<String> listaPerfisPermitidos;

	public DadosFilter() {

	}

	public DadosFilter(Set<String> papeis, Set<Long> departamentos, Set<Long> empresas, Set<Long> parceiros,
			Set<Long> redesCredenciadas, Set<Long> sindicatos, Set<Long> idsTrabalhadores) {

		this.papeis = papeis;
		this.administrador = contemPapel(ADMINISTRADOR);
		this.diretoriaDr = contemPapel(DIRETOR_DR);
		this.diretoriaDn = contemPapel(DIRETOR_DN);
		this.gestorDr = contemPapel(GESTOR_DR);
		this.gestorDn = contemPapel(GESTOR_DN);
		this.gestorEmpresa = contemPapel(GESTOR_EMPRESA);
		this.profissionalSaude = contemPapel(PROF_SAUDE);
		this.gestorParceiroCredenciado = contemPapel(GESTOR_PARCEIRO);
		this.gestorRedeCredenciada = contemPapel(GESTOR_REDE);
		this.gestorSindicato = contemPapel(GESTOR_SINDICATO);
		this.trabalhador = contemPapel(TRABALHADOR);

		if (diretoriaDr || gestorDr) {
			this.idsDepartamentoRegional = addHash(departamentos);
		}

		if ((gestorEmpresa || profissionalSaude) && !(diretoriaDr || gestorDr)) {
			this.idsEmpresa = addHash(empresas);
		}

		if (trabalhador && !(diretoriaDr || gestorDr || gestorEmpresa)) {
			this.idsTrabalhador = addHash(idsTrabalhadores);
		}

		// TODO SO PREEENCHER DE ACORDO COM O PERFIL
		this.idsParceiro = parceiros;
		this.idsRedeCredenciada = redesCredenciadas;
		this.idsSindicato = sindicatos;

		this.temIdsDepartamentoRegional = CollectionUtils.isNotEmpty(this.idsDepartamentoRegional);
		this.temIdsEmpresa = CollectionUtils.isNotEmpty(this.idsEmpresa);
		this.temIdsParceiro = CollectionUtils.isNotEmpty(this.idsParceiro);
		this.temIdsRedeCredenciada = CollectionUtils.isNotEmpty(this.idsRedeCredenciada);
		this.temIdsSindicato = CollectionUtils.isNotEmpty(this.idsSindicato);

		this.temIdsTrabalhador = CollectionUtils.isNotEmpty(this.idsTrabalhador);

		this.preenchePerfil();

	}
	
	private Set<Long> addHash(Set<Long> obj) {
		return CollectionUtils.isEmpty(obj) ? Sets.newHashSet(0L) : obj;
	}

	private void preenchePerfil() {
		if (isTrabalhador() || isProfissionalSaude()) {
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

		if (isGestorEmpresa()) {
			listaPerfisPermitidos = Sets.newHashSet(GESTOR_SINDICATO, GESTOR_PARCEIRO, GESTOR_REDE);
		}

		if (isGestorDr() || isDiretoriaDr()) {
			listaPerfisPermitidos = Sets.newHashSet(TRABALHADOR, GESTOR_SINDICATO, GESTOR_PARCEIRO, GESTOR_REDE,
					GESTOR_EMPRESA);
		}

		if (isGestorDn() || isDiretoriaDn()) {
			listaPerfisPermitidos = Sets.newHashSet(TRABALHADOR, GESTOR_SINDICATO, GESTOR_PARCEIRO, GESTOR_REDE,
					GESTOR_EMPRESA, GESTOR_DR, DIRETOR_DR);
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

	public boolean temIdsSindicato() {
		return temIdsSindicato;
	}

	public boolean temIdsDepRegional() {
		return temIdsDepartamentoRegional;
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

	public boolean isSuperUsuario(){
		return this.isAdministrador() || this.isGestorDn() | this.isDiretoriaDn();
	}

	public void setGestorDn(boolean gestorDn) {
		this.gestorDn = gestorDn;
	}

	public boolean isGestorEmpresa() {
		return gestorEmpresa;
	}

	public boolean isProfissionalSaude() {
		return profissionalSaude;
	}

	public void setProfissionalSaude(boolean profissionalSaude) {
		this.profissionalSaude = profissionalSaude;
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

	public boolean contemPapel(String papel) {
		return papeis.contains(papel);
	}

	public Set<String> getListaPerfisPermitidos() {
		return listaPerfisPermitidos;
	}

	public void setListaPerfisPermitidos(Set<String> listaPerfisPermitidos) {
		this.listaPerfisPermitidos = listaPerfisPermitidos;
	}
}