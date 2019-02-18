# RST Cadastro

Manual de montagem de ambiente de desenvolvimento.

## Conteúdo

- [Pré requisitos](#pré-requisitos)
- [Instalação](#instalação)


## Pré requisitos

O RST Cadastro depende de outras bibliotecas e aplicações. Certifique-se de que todas as dependências estão instaladas. Para mais informações consulte o arquivo [README.md](../README.md) deste projeto.

## Instalação

**Atenção**
> Os passos 1 e 2 são obrigatórios para que o download das dependências do projeto sejam realizados a partir do repositório Nexus.

1. Inclua os dados de acesso abaixo em seu arquivo "Settings.xml"

```
<username>SEU_USAURIO</username>
<password>SUA_SENHA</password>
```

**Atenção**
> Este arquivo **deve** estar dentro da pasta .m2 no diretório do seu usuário local. Crie a pasta .m2 caso não exista.

> Nos comandos acima, substitua SEU_USUARIO e SUA_SENHA por seus dados de acesso a rede.

2. Crie um arquivo chamado .npmrc em sua $HOME e adicione as seguintes instruções:

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

3. Altere o arquivo .bashrc que fica em sua $HOME e crie a seguinte variável de ambiente:

```
export SOLUTIS_DEV_ENV = "true"
```

4. Clone o projeto rst-cadastro

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