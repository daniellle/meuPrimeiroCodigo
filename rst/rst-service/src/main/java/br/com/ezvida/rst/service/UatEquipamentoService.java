package br.com.ezvida.rst.service;

import java.util.List;
import java.util.Map;
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
import br.com.ezvida.rst.model.dto.UatEquipamentoDTO;
import br.com.ezvida.rst.model.dto.UatEquipamentoGroupedByAreaDTO;
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
		validarSeUsuarioTemPermissao(dados, idUat);
		return createListUatEquipamentoGroupedByAreaDTO(idUat);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void desativar(Long idEquipamento, Long idUat, ClienteAuditoria auditoria, DadosFilter dados) {
		LogAuditoria.registrar(LOGGER, auditoria, "Desativando UAT Equipamento de ID " + idEquipamento);
		validarSeUsuarioTemPermissao(dados, idUat);
		uatEquipamentoDAO.desativar(idEquipamento);
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
}
