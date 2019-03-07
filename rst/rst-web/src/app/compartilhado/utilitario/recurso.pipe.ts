import { Pipe, PipeTransform } from '@angular/core';

export const MensagemProperties = {

  // SISTEMA E lOGIN
  app_versao: '${project.version}',
  app_rst_operacao_invalida: 'Operação invalida',
  app_rst_operacao_nao_realizada: 'Não foi possível processar a operação',
  app_seguranca_acesso_negado: 'Usuário não possui acesso autorizado.',
    app_seguranca_email_nao_cadastrado: 'Email informado não cadastrado',
    app_rst_usuario_email_invalido: 'Digite um e-mail válido',
    app_seguranca_usuario_barramento: 'Usuário não possui perfil associado',
  app_rst_usuario_senha_invalido: 'Senha não poder ser branco ou nulo',
  app_rst_usuario_credencias_invalida: 'Não foi possível validar as credências do usuário!',
  app_rst_operacao_sucesso: 'Operação realizada com sucesso!',
  app_rst_operacao_sucesso_trabalhador: 'Operação realizada com sucesso! Para prosseguir com o cadastro, favor cadastrar uma data de admissao para o trabalhador na empresa.',
  app_rst_usuario_email: 'Digite seu E-mail',
  app_rst_usuario_senha: 'Digite sua Senha',
  app_rst_usuario_recuperar_senha: 'Recuperar senha?',
  app_rst_menu_dashboard: 'Dashboard',
  app_rst_menu_prontuario: 'Prontuário Médico',
  app_rst_menu_funcionalidade: 'Funcionalidade {0}',
  app_rst_menu_componentes: 'Componentes',
  app_rst_menu_componente: 'Componente {0}',
  app_rst_menu_perfil: 'Perfil',
  app_rst_menu_sair: 'Sair',
  app_rst_registro_novo: 'Novo',
  app_rst_autenticacao_entar: 'Entrar',
  app_rst_retorno_pagina_inicial:'Retornar para a Página Inicial',

  // LABELS COMPARTILHADOS
  app_rst_menu_cadastro: 'Cadastro',
  app_rst_menu_trabalhador: 'Trabalhador',
  app_rst_menu_empresa: 'Empresa',
  app_rst_labels_nome: 'Nome',
  app_rst_labels_data: 'Data',
  app_rst_labels_resultado: 'Resultado',
  app_rst_labels_cpf: 'CPF',
  app_rst_labels_ambiente: 'Ambiente',
  app_rst_labels_razao_social: 'Razão Social',
  app_rst_labels_data_desativacao: 'Data de Desativação',
  app_rst_labels_data_nascimento: 'Data de Nascimento',
  aps_rst_labels_data_vacina: 'Data de Vacinação',
  app_rst_labels_data_nascimento_required: 'Data de Nascimento *',
  app_rst_labels_data_falecimento: 'Data de Falecimento',
  app_rst_labels_data_naturalizacao: 'Data de Naturalização',
  app_rst_labels_data_futura: 'O campo {0} não pode ser maior que a data atual.',
  app_rst_labels_data_1_maior_que_data_2: 'O campo {0} não pode ser maior que o campo {1}.',
  app_rst_label_dados_gerais: 'Dados Gerais',
  app_rst_label_cnpj_sem_perfis: 'CNPJ sem Perfis Associados',
  app_rst_label_incluir: 'Incluir',
  app_rst_labels_sexo: 'Sexo',
  app_rst_labels_genero: 'Gênero',
  app_rst_labels_rg: 'RG',
  app_rst_labels_data_endereco_contato: 'Endereço e Contato',
  app_rst_labels_data_informacoes_adicionais: 'Informações Adicionais',
  app_rst_labels_tipo_profissional: 'Tipo de Profissional',
  app_rst_labels_tipo_curso: 'Tipo de Curso',
  app_rst_labels_tipo_resposta: 'Tipo de Resposta',
  app_rst_labels_modalidade: 'Modalidade',
  app_rst_labels_carga_horaria: 'Carga horária',
  app_rst_labels_data_validade: 'Data de validade',
  app_rst_labels_certificado: 'Certificado',
  app_rst_labels_nit: 'NIT',
  app_rst_labels_nit_responsavel: 'NIT Responsável',
  app_rst_labels_nit_contato: 'NIT Contato',
  app_rst_labels_rg_conselho_regional: 'Registro no Conselho Regional',
  app_rst_labels_cnpj: 'CNPJ',
  app_rst_labels_cnpj_cpf: 'CNPJ/CPF',
  app_rst_labels_nome_fantasia: 'Nome Fantasia',
  app_rst_labels_horas_semanais: 'Horas Semanais',
  app_rst_labels_situacao: 'Situação',
  app_rst_labels_inscricao_estadual: 'Inscr. Estadual',
  app_rst_labels_inscricao_munincipal: 'Inscr. Munincipal',
  app_rst_labels_sesmt: 'SESMT',
  app_rst_labels_cipa: 'CIPA',
  app_rst_labels_matriz: 'MATRIZ',
  app_rst_labels_endereco: 'Endereço',
  app_rst_labels_quantidade: 'Quantidade',
  app_rst_labels_tipo_de_atendimento: 'Tipo de atendimento',
  app_rst_labels_complemento: 'Complemento',
  app_rst_labels_numero: 'Número',
  app_rst_labels_estado: 'Estado',
  app_rst_labels_conselhoregional: 'ConselhoRegional',
  app_rst_labels_jornada: 'Jornada',
  app_rst_labels_data_inicio: 'Data Inicio',
  app_rst_labels_data_fim: 'Data fim',
  app_rst_labels_ano_vigencia: 'Ano Vigência',
  app_rst_labels_tipo_programa: 'Tipo Programa',
  app_rst_labels_unidade_sesi: 'Unidade Sesi',
  app_rst_remover_segmento: 'Remover Segmento',
  app_rst_remover_setor: 'Remover Setor',
  app_rst_remover_cargo: 'Remover Cargo',
  app_rst_remover_funcao: 'Remover Função',
  app_rst_labels_municipio: 'Município',
  app_rst_labels_municipios: 'Municípios',
  app_rst_labels_municipio_nascimento: 'Município Nascimento',
  app_rst_labels_estado_nascimento: 'Estado Nascimento',
  app_rst_labels_bairro: 'Bairro',
  app_rst_labels_CEP: 'CEP',
  app_rst_labels_adicionar_produto: 'Produto',
  app_rst_labels_adicionar_servico: 'Serviço',
  app_rst_labels_adicionar_telefone: 'Telefone',
  app_rst_labels_adicionar_uat: 'Unidade SESI',
  app_rst_labels_telefone: 'Telefone',
  app_rst_labels_tipo: 'Tipo',
  app_rst_labels_turno: 'Turno',
  app_rst_labels_email: 'E-mail',
  app_rst_labels_email_alternativo: 'E-mail Alternativo',
  app_rst_labels_cargo: 'Cargo',
  app_rst_labels_apelido: 'Apelido',
  app_rst_labels_exibir_apelido: 'Exibir Apelido',
  app_rst_labels_selecione: 'Selecione',
  app_rst_labels_residencial: 'Residencial',
  app_rst_labels_comercial: 'Comercial',
  app_rst_labels_fax: 'Fax',
  app_rst_labels_categoria: 'Categoria',
  app_rst_labels_celular: 'Celular',
  app_rst_labels_contato: 'Contato',
  app_rst_labels_ativo: 'Ativo',
  app_rst_labels_inativo: 'Inativo',
  app_rst_labels_especialidades: 'Especialidades',
  app_rst_labels_registro_profissional_up: 'Registro Profissional',
  app_rst_labels_nome_profissional_up: 'Nome do Profissional',
  app_rst_labels_epecialidade_up: 'Especialidade',
  app_rst_labels_tipo_profissional_up: 'Tipo de Profissional',
  app_rst_labels_uat_up: 'Unidade SESI',
  app_rst_labels_responsavel: 'Responsável',
  app_rst_labels_situacao_todos: 'Todos',
  app_rst_labels_pais: 'País',
  app_rst_labels_pais_nascimento: 'País Nascimento',
  app_rst_labels_brasil_up: 'BRASIL',
  app_rst_labels_cadastro_basico: 'Cadastro Básico',
  app_rst_labels_departamento_regional: 'Departamento Regional',
  app_rst_labels_parceiro_credenciado: 'Parceiro Credenciado',
  app_rst_labels_rede_credenciada: 'Rede Credenciada',
  app_rst_labels_unidade_nome_obra: 'Unidade/Nome Obra',
  app_rst_labels_cargo_responsavel: 'Cargo Responsável',
  app_rst_labels_telefone_contato: 'Telefone Contato',
  app_rst_labels_inscricao_estadual_completo: 'Inscrição Estadual',
  app_rst_labels_membros_cipa: 'Membros CIPA',
  app_rst_labels_porte: 'Porte',
  app_rst_labels_email_responsavel: 'E-mail Responsável',
  app_rst_labels_email_contato: 'E-mail Contato',
  app_rst_labels_inscricao_municipal: 'Inscrição Municipal',
  app_rst_labels_design_cipa: 'Design CIPA',
  app_rst_labels_situacao_ativo: 'Ativo',
  app_rst_labels_situacao_inativo: 'Inativo',
  app_rst_labels_url: 'URL',
  app_rst_labels_cnpj_incompleto: 'Por favor informar o CNPJ completo',
  app_rst_labels_cnpj_cpf_incompleto: 'Por favor informar o CNPJ/CPF completo',
  app_rst_labels_cpf_incompleto: 'Por favor informar o CPF completo',
  app_rst_labels_nit_incompleto: 'Por favor informar o NIT completo',
  app_rst_labels_informações_adicionais: 'Informações Adicionais',
  app_rst_labels_card_enderecoContato: 'Endereço',
  app_rst_labels_card_dependente: 'Dependentes',
  app_rst_labels_card_dados_empresa: 'Dados na Empresa',
  app_rst_labels_orgaoRg: 'Órgão Emissor',
  app_rst_labels_ctps: 'Nº CTPS',
  app_rst_labels_serieCtps: 'Série CTPS',
  app_rst_labels_ufCtps: 'UF da CTPS',
  app_rst_labels_brPdh: 'BR/PDH',
  app_rst_labels_profissao: 'Profissão',
  app_rst_labels_tipo_sanguineo: 'Tipo Sanguíneo',
  app_rst_labels_situacao_trabalhador: 'Situação do Trabalhador',
  app_rst_labels_escolaridade: 'Escolaridade',
  app_rst_labels_faixaSalarial: 'Faixa salarial',
  app_rst_labels_racaCor: 'Raça/cor',
  app_rst_labels_ramo: 'Ramo',
  app_rst_labels_estadoCivil: 'Estado civil',
  app_rst_labels_dataAdmissao: 'Data de Admissão',
  app_rst_labels_dataDemissao: 'Data de Demissão',
  app_rst_labels_dataAssociacao: 'Data de Associação',
  app_rst_labels_dataDesligamento: 'Data de Desligamento',
  app_rst_labels_dataFimDependencia: 'Data Fim da Dependência',
  app_rst_labels_data_entrada_pais: 'Data de Entrada no País',
  app_rst_labels_identificacao_regional: 'Identificação da Regional',
  app_rst_labels_sigla_dr: 'Sigla do DR',
  app_rst_labels_sigla: 'Sigla',
  app_rst_labels_data_inicio_contrato: 'Data de Inicio*',
  app_rst_labels_data_final_contrato: 'Data de Fim*',
  app_rst_labels_unidade_sesi_nome: 'Unidade SESI',
  app_rst_labels_contrato_status: 'Status',
  app_rst_labels_programa_contrato: 'Tipo de Programa',
  app_rst_labels_ano_vigencia_contrato: 'Ano Vigência',
  app_rst_labels_cnae_: 'CNAE',
  app_rst_labels_possui_plano_saude: 'Possui Plano de Saúde',
  app_rst_labels_possui_automovel: 'Possui Automóvel',
  app_rst_labels_pratica_atividade_fisica_com_frequencia: 'Prática de Atividade Física com Frequência',
  app_rst_labels_realiza_exame_regularmente: 'Realiza Exame Regularmente',
  app_rst_labels_notificacao_campanhas_sms_email_whatsapp:
  'Gostaria de receber notificações de agendamento, avisos de campanhas, notícias, propagandas,  por SMS, e-mail, whatsapp?',
  app_rst_labels_nome_mae: 'Nome da Mãe',
  app_rst_labels_nome_pai: 'Nome do Pai',
  app_rst_labels_nome_completo: 'Nome Completo',
  app_rst_labels_nome_social: 'Nome Social',
  app_rst_labels_nome_responsavel: 'Nome do Responsável',
  app_rst_labels_nome_federacao: 'Nome da Federação',
  app_rst_labels_localizacao_regional: 'Localização da Regional',
  app_rst_labels_telefone_tipo_telefone_residencial: 'RESIDENCIAL',
  app_rst_labels_telefone_tipo_telefone_comercial: 'COMERCIAL',
  app_rst_labels_telefone_tipo_telefone_fax: 'FAX',
  app_rst_labels_telefone_tipo_telefone_celular: 'CELULAR',
  app_rst_labels_proximo: 'Próximo',
  app_rst_labels_primeiro: 'Primeiro',
  app_rst_labels_ultimo: 'Último',
  app_rst_labels_anterior: 'Anterior',
  app_rst_labels_razao_social_nome: 'Razão Social/ Nome',
  app_rst_labels_cpf_cnpj: 'CNPJ/CPF',
  app_rst_labels_pessoa_fisica: 'Pessoa Física',
  app_rst_labels_pessoa_juridica: 'Pessoa Jurídica',
  app_rst_labels_segmento: 'Segmento',
  app_rst_labels_nacionalidade: 'Nacionalidade',
  app_rst_label_uf: 'UF',
  app_rst_labels_pesquisa_segmento: 'Pesquisar Segmento',
  app_rst_labels_pesquisa_produto: 'Pesquisar Produto',
  app_rst_labels_pesquisa_servico: 'Pesquisar Serviço',
  app_rst_labels_pesquisa_setor: 'Pesquisar Setor',
  app_rst_labels_pesquisa_cargo: 'Pesquisar Cargo',
  app_rst_labels_pesquisa_funcao: 'Pesquisar Função',
  app_rst_labels_pesquisa_uat: 'Pesquisar Unidade SESI',
  app_rst_labels_codigo: 'Código',
  app_rst_labels_descricao: 'Descrição',
  app_rst_labels_orientacao: 'Orientação',
  app_rst_labels_orientacoes: 'Orientações',
  app_rst_labels_pontuacao: 'Pontuação',
  app_rst_labels_unidade_obra: 'Unidade/Obra',
  app_rst_labels_codigo_cei: 'Código CEI',
  app_rst_labels_login: 'Login',
  app_rst_labels_versao: 'Versão',
  app_rst_labels_grau_de_risco: 'Grau de Risco',
  app_rst_labels_principal: 'Principal',
  app_rst_labels_senha: 'Senha',
  app_rst_labels_nova_senha: 'Nova Senha',
  app_rst_labels_confirmar_senha: 'Confirmar Senha',
  app_rst_labels_todos: 'Todos',
  app_rst_labels_login_cpf: 'Login/CPF',
  app_rst_labels_titulo: 'Título',
  app_rst_labels_perfil: 'Perfil',
  app_rst_labels_validade: 'Ciclo Preenchimento',
  app_rst_labels_resposta: 'Resposta',
  app_rst_labels_grupo: 'Grupo',
  app_rst_labels_indicador: 'Indicador',
  app_rst_labels_ordem: 'Ordem',
  app_rst_labels_anual: 'Anual',
  app_rst_labels_mensal: 'Mensal',
  app_rst_labels_ordem_grupo: 'Ordem Grupo',
  app_rst_labels_ordem_pergunta: 'Ordem Pergunta',
  app_rst_labels_ordem_resposta: 'Ordem Resposta',
  app_rst_labels_respostas: 'Respostas',
  app_rst_labels_perguntas_adicionadas: 'Perguntas Adicionadas',
  app_rst_labels_respostas_adicionadas: 'Respostas Adicionadas',
  app_rst_labels_tipo_questionario: 'Tipo',
  app_rst_labels_perfis: 'Perfis',
  app_rst_labels_status: 'Status',
  app_rst_labels_periodicidade: 'Periodicidade',
  app_rst_labels_pontuacao_maxima: 'Pontuação Máxima',
  app_rst_labels_pontuacao_minima: 'Pontuação Mínima',
  app_rst_labels_recomendacao: 'Recomendação',
  app_rst_labels_mensagem: 'Mensagem',
  app_rst_labels_vacinas_alergias_medicamentos_auto_declarados: 'Alergias e Medicamentos Autodeclarados',
  app_rst_labels_vacinas_auto_declarados: 'Vacinas autodeclaradas',
  app_rst_labels_alergias_auto_declarados: 'Alergias autodeclaradas',
  app_rst_labels_medicamentos_auto_declarados: 'Medicamentos autodeclarados',
  app_rst_labels_vacinas_auto_declaradas_minha_saude: 'Vacinas autodeclaradas',
  app_rst_labels_ultima_fco: 'Fonte: Última FCO',
  app_rst_labels_fonte_trabalhador: 'Fonte: Trabalhador',
  app_rst_labels_nenhuma_vacina_informada: 'Nenhuma vacina informada.',
  app_rst_labels_alergias_auto_declaras_minha_saude: 'Alergias autodeclaradas',
  app_rst_labels_nenhuma_alergia_informada: 'Nenhuma alergia informada.',
  app_rst_labels_medicamento: 'Medicamentos',
  app_rst_labels_medicamentos_auto_declarados_minha_saude: 'Medicamentos autodeclarados',
  app_rst_labels_nenhum_medicamento_informado: 'Nenhuma medicamento informado.',
  app_rst_telefone_responsavel: 'Telefone Responsável',
  app_rst_label_sistema: 'Sistema',
  app_rst_label_entidade: 'Entidade',
  app_rst_label_data_criacao: 'Data de criação',
  app_rst_label_client_id: 'Client id',
  app_rst_labels_area: 'Área',
  app_rst_labels_area_m2: 'Área m²',
  app_rst_labels_tipo_servico: 'Tipo Serviço',

  // MSG COMPARTILHADAS
  app_rst_msg_nenhum_registro_adicionado: 'Nenhum registro adicionado.',
  app_rst_campo_obrigatorio: 'O campo {0} é obrigatório',
  app_rst_nenhum_registro_encontrado: 'Nenhum registro encontrado',
  app_rst_msg_sem_resultado: 'Sem Resultado',
  app_rst_endereco_campo_obrigatorio: 'Endereço não adicionado: O campo {0} é obrigatório',
  app_rst_endereco_campo_invalido: 'Endereço não adicionado: O campo {0} está inválido',
  app_rst_empresa_campos_obrigatorios: 'Os campos em * são obrigatórios',
  app_rst_msg_3_caracteres: 'Por favor digitar pelo menos 3 caracteres na pesquisa.',
  app_rst_msg_pesquisar_todos_vazios: 'Por favor digitar pelo menos um dos campos.',
  app_rst_msg_pesquisar_perfil_vazio: 'Por favor selecione o perfil do usuário',
  app_rst_cadastro_realizado_sucesso: 'Cadastro realizado com sucesso',
  app_rst_cnpj_obrigatorio: 'CNPJ obrigatório',
  app_rst_cnpj_invalido: 'CNPJ inválido.',
  app_rst_cpf_invalido: 'CPF inválido.',
  app_rst_campo_invalido: 'O campo {0} está inválido',
  app_rst_campo_inteiro: 'O campo {0} deve ser um número inteiro',
  app_rst_pesquisa_realizada_com_sucesso: 'Pesquisa realizada com sucesso',
  app_rst_selecione_um_item: 'É necessário selecionar um item',
  app_rst_erro_nenhuma_resposta_adicionada: 'É necessário adicionar pelo menos uma resposta',
  app_rst_quantidade_caracteres_minimos_invalido: 'O campo {0} deve conter pelo menos {1} caracteres.',
  app_rst_quantidade_caracteres_maximos_invalido: 'O campo {0} deve conter no máximo {1} caracteres.',
  app_rst_pesquisar_todos_vazios: 'Preencha pelo menos um dos campos.',
  app_rst_erro_geral: 'Ocorreu um erro ao processar a solicitação. Tente novamente mais tarde.',
  app_rst_mensagem_inconsistencia: 'O(s) campo(s): {0}  {1}  {2}  {3}  é/são importante(s) para o cadastro. Favor preenchê-lo(s).',
  app_rst_tamanho_arquivo: 'O arquivo deverá possuir o tamanho máximo de {0} MB',
  app_rst_label_tamanho_arquivo: 'Tamanho máximo do arquivo {0}MB',
  app_rst_mensagem_pontuacao_maxima_maior_que_pontuacao_minima: 'O campo Pontuação Máxima deve ser maior que o campo Pontuação Mínima',
  app_rst_msg_nenhuma_quantidade_informada: 'Nenhumma quantidade informada',

  // BOTOES COMPARTILHADOS
  app_rst_btn_voltar: 'Voltar',
  app_rst_btn_novo: 'Novo',
  app_rst_btn_alterar: 'Alterar',
  app_rst_btn_consultar: 'Consultar',
  app_rst_btn_excluir: 'Excluir',
  app_rst_btn_desativar: 'Desativar',
  app_rst_btn_pesquisar: 'Pesquisar',
  app_rst_btn_novaVacina: 'Nova Vacina',
  app_rst_btn_verHistorico: 'Ver Historico',
  app_rst_btn_salvar: 'Salvar',
  app_rst_btn_publicar: 'Publicar',
  app_rst_btn_selecionar: 'Selecionar',
  app_rst_btn_continuar: 'Continuar',
  app_rst_btn_concordar: 'Concordar',
  app_rst_btn_confirmar: 'Confirmar',
  app_rst_btn_cancelar: 'Cancelar',
  app_rst_btn_adicionar: 'Adicionar',
  app_rst_bnt_cadastrar: 'Cadastrar',
  app_rst_btn_fechar: 'Fechar',
  app_rst_btn_ok: 'Ok',
  app_rst_btn_enviar: 'Enviar',

  // VALORES COMPARTILHADOS
  app_rst_valor_sim: 'SIM',
  app_rst_valor_nao: 'NAO',
  app_rst_valor_principal: 'PRINCIPAL',
  app_rst_valor_pessoal: 'PESSOAL',
  app_rst_valor_consultar: 'consultar',
  app_rst_valor_cadastrar: 'cadastrar',
  app_rst_valor_alterar: 'alterar',
  app_rst_valor_masculino: 'Masculino',
  app_rst_valor_feminino: 'Feminino',

  // Empresa
  app_rst_empresa_title_cadastrar: 'Cadastro da Empresa',
  app_rst_empresa_title_alterar: 'Cadastro da Empresa',
  app_rst_empresa_title_pesquisar: 'Pesquisar Empresa',
  app_rst_empresa_title_consultar: 'Cadastro da Empresa',
  app_rst_empresa_subtitulo: 'Empresa',
  app_rst_empresa_cnae: 'Deve selecionar um CNAE como principal',

  // Unidade SESI
  app_rst_uat_title_pesquisar: 'Pesquisar Unidade SESI',
  app_rst_uat_title_consultar: 'Consultar Unidade SESI',
  app_rst_uat_title_cadastrar: 'Cadastro da Unidade SESI',
  app_rst_uat_title_alterar: 'Cadastro da Unidade SESI',
  app_rst_menu_uat: 'Unidade SESI',

  // Pesquisa SESI
  app_rst_menu_pesquisa_sesi: 'Pesquisa SESI',
  app_rst_pesquisa_sesi_title: 'Encontre uma Unidade SESI',
  app_rst_pesquisa_sesi_busca_unidade_sesi: 'Busca por Unidade SESI',
  app_rst_pesquisa_sesi_busca_local: 'Busca por local',
  app_rst_pesquisa_sesi_busca_produto: 'Busca por produto',

  //Gestão de Unidade SESI Instalações Físicas
  app_rst_cadastro_instalacoes_fisicas_title: 'Cadastro de Instalações Físicas',
  app_rst_categorias_cadastradas_title: 'Categorias cadastradas',
  app_rst_tipos_cadastrados_title: 'Tipos cadastrados',
  app_rst_areas_cadastradas: 'Áreas cadastradas',

//Gestão de Unidade SESI Quadro Pessoal
  app_rst_cadastro_quadro_pessoal_title: 'Cadastro de Quadro de Pessoal',

  // Trabalhador
  app_rst_trabalhador_title_pesquisar: 'Pesquisar Trabalhador',
  app_rst_trabalhador_title_historico: 'Meus Questionários',
  app_rst_trabalhador_title_cadastrar: 'Cadastro do Trabalhador',
  app_rst_trabalhador_title_alterar: 'Cadastro do Trabalhador',
  app_rst_trabalhador_title_consultar: 'Consultar Trabalhador',
  app_rst_trabalhador_title_igev: 'IGEV - Diagnóstico inicial',
  app_rst_trabalhador_title_quest_resultado: 'Resultado Questionário',
  app_rst_trabalhador_title_responder_quest: 'Responder Questionário',
  app_rst_trabalhador_dataFalecimento_maior_que_dataNascimento: 'A Data de Falecimento deve ser maior que a Data de Nascimento',
  app_rst_trabalhador_label_uplad_imagem: 'Modificar a imagem do seu perfil',
    app_rst_trabalhador_title_meus_dados: 'Meus Dados',

  //Imunizacao
  app_rst_imunizacao_title_cadastrar_vacina: 'Cadastrar Vacina Autodeclarada',
    app_rst_imunizacao_title_tela_inicial_vacina: 'Vacinas Autodeclaradas',
    app_rst_labels_nome_vacina: 'Nome',
  app_rst_labels_periodo: 'Período',
  app_rst_labels_lote: 'Lote',
  app_rst_labels_local: 'Local',
  app_rst_label_data_vacinacao: 'Data da Vacinação *',
  app_rst_labels_adicionar_vacina: 'Próximas Doses',
  app_rst_labels_data_proxima_dose: 'Data',
  app_rst_data_proxima_campo_obrigatorio : 'Dose da próxima vacina não adicionado: O campo {0} é obrigatório',
  app_rst_data_proxima_campo: 'A data da próxima dose não pode ser anterior a data atual',


  // Profissionais de Saude
  app_rst_profissionais_saude_menu: 'Profissional',
  app_rst_profissionais_title_cadastrar: 'Cadastro do Profissional',
  app_rst_profissionais_title_pesquisar: 'Pesquisar Profissional',

  // Parceiro Credeciado
  app_rst_parceiro_title_pesquisar: 'Pesquisar Parceiro Credenciado',
  app_rst_parceiro_title_cadastro: 'Cadastro do Parceiro Credenciado',

  // Questionário
  app_rst_menu_questionario: 'Questionário',

  // Sindicatos
  app_rst_sindicatos_title_pesquisar: 'Pesquisar Sindicato',
  app_rst_sindicatos_title_menu: 'Sindicato',
  app_rst_sindicato_title_consultar: 'Consultar Sindicato',
  app_rst_sindicatot_title_cadastrar: 'Cadastro do Sindicato',
  app_rst_sindicato_title_alterar: 'Cadastrar do Sindicato',

  // Departamento Regional
  app_rst_depart_regional_pesquisar: 'Pesquisar Departamento Regional',
  app_rst_depart_regional_cadastro: 'Cadastro do Departamento Regional',
  app_rst_depart_regional_consultar: 'Consultar Departamento Regional',
  app_rst_depart_regional_alterar: 'Cadastro do Departamento Regional',

  // Rede Credenciada
  app_rst_rede_credenciada_pesquisar: 'Pesquisar Rede Credenciada',
  app_rst_rede_credenciada_cadastro: 'Cadastro da Rede Credenciada',

  // Telefone
  app_rst_msg_telefone_existente: 'Apenas um número para contato é permitido',
  app_rst_msg_telefone_campo_obrigatorio: 'O Telefone não foi salvo: O campo {0} é obrigatório',
  app_rst_msg_telefone_campo_invalido: 'O Telefone não foi salvo: O campo {0} está inválido',

  // Unidade Obra
  app_rst_msg_unidade_descricao_campo_obrigatorio: 'A Unidade/Obra não foi salva: O campo {0} é obrigatório',
  app_rst_msg_unidade_existente: 'Unidade/Obra já existente nesta empresa.',
  app_rst_msg_unidade_mesmo_codigo_cei: 'Já existe uma Unidade/Obra com o mesmo código CEI',

  // Email
  app_rst_email_campo_obrigatorio: 'E-mail não adicionado: O campo {0} é obrigatório',
  app_rst_email_campo_invalido: 'E-mail não adicionado: O campo {0} está inválido',

  // Produto
  app_rst_produtos_title_menu: 'Produtos e Serviços',
  app_rst_Departamento_subtitulo_pesquisar_produto: 'Pesquisar Produto',
  app_rst_labels_produto: 'Produto',
  app_rst_departamento_subtitulo_produtos_e_servicos_associados: 'Produtos e Serviços Associados',

  // Estrutura da Unidade
  app_rst_estrutura_da_unidade_title_menu: 'Estrutura da Unidade',

  // Auditoria
  app_rst_tittle_auditoria: 'Pesquisar Auditoria',
  app_rst_labels_auditoria: 'Auditoria',
  app_rst_labels_funcionalidade: 'Funcionalidade',
  app_rst_labels_tipo_operacao: 'Tipo de Operação',
  app_rst_labels_usuario: 'Usuário',
  app_rst_labels_data_ocorrencia: 'Data da Ocorrência',
  app_rst_btn_data_hora: 'Data/Hora',
  app_rst_btn_login: 'Login',
  app_rst_btn_funcionalidade: 'Funcionalidade',
  app_rst_btn_tipo_operacao: 'Tipo de Operação',
  app_rst_btn_descricao: 'Descrição',
  app_rst_labels_dispositivo_movel: 'Dispositivo Móvel',
  app_rst_msg_validacao_data: 'Informe um intervalo de datas válido',
  app_rst_auditoria_data_inicio_maior_que_fim: 'A Data de Ocorrência inicial não pode ser maior que a Data de Ocorrência final',

  // Dependente
  app_rst_dependente_campo_obrigatorio: 'Dependente não adicionado: O campo {0} é obrigatório',
  app_rst_dependente_campo_invalido: 'Dependente não adicionado: O campo {0} está inválido',
  app_rst_dependente_tipo: 'Tipo',
  app_rst_dependente_inativar_dependente: 'Inativar Dependente',
  app_rst_dependente_ja_cadastrado: 'CPF já cadastrado',
  app_rst_dependente_nao_associado: 'O trabalhador não possui dependentes associados',
  app_rst_dependente_associados: 'Dependentes Associados',

  // Parceiro Credenciado
  app_rst_parceiro_credenciado_title_cadastro: 'Cadastro do Parceiro Credenciado',
  app_rst_parceiro_credenciado_subtitulo: 'Rede Credenciada Parceira',

  // Usuario
  app_rst_usuario_title_pesquisar: 'Pesquisar Usuário',
  app_rst_usuario_title_cadastrar: 'Cadastro do Usuário',
  app_rst_usuario_title_minha_conta: 'Minha Conta',
  app_rst_usuario_title_associar_perfil: 'Associar Perfil',

  app_rst_usuario_validacao_selecione_sistema_perfil: 'Selecione um sistema e seus perfis',
  app_rst_usuario_desativar_sucesso: 'Usuário desativado com sucesso',

  app_rst_usuario_label_sistemas: 'Sistemas',
  app_rst_usuario_label_perfis: 'Perfis',
    app_rst_usuario_label_cnpj: 'CNPJ',

    //Empresa Contrato
    app_rst_empresa_contrato_title: 'Contratos',
    app_rst_empresa_contrato_cadastrar_title: 'Cadastrar Contrato',


  // Empresa Função
  app_rst_empresa_funcao_title: 'Função',
  app_rst_empresa_funcao_adicionadas: 'Funções Adicionadas',

  // Empresa Setor
  app_rst_empresa_setor_title: 'Setor',
  app_rst_empresa_setores_adicionados: 'Setores Adicionados',



  // Empresa Sindicato
  app_rst_sindicato_ja_cadastrado: 'O Sindicato selecionado já foi adicionado',
  app_rst_empresa_subtitulo_pesquisar_sindicato: 'Pesquisar',
  app_rst_empresa_subtitulo_sindicatos_associados: 'Sindicatos Associados',
  app_rst_empresa_subtitulo_associar_sindicatos: 'Associar Sindicato',
  app_rst_empresa_subtitulo_editar_sindicatos_associados: 'Editar Sindicato Associado',
  app_rst_empresa_dataDesligamento_maior_que_dataAssociacao: 'A Data de Desligamento deve ser maior que a Data de Associação',

  // Produtos e Serviço
  app_rst_labels_linha: 'Linha',
  app_rst_labels_produto_servico: 'Produtos e Serviços',
  app_rst_empresa_subtitulo_pesquisar_produtos_servicos: 'pesquisa',

  // Empresa Lotação
  app_rst_empresa_lotacao_title: 'Lotação',
  app_rst_empresa_lotacoes_adicionadas: 'Lotações Adicionadas',

  // Trabalhador Lotação
  app_rst_empresa_trabalhador_lotacao_title: 'Trabalhador Lotação',
  app_rst_empresa_subtitulo_associar_trabalhador_lotacao: 'Associar Trabalhador Lotação',
  app_rst_empresa_subtitulo_editar_trabalhador_lotacao_associados: 'Editar Trabalhador Lotação Associado',

  // Empresa Trabalhador
  app_rst_empresa_trabalhador_title: 'Trabalhador',
  app_rst_empresa_subtitulo_associar_trablhador: 'Associar Trabalhador',
  app_rst_empresa_trabalhador_associados_title: 'Trabalhadores Associados',
  app_rst_empresa_subtitulo_editar_trabalhador_associados: 'Editar Trabalhador Associado',
  app_rst_btn_lotacao: 'Lotação',

  // Empresa Cbo
  app_rst_empresa_cbo_adicionadoss: 'Cargos Adicionados',

  // Produto-Servico
  app_rst_produto_servico_title_pesquisar: 'Pesquisar Produtos e Serviços',
  app_rst_produto_servico_title_cadastro: 'Cadastro de Produtos e Serviços',

  // Associar Trabalhador Empresa
  app_rst_associar_trabalhador_empresa_title: 'Associação Trabalhador Empresa',
  app_rst_associar_trabalhador_empresa_title_cadastro: 'Associar Trabalhador Empresa',
  app_rst_labels_associar_trabalhador_empresa: 'Associar Trabalhador Empresa',
  app_rst_associar_trabalhador_empresa_associados: 'Trabalhadores Associados',
  app_rst_empresa_subtitulo_lotacao_associados: 'Lotações Associadas',

  // Primeiro Acesso
  app_rst_primeiro_acesso_title_primeiro_acesso_trabalhador: 'Primeiro Acesso do Trabalhador',
  app_rst_primeiro_acesso_campos_senha_confirmar_senha_diferentes: 'Os campos Senha e Confirmar Senha estão diferentes',
  app_rst_primeiro_acesso_codigo_sistema: 'CADASTRO',
  app_rst_primeiro_acesso_codigo_perfil: 'TRA',
  app_rst_primeiro_acesso_confirme_email: 'Confirme seu E-mail',
  app_rst_primeiro_acesso_digite_aqui_email: 'Digite aqui seu E-mail',
  app_rst_primeiro_acesso_soliticat_email_sesi: 'Você também pode solicitar seu email do Rede SESI VIVA +',

  // Termo de Uso
  app_rst_primeiro_acesso_title_termo_uso: 'Termo de Uso',

  // Usuario Empresa
  app_rst_usuario_empresa_empresas_associadas_title: 'Empresas Associadas',
  app_rst_usuario_empresa_subtitulo_associar_empresa: 'Associar Empresa',
  app_rst_usuario_empresa_empresas_adicionados: 'Empresas do Gestor Empresa',
  app_rst_usuario_empresa_empresas_adicionados_master: 'Empresas do Gestor Empresa Master',
  app_rst_usuario_empresa_empresas_trabalhador: 'Empresas do Trabalhador',
  app_rst_usuario_empresa_empresas_adicionados_profissional: 'Empresas do Profissional de Saúde',
  app_rst_usuario_empresa_empresas_recursos_humanos: 'Empresas do Recursos Humanos',
  app_rst_usuario_empresa_empresas_seguranca_do_trabalho: 'Empresas do Segurança de Trabalho',

  // Usuario Sindicato
  app_rst_usuario_sindicato_subtitulo_associar_sindicato: 'Associar Sindicato',
  app_rst_usuario_sindicato_sindicatos_adicionados: 'Sindicatos Adicionados',

  // Usuario Departamento Regional
  app_rst_usuario_departamento_regional_associados_title: 'Departamentos Regionais Associados',
  app_rst_usuario_departamento_regional_subtitulo_associar_departamento: 'Associar Departamento Regional',
  app_rst_usuario_departamento_regional_adicionados: 'Departamentos Regionais Adicionados',

  // Usuario Unidade SESI
  app_rst_usuario_unidade_sesi_associados_title: 'Unidades SESI Associadas',
  app_rst_usuario_unidade_sesi_subtitulo_associar_unidade: 'Associar Unidade SESI',

  // Recuperar Senha
  app_rst_recupera_senha_titulo: 'Recuperar Senha',
  app_rst_recupera_senha_sucesso: 'Um link para cadastrar uma nova senha foi enviado para o seu email',
  app_rst_recupera_senha_usuario_invalido: 'Não foi possível recuperar senha',
  app_rst_alterar_senha_titulo: 'Cadastrar Nova Senha',
  app_rst_alterar_senha_pattern_invalido: 'A senha está inválida',
  app_rst_alterar_senha_confirmar_invalido: 'A senha e sua confirmação não combinam',
  app_rst_alterar_senha_sucesso: 'Senha alterada com sucesso',
  app_rst_minhaconta: 'Dados da conta alterados com sucesso',

  // Solicitar email
  app_rst_solicitar_email_sesi: 'Solicitar email do SESI VIVA +',
  app_rst_msg_telefone_campo_invalido_solicitar_email: 'Telefone inválido',
  app_rst_email_campo_invalido_solicitar_email: 'E-mail inválido',
  app_rst_email_campo_obrigatorio_solicitar_email: 'O campo E-mail é obrigatório',

  // Páginas de Erros
  app_rst_acesso_negado_titulo: 'Acesso Negado',

  // Certificado
  app_rst_labels_cadastro_certificado: 'Cadastro de Certificado',
  app_rst_labels_certificados_adicionados: 'Certificados adicionados',
  app_rst_labels_data_conclusao: 'Data de Conclusão',
  app_rst_labels_adicionar_certificados: 'Adicionar Certificados',
  app_rst_labels_certificados__adicionados_mim: 'Certificados adicionados por mim',
  app_rst_labels_certificados__adicionados_empresa: 'Certificados adicionados pela empresa',
  app_rst_labels_certificados__adicionados_trabalhador: 'Certificados adicionados pelo trabalhador',
  app_rst_labels_certificados_adicionais: 'Certificados adicionais',


  // Pergunta
  app_rst_labels_pergunta: 'Pergunta',
  app_rst_pergunta_title_pesquisar: 'Pesquisar Pergunta',
  app_rst_pergunta_title_cadastro: 'Cadastro de Pergunta',
  app_rst_nenhuma_pergunta_adicionada: 'Este questionário ainda não possui perguntas adicionadas.',

  // Grupo de Perguntas
  app_rst_labels_grupo_pergunta: 'Grupo',
  app_rst_grupo_pergunta_title_pesquisar: 'Pesquisar Grupo de Perguntas',
  app_rst_grupo_pergunta_title_cadastro: 'Cadastro de Grupo de Perguntas',

  // Indicadore Questionário
  app_rst_indicador_questionario_title_pesquisar: 'Pesquisar Indicador de Questionário',
  app_rst_indicador_questionario_title_cadastro: 'Cadastro de Indicador de Questionário',
  app_rst_labels_incentivo: 'Incentivo',

  // Classificação Pontuação
  app_rst_classificacao_pontuacao_title_pesquisar: 'Pesquisar Classificação de Pontuação',
  app_rst_classificacao_pontuacao_title_cadastro: 'Cadastro de Classificação de Pontuação',

  // Indicadore Questionário
  app_rst_resposta_title_pesquisar: 'Pesquisar Resposta',
  app_rst_resposta_title_cadastro: 'Cadastro de Resposta',

  // Resposta Questionário
  app_rst_resposta_questionario_campo_obrigatorio: 'O campo {0} da Resposta é obrigatório(a)',
  app_rst_resposta_questionario_pontuacao_invalida: 'O campo Pontuação da Resposta deve ser 0 ou 1',
  app_rst_resposta_questionario_ordem_ja_existe: 'Já existe uma Resposta com a ordem {0}',

  // Resultado Questionário
  app_rst_resultado_questionario_baixo_risco: 'Baixo Risco',
  app_rst_resultado_questionario_medio_risco: 'Medio Risco',
  app_rst_resultado_questionario_medio_alto_risco: 'Médio Alto',
  app_rst_resultado_questionario_alto_risco: 'Alto',

  // Questionário
  app_rst_labels_questionario: 'Questionários',
  app_rst_questionario_title_pesquisar: 'Pesquisar Questionário',
  app_rst_questionario_title_cadastro: 'Cadastro de Questionário',
  app_rst_questionario_title_aplicar: 'Questionário',
  app_rst_questionario_title_pergunta: 'Cadastro de Pergunta',
  app_rst_resposta_existente: 'Essa resposta já foi incluída',
  app_rst_questionario_pergunta_obrigatoria: 'Todas as perguntas são obrigatórias. Responda a(s) pergunta(s) {0}',
  app_rst_questionario_indicador_orientacao: 'Orientação',
  app_rst_questionario_preencha_seu_igev: 'Preencha seu IGEV',
  app_rst_questionario_consulte_seu_historico: 'Consulte seu Histórico',
  app_rst_questionario_igev_periodo: 'Você já preencheu o seu IGEV do período. Aguarde o próximo ciclo.',

  //Sistema Credenciado
  app_rst_sistema_credenciado_cadastrar: 'Cadastro de Sistema Credenciado',
  app_rst_sistema_credenciado_pesquisar: 'Pesquisar Sistema Credenciado',
  app_rst_sistema_credenciado: 'Sistema Credenciado',
  app_rst_sistema_credenciado_alterar: 'Alterar Sistema Credenciado',
  app_rst_sistema_credenciado_reset_client_secret: 'Reset client secret',
  //Relatório de Perfis por Usuários
  app_rst_relatorio_perfil_usuario: 'Relatórios de Perfis por Usuários',

  senha_atual: 'Digite sua Senha Atual',
  nova_senha: 'Digite a sua Nova Senha',
  confirme_sua_nova_senha: 'Confirme a sua Nova Senha',
  minha_conta: 'Minha Conta',
};

@Pipe({
  name: 'recurso',
})
export class RecursoPipe implements PipeTransform {

  transform(mensagem: any, ...parametros: any[]): any {

    if (mensagem) {

      mensagem = MensagemProperties[mensagem];

      parametros.forEach((value, index) => {
        mensagem = mensagem.replace(new RegExp('\\{' + index + '}', 'gm'), value);
      });

    }

    return mensagem;

  }

}
