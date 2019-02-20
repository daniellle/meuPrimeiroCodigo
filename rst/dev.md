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
> Todos os passos desta documentação são obrigatórios e é imprescidível que você obtenha sucesso na realização de cada passo.  


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

1. Edite o arquivo _.profile que fica na sua $HOME e adicione a seguinte variável de ambiente:

    ```
    export SOLUTIS_DEV_ENV="true"
    ```
> Reinicie seu computador para que as informações acima reflita em seu ambiente.

### JBoss WidFly

1. Configuração do certificado:

    1.1 Acesse a pasta de instalação do WidFly, em seguida, Modules e crie a estrutura de pastas abaixo:
    ```
    br > com > ezvida > rst > load > main
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
    1.3 Ainda dentro da pasta main, crie uma pasta com o nome certificados, abra o terminal de comando do seu sistema, acesse esta pasta e execute os comandos abaixo, um por vez e na sequência exposta:
    
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

    > Verifique a pasta certificados e confirme que foram gerados os 3 arquivos do certificado.

2. Instalando o driver do PostgreSQL:

    2.1 Faça o download do PostgreSQL JDBC Driver mais recente [neste site](https://jdbc.postgresql.org/download.html)

    2.2 Mova o arquivo do JDBC driver **.jar** baixado para: 
    ```
    PASTA_DE_INSTALACAO_DO_WIDFLY/bin
    ```

    2.3 Ainda na pasta bin execute o arquivo _jboss-cli.sh_ através do terminal, para acessarmos ao jboss-cli (Command Line Interface).

    2.4 Após executar o jboss-cli, ele irá pedir para que você se conecte ao servidor, para isso, basta digitar: connect

    2.5 Depois de conectado, adicione o módulo chamado org.postgres apontando para o driver do postgres, comando:

    ```shell
    module add --name=org.postgres --resources=NOME_DO_ARQUIVO_DO_DRIVER.jar --dependencies=javax.api,javax.transaction.api
    ```
    > Substitua **NOME_DO_ARQUIVO_DO_DRIVER** no comando acima pelo nome exato do arquivo do driver JDBC que foi copiado para a pasta bin.

    2.6 Instale o driver JDBC, comando:

    ```shell
    /subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgres",driver-class-name=org.postgresql.Driver)
    ```

    2.7 Reinicie o WidFly caso esteja sendo executado.

    2.8 Após efetuar essas etapas, podemos iniciar a configuração do Datasource via console administrativo do Wildfly.

3. Configuração do datasource:

    > Caso esta seja sua primeira instalação e configuração do Jboss WidFly, recomendamos a leitura desse [Getting Started Guide](https://docs.jboss.org/author/display/WFLY10/Getting+Started+Guide). Para os próximos passos será necessário que seu servidor esteja iniciado e configurado com o usuário de acesso ao console administrativo.

    2.1 Acesse o console administrativo do WidFly:  
    ```
    http://localhost:9990/console
    ```

    2.2 Clique na aba **Configuration**

    2.3 No menu esquerdo, clique em **Subsystems** → **Datasources** → **Non-XA** → **Add**

    2.4 Na janela que foi aberta, selecione a opção **PostgreSQL Datasource** e clique em **Next**

    2.5 Entre com os valores **RstDS** e **java:jboss/datasources/rst** para os campos nome e JNDI name respectvamente, em seguida clique em **Next**

    2.6 Clique na aba **Detected Driver**, selecione a opção **postgres** e, em seguida, clique em **Next**

    2.7 Edite o campo Connection URL informando o host do banco de dados, a porta e o nome do banco. Não altere o começo da url. No fnal, o resultado deve estar nesse formato:

    ```
    jdbc:postgresql://HOST_DO_BANCO:PORTA_DO_BANCO/NOME_DO_BANCO
    ```
    > Substitua os campos **HOST_DO_BANCO**, **PORTA_DO_BANCO** e **NOME_DO_BANCO** pelos dados de conexão com o banco de dados da aplicação.

    2.8 Os campos **Username** e **Password** devem ser preenchidos com os dados do usuário com acesso ao banco da aplicação, em seguida, clique em **Next**

    2.9 Revise os dados apresentados e clique em **Finish**

    > Para testar a conexão, selecione o datasource criado, clique em **View**, em seguida, clique na aba **Connection** e, em seguida clique no botão **Test Connection**.

### Maven

### NPM

## Execução


O projeto rst-cadastro é subdividido em módulos. Ao clonar o projeto você encontrará os modulos rst-app, rst-service e rst-web. O módulo rst-app é o core do projeto, rst-service é responsável pelos serviços de regra de negócio e o módulo rst-web é responsável pela interface da aplicação.

**Atenção**

> Neste momento a sua IDE já deve estar configurada com o JDK8 e o servidor de aplicações Jboss WidFly. É necessário também que seu WidFly já esteja configurado com os dados de conexão ao banco de dados da aplicação. 

1. Clone o projeto

2. Importe-o para sua IDE como um projeto Maven e aguarde o download das dependências

3. Adicione o artefato rst-app no servidor WidFly e start o servidor.

4. Você pode confirmar que o serviço de backend está online e pronto para receber requisições acessando a URL abaixo. Se tudo tiver corrido bem até aqui, você deve receber uma resposta de status Ok.
```
http://localhost:8080/rst/api/health
```
5. Abra seu terminal de comando e acesse a pasta _rst-web_ que está na pasta raiz do projeto > rst

    5.1 Execute o comando abaixo para instalar as dependências deste módulo:
    ```shell
    $ npm install
    ```
    5.2 Após instalação das dependências acima, start a aplicação (parte web) executando o comando:
    ```shell
    $ npm start
    ```
6. Você pode confirmar que sua aplicação está online acessando a URL abaixo:
```
http://localhost:4200/cadastro
```

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