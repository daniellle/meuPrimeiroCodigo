# RST Cadastro

Manual de montagem de ambiente de desenvolvimento.

## Conteúdo

- [Pré requisitos](#pré-requisitos)
- [Configuração](#configuração)


## Pré requisitos

O RST Cadastro depende de outras bibliotecas e aplicações. Certifique-se de que todas as dependências estão instaladas. Para mais informações consulte o arquivo [README.md](../README.md) deste projeto.

## Configuração

*Atenção*

> Neste momento a sua IDE já deve estar configurada com o JDK8 e o servidor WidFly, conforme instruções da sessão pré requisitos.

1. Edite o arquivo .bashrc que fica na pasta do seu usuário local e adicione a seguinte variável de ambiente:

    ```
    export SOLUTIS_DEV_ENV = "true"
    ```

2. Configurando o certificado de acesso no WildFly

    2.1 Acesse a pasta de instalação do WidFly, em seguida, Modules e crie a estrutura de pastas abaixo:
    ```
    br > com > ezvida > rst > load > main > certificados
    ```
    2.2 Dentro da pasta main crie o arquivo _module.xml_ com o conteúdo abaixo:
    ```
    <?xml version="1.0" encoding="UTF-8"?>
    <module xmlns="urn:jboss:module:1.1" name="br.com.ezvida.rst.load">
    <resources>
        <resource-root path="."/>
    </resources>
    </module>
    ```
    2.3 Abra o terminal de comando do seu sistema, acesse a pasta certificados, criada anteriormente, e execute os comandos abaixo, um por vez e na sequência descrita:
    
    - ```openssl genrsa -aes256 -out rsa.pem 2048``` _(Será solicitada uma senha, pode ser utilizado sua senha de acesso a rede.)_
    
    - ```openssl pkcs8 -topk8 -inform PEM -outform PEM -in rsa.pem -out rsa-private.pem -nocrypt``` _(Será solicitada uma senha, informe  a mesma senha que foi utilizada no comando anterior.)_
    
    - ```openssl rsa -in rsa-private.pem -pubout -outform PEM -out rsa-public.pem```

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