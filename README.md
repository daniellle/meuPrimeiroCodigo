# rst-cadastros
-Aplicações para manutenção dos cadastros RST

Características
-------------------------

* EJB / JAX-RS
* Angular / Bootstrap
* JPA (Hibernate / HSQLDB)
* OAuth com JWT
* Maven / Nexus
* JUnit / Mockito / Power Mokito
* fw-ezvida
* girst-apiclient
* adl-core
* AJP
* Wildfly
* PostgreSQL

Considerações
============

1- Possui um Backend (Java) e um FrontEnd (Angular 4).

2- Utiliza componentes da biblioteca [fw](https://github.com/ezvida/fw-ezvida).

3- Possui integração com o [Shibboleth](https://www.shibboleth.net/) para obter o usuário logado através do protocolo AJP configurado no servidor Wildfly.

4- Utiliza a biblioteca [girst-apiclient](https://github.com/ezvida/rst-girst-api-client) para se conectar ao Projeto [Girst](https://github.com/ezvida/rst-girst) através de requisições Restfull com Tokens e obter as autorizações do usuário logado.

5- Utiliza a biblioteca [adl-core](https://github.com/ezvida/adl-core) para se conectar ao Projeto [Girst](https://github.com/ezvida/rst-girst) através de requisições Restfull com Tokens e obter as autorizações do usuário logado.

6- É utilizado nos ambientes da Rede Sesi Viva+ através de um iframe em uma Aplicação Django.

7- Conecta-se ao banco de dados SESI_RST para armazenamento de dados.

8- As parametrizações do projeto devem ser alteradas na tabela parametros do banco de dados SESI_RST.

9- Utiliza o pacote ELK para salvar dados de Auditoria.

10- Disponibiliza API de consulta a alguns de seus dados para outros sistemas, como Portal, Res Online e Microserviço IGEV

Dependências
============

1) Node : v9.9.0

2) Npm  : 5.6.0 

3) Oracle JDK : [jdk8-downloads-2133151](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

4) girst-apiclient (publicado no Nexus)

5) fw (publicado no Nexus)

6) adl-core (publicada no Nexus)

7) Configuração do settings.xml do maven apontando para o Nexus instalado

BUILD
==========

Entre no diretório rst-cadastros da aplicação e execute o comando abaixo de acordo com o ambiente que deseja gerar o pacote.
Após build com sucesso o pacote estará disponível na pasta rst-cadastros/rst-cadastros-app/target.

1) Ambiente Demo
	
	mvn -e clean package -P coverage -P frontend -Dmaven.test.skip=true -Dfrontend.environment=demo

2) Ambiente Test

	mvn -e clean package -P coverage -P frontend -Dmaven.test.skip=true -Dfrontend.environment=test

3) Ambiente Homolog

	mvn -e clean package -P coverage -P frontend -Dmaven.test.skip=true -Dfrontend.environment=homolog

4) Ambiente Prod

	mvn -e clean package -P coverage -P frontend -Dmaven.test.skip=true -Dfrontend.environment=prod