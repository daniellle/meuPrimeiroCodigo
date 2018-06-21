package br.com.ezvida.rst.service;

import br.com.ezvida.girst.apiclient.model.ListaPaginada;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.dao.filter.UsuarioFilter;
import br.com.ezvida.rst.enums.Ambiente;
import br.com.ezvida.rst.model.Trabalhador;
import br.com.ezvida.rst.model.Usuario;
import br.com.ezvida.rst.model.UsuarioGirstView;
import br.com.ezvida.rst.model.dto.UsuarioDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

public class UsuarioServiceDev implements UsuarioService {

    private Logger logger = LoggerFactory.getLogger(UsuarioServiceDev.class);

    @Override
    public Usuario getUsuario(String login) {
        logger.warn("UTILIZANDO MÉTODO DO AMBIENTE DE DESENVOLVIMENTO");
        Usuario u = new Usuario("", login, "Super", "Homem", "ADM", "super@homem.com");
        u.setPermissoes(new HashSet<>(Arrays.asList("funcao",
                "setor",
                "trabalhador",
                "trabalhador_certificado",
                "departamento_regional",
                "indicador_questionario",
                "indicador_consultar",
                "jornada",
                "analise_dinamica_consultar",
                "profissional",
                "cat_produtos_servicos",
                "pesquisa_sei_consultar",
                "grupo_pergunta",
                "rede_credenciada_produto_servico",
                "empresa_lotacao",
                "empresa_setor",
                "parceiro_produto_servico",
                "resposta",
                "sindicato",
                "cat",
                "rede_credenciada",
                "empresa_jornada",
                "auditoria",
                "empresa_lotacao_desativar",
                "cargo",
                "departamento_regional_produtos_servicos",
                "usuario_entidade",
                "classificacao_pontuacao",
                "cnae",
                "consultar_dados_epidem",
                "consultar_dados_usuario",
                "dependente",
                "linha",
                "empresa_sindicato",
                "empresa_trabalhador",
                "empresa_funcao",
                "trabalhador_dependente",
                "pergunta",
                "produto_servico",
                "usuario",
                "parceiro_credenciada",
                "empresa",
                "questionario",
                "empresa_cargo",
                "empresa_trabalhador_lotacao")));
        u.setPapeis(new HashSet<>(Arrays.asList("ADM")));
        return u;
    }

    @Override
    public Map<String, String> getConfiguracao(Ambiente ambiente) {
        logger.warn("UTILIZANDO MÉTODO DO AMBIENTE DE DESENVOLVIMENTO");
        return null;
    }

    @Override
    public ListaPaginada<br.com.ezvida.girst.apiclient.model.Usuario> pesquisarPaginado(UsuarioFilter usuarioFilter) {
        logger.warn("UTILIZANDO MÉTODO DO AMBIENTE DE DESENVOLVIMENTO");
        return null;
    }

    @Override
    public br.com.ezvida.rst.dao.filter.ListaPaginada<UsuarioGirstView> pesquisarPaginadoGirst(UsuarioFilter usuarioFilter, DadosFilter dados, ClienteAuditoria auditoria) {
        logger.warn("UTILIZANDO MÉTODO DO AMBIENTE DE DESENVOLVIMENTO");
        return null;
    }

    @Override
    public br.com.ezvida.girst.apiclient.model.Usuario buscarPorId(String id, ClienteAuditoria auditoria) {
        logger.warn("UTILIZANDO MÉTODO DO AMBIENTE DE DESENVOLVIMENTO");
        return null;
    }

    @Override
    public br.com.ezvida.girst.apiclient.model.Usuario cadastrarUsuario(br.com.ezvida.girst.apiclient.model.Usuario usuario, ClienteAuditoria auditoria) {
        logger.warn("UTILIZANDO MÉTODO DO AMBIENTE DE DESENVOLVIMENTO");
        return null;
    }

    @Override
    public br.com.ezvida.girst.apiclient.model.Usuario alterarUsuario(br.com.ezvida.girst.apiclient.model.Usuario usuario, ClienteAuditoria auditoria) {
        logger.warn("UTILIZANDO MÉTODO DO AMBIENTE DE DESENVOLVIMENTO");
        return null;
    }

    @Override
    public br.com.ezvida.girst.apiclient.model.Usuario sicronizarTrabalhadorUsuario(Trabalhador trabalhador, ClienteAuditoria auditoria) {
        logger.warn("UTILIZANDO MÉTODO DO AMBIENTE DE DESENVOLVIMENTO");
        return null;
    }

    @Override
    public br.com.ezvida.girst.apiclient.model.Usuario alterarPerfilSenha(br.com.ezvida.girst.apiclient.model.UsuarioCredencial usuarioCredencial, ClienteAuditoria auditoria) {
        logger.warn("UTILIZANDO MÉTODO DO AMBIENTE DE DESENVOLVIMENTO");
        return null;
    }

    @Override
    public br.com.ezvida.girst.apiclient.model.Usuario desativarUsuario(String id, ClienteAuditoria auditoria) {
        logger.warn("UTILIZANDO MÉTODO DO AMBIENTE DE DESENVOLVIMENTO");
        return null;
    }

    @Override
    public br.com.ezvida.girst.apiclient.model.Usuario buscarPorEmail(String email) {
        logger.warn("UTILIZANDO MÉTODO DO AMBIENTE DE DESENVOLVIMENTO");
        return null;
    }

    @Override
    public br.com.ezvida.girst.apiclient.model.Usuario buscarPorLogin(String login) {
        logger.warn("UTILIZANDO MÉTODO DO AMBIENTE DE DESENVOLVIMENTO");
        return null;
    }

    @Override
    public boolean isAdm(String cpf) {
        logger.warn("UTILIZANDO MÉTODO DO AMBIENTE DE DESENVOLVIMENTO");
        return false;
    }

    @Override
    public UsuarioDTO consultarDadosUsuario(String login) {
        logger.warn("UTILIZANDO MÉTODO DO AMBIENTE DE DESENVOLVIMENTO");
        return null;
    }

}
