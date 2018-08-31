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

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
        result = prime * result + ((departamentoRegional == null) ? 0 : departamentoRegional.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((empresa == null) ? 0 : empresa.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((parceiro == null) ? 0 : parceiro.hashCode());
        result = prime * result + ((redeCredenciada == null) ? 0 : redeCredenciada.hashCode());
        result = prime * result + ((sindicato == null) ? 0 : sindicato.hashCode());
        result = prime * result + ((termo == null) ? 0 : termo.hashCode());
        result = prime * result + ((getDataAlteracao() == null) ? 0 : getDataAlteracao().hashCode());
        result = prime * result + ((getDataCriacao() == null) ? 0 : getDataCriacao().hashCode());
        result = prime * result + ((getDataExclusao() == null) ? 0 : getDataExclusao().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        UsuarioEntidade other = (UsuarioEntidade) obj;
        if (cpf == null) {
            if (other.cpf != null)
                return false;
        } else if (!cpf.equals(other.cpf))
            return false;
        if (departamentoRegional == null) {
            if (other.departamentoRegional != null)
                return false;
        } else if (!departamentoRegional.equals(other.departamentoRegional))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (empresa == null) {
            if (other.empresa != null)
                return false;
        } else if (!empresa.equals(other.empresa))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (parceiro == null) {
            if (other.parceiro != null)
                return false;
        } else if (!parceiro.equals(other.parceiro))
            return false;
        if (redeCredenciada == null) {
            if (other.redeCredenciada != null)
                return false;
        } else if (!redeCredenciada.equals(other.redeCredenciada))
            return false;
        if (sindicato == null) {
            if (other.sindicato != null)
                return false;
        } else if (!sindicato.equals(other.sindicato))
            return false;
        if (termo != other.termo)
            return false;
        if (getDataAlteracao() == null) {
            if (other.getDataAlteracao() != null)
                return false;
        } else if (!getDataAlteracao().equals(other.getDataAlteracao()))
            return false;
        if (getDataCriacao() == null) {
            if (other.getDataCriacao() != null)
                return false;
        } else if (!getDataCriacao().equals(other.getDataCriacao()))
            return false;
        if (getDataExclusao() == null) {
            if (other.getDataExclusao() != null)
                return false;
        } else if (!getDataExclusao().equals(other.getDataExclusao()))
            return false;
        return true;
    }

}
