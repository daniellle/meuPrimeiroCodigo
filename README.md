# rst-cadastros
-Aplicações para manutenção dos cadastros RST

**v2.23.0**

============

(RELEASE EM 03/07/2018)

**Novidades**

	Diponibilização dos serviços do IGEV via JWT

------------

**Melhorias**
	
	
------------

**Correções**

------------


**v2.17.1**

============

(RELEASE EM 26/06/2018)

**Novidades**


------------

**Melhorias**
	
	
------------

**Correções**

	Ajuste no serviço para retornar os trabalhadores para o RES Online
	Ajuste de redirecionamento após salvar dados de Minha Conta
	
------------



**v2.17.0**

============

(RELEASE EM 21/06/2018)

**Novidades**


------------

**Melhorias**
	
	Adição de permissão do trabalhador no APP Mobile após realizar o primeiro acesso

------------

**Correções**

	Ajuste ao salvar foto e apelido em Minha Conta
	Ajuste no serviço para retornar os trabalhadores para o RES Online
	Ajuste para exibir os dados da última ficha em Minha Saúde

------------


**v2.15.0**

============

(RELEASE EM 07/06/2018)

**Novidades**


------------

**Melhorias**
	
	Modificado busca dos filtros de texto para "contêm"
	Modificado Pesquisa de Empresas
	Modificado Pesquisa de Profissionais
	Modificado Pesquisa de Trabalhador
	Removido botão para cadastrar novo trabalhador
	Nova imagem para o card Minha Conta

------------

**Correções**

	Ajuste do filtro para pesquisar Unidade Sesi
	Removido exibição do valor "0" em Circunferência Abdominal

------------


**v2.13.0**

============

(RELEASE EM 04/06/2018)

**Novidades**

	Inclusão do perfil Profissional de Saúde e do Sistema Res Online no cadastro do usuário

------------

**Melhorias**
	
	Adaptação da tela de Minha Saúde para a nova versão do RES

------------

**Correções**

------------


**v2.11.0**

============

(RELEASE EM 24/05/2018)

**Novidades**

	Inclusão da página Minha Conta 
	Atualização do termo de uso
	Atualização do Sincato com dados do SIGA

------------
**Melhorias**
	
	Sincronização dos dados do Usuário com o Trabalhador
	Validação do e-mail do Usuário com perfil diferente de Trabalhador na tela de Primeiro Acesso

------------

**Correções**
	
	Exibir idade do trabalhador de acordo com os dados da última ficha na tela de Minha Saúde

------------


**v2.8.0**

============

**Novidades**
	Mundaça no tratamento ao clicar em um link expirado para cadastro de senha


------------


**v2.7.0**

============

(RELEASE EM 09/05/2018)

**Novidades**

	Mudança de regra na definição de senhas para novos usuários

------------
**Melhorias**
	
	Modificado acesso à API de cadastros
  	Adicionado "Pressão Sanguínea" no histórico do exame
  	Modificado gráfico de minha saúde para contemplar dois antendimentos

------------

**Correções**

	Ajuste no filtro do pesquisar Unidade Sesi
  	Ajuste na ordem cronológica nos gráficos de minha saúde
  	Tratamento de erro para minimizar bug do Retrofit ao retornar body null nos serviços
  	Ajuste da token do serviço do RES para atualizar dinamicamente

------------

**v2.4.0**

============

(RELEASE EM 24/04/2018)

**Novidades**

------------
**Melhorias**

------------

**Correções**

------------

- Exibir atendimentos com mesma data na tela de Minha Saúde


**v2.3.7.1**

============

(RELEASE EM 19/04/2018)

**Novidades**

- Adição do campo Conselho Regional no formulário de cadastro do Profissional
- Adição do indicador de Vida Ativa no cadastro do Trabalhador
- Adição de Apelido e Foto do Perfil no Web Service da Consultar Dados do Usuário

------------
**Melhorias**

------------

**Correções**

------------

- Ajuste de layout nas telas de Profissional e Trocar Senha
- Ajuste de limite do campo RG na tela de Profissional


**v2.3.7**

============

(RELEASE EM 13/04/2018)

 

**Novidades**

------------

- Minha Conta | Possibilidade de alterar senha, definir um nome de usuário alterar sua foto (GLPI_533) (rst-cadastros/issues/20)

- Trabalhador deve poder inserir os medicamentos que toma e referir alergias e vacinas pelo MINHA SAÚDE (GLPI_400) (rst-cadastros/issues/16)