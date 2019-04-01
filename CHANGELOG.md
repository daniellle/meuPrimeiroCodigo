# rst-cadastros

-Aplicações para manutenção dos cadastros RST

**v2.139.0**
(RELEASE EM 25/03/2019)
- [CADASTROS] IGEV - Habilitar a possibilidade de preencher formulário a qualquer tempo (GLPI 3309)
- [CADASTROS] Refatorar pesquisa e cadastro de usuários (v2.127.0 devolvido)
- [CADASTROS] Perfil Gestor de Conteúdos DR aparece duplicado na seleção do DR (GLPI 4458)
- [CADASTROS] Card Vacinas Autodeclaradas não está acessível (GLPI 3020)
- [CADASTROS] Não é possível vincular os perfis Profissional de Saúde SESI DR e Gestor de Conteúdos Indústria (GLPI 4464)
- [CADASTROS] - Pesquisar Usuário Gestor Unidade Sesi não consegue pesquisar. (GLPI 4812)

**v2.133.1**
(RELEASE EM 20/03/2019)
- [CADASTROS] Adequação dos dados retornados pelo endpoint '/public/v1/usuarios/dados/{cpf}' para sempre retornar a propriedade empresa->uats->departamentoRegional->estado
- [CADASTROS] Script que adiciona a permissões e os perfis relacionados ao Formulário de Estrutura de Unidade SESI

**v2.133.0**
(RELEASE EM 15/03/2019)
- [CADASTROS] Usuários que são do perfil GUS não possuem vínculos com a sua UAT. (GLPI 4620)
- [CADASTROS] Lista de Usuários x Perfil, Resultado de pesquisa não está retornando todos os dados (GLPI 2678)
- [CADASTROS] Erro ao pesquisar usuário (GLPI 4260)	
- [CADASTROS] Não é possível vincular os perfis Profissional de Saúde SESI DR e Gestor de Conteúdos Indústria (GLPI 4464)
- [CADASTROS] Não visualização do perfil GESTOR COMERCIAL DR (GLPI 4752)
- [CADASTROS] Pesquisa de Usuários (GLPI 4812)
- [CADASTROS] Código da eZVida para Alteração Formulario Esqueci Minha Senha, caractere "+" no e-mail
- [CADASTROS] Disponibilização do formulário de estrutura de Unidade SESI

**v2.127.0**
(RELEASE EM 28/02/2019)
- [CADASTROS] Otimização da API/Endpoint empresaFornecedor (INTERNO)
- [CADASTROS] Fixar as versões de bibliotecas de terceiros no arquivo package.json de todos os sistemas/projetos (INTERNO)
- [CADASTROS] Correção de Segurança na Pesquisa de Usuários 
- [CADASTROS] Lista de Usuários x Perfil, Resultado de pesquisa não está retornando todos os dados (GLPI 2678)
- [CADASTROS] Refatorar Pesquisa e Cadastro de Usuários (INTERNO)

**v.2.121.1**
(RELEASE EM 21/02/2019)
- [CADASTROS] Otimização da API/Endpoint empresaFornecedor

**v.2.121.0**
(RELEASE EM 15/02/2019)
- [CADASTROS] Ocultar os Perfis de Diretor DR e Diretor DN
- [CADASTROS] Mensagem de e-mail já existente ao se criar novo usuário não é exibida (GLPI 4461)
- [CADASTROS] Trabalhador sem perfil Trabalhador (GLPI 4308)
- [CADASTROS] Erro ao pesquisar unidade sesi (INTERNO)
- [CADASTROS] Usuário perfil DR Aplicações não consegue buscar usuário perfil Gestor Empresa Master e Gestor Empresa (GLPI 3837)
- [CADASTROS] ERRO NA PESQUISA DE NOVOS PROFISSIONAIS DE SAÚDE EMPRESA CADASTRADOS (GLPI 4476)
- [CADASTROS] Erro ao pesquisar usuário (GLPI 4260)
- [CADASTROS] Lista de Usuários x Perfil, Resultado de pesquisa não está retornando todos os dados (GLPI 2678)


**v.2.113.2**
(RELEASE EM 11/02/2019)

- [CADASTROS] Pesquisa de Usuários sem Filtro de Dados
- [CADASTROS] Erro ao associar Unidade SESI
- [CADASTROS] Remover perfil GESTOR DR PORTAL para seleção
- [CADASTROS] Colocar como Obrigatório do Campo Perfil em Associar DR, Empresa e Unidade SESI
- [CADASTROS] Relatório de Perfil - Mostrar DR
- [CADASTROS] Corrigir "Sem perfil cadastrado" no cadastro de usuário
- [CADASTROS] Erro ao salvar perfil do usuário Gestor Unidade Sesi
- [CADASTROS] O card EMPRESA não desaparece quando retirado o perfil
- [CADASTROS] Retirar bloco de "Unidades SESI Associadas" da tela de "Associar Empresas"
- [CADASTROS] Exibir Cards CADASTRO e DEPARTAMENTO REGIONAL após definir o perfil do usuário
- [CADASTROS] Card de associação, o campo PERFIL aparece sem opção para seleção

**v.2.113.0**
(RELEASE EM 29/01/2019)

- [CADASTROS] - Perfil Gestor Comercial Não visualiza indicadores de saúde (GLPI 3813)
- [CADASTROS] Erros Falta de Campos na "Lista de Usuários x Perfil" (GLPI 2678)
- [CADASTROS] - NOVO USUÁRIO - PERFIL GESTOR DR APLICAÇÕES - Sistema RES Online deve ser desabilitado para este perfil (GLPI_3185)
- [CADASTROS] - PESQUISAR USUÁRIO - PERFIL GESTOR DN - Na operação de busca exibe perfis indevidos para o usuário logado. (GLPI_3185)
- [CADASTROS] - EXIBIR CARDS - PERFIL GESTOR EMPRESA MASTER - Exibir corretamente os cards de acordo com os perfis cadastrados (GLPI_3185)
- [CADASTROS] - FIltro de Unidades Sesi Não retorna todas as informações da lista do OBA (GLPI 3783)
- [CADASTROS] - EXIBIR CARDS - PERFIL GESTOR DN - Exibir corretamente os cards de acordo com os perfis cadastrados (GLPI_3185)
- [CADASTROS] - NOVO USUÁRIO - PERFIL GESTOR DN - Gestor DR Portal Não deve estar disponível para seleção. (GLPI_3185)
- [CADASTROS] - NOVO USUÁRIO - PERFIL GESTOR DR MASTER - Gestor DR Portal Não deve estar disponível para seleção. (GLPI_3185)
- [CADASTROS] - NOVO USUÁRIO - PERFIL GESTOR SUPERINTENDENTE - Gestor DR Portal Não deve estar disponível para seleção. (GLPI_3185)
- [CADASTROS] - Remover carregamento de unidade obra na tela de Empresa RST
- [CADASTROS] - Para todos perfis - Pesquisa de Usuário exibe Usuários sem validar a hierarquia conforme usuário logado, permitindo visualizar, editar e excluir Usuários indevidamente. (GLPI_3185)
- [CADASTROS] - PESQUISAR USUÁRIO - PERFIL GESTOR DR MASTER - Na operação de busca exibe perfis indevidos para o usuário logado. (GLPI_3185)
- [CADASTROS] - EXIBIR CARDS - PERFIL GESTOR DR MASTER - Exibir corretamente os cards de acordo com os perfis cadastrados (GLPI_3185)
- [CADASTROS] - EXIBIR CARDS - PERFIL GESTOR DR APLICAÇÕES - Exibir corretamente os cards de acordo com os perfis cadastrados (GLPI_3185)
- [CADASTROS] - EXIBIR CARDS - PERFIL GESTOR UNIDADE SESI - Exibir corretamente os cards de acordo com os perfis cadastrados (GLPI_3185)
- [CADASTROS] - EXIBIR CARDS - PERFIL GESTOR SUPERINTENDENTE - Exibir corretamente os cards de acordo com os perfis cadastrados (GLPI_3185)
- [CADASTROS] - NOVO USUÁRIO - PERFIL GESTOR DN - Sistema RES Online deve ser desabilitado para este perfil (GLPI_3185)
- [CADASTROS] - NOVO USUÁRIO - PERFIL GESTOR DR MASTER - Sistema RES Online deve ser desabilitado para este perfil (GLPI_3185)
- [CADASTROS] - PESQUISAR USUÁRIO - PERFIL GESTOR DR APLICAÇÕES - Na operação de busca exibe perfis indevidos para o usuário logado. (GLPI_3185)
- [CADASTROS] - NOVO USUÁRIO - PERFIL GESTOR UNIDADE SESI - Sistema RES Online deve ser desabilitado para este perfil (GLPI_3185)
- [CADASTROS] - PESQUISAR USUÁRIO - PERFIL GESTOR UNIDADE SESI - Na operação de busca exibe perfis indevidos para o usuário logado. (GLPI_3185)
- [CADASTROS] - PESQUISAR USUÁRIO - PERFIL GESTOR EMPRESA MASTER - Na operação de busca exibe perfis indevidos para o usuário logado. (GLPI_3185)
- [CADASTROS] - NOVO USUÁRIO - PERFIL GESTOR GESTOR EMPRESA MASTER - Sistema RES Online deve ser desabilitado para este perfil (GLPI_3185)
- [CADASTROS] - PESQUISAR USUÁRIO - PERFIL GESTOR SUPERINTENDENTE - Na operação de busca exibe perfis indevidos para o usuário logado. (GLPI_3185)
- [CADASTROS] - NOVO USUÁRIO - PERFIL GESTOR GESTOR SUPERINTENDENTE - Sistema RES Online deve ser desabilitado para este perfil (GLPI_3185)
- [CADASTROS] - Encontre Uma Unidade Sesi - Para o Filtro ESTADO, os Estados exibidos para seleção Não devem estar vinculado ao perfil do usuário logado. (GLPI 3783)
- [CADASTROS] - Encontre Uma Unidade Sesi - Para o Filtro UNIDADE SESI, as Unidades Sesi exibidas para seleção Não devem estar vinculadas a DR do usuário logado (GLPI 3783)
- [CADASTROS] - Mecanismo de "esqueci minha senha" Não está funcionando para usuário vindo do barramento com perfil associado. (GLPI 4269)
- [CADASTROS] - CARD contratos Não é apresentado (GLPI 2115)
- [CADASTROS] - ADM Não visualiza "Lista de Usuários x Perfil"

**v.2.105.0**
(RELEASE EM 09/01/2019)

- [CADASTROS] Usuário perfil DR Aplicações não consegue buscar usuário perfil Gestor Empresa Master e Gestor Empresa - GLPI 3837.
- [CADASTROS] Ajustes no campo de foto - já solicitado no chamado 1899 (GLPI 3017)
- [CADASTROS] Card Vacinas Autodeclaradas não está acessível (GLPI 3020)
- [CADASTROS] Minha Saúde - Erro filtro histórico de FCO (GLPI 2951)
- [CADASTROS] Minha Saúde - Mensagem "Registro não encontrado" (GLPI 2951)
- [CADASTROS] Lista de Usuários x Perfil (GLPI 2678)
- [CADASTROS] IGEV - Habilitar a possibilidade de preencher formulário a qualquer tempo (GLPI 3309)
- [CADASTROS] Campo Estado civil: colocar conforme e-social (Interno)
- [CADASTROS] Perfil gestor comercial não visualiza indicadores de saúde (GLPI 3837)

**v.2.103.0**
(RELEASE EM 21/12/2018)

- [CADASTRO] Cadastro de Usuario agora possui integração para usuarios vindos do Barramento.
- [CADASTRO] Cards são exibidos de acordo com as associações de Usuário com CNPJ
- [CADASTRO] Pesquisa de Usuário agora possui campo de perfil "Sem Perfil", para procura de usuarios vindos do Barramento.
- [CADASTRO] Os cards de "Unidade SESI", "Departamento Regional" e "Empresa" na tela intermediária de Usuário foram adaptados para suportar usuarios do Barramento.
- [CADASTRO] Cards "Unidade SESI", "Departamento Regional" e "Empresa" mostram somente perfis para associação referentes a eles.
- [CADASTRO] Card Cadastro Basico mostra para Diretor DN somente as opções de associação de perfil referentes a DN.
- [CADASTRO] Usuarios sem perfil não podem alterar senha
- [CADASTRO] Usuarios sem perfil não podem fazer primeiro acesso
- [CADASTRO] Primeira associação de perfil para usuario do Barramento resulta no envio de um email de confirmação para o usuario associado
- [CADASTRO] Adicionado bloco novo em cada um dos CARDS de Usuarios em que faz a relação de CNPJ sem perfil,com botão para associar perfil
- [CADASTRO] Corrigido problema em que API não retorna vinculo com usuário(GLPI 3867)
- [CADASTRO] Corrigido problema em que API não retornava CNPJ para usuário DN (GLPI 1551)

**v.2.99.0**
(RELEASE EM 06/12/2018)

- [CADASTRO] Perfil Médico do Trabalho Empresa não está mais disponível para Cadastro (GLPI 3510)
- [CADASTRO] Alterado Gênero para Sexo em Cadastro do Trabalhador
- [CADASTRO] Novo campo "Nome Social" em Cadastro do Trabalhador
- [CADASTRO] Alterado de Raça Preta para Negra em Cadastro de Trabalhador
- [CADASTRO] Retirado campo BR/PDH do Cadastro do Trabalhador
- [CADASTRO] Removido campo profissão do Cadastro do Trabalhador
- [CADASTRO] Alterado labels em cadastro da empresa de CNPJ para CNPJ/CPF
- [CADASTRO] Criado sistema Epidemiologia para Cadastro de Usuário
- [CADASTRO] Alterado visualização dos perfis para ser por ordem alfabética em colunas na seção Associar Perfil em Cadastro de Usuário
- [CADASTRO] Alterado nome do perfil MÉDICO DO TRABALHO para PROFISSIONAL DE SAÚDE
- [CADASTRO] Alterado Sistema Cadastro para abranger sistemas "Indicadores e Análise Dinâmica", "Indicador IGEV" e "Portal" em Cadastro de Usuário
- [CADASTRO] Adicionado ícones de lápis e lixeira em listagem de usuários para perfis com permissão de criar, editar ou excluir perfil em Associação de Perfil em Cadastro de Usuário
- [CADASTRO] Perfil Epidemiologia de Sistema Epidemiologia só pode ser salvo se houver um perfil do sistema Cadastro já escolhido em Cadastro de Usuário

# **v.2.97.0**

(RELEASE EM 29/11/2018)

- Tela para gerenciar (incluir, editar, desativar/ativar, pesquisar, resetar client_secret) Sistemas Credenciados para o projeto do barramento Sesi Viva+ ESB

# **v.2.91.0**

(RELEASE EM 16/11/2018)

- [CADASTROS] IGEV - Habilitar a possibilidade de preencher formulário a qualquer tempo (GLPI 3309)
- [CADASTROS] Card Vacinas Autodeclaradas não está acessível (GLPI 3020)
- [CADASTROS] Minha Saúde - Ajustes solicitados pelo DR/BA - email de 16/10/2018 (GLPI 2951)
- [CADASTROS] Erro combo Pesquisa Departamento Regional /AC (GLPI 3144)
- [CADASTROS] Ajustes no campo de foto - já solicitado no chamado 1899 (GLPI 3017)
- [CADASTROS] Fazer script para colocar perfil TRABALHADOR para ANDREA FERREIRA LEITE cpf 82099340153 (GLPI 3089)
- [CADASTROS] Não é possível pesquisar usuários pelo menu CADASTRO DE USUÁRIOS (GLPI 3369)
- [CADASTROS] Mudança de mensagem para email não encontrado em "Esqueci minha senha" (GLPI 1533)

# **v2.87.1**

(RELEASE EM 07/11/2018)

- [CADASTRO] - Consertado erro que impedia que Gestor Unidade Sesi cadastrasse Gestor Empresa e Gestor Empresa Master (GLPI 3246)

# **v2.87.0**

(RELEASE EM 05/11/2018)

- [CADASTRO] - Consertado erro que fazia Gestor perder a associação com a empresa caso fosse alterado o perfil (GLPI 3050)
- [CADASTRO] - Consertado erro que card vacinas autodeclaradas não estar acessível (GLPI 3020)
- [CADASTRO] - Consertado erro que não mostrava os perfis GESTOR UNIDADE SESI e GESTOR MEDICO DO TRABALHO EMPRESA para GESTOR associar (GLPI 2610)
- [CADASTRO] - Desativada edição de foto em CADASTRO BÁSICO(GLPI 3017)
- [CADASTRO] - Alteração da posição do Ícone de mudança de foto(GLPI 3017)
- [CADASTRO] - Consertado erro que direcionava usuário para tela em branco(GLPI 3017)
- [CADASTRO] - Mudanças estéticas solicitadas por DR/BA(GLPI 2951)
- [CADASTRO] - Adicionado redirecionamento para usuário que tenha email incorreto ao fazer login(GLPI 1533)
- [CADASTRO] - Consertado erro que bloqueava campos na tela de criar nova empresa
- [CADASTRO] - Adicionado opção de preencher Formulário IGEV a qualquer tempo.

# **v2.81.2**

(RELEASE EM 26/10/2018)

- [CADASTROS] Corrigido: Ao acessar o módulo com o perfil SUPERINTENDENTE DR e tentar criar um perfil GESTOR DR MASTER não consigo vincular um DR ao novo usuário cadastrado. Surge mensagem de erro e o resultado de pesquisa para o DR não é exibido.
- [CADASTROS] Corrigido: O Gestor DR Aplicações tenta criar usuário GESTOR UNIDADE SESI Card do DR e da Unidade não são exibidos para vinculação.

# **v2.81.1**

(RELEASE EM 24/10/2018)

- [CADASTROS] Erro arquivo pon.xml (versão 2.81.0) to (versão 2.81.1)
- [CADASTROS] Erro campo CPF do Primeiro Acesso

# **v2.81.0**

(RELEASE EM 22/10/2018)

**Novidades**

- [CADASTROS] Link Criação de Senhas Primeiro Acesso (GLPI 2493)
- [CADASTROS] Indisponibilidade de Dados em Produção /Trabalhador (GLPI 2520 e 2595)
- [CADASTROS] Remover restrição para responder o formulário de IGEV (GLPI 1641)
- [CADASTROS] Melhorar mecanismo de esqueci minha senha (GLPI 1533)
- [CADASTROS] Card Departamento Regional Superintendente (GLPI 2729)
- [CADASTROS] PERFIL Médico do Trabalho SESI DR deve visualizar os mesmos cards do trabalhador que os demais perfis médicos (GLPI 2535)
- [CADASTROS] Erro Cadastro de Usuário Master por Superintendente (GLPI 2675)
- [CADASTROS] Erro - circunferência abdominal não é exibida em dashboard de saúde (GLPI 2445)
- [CADASTROS] Gestão de Fornecedores - API com retorno de dados do CNPJ.

Obs.: Conforme definição da EZVIDA, a SOLUTIS desenvolveu um serviço para retornar os dados por CNPJ em formato JSON e a Tela de Indicadores em i-frame exibindo os dados de outro CNPJ requerido, a ser informado através de uma estrutura de URL (exemplo: https://www.sesivivamais.com.br/......indicadores/CNPJ).
Vale ressaltar que não foi definido no escopo o serviço que deverá ser consultado para validar se o usuário logado tem permissão para visualizar os dados do CNPJ em questão, desta forma, a aplicação de Indicadores da Rede SESI VIVA+ NÃO fará essa validação. Assim, outros usuários que estejam logados na rede SESI VIVA+, caso conheçam a URL, poderão ter acesso indevido aos dados do CNPJ requerido, essa situação se caracteriza como uma falha de segurança no controle de permissões da rede SESI VIVA+.
=======

# **v2.75.0**

(RELEASE EM 05/10/2018)

**Novidades**

[CADASTROS] Ajustes nas permissões de cadastro conforme perfil (GLPI 2199)
[CADASTROS] Todas as mensagens de erro do portal devem constar o telefone o call center (GLPI 2301)

---

**Melhorias**

[CADASTROS] Em Unidades SESI - Campo Departamento Regional deve ser aumentado (GLPI 2286)
[CADASTROS] Ajustar foto de Minha Conta (GLPI 1899)

---

**Correções**

- [CADASTROS] Erros identificados no MINHA SAÚDE 03 (GLPI 2133)
- [CADASTROS] Erro para visualizar permissões no cadastramento de usuários (RST > Sustentação) (GLPI 2598 e 2592)

---

**Instruções para deploy**

1. Executar o script(https://github.com/ezvida/rst-aplicacoes/tree/master/Documenta%C3%A7%C3%A3o/BANCO/v2.75.0) no banco REDE_SESI.
2. Realizar deploy do [build](https://github.com/ezvida/rst-cadastros/blob/release/rst/README.md) do ear nos respectivos ambientes.
3. Atualizar arquivo da tera de erro padrão do Shibboleth (erro.jsp)

---

# **v2.71.0**

(RELEASE EM 26/09/2018)

**Novidades**

---

**Melhorias**

---

**Correções**

- Ajuste em imunizações - Minha Saúde (GLPI 2448)
- Ajuste ao excluir o usuário

---

**Instruções para deploy**

1. Realizar deploy do [build](https://github.com/ezvida/rst-cadastros/blob/release/rst/README.md) do ear nos respectivos ambientes

---

# **v2.65.1**

(RELEASE EM 25/09/2018)

**Novidades**

---

**Melhorias**

---

**Correções**

- [CADASTROS] Resultado do IMC no DASHBOARD de saúde está desconfigurado (GLPI 2133)

---

**Instruções para deploy**

1. Realizar deploy do [build](https://github.com/ezvida/rst-cadastros/blob/release/rst/README.md) do ear nos respectivos ambientes

---

# **v2.45.0**

(RELEASE EM 15/08/2018)

**Novidades**

- Gráficos de Hábitos de Vida com comparativos para as visões DN, DR e Empresa
- Gráficos de Hábitos de Vida com comparativos para as visões DN, DR e Empresa
- Filtro para ser aplicado em todos os indicadores

---

**Melhorias**

- Permitir a busca por empresa utilizando o CNPJ
- Todas as categorias dos indicadores de SST selecionadas por default
- Modificado cor de fundo para a mesma cor do portal

---

**Correções**

- Ajuste no campo de pesquisa “Unidade SESI”

---

# **v2.39.0**

(RELEASE EM 31/07/2018)

**Novidades**

---

**Melhorias**

- Modificado a apresentação dos dados da minha saúde exibindo os FCÓs mais recentes
- Modificado a obrigatoriedade dos campos de endereço e unidade sesi no cadastro de departamento regional

---

**Correções**

- Ajuste no usuario administrador para consultar empresas
- Ajuste na exibição da idade no dashborad de saúde

---

# **v2.35.0**

(RELEASE EM 23/07/2018)

**Novidades**

- Cadastro e consulta de Vacinas Autodeclaradas pelo Trabalhador
- Exibição dos dados de fichas de Imunizações na tela de Minha Saúde

---

**Melhorias**

---

**Correções**

---

# **v2.29.0**

(RELEASE EM 13/07/2018)

**Novidades**

- Criação de perfis de usurios master

---

**Melhorias**

- Solicitar nova token do RES apenas quando der erro 401

---

**Correções**

- Ajustes necessários nos perfis Gestor DR e Master e Gestor DN

---

# **v2.25.0**

(RELEASE EM 05/07/2018)

**Novidades**

---

**Melhorias**

- Solicitar nova token do RES apenas quando der erro 401

---

**Correções**

- Perfis duplicados ao editar associação do sistema
- Erro ao pesquisar profissional por CPF

---

# **v2.23.0**

(RELEASE EM 03/07/2018)

**Novidades**

- Diponibilização dos serviços do IGEV via JWT

---

**Melhorias**

---

**Correções**

---

# **v2.17.1**

(RELEASE EM 26/06/2018)

**Novidades**

---

**Melhorias**

---

**Correções**

- Ajuste no serviço para retornar os trabalhadores para o RES Online
- Ajuste de redirecionamento após salvar dados de Minha Conta

---

# **v2.17.0**

(RELEASE EM 21/06/2018)

**Novidades**

---

**Melhorias**

- Adição de permissão do trabalhador no APP Mobile após realizar o primeiro acesso

---

**Correções**

- Ajuste ao salvar foto e apelido em Minha Conta
- Ajuste no serviço para retornar os trabalhadores para o RES Online
- Ajuste para exibir os dados da última ficha em Minha Saúde

---

# **v2.15.0**

(RELEASE EM 07/06/2018)

**Novidades**

---

**Melhorias**

- Modificado busca dos filtros de texto para "contêm"
- Modificado Pesquisa de Empresas
- Modificado Pesquisa de Profissionais
- Modificado Pesquisa de Trabalhador
- Removido botão para cadastrar novo trabalhador
- Nova imagem para o card Minha Conta

---

**Correções**

- Ajuste do filtro para pesquisar Unidade Sesi
- Removido exibição do valor "0" em Circunferência Abdominal

---

# **v2.13.0**

(RELEASE EM 04/06/2018)

**Novidades**

- Inclusão do perfil Profissional de Saúde e do Sistema Res Online no cadastro do usuário

---

**Melhorias**

- Adaptação da tela de Minha Saúde para a nova versão do RES

---

**Correções**

---

# **v2.11.0**

(RELEASE EM 24/05/2018)

**Novidades**

- Inclusão da página Minha Conta
- Atualização do termo de uso
- Atualização do Sincato com dados do SIGA

---

**Melhorias**

- Sincronização dos dados do Usuário com o Trabalhador
- Validação do e-mail do Usuário com perfil diferente de Trabalhador na tela de Primeiro Acesso

---

**Correções**

- Exibir idade do trabalhador de acordo com os dados da última ficha na tela de Minha Saúde

---

# **v2.8.0**

**Novidades**

- Mudança no tratamento ao clicar em um link expirado para cadastro de senha

---

# **v2.7.0**

(RELEASE EM 09/05/2018)

**Novidades**

- Mudança de regra na definição de senhas para novos usuários

---

**Melhorias**

- Modificado acesso à API de cadastros
- Adicionado "Pressão Sanguínea" no histórico do exame
- Modificado gráfico de minha saúde para contemplar dois antendimentos

---

**Correções**

- Ajuste no filtro do pesquisar Unidade Sesi
- Ajuste na ordem cronológica nos gráficos de minha saúde
- Tratamento de erro para minimizar bug do Retrofit ao retornar body null nos serviços
- Ajuste da token do serviço do RES para atualizar dinamicamente

---

# **v2.4.0**

(RELEASE EM 24/04/2018)

**Novidades**

---

**Melhorias**

---

**Correções**

- Exibir atendimentos com mesma data na tela de Minha Saúde

---

# **v2.3.7.1**

(RELEASE EM 19/04/2018)

**Novidades**

- Adição do campo Conselho Regional no formulário de cadastro do Profissional
- Adição do indicador de Vida Ativa no cadastro do Trabalhador
- Adição de Apelido e Foto do Perfil no Web Service da Consultar Dados do Usuário

---

**Melhorias**

---

**Correções**

- Ajuste de layout nas telas de Profissional e Trocar Senha
- Ajuste de limite do campo RG na tela de Profissional

---

# **v2.3.7**

(RELEASE EM 13/04/2018)

**Novidades**

- Minha Conta | Possibilidade de alterar senha, definir um nome de usuário alterar sua foto (GLPI_533) (rst-cadastros/issues/20)
- Trabalhador deve poder inserir os medicamentos que toma e referir alergias e vacinas pelo MINHA SAÚDE (GLPI_400) (rst-cadastros/issues/16)

---
