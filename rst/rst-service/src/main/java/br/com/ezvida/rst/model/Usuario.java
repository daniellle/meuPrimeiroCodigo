package br.com.ezvida.rst.model;

import java.util.Set;

import javax.persistence.*;

import br.com.ezvida.rst.converter.SimNaoConverter;
import br.com.ezvida.rst.enums.SimNao;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;

import fw.core.model.BaseEntity;

//@formatter:off
@Entity
@Table(name = "usuario", uniqueConstraints = {
        @UniqueConstraint(name = "un_usuario_dn", columnNames = {"dn"}),
        @UniqueConstraint(name = "un_usuario_login", columnNames = {"login"}),
        @UniqueConstraint(name = "un_usuario_email", columnNames = {"email"})})
// @formatter:on
public class Usuario extends BaseEntity<Long> {

    private static final long serialVersionUID = 1479515218652908922L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_usuario")
    @SequenceGenerator(name = "sequence_usuario", sequenceName = "seq_usuario_id", allocationSize = 1)
    private Long id;

    @NotBlank
    @Column(name = "dn", nullable = false)
    private String dn;

    @NotBlank
    @Column(name = "login", nullable = false)
    private String login;

    @NotBlank
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotBlank
    @Column(name = "sobrenome", nullable = false)
    private String sobrenome;

    @Column(name = "apelido")
    private String apelido;

    @Column(name = "fl_apelido")
    @Convert(converter = SimNaoConverter.class)
    private SimNao exibirApelido;

    @Column(name = "imagem")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] foto;

    @Column(name = "cargo", nullable = false)
    private String cargo;

    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "nivel")
    private Integer hierarquia;

    @Transient
    private Set<String> papeis;

    @Transient
    private Set<String> permissoes;

    @Transient
    private Set<Long> idDepartamentos;

    @Transient
    private Set<Long> idEmpresas;

    @Transient
    private Set<Long> idParceiros;

    @Transient
    private Set<Long> idRedesCredenciadas;

    @Transient
    private Set<Long> idSindicatos;

    @Transient
    private Set<Long> idTrabalhadores;

    @Transient
    private Set<Long> idUnidadesSESI;

    public Usuario() {
        // Padrão
    }

    public Usuario(String dn, String login, String nome, String sobrenome, String cargo, String email, Integer hierarquia) {
        this.dn = dn;
        this.login = login;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.cargo = cargo;
        this.email = email;
        this.hierarquia = hierarquia;
        this.papeis = Sets.newHashSet();
        this.permissoes = Sets.newHashSet();
        this.idDepartamentos = Sets.newHashSet();
        this.idEmpresas = Sets.newHashSet();
        this.idParceiros = Sets.newHashSet();
        this.idRedesCredenciadas = Sets.newHashSet();
        this.idSindicatos = Sets.newHashSet();
        this.idTrabalhadores = Sets.newHashSet();
        this.idUnidadesSESI = Sets.newHashSet();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getDn() {
        return dn;
    }

    public void setDn(String dn) {
        this.dn = dn;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public SimNao getExibirApelido() {
        return exibirApelido;
    }

    public void setExibirApelido(SimNao exibirApelido) {
        this.exibirApelido = exibirApelido;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getPapeis() {
        return papeis;
    }

    public void setPapeis(Set<String> papeis) {
        this.papeis = papeis;
    }

    public Set<String> getPermissoes() {
        return permissoes;
    }

    public void setPermissoes(Set<String> permissoes) {
        this.permissoes = permissoes;
    }

    public Set<Long> getIdDepartamentos() {
        return idDepartamentos;
    }

    public void setIdDepartamentos(Set<Long> idDepartamentos) {
        this.idDepartamentos = idDepartamentos;
    }

    public Set<Long> getIdEmpresas() {
        return idEmpresas;
    }

    public void setIdEmpresas(Set<Long> idEmpresas) {
        this.idEmpresas = idEmpresas;
    }

    public Set<Long> getIdParceiros() {
        return idParceiros;
    }

    public void setIdParceiros(Set<Long> idParceiros) {
        this.idParceiros = idParceiros;
    }

    public Set<Long> getIdRedesCredenciadas() {
        return idRedesCredenciadas;
    }

    public void setIdRedesCredenciadas(Set<Long> idRedesCredenciadas) {
        this.idRedesCredenciadas = idRedesCredenciadas;
    }

    public Set<Long> getIdSindicatos() {
        return idSindicatos;
    }

    public void setIdSindicatos(Set<Long> idSindicatos) {
        this.idSindicatos = idSindicatos;
    }

    public Set<Long> getIdTrabalhadores() {
        return idTrabalhadores;
    }

    public void setIdTrabalhadores(Set<Long> idTrabalhadores) {
        this.idTrabalhadores = idTrabalhadores;
    }

    public Integer getHierarquia() {
        return hierarquia;
    }

    public void setHierarquia(Integer hierarquia) {
        this.hierarquia = hierarquia;
    }

    public Set<Long> getIdUnidadesSESI() {
        return idUnidadesSESI;
    }

    public void setIdUnidadesSESI(Set<Long> idUnidadesSESI) {
        this.idUnidadesSESI = idUnidadesSESI;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("dn", dn).add("login", login).add("nome", nome).add("sobrenome", sobrenome).add("cargo", cargo)
                .add("email", email).toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((cargo == null) ? 0 : cargo.hashCode());
        result = prime * result + ((dn == null) ? 0 : dn.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((login == null) ? 0 : login.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((sobrenome == null) ? 0 : sobrenome.hashCode());
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
        Usuario other = (Usuario) obj;
        if (cargo == null) {
            if (other.cargo != null)
                return false;
        } else if (!cargo.equals(other.cargo))
            return false;
        if (dn == null) {
            if (other.dn != null)
                return false;
        } else if (!dn.equals(other.dn))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (login == null) {
            if (other.login != null)
                return false;
        } else if (!login.equals(other.login))
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (sobrenome == null) {
            if (other.sobrenome != null)
                return false;
        } else if (!sobrenome.equals(other.sobrenome))
            return false;
        return true;
    }

}