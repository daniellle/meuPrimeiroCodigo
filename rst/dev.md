# RST Cadastro

Manual de montagem de ambiente de desenvolvimento.

## Conteúdo

- [Pré requisitos](#pré-requisitos)
- [Configuração](#configuração)
- [Execução](#execução)
- [Problemas comuns](#problemas-comuns)

## Pré requisitos

O RST Cadastro depende de outras bibliotecas e aplicações. Certifique-se de que todas as dependências estão instaladas. Para mais informações consulte o arquivo [README.md](../README.md) deste projeto.

Todos os passos desta documentação são obrigatórios e é imprescidível que você obtenha sucesso na realização de cada passo.

## Configuração

1. Edite o arquivo .bashrc que fica na pasta do seu usuário local e adicione a seguinte variável de ambiente:

    ```
    export SOLUTIS_DEV_ENV = "true"
    ```
> Após o adicionar a variável acima, acesse o terminal e execute o comando `source ~/.bashrc` para que essas informações sejam refletidas em seu ambiente ou reinicie seu sistema.

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
    2.3 Ainda dentro da pasta main, crie uma pasta com o nome _certificados_, abra o terminal de comando do seu sistema, acesse esta pasta e execute os comandos abaixo, um por vez e na sequência exposta:
    
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


O projeto rst-cadastro é subdividido em módulos. Ao clonar o projeto você encontrará os modulos rst-app, rst-service e rst-web. O módulo rst-app é o core do projeto, rst-service é responsável pelos serviços (backend) e o módulo rst-web é responsável pela de interface (frontend).

**Atenção**

> Neste momento a sua IDE já deve estar configurada com o JDK8 e o servidor de aplicações WidFly. É necessário também que seu WidFly já esteja configurado com os dados de conexão ao banco de dados da aplicação. Para mais informações consulte o arquivo [README.md](../README.md) deste projeto.

1. Clone o projeto

2. Importe-o para sua IDE como um projeto Maven

3. Adicione o artefato rst-app no servidor WidFly e start o servidor.

4. Você pode confirmar que o backend está online e pronto para receber requisiões acessando a URL abaixo. você deve receber uma resposta de status Ok.
```
http://localhost:8080/rst/api/health
```
5. Através do terminal de comando, acesse a pasta _rst-web_ que está na pasta raiz do projeto

6. Execute o comando abaixo para instalar as dependências deste módulo:
```shell
$ npm install
```
7. Após instalação das dependências acima, execute o comando:
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

## Problemas comuns

- **Erro ao baixar dependências?**

    _Algumas dependências estão disponíveis somente no repositório Nexus, por esse motivo é obrigatório que suas credências de rede estejam configuradas no arquivo settings.xml (Maven) e .npmrc (NPM). Para obter informações de como configurar acesse o [README.md](../README.md) deste projeto._


- **Serviço backend não inicializa?**
    
    _Verifique se o servidor WidFly está conectado com o banco de dados da aplicação. Através do painel administrativo do WidFly é possível testar a conexão. Durante a inicializaçao dos serviços do backend a aplicação precisa se conectar com o banco de dados._

- **Quando acesso a url da aplicação web não é exibido a tela do sistema?**

    _Ao chamar a URL da aplicação (localhost:4200/cadastro) é verificado em qual ambiente a aplicação está (Desenvolvimento, Homologação, Produção, etc) para que sejam aplicadas as regras de segurança. Certifique-se de ter inicializado a aplicação pelo comando `npm start` e de ter adicionado a variável de ambiente SOLUTIS_DEV_ENV em seu sistema, a partir dessas informações a aplicação identificará que ela está sendo executada em um ambiente de desenvolvimento._