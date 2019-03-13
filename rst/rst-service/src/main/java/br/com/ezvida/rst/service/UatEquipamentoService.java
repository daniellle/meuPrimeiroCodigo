package br.com.ezvida.rst.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UatEquipamentoDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.model.UatEquipamento;
import br.com.ezvida.rst.model.UatEquipamentoArea;
import br.com.ezvida.rst.model.UatEquipamentoTipo;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.model.dto.UatEquipamentoDTO;
import br.com.ezvida.rst.model.dto.UatEquipamentoGroupedByAreaDTO;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;
import fw.security.exception.UnauthorizedException;

@Stateless
public class UatEquipamentoService extends BaseService {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UatEquipamentoService.class);

	@Inject
	private UatEquipamentoDAO uatEquipamentoDAO;

	@Inject
	private ValidationService validationService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UatEquipamentoGroupedByAreaDTO> listarTodosEquipamentosPorIdUatAgrupadosPorArea(Long idUat,
			ClienteAuditoria auditoria, DadosFilter dados) {
		LogAuditoria.registrar(LOGGER, auditoria, "Buscando Equipamentos da UAT com id " + idUat);
		if(validarSeUsuarioTemPermissaoConsulta(dados, idUat)){
			return createListUatEquipamentoGroupedByAreaDTO(idUat);
		}else {
			throw new UnauthorizedException(getMensagem("app_seguranca_acesso_negado"));
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void desativar(Long idEquipamento, Long idUat, ClienteAuditoria auditoria, DadosFilter dados) {
		LogAuditoria.registrar(LOGGER, auditoria, "Desativando UAT Equipamento de ID " + idEquipamento);
		validarSeIdEquipamentoEIdUatForamInformados(idEquipamento, idUat);
		validarSeUsuarioTemPermissao(dados, idUat);
		uatEquipamentoDAO.desativar(idEquipamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<UatEquipamentoDTO> salvar(List<UatEquipamentoDTO> listUatEquipamentoDTO, ClienteAuditoria auditoria,
			DadosFilter dados) {
		Long idUat = listUatEquipamentoDTO.get(0).getIdUat();
		LogAuditoria.registrar(LOGGER, auditoria, "Salvando Equipamentos para uat de ID " + idUat);
		validarSeUsuarioTemPermissao(dados, idUat);
		validarSeOEquipamentoJaEstaCadastrado(listUatEquipamentoDTO, idUat);
		
		return listUatEquipamentoDTO.stream().map(item -> {
			UatEquipamento uatEquipamento = parseToEntity(item);
			uatEquipamentoDAO.salvar(uatEquipamento);
			item.setId(uatEquipamento.getId());
			return item;
		}).collect(Collectors.toList());
	}
	
	private UatEquipamento parseToEntity(UatEquipamentoDTO dto) {
		UatEquipamento uatEquipamento = new UatEquipamento();
		uatEquipamento.setQuantidade(dto.getQuantidade());
		uatEquipamento.setUatEquipamentoTipo(new UatEquipamentoTipo(dto.getIdTipo()));
		uatEquipamento.setUnidadeAtendimentoTrabalhador(new UnidadeAtendimentoTrabalhador(dto.getIdUat()));
		return uatEquipamento;
	}
	
	private void validarSeOEquipamentoJaEstaCadastrado(List<UatEquipamentoDTO> listUatEquipamentoDTO, Long idUat) {
		Set<Long> idsTiposCadastrados = uatEquipamentoDAO.listAllUatEquipamentoByIdUatAndAtivo(idUat).stream()
				.map(item -> item.getUatEquipamentoTipo().getId()).collect(Collectors.toSet());
		
		listUatEquipamentoDTO.stream().forEach(item -> {
			if(idsTiposCadastrados.contains(item.getIdTipo())) {
				throw new BusinessErrorException(getMensagem("app_rst_uat_equipamento_duplicado", item.getDescricao()));
			}
		});
	}

	private void validarSeIdEquipamentoEIdUatForamInformados(Long idEquipamento, Long idUat) {
		if (idEquipamento == null) {
			throw new BusinessErrorException("Parâmetro idEquipamento é obrigatório.");
		}
		
		if (idUat == null) {
			throw new BusinessErrorException("Parâmetro idUat é obrigatório.");
		}
	}

	private List<UatEquipamentoGroupedByAreaDTO> createListUatEquipamentoGroupedByAreaDTO(Long idUat) {
		List<UatEquipamento> equipamentos = uatEquipamentoDAO.listAllUatEquipamentoByIdUatAndAtivo(idUat);
		Map<UatEquipamentoArea, List<UatEquipamento>> equipamentosPorArea = equipamentos.stream()
				.collect(Collectors.groupingBy(item -> item.getUatEquipamentoTipo().getUatEquipamentoArea()));
		return equipamentosPorArea.entrySet().stream().map(
				item -> new UatEquipamentoGroupedByAreaDTO(item.getKey(), parseToListEquipamentoDTO(item.getValue())))
				.collect(Collectors.toList());
	}

	private List<UatEquipamentoDTO> parseToListEquipamentoDTO(List<UatEquipamento> list) {
		return list.stream().map(temp -> new UatEquipamentoDTO(temp)).collect(Collectors.toList());
	}

	private void validarSeUsuarioTemPermissao(DadosFilter dados, Long idUat) {
		if (!validationService.validarFiltroDadosGestaoUnidadeSesi(dados, idUat)) {
			throw new UnauthorizedException(getMensagem("app_seguranca_acesso_negado"));
		}
	}

	private boolean validarSeUsuarioTemPermissaoConsulta(DadosFilter dados, Long idUat) {
		return validationService.validarFiltroDadosGestaoUnidadeSesi(dados, idUat) || dados.isCallCenter();
	}

}
