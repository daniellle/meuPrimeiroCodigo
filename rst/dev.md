# RST Cadastro

Manual de montagem de ambiente de desenvolvimento.

## Conteúdo

- [Pré requisitos](#pré-requisitos)
- [Configuração](#configuração)
    - [Ambiente](#ambiente)
    - [Jboss WidFly](#jboss-widfly)
    - [Maven](#maven)
    - [NPM](#npm)
- [Execução](#execução)
- [FAQ](#faq)


**Atenção**
> Todos os passos desta documentação são obrigatórios e é imprescindível que você obtenha sucesso na realização de cada passo.  


> Nesta documentação consideramos que você está utilizando uma distribuição Linux (Ubuntu, OpenSuse, Mint, etc), caso esteja utilizando outro sistema operacional faça as devidas adaptações.

## Pré requisitos

É necessário que você tenha instalado em sua máquina:  

- [JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) (_1.8.x_)  
    _Para verificar sua versão, execute `javac -version` em uma janela do terminal/console._

- [Node.js](https://nodejs.org/en/) (_8.x ou superior_)  
 _Para verificar sua versão, execute `node --version` em uma janela do terminal/console._

- [NPM](https://www.npmjs.com/) (_5.x ou superior_).  
 _Para verificar sua versão, execute `npm --version` em uma janela do terminal/console._

- [JBoss WidFly](http://wildfly.org/downloads/) (_10.x_)  

- IDE para codificação do backend Java (Recomendamos o [Eclipse](https://www.eclipse.org/downloads/) ou [IntelliJ IDEA Ultimate](https://www.jetbrains.com/idea/download))
- IDE para codificação da parte web (Recomendamos o [Visutal Studio Code](https://code.visualstudio.com/download))

O RST Cadastro depende de algumas bibliotecas proprietárias, certifique-se de que essas bibliotecas estão disponíveis no repositório Nexus. 

Para mais informações de como instalar e configurar essas bibliotecas e ferramentas, consulte o arquivo [README.md](../README.md) deste projeto.

## Configuração

### Ambiente


1. Edite o arquivo _.profile_ que fica na sua $HOME e adicione no final do arquivo a variável de ambiente abaixo:

    ```
    export SOLUTIS_DEV_ENV="true"
    ```
    
**Atenção**
> Reinicie seu computador para que as informações acima reflita em seu ambiente.

> Essa variável de ambiente é utilizada pela classe  _SegurancaUtils_. A partir do valor dessa variável a aplicação identifica em qual ambiente ela está sendo iniciada.
> Para simular o acesso com diferentes perfis em ambiente local, altere o CPF retornado no metódo **validarAutenticacao** que fica na classe _SegurancaUtils_ dentro do módulo rst-service.

### JBoss WidFly

1. Configuração do certificado:

    1.1 Acesse a pasta de instalação do seu Jboss WidFly, em seguida, modules e crie a estrutura de pastas abaixo:
    ```
    br > com > ezvida > rst > load > main > certificados
    ```
    1.2 Dentro da pasta main crie o arquivo _module.xml_ com o conteúdo abaixo:
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <module xmlns="urn:jboss:module:1.1" name="br.com.ezvida.rst.load">
    <resources>
        <resource-root path="."/>
    </resources>
    </module>
    ```
    1.3 Abra o terminal/console do seu sistema, navegue até a pasta certificados, criada anteriormente, e execute os comandos abaixo:
    
     ```shell
    $ openssl genrsa -aes256 -out rsa.pem 2048
    ```
    _Será solicitada que crie uma senha e digite sua confirmação, você deve utilizar sua senha de acesso a rede._

    ```shell
    $ openssl pkcs8 -topk8 -inform PEM -outform PEM -in rsa.pem -out rsa-private.pem -nocrypt
    ```
    _Será solicitada uma senha, informe a mesma senha que foi utilizada no comando anterior._

    ```shell
    $ openssl rsa -in rsa-private.pem -pubout -outform PEM -out rsa-public.pem
    ```

    > Confirme se na pasta certificados foram gerados os 3 arquivos do certificado.

2. Instalando o driver do PostgreSQL:

    2.1 Faça o download do PostgreSQL JDBC Driver mais recente [neste site](https://jdbc.postgresql.org/download.html).

    2.2 Mova o arquivo **.jar** baixado para a pasta bin que fica dentro da pasata de instalação do WidFly.
    
    2.3 Abra uma janela do terminal/console acesse esta pasta bin e execute o comando `./standalone.sh`, para que o servidor WidFly seja iniciado.

    2.4 Em uma outra janela do terminal execute o comando `./jboss-cli.sh` ainda na pasta bin, para acessarmos ao jboss-cli (Command Line Interface).

    2.5 Após executar o jboss-cli, ele irá pedir para que você se conecte ao servidor, para isso, digite _connect_ e tecle **enter**

    2.6 Depois de conectado, execute o comando abaixo para adicionarmos o módulo chamado _org.postgres_ apontando para o driver do postgres:

    ```shell
    module add --name=org.postgres --resources=NOME_DO_ARQUIVO.jar --dependencies=javax.api,javax.transaction.api
    ```
    > Substitua **NOME_DO_ARQUIVO** no comando acima pelo nome exato do arquivo do driver JDBC que foi copiado para a pasta bin.

    2.7 Instale o driver JDBC:

    ```shell
    /subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgres",driver-class-name=org.postgresql.Driver)
    ```

    2.8 Reinicie o WidFly.

    2.9 Após efetuar essas etapas, o PostgresSQL JDBC Driver já pode ser utilizado na criação do Datasource.

3. Configuração do datasource:

    > Caso esta seja sua primeira instalação e configuração do Jboss WidFly, recomendamos a leitura desse [Getting Started Guide](https://docs.jboss.org/author/display/WFLY10/Getting+Started+Guide). Para os próximos passos será necessário que seu servidor esteja iniciado e configurado com o usuário de acesso ao console administrativo.

    3.1 Acesse o console administrativo do WidFly:  
    ```
    http://localhost:9990/console
    ```

    3.2 Clique na aba **Configuration**

    3.3 No menu esquerdo, clique em **Subsystems** → **Datasources** → **Non-XA** → **Add**

    3.4 Na janela que foi aberta, selecione a opção **PostgreSQL Datasource** e clique em **Next**

    3.5 Entre com os valores **RstDS** e **java:jboss/datasources/rst** para os campos nome e JNDI name respectvamente, em seguida clique em **Next**

    3.6 Clique na aba **Detected Driver**, selecione a opção **postgres** e, em seguida, clique em **Next**

    3.7 Edite o campo Connection URL informando o host do banco de dados, a porta e o nome do banco. Não altere o começo da url. No fnal, o resultado deve estar nesse formato:

    ```
    jdbc:postgresql://HOST_DO_BANCO:PORTA_DO_BANCO/NOME_DO_BANCO
    ```
    > Substitua os campos **HOST_DO_BANCO**, **PORTA_DO_BANCO** e **NOME_DO_BANCO** pelos dados de conexão com o banco de dados da aplicação.

    3.8 Os campos **Username** e **Password** devem ser preenchidos com os dados do usuário com acesso ao banco da aplicação, em seguida, clique em **Next**

    3.9 Revise os dados apresentados e clique em **Finish**

    > Para testar a conexão, selecione o datasource criado, clique em **View**, em seguida, clique na aba **Connection** e, em seguida clique no botão **Test Connection**.

### Maven

> Para que as dependências do maven sejam baixadas do repositório Nexus é necessário que o arquivo do maven, _settings.xml_, seja configurado com as credências do usuário e os dados de conexão ao servidor Nexus.

1. Caso não exista, crie um arquivo com o nome _settings.xml_ dentro da pasta $HOME/.m2, o arquivo deve ter a seguinte estrutra:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"   xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                        https://maven.apache.org/xsd/settings-1.0.0.xsd">
    
    <!-- Configuração de acesso ao Servidor Nexus -->
	<servers>
		<server>
			<id>solutis-nexus</id>
			<username>USUARIO_DE_REDE</username>
			<password>SENHA_DE_REDE</password>
		</server>
	</servers>

    <!-- Url do repositório nexus -->
	<mirrors>
		<mirror>
			<id>solutis-nexus</id>
			<mirrorOf>*</mirrorOf>
			<url>http://nexus.solutis.net.br/content/groups/public</url>
		</mirror>
	</mirrors>

    <!-- Configurações de Perfis -->
	<profiles>
		<profile>

			<id>solutis-nexus</id>
			<activation>
				<activeByDefault>true</activeByDefault>
			</activation>
			<repositories>
				<repository>
					<id>central</id>
					<url>http://central</url>
					<releases>
						<enabled>true</enabled>
					</releases>
					<snapshots>
						<enabled>true</enabled>
					</snapshots>
				</repository>
			</repositories>
			<pluginRepositories>
				<pluginRepository>
					<id>central</id>
					<url>http://central</url>
					<releases>
						<enabled>true</enabled>
					</releases>
					<snapshots>
						<enabled>true</enabled>
					</snapshots>
				</pluginRepository>
			</pluginRepositories>
		</profile>
	</profiles>
 
	<activeProfiles>
		<activeProfile>solutis-nexus</activeProfile>
	</activeProfiles>
 
	<interactiveMode>true</interactiveMode>
	<usePluginRegistry>false</usePluginRegistry>
	<offline>false</offline>
</settings>
```
**Atenção**

> Este é um arquivo exemplo e contém os dados de conexão com o repositório Nexus da Solutis.  

> Os dados USUARIO_DE_REDE e SENHA_DE_REDE devem ser substituidos pelas credências de acesso a rede.

### NPM

> Para que as dependências npm sejam baixadas do repositório Nexus é necessário que o arquivo do npm, _.npmrc_, seja configurado com as credências do usuário e os dados de conexão ao servidor Nexus.

1. Caso não exista, crie um arquivo com o nome _.npmrc_ dentro da pasta $HOME, o arquivo deve ter a seguinte estrutra:

```
registry=http://nexus.solutis.net.br/content/groups/npmjsolutis/
email=EMAIL_CORPORATIVO
always-auth=true
_auth=TOKEN_DE_AUTENTICAÇÃO
```
**Atenção**

> Este é um arquivo exemplo e contém os dados de conexão com o repositório Nexus da Solutis.  

> EMAIL_CORPORATIVO = Deve ser substituido por seu email corporativo.  

> TOKEN_DE_AUTENTICAÇÃO = Este token é um hash gerado em base64 do seu **usuário e senha de rede**. Para gerar esse token, abra uma janela do terminal/console e execute o comando `echo -n 'USUARIO:SENHA' | openssl base64`. O retorno desse comando é o token que deve ser utilizado.

## Execução


O projeto rst-cadastro é subdividido em módulos. Ao clonar o projeto você encontrará os modulos rst-app, rst-service e rst-web. O módulo rst-app é o core do projeto, rst-service é responsável pelos serviços de regra de negócio e o módulo rst-web é responsável pela interface da aplicação.

**Atenção**

> Neste momento a sua IDE já deve estar configurada com o JDK8 e o servidor de aplicações Jboss WidFly. É necessário também que seu WidFly já esteja configurado com os dados de conexão ao banco de dados da aplicação. 

1. Clone o projeto para sua máquina local.

2. Importe-o para sua IDE como um projeto Maven e aguarde o download das dependências.

3. Adicione o artefato rst-app no servidor WidFly e start-o.

4. Acompanhe no console da sua IDE se o start do servidor e deploy da aplicação vão ser bem sucedidos.  

    > Você pode confirmar que o serviço de backend está online acessando a URL `http://localhost:8080/rst/api/health`. Se tudo tiver corrido bem até aqui, você deve receber uma resposta de status Ok.

5. Abra uma janela do terminal/console e acesse a pasta `rst-cadastro/rst/rst-web`

    5.1 Execute o comando abaixo para instalar as dependências deste módulo:
    ```shell
    $ npm install
    ```
    5.2 Após instalação das dependências acima, start a aplicação (interface web) executando o comando:
    ```shell
    $ npm start
    ```
> Para acessa a aplicação (interface web), acesse a URL `http://localhost:4200/cadastro`.

## FAQ

- **Erro no download das dependências?**

    - _Algumas dependências estão disponíveis somente no repositório Nexus, por esse motivo é obrigatório que suas credências de rede estejam configuradas no arquivo settings.xml (Maven) e .npmrc (NPM). Para obter informações de como configurar esses arquivos acesse o [README.md](../README.md) deste projeto._


- **O serviço backend não inicializa?**
    
    - _Verifique se o servidor WidFly está conectado com o banco de dados da aplicação. Através do painel administrativo do WidFly é possível testar a conexão. Durante a inicializaçao dos serviços do backend a aplicação precisa se conectar com o banco de dados._

    - _Abra o console da sua IDE para acompanhar o start da aplicação e identificar possíveis erros na inicialização dos serviços._

- **Quando acessa a url da aplicação web não é exibido a tela do sistema?**

    - _Quando é feito uma requisição para a URL (localhost:4200/cadastro), a aplicação verifica em qual ambiente ela está sendo executada (Desenvolvimento, Homologação, Produção, etc) para que sejam aplicadas as regras de segurança adequadamente. Certifique-se de ter inicializado a aplicação (web) com o comando `npm start` e de ter adicionado a variável de ambiente SOLUTIS_DEV_ENV em seu sistema operacional, a partir dessas informações a aplicação identificará se ela está sendo executada em um ambiente de desenvolvimento ou não._

    - Verifique se os certificados foram configurados no WidFly corretamente, reveja o passo [JBoss WidFly](#jboss-widfly).

    - _No seu navegador abra a ferramentas de desenvolvedor (F12) e verifique se é exibido algum erro no console ao tentar acessar a url da aplicação._

    - _Abra o console da sua IDE onde o backend foi inicializado para verificar se o serviço está sendo chamado e se é exibido algum erro quando é feito uma requisição à URL aplicação web._
    
    - _Limpe o cache e arquivos temporários do seu navegador e tente também o acesso por outros navegador._