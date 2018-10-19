Instalação
==========

1- Baixe o projeto: 

```
$ git clone http://bulma.solutis.net.br/ezvida/sesi.git
```

2- Alterar variável de Ambiente

```
$ vim .bashrc
$ export SOLUTIS_DEV_ENV="true"
```

3- Editar e Salvar o Settings.mxl (Salve o arquivo, e coloque ele na pasta .m2 na pasta pessoal, caso a pasta não exista então crie)

```
<username>nome.usuario</username>
<password>senha</password>
```

4- Crie ou copie de alguém um arquivo .npmrc na sua pasta pessoal. Para sistemas Linux edite com sua informações o comando:

```
echo -n 'nome_de_usuario_utiliza_para_logar_em_sua_maquina:@senha_de_usuario_utiliza_para_logar_em_sua_maquina' | openssl base64
```

5- Copie e a String gerada e cole como valor do campo _auth que fica logo no inicio do arquivo .npmrc

```
registry=http://nexus.solutis.net.br/content/groups/npmjsolutis/
always-auth=true
init-author-email=danilo.silva@solutis.com.br
init-author-name=danilo Silva
_auth=ZGFuaWxvLnNpbHZhOjFSN0YzQDNt
```
6- Configure o Eclipse ou Intellij.

## Rodando o Projeto

Abra o rst-service no eclipse e inicie o Wildfly. 
Para o frontend

```
## cd rst-web

npm start
```

Feito isso, será aberto o navegador padrão com: http://localhost:4200/cadastro/

## Testes automatizados

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

## Crie o módulo no wildfly

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


## Pode Ajudar

# Um documento auxiliar pode ser encontrado em: [Doc](https://docs.google.com/document/d/12R7p-5y0QOlEIvBe_p0T0OIxLn0vWr3TmTSlKtlbcrE/edit?usp=sharing) 

obs1: Maven updates
obs2: chmod -R 777 /nomePasta 
obs3: home >> solutis.net.br >> nome.usuario
obs4: su chown -R nome_de_usuario_utiliza_para_logar_em_sua_maquina /opt


