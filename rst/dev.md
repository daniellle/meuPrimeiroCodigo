# rst-cadastro

Manual de montagem de ambiente de desenvolvimento.

## Conteúdo

- [Pré-requisitos](#pré-requisitos)
- [Instalação](#instalação)


## Pré-requisitos

Para que o projeto rst-cadastro seja executado, é necessário que as seguintes ferramentas estejam instaladas:

- [Node.js](https://nodejs.org/en/) _Versão 8.x ou superior_

  Para verificar sua versão, execute o comando `node -v` eu seu terminal de comando.

- [NPM](https://www.npmjs.com/) _Versão 5.x ou superior_

  Para verificar sua versão, execute o comando `npm -v` eu seu terminal de comando.

- [JDK8](https://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html) _Versão 1.8.x_

  Para verificar sua versão, execute o comando `java -version` eu seu terminal de comando.

- [WidFly](http://wildfly.org/downloads/) _Versão 10.x_

O rst-cadastro se comunica com outras aplicações durante sua execução, então para o pleno funcionamento do projeto é necessário que as aplicações abaixo estejam sendo executadas durante o uso do rst-cadastro.

- [Antlr4ts](https://github.com/ezvida/antlr4ts)
- [adl-core](https://github.com/ezvida/adl-core)
- [fw-ezvida]
- [girst](https://github.com/ezvida/rst-girst)
- [girst-api-client] ()

# Instalação

**Atenção**
> Os passos 3 e 4 são necessários para que as dependências do projeto sejam baixadas diretamente do repositório Nexus.

1. Clone o projeto

2. Altere o arquivo .bashrc que fica em sua $HOME e crie a seguinte variável de ambiente:

```
export SOLUTIS_DEV_ENV = "true"
```

3. Inclua as chaves abaixo no arquivo "Settings.xml"

```
<username>SEU_USAURIO</username>
<password>SUA_SENHA</password>
```

**Atenção**
> Este arquivo **deve** estar dentro da pasta .m2 no diretório do seu usuário local. Crie a pasta .m2 caso não exista.

> Nos comandos acima, substitua SEU_USUARIO e SUA_SENHA por seus dados de acesso a rede.

4. Crie um arquivo chamado .npmrc em sua $HOME e adicione as seguintes instruções:

```
registry=http://nexus.solutis.net.br/content/groups/npmjsolutis/
always-auth=true
init-author-email=SEU_EMAIL
init-author-name=SEU_NOME
_auth=TOKEN_DE_AUTENTICAÇÂO
```

**Atenção**

> SEU_EMAIL = Digite seu e-mail corporativo.

> SEU_NOME = Digite seu nome.

> TOKEN_DE_AUTENTICAÇÂO = O Token de autenticação é um hash base64 gerado a partir de **seus dados de acesso a rede**, para criar esse token abra o terminal do seu sistema e execute o comando `echo -n 'SEU_USUARIO:@SUA_SENHA' | openssl base64`. Substitua o TOKEN_DE_AUTENTICAÇÃO pelo token gerado.

5. Crie o módulo no wildfly

Em wildfly-10.1.0.Final\modules crie a estrutura de pastas de acordo com o pacote do projeto:
```
br > com > ezvida > rst > load > main
```
Crie um arquivo chamado module.xml dentro da pasta main com o conteúdo abaixo:
```
<?xml version="1.0" encoding="UTF-8"?>
<module xmlns="urn:jboss:module:1.1" name="br.com.ezvida.rst.load">
<resources>
    <resource-root path="."/>
</resources>
</module>
```
Dentro da pasta main, crie uma pasta chamada certificados e crie as chaves abaixo dentro dela:

```
openssl genrsa -aes256 -out rsa.pem 2048
```

Exportando chave privada, necessário a senha configurada acima.

```
openssl pkcs8 -topk8 -inform PEM -outform PEM -in rsa.pem -out rsa-private.pem -nocrypt
```

Exportando chave publica, necessário a senha configurada acima.

```
openssl rsa -in rsa-private.pem -pubout -outform PEM -out rsa-public.pem
```

6. Configure o Eclipse ou Intellij

## Execução

Abra o rst-service no eclipse e inicie o Wildfly.

Para o frontend

```
## cd rst-web

npm start
```

Feito isso, será aberto o navegador padrão com: http://localhost:4200/cadastro/

## Testes

O projeto possui testes no backend em Java que são utilizado pelo Jeckins. Em desenvolvimento possui testes E2E (End to End).

```
## Para os testes e2e.

protractor
```

## Deploy

1- Vá no [Jenkins](http://jenkins.solutis.net.br/job/sesi%20rst/) do projeto, verifique se esta tudo ok com a versão, clique em workspace -> rst -> rst-app -> target -> baixe o arquivo terminado em .ear.

 2- Vá no [Wildfly](http://dev-wildfly.sesivivamais.com.br:9990/console/App.html#standalone-deployments) do projeto. Click em Deployments ->
escolha o EAR que você quer substituir -> disable ele. Depois REMOVE esse EAR. Vá até o botão add, selecione a primeira opção e aperte next, busque o .EAR que você baixou no seu computador.
Deixe o nome como está no de baixo no final, antes do .ear, coloque a data de hoje exemplo 01-01-2018. Aperte em finish.


## Pode Ajudar

 Um documento auxiliar pode ser encontrado em: [Doc](https://docs.google.com/document/d/12R7p-5y0QOlEIvBe_p0T0OIxLn0vWr3TmTSlKtlbcrE/edit?usp=sharing)

obs1: Maven updates
obs2: chmod -R 777 /nomePasta 
obs3: home >> solutis.net.br >> nome.usuario
obs4: su chown -R nome_de_usuario_utiliza_para_logar_em_sua_maquina /opt