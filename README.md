# rst-cadastros
-Aplicações para manutenção dos cadastros RST

Características
-------------------------

- Java
- EJB / JAX-RS
- Angular / Bootstrap
- JPA (Hibernate / HSQLDB)
- OAuth com JWT
- Maven / Nexus
- JUnit / Mockito / Power Mokito
- NodeJS
- fw-ezvida
- girst-apiclient
- adl-core
- AJP
- Wildfly
- PostgreSQL

Considerações
-------------------------

- Possui um Backend (Java) e um FrontEnd (Angular 4).
- Utiliza componentes da biblioteca [fw](https://github.com/ezvida/fw-ezvida).
- Possui integração com o [Shibboleth](https://www.shibboleth.net/) para obter o usuário logado através do protocolo AJP configurado no servidor Wildfly.
- Utiliza a biblioteca [girst-apiclient](https://github.com/ezvida/rst-girst-api-client) para se conectar ao Projeto [Girst](https://github.com/ezvida/rst-girst) através de requisições Restfull com Tokens e obter as autorizações do usuário logado.
- Utiliza a biblioteca [adl-core](https://github.com/ezvida/adl-core) para ler as fichas (FCO, Imunização) obtidas do Projeto [RES](https://github.com/ezvida/ezehr-sl) através de requisições Restfull com Tokens.
- É utilizado nos ambientes da Rede Sesi Viva+ através de um iframe em uma Aplicação Django.
- Conecta-se ao banco de dados SESI_RST para armazenamento de dados.
- As parametrizações do projeto devem ser alteradas na tabela parametros do banco de dados SESI_RST.
- Conecta-se ao Projeto [edge-server](https://github.com/ezvida/edge-server) através de requisições Restfull com Tokens para obter dados de Vacinas Autodeclaradas.
- Utiliza o pacote ELK para salvar dados de Auditoria.
- Disponibiliza API de consulta a alguns de seus dados para outros sistemas, como Portal, Res Online e Microserviço IGEV.
- Uma das urls de acesso é <host_ambiente>/rst/cadastro/usuario.
- Deve ser dado permissão aos usuários através da tela de usuários.
- Mais informações ver arquivo [dev.md](https://github.com/ezvida/rst-aplicacoes/blob/release/rst/dev.md)

Dependências
-------------------------

- Node 8 ou 9
- Npm 5.6.0 
- Oracle JDK 8+
- girst-apiclient (publicado no Nexus)
- fw (publicado no Nexus)
- adl-core (publicada no Nexus)
- Configuração do settings.xml do maven apontando para o Nexus instalado
- Configuração do .npmrc apontando para o Nexus instalado

Build
-------------------------

Entre no diretório rst-cadastros/rst da aplicação e execute o comando abaixo de acordo com o ambiente que deseja gerar o pacote.
Após build com sucesso o pacote estará disponível na pasta rst-cadastros/rst-cadastros-app/target.

1) Ambiente Demo
	
	mvn -e clean package -P coverage -P frontend -Dmaven.test.skip=true -Dfrontend.environment=demo

2) Ambiente Test

	mvn -e clean package -P coverage -P frontend -Dmaven.test.skip=true -Dfrontend.environment=test

3) Ambiente Homolog

	mvn -e clean package -P coverage -P frontend -Dmaven.test.skip=true -Dfrontend.environment=homolog

4) Ambiente Prod

	mvn -e clean package -P coverage -P frontend -Dmaven.test.skip=true -Dfrontend.environment=prod

Deploy
-------------------------

Ver 
[CHANGELOG.MD](https://github.com/ezvida/rst-cadastros/blob/release/CHANGELOG.md)
[MANUAL_DEPLOY](https://github.com/ezvida/rst-aplicacoes/blob/master/Documenta%C3%A7%C3%A3o/Manuais/RST-CADASTROS/DEPLOY_RST_CADASTROS.pdf)
