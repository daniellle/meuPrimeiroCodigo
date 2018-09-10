package br.com.ezvida.rst.model;

import br.com.ezvida.rst.converter.SimNaoConverter;
import br.com.ezvida.rst.enums.SimNao;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "usuario_entidade", uniqueConstraints = @UniqueConstraint(name = "PK_USUARIO_ENTIDADE", columnNames = {"id_usuario_entidade"}))
public class UsuarioEntidade extends AbstractData {

    private static final long serialVersionUID = 7088832963626673768L;

    @Id
    @Column(name = "id_usuario_entidade")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE_USUARIO_ENTIDADE")
    @SequenceGenerator(name = "SEQUENCE_USUARIO_ENTIDADE", sequenceName = "seq_usuario_entid_id_usuario_e", allocationSize = 1)
    private Long id;

    @Column(name = "ds_nome", nullable = true, length = 160)
    private String nome;

    @Column(name = "no_cpf", nullable = false, length = 11)
    private String cpf;

    @Column(name = "ds_email_usuario", nullable = true, length = 100)
    private String email;

    @Column(name = "fl_termo")
    @Convert(converter = SimNaoConverter.class)
    private SimNao termo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empresa_fk", referencedColumnName = "ID_EMPRESA", nullable = true)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_departamento_regional_fk", referencedColumnName = "ID_DEPARTAMENTO_REGIONAL", nullable = true)
    private DepartamentoRegional departamentoRegional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_parceiro_fk", referencedColumnName = "id_parceiro", nullable = true)
    private Parceiro parceiro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rede_credenciada_fk", referencedColumnName = "id_rede_credenciada", nullable = true)
    private RedeCredenciada redeCredenciada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sindicato_fk", referencedColumnName = "ID_SINDICATO", nullable = true)
    private Sindicato sindicato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_und_atd_trab_fk", referencedColumnName = "ID_UND_ATD_TRABALHADOR", nullable = true)
    private UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador;

    @Column(name = "perfil", length = 11)
    private String perfil;

    public UsuarioEntidade() {
    }

    @PreUpdate
    public void preUpdate() {
        setDataAlteracao(new Date());
    }

    @PrePersist
    public void prePersist() {
        setDataCriacao(new Date());
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SimNao getTermo() {
        return termo;
    }

    public void setTermo(SimNao termo) {
        this.termo = termo;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public DepartamentoRegional getDepartamentoRegional() {
        return departamentoRegional;
    }

    public void setDepartamentoRegional(DepartamentoRegional departamentoRegional) {
        this.departamentoRegional = departamentoRegional;
    }

    public Parceiro getParceiro() {
        return parceiro;
    }

    public void setParceiro(Parceiro parceiro) {
        this.parceiro = parceiro;
    }

    public RedeCredenciada getRedeCredenciada() {
        return redeCredenciada;
    }

    public void setRedeCredenciada(RedeCredenciada redeCredenciada) {
        this.redeCredenciada = redeCredenciada;
    }

    public Sindicato getSindicato() {
        return sindicato;
    }

    public void setSindicato(Sindicato sindicato) {
        this.sindicato = sindicato;
    }

    public UnidadeAtendimentoTrabalhador getUnidadeAtendimentoTrabalhador() {
        return unidadeAtendimentoTrabalhador;
    }

    public void setUnidadeAtendimentoTrabalhador(UnidadeAtendimentoTrabalhador unidadeAtendimentoTrabalhador) {
        this.unidadeAtendimentoTrabalhador = unidadeAtendimentoTrabalhador;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        UsuarioEntidade that = (UsuarioEntidade) o;

        if (!id.equals(that.id)) return false;
        if (!nome.equals(that.nome)) return false;
        if (!cpf.equals(that.cpf)) return false;
        if (!email.equals(that.email)) return false;
        if (termo != that.termo) return false;
        if (!empresa.equals(that.empresa)) return false;
        if (!departamentoRegional.equals(that.departamentoRegional)) return false;
        if (!parceiro.equals(that.parceiro)) return false;
        if (!redeCredenciada.equals(that.redeCredenciada)) return false;
        if (!sindicato.equals(that.sindicato)) return false;
        if (!unidadeAtendimentoTrabalhador.equals(that.unidadeAtendimentoTrabalhador)) return false;
        return perfil.equals(that.perfil);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + nome.hashCode();
        result = 31 * result + cpf.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + termo.hashCode();
        result = 31 * result + empresa.hashCode();
        result = 31 * result + departamentoRegional.hashCode();
        result = 31 * result + parceiro.hashCode();
        result = 31 * result + redeCredenciada.hashCode();
        result = 31 * result + sindicato.hashCode();
        result = 31 * result + unidadeAtendimentoTrabalhador.hashCode();
        result = 31 * result + perfil.hashCode();
        return result;
    }
}
