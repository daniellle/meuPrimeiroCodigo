# RST Cadastro

Manual de montagem de ambiente de desenvolvimento.

## Conteúdo

- [Pré requisitos](#pré-requisitos)
- [Configuração](#configuração)
- [Execução](#execução)
- [FAQ](#faq)


**Atenção**
> Todos os passos desta documentação são obrigatórios e é imprescidível que você obtenha sucesso na realização de cada passo.  


> Nesta documentação consideramos que você está utilizando uma distribuição Linux (Ubuntu, OpenSuse, Mint, etc), caso esteja utilizando outro sistema operacional faça as devidas adaptações.

## Pré requisitos

É necessário que você tenha instalado em sua máquina: 
- [JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) (_1.8.x_)
- [WidFly](http://wildfly.org/downloads/) (_10.x_)
- [Node.js](https://nodejs.org/en/) (_8.x ou superior_)
- [NPM](https://www.npmjs.com/) (_5.x ou superior_). 
- IDE para codificação do backend Java (Recomendamos o [Eclipse](https://www.eclipse.org/downloads/))
- IDE para codificação da parte web (Recomendamos o [Visutal Studio Code](https://code.visualstudio.com/download))

O RST Cadastro depende de algumas bibliotecas proprietárias, certifique-se de que essas bibliotecas estão disponíveis no repositório Nexus. 

Para mais informações de como instalar e configurar essas bibliotecas e ferramentas, consulte o arquivo [README.md](../README.md) deste projeto.

## Configuração

1. Edite o arquivo _.bashrc_ que fica na pasta do seu usuário local e adicione a seguinte variável de ambiente:

    ```
    export SOLUTIS_DEV_ENV = "true"
    ```
> Para que a informação acima reflita em seu ambiente, acesse o terminal e execute o comando `source ~/.bashrc` ou reinicie seu sistema.

2. Configure o certificado de acesso no WildFly:

    2.1 Acesse a pasta de instalação do WidFly, em seguida, Modules e crie a estrutura de pastas abaixo:
    ```
    br > com > ezvida > rst > load > main
    ```
    2.2 Dentro da pasta main crie o arquivo _module.xml_ com o conteúdo abaixo:
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <module xmlns="urn:jboss:module:1.1" name="br.com.ezvida.rst.load">
    <resources>
        <resource-root path="."/>
    </resources>
    </module>
    ```
    2.3 Ainda dentro da pasta main, crie uma pasta com o nome certificados, abra o terminal de comando do seu sistema, acesse esta pasta e execute os comandos abaixo, um por vez e na sequência exposta:
    
     ```shell
    $ openssl genrsa -aes256 -out rsa.pem 2048
    ```
    _Será solicitada uma senha, pode ser utilizado sua senha de acesso a rede._

    ```shell
    $ openssl pkcs8 -topk8 -inform PEM -outform PEM -in rsa.pem -out rsa-private.pem -nocrypt
    ```
    _Será solicitada uma senha, informe  a mesma senha que foi utilizada no comando anterior._

    ```shell
    $ openssl rsa -in rsa-private.pem -pubout -outform PEM -out rsa-public.pem
    ```

## Execução


O projeto rst-cadastro é subdividido em módulos. Ao clonar o projeto você encontrará os modulos rst-app, rst-service e rst-web. O módulo rst-app é o core do projeto, rst-service é responsável pelos serviços de regra de negócio e o módulo rst-web é responsável pela interface da aplicação.

**Atenção**

> Neste momento a sua IDE já deve estar configurada com o JDK8 e o servidor de aplicações WidFly. É necessário também que seu WidFly já esteja configurado com os dados de conexão ao banco de dados da aplicação. Para mais informações consulte o arquivo [README.md](../README.md) deste projeto.

1. Clone o projeto

2. Importe-o para sua IDE como um projeto Maven

3. Adicione o artefato rst-app no servidor WidFly e start o servidor.

4. Você pode confirmar que o serviço de backend está online e pronto para receber requisiões acessando a URL abaixo. Se tudo tiver corrido bem até aqui, você deve receber uma resposta de status Ok.
```
http://localhost:8080/rst/api/health
```
5. Abra seu terminal de comando e acesse a pasta _rst-web_ que está na pasta raiz do projeto

6. Execute o comando abaixo para instalar as dependências deste módulo:
```shell
$ npm install
```
7. Após instalação das dependências acima, start a aplicação frontend executando o comando:
```shell
$ npm start
```
8. Você pode confirmar que sua aplicação está online acessando a URL abaixo:
```
http://localhost:4200/cadastro
```

## Testes

O projeto possui testes no backend em Java que são utilizado pelo Jeckins. Em desenvolvimento possui testes E2E (End to End). 

```
## Para os testes e2e.

protractor
```

## FAQ

- **Erro ao baixar dependências?**

    _Algumas dependências estão disponíveis somente no repositório Nexus, por esse motivo é obrigatório que suas credências de rede estejam configuradas no arquivo settings.xml (Maven) e .npmrc (NPM). Para obter informações de como configurar acesse o [README.md](../README.md) deste projeto._


- **Serviço backend não inicializa?**
    
    _Verifique se o servidor WidFly está conectado com o banco de dados da aplicação. Através do painel administrativo do WidFly é possível testar a conexão. Durante a inicializaçao dos serviços do backend a aplicação precisa se conectar com o banco de dados._

- **Quando acesso a url da aplicação web não é exibido a tela do sistema?**

    _Ao chamar a URL da aplicação (localhost:4200/cadastro) é verificado em qual ambiente a aplicação está (Desenvolvimento, Homologação, Produção, etc) para que sejam aplicadas as regras de segurança. Certifique-se de ter inicializado a aplicação pelo comando `npm start` e de ter adicionado a variável de ambiente SOLUTIS_DEV_ENV em seu sistema, a partir dessas informações a aplicação identificará que ela está sendo executada em um ambiente de desenvolvimento._