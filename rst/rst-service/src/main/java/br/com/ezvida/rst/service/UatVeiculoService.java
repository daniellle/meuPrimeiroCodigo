package br.com.ezvida.rst.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ezvida.rst.auditoria.logger.LogAuditoria;
import br.com.ezvida.rst.auditoria.model.ClienteAuditoria;
import br.com.ezvida.rst.dao.UatVeiculoDAO;
import br.com.ezvida.rst.dao.filter.DadosFilter;
import br.com.ezvida.rst.enums.SimNao;
import br.com.ezvida.rst.model.UatVeiculo;
import br.com.ezvida.rst.model.UatVeiculoTipo;
import br.com.ezvida.rst.model.UatVeiculoTipoAtendimento;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.model.dto.UatVeiculoDTO;
import br.com.ezvida.rst.model.dto.UatVeiculoGroupedByTipoDTO;
import fw.core.exception.BusinessErrorException;
import fw.core.service.BaseService;
import fw.security.exception.UnauthorizedException;

@Stateless
public class UatVeiculoService extends BaseService {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UatVeiculoService.class);

	@Inject
	private UatVeiculoDAO uatVeiculoDAO;
	
	@Inject
	private UatVeiculoTipoService uatVeiculoTipoService;
	
	@Inject
    private ValidationService validationService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UatVeiculoGroupedByTipoDTO> listAllUatVeiculoGroupedByTipo(Long idUat, ClienteAuditoria auditoria, DadosFilter dados) {
		LogAuditoria.registrar(LOGGER, auditoria, "Buscando Veículos por id da uat " + idUat);
		validarSeUsuarioTemPermissao(dados, idUat);
		return createListUatVeiculoGroupedByTipoDTO(uatVeiculoDAO.listAllUatVeiculosByIdUatAndAtivo(idUat), auditoria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<UatVeiculoDTO> salvar(List<UatVeiculoDTO> listVeiculoDTO, ClienteAuditoria auditoria, DadosFilter dados) {
		Long idUat = listVeiculoDTO.get(0).getIdUat();
		LogAuditoria.registrar(LOGGER, auditoria, "Salvando Veículos para uat " + idUat);
		validarSeUsuarioTemPermissao(dados, idUat);
		validarSeOVeiculoJaEstaCadastrado(listVeiculoDTO, idUat);
		
		return listVeiculoDTO.stream().map(item -> {
			UatVeiculo uatVeiculo = parseToEntity(item);
			uatVeiculoDAO.salvar(uatVeiculo);
			item.setId(uatVeiculo.getId());
			return item;
		}).collect(Collectors.toList());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void desativar(Long idVeiculo, Long idUat, ClienteAuditoria auditoria, DadosFilter dados) {
		LogAuditoria.registrar(LOGGER, auditoria, "Desativando UAT Veículo de ID " + idVeiculo);
		validarSeIdVeiculoEIdUatForamInformados(idVeiculo, idUat);
		validarSeUsuarioTemPermissao(dados, idUat);
		uatVeiculoDAO.desativar(idVeiculo);
	}

	private void validarSeIdVeiculoEIdUatForamInformados(Long idVeiculo, Long idUat) {
		if (idVeiculo == null) {
			throw new BusinessErrorException("Parâmetro idVeiculo é obrigatório.");
		}
		
		if (idUat == null) {
			throw new BusinessErrorException("Parâmetro idUat é obrigatório.");
		}
		
	}

	private void validarSeUsuarioTemPermissao(DadosFilter dados, Long idUat) {
		if(!validationService.validarFiltroDadosGestaoUnidadeSesi(dados, idUat)) {
			 throw new UnauthorizedException(getMensagem("app_seguranca_acesso_negado"));
		}
	}

	private void validarSeOVeiculoJaEstaCadastrado(List<UatVeiculoDTO> listVeiculoDTO, Long idUat) {
		List<UatVeiculo> uatVeiculosCadastrados = uatVeiculoDAO.listAllUatVeiculosByIdUatAndAtivo(idUat);
		boolean hasVeiculoPasseioCadastrado = uatVeiculosCadastrados.stream().anyMatch(item -> item.getUatVeiculoTipo().getId().equals(2L));
		List<Long> unidadesMoveisCadastradas = uatVeiculosCadastrados.stream()
				.filter(item -> !item.getUatVeiculoTipo().getId().equals(2L))
				.map(item -> item.getUnidadeVeiculoTipoAtendimento().getId())
				.collect(Collectors.toList());
		
		String descricaoVeiculoPasseio = getDescricaoVeiculoTipo2(uatVeiculosCadastrados, hasVeiculoPasseioCadastrado);
		
		for (UatVeiculoDTO uatVeiculoDTO : listVeiculoDTO) {
			if (uatVeiculoDTO.getIdTipo() != null && uatVeiculoDTO.getIdTipo().equals(2L) && hasVeiculoPasseioCadastrado) {
				throw new BusinessErrorException(getMensagem("app_rst_veiculo_duplicado", descricaoVeiculoPasseio));
			}
			
			if(uatVeiculoDTO.getIdVeiculoTipoAtendimento() != null && unidadesMoveisCadastradas.contains(uatVeiculoDTO.getIdVeiculoTipoAtendimento())) {
				throw new BusinessErrorException(getMensagem("app_rst_veiculo_duplicado", uatVeiculoDTO.getDescricao()));
			}
		}
	}

	private String getDescricaoVeiculoTipo2(List<UatVeiculo> uatVeiculosCadastrados,
			boolean hasVeiculoPasseioCadastrado) {
		if(hasVeiculoPasseioCadastrado) {
			return uatVeiculosCadastrados.stream()
				.filter(item -> item.getUatVeiculoTipo().getId().equals(2L))
				.map(item -> item.getUatVeiculoTipo().getDescricao())
				.findFirst().orElse("");
		}
		return "";
	}

	private UatVeiculo parseToEntity(UatVeiculoDTO dto) {
		UatVeiculo uatVeiculo = new UatVeiculo();
		uatVeiculo.setQuantidade(dto.getQuantidade());
		uatVeiculo.setUatVeiculoTipo(new UatVeiculoTipo(dto.getIdTipo()));
		uatVeiculo.setUnidadeAtendimentoTrabalhador(new UnidadeAtendimentoTrabalhador(dto.getIdUat()));
		if (dto.getIdVeiculoTipoAtendimento() != null) {
			uatVeiculo
				.setUnidadeVeiculoTipoAtendimento(new UatVeiculoTipoAtendimento(dto.getIdVeiculoTipoAtendimento()));
		}
		return uatVeiculo;
	}
	
	private List<UatVeiculoGroupedByTipoDTO> createListUatVeiculoGroupedByTipoDTO(List<UatVeiculo> list, ClienteAuditoria auditoria) {
		List<UatVeiculoGroupedByTipoDTO> listUatVeiculoGroupedByTipoDTO = new ArrayList<>();
		List<UatVeiculoTipo> uatVeiculoTipos = uatVeiculoTipoService.listarTodos(auditoria);
		
		for (UatVeiculoTipo uatVeiculoTipo : uatVeiculoTipos) {
			List<UatVeiculo> listUatVeiculosFiltered = list.stream()
					.filter(uatVeiculo -> uatVeiculoTipo.getId().equals(uatVeiculo.getUatVeiculoTipo().getId()))
					.collect(Collectors.toList());
			
			boolean isTipoAtendimento = listUatVeiculosFiltered.stream()
					.anyMatch(item -> SimNao.SIM.equals(item.getUatVeiculoTipo().getAtendimento()));
			
			if(CollectionUtils.isNotEmpty(listUatVeiculosFiltered)) {
				UatVeiculoGroupedByTipoDTO uatVeiculo = new UatVeiculoGroupedByTipoDTO();
				uatVeiculo.setIdTipo(uatVeiculoTipo.getId());
				uatVeiculo.setIsTipoAtendimento(isTipoAtendimento);
				uatVeiculo.setDescricaoTipo(uatVeiculoTipo.getDescricao());
				uatVeiculo.addVeiculos(createListUatVeiculoDTO(listUatVeiculosFiltered));
				listUatVeiculoGroupedByTipoDTO.add(uatVeiculo);
			}
		}
		return listUatVeiculoGroupedByTipoDTO;
	}
	
	private List<UatVeiculoDTO> createListUatVeiculoDTO(List<UatVeiculo> list) {
		boolean hasDescricao = false;
		List<UatVeiculoDTO> listResult = new ArrayList<>();
		for (UatVeiculo uatVeiculo : list) {
			hasDescricao = uatVeiculo.getUnidadeVeiculoTipoAtendimento() != null;
			String descricao = hasDescricao ? uatVeiculo.getUnidadeVeiculoTipoAtendimento().getDescricao() : null;
			listResult.add(new UatVeiculoDTO(uatVeiculo.getId(), uatVeiculo.getQuantidade(), descricao));
		}
		if (hasDescricao) {
			listResult.sort(Comparator.comparing(UatVeiculoDTO::getDescricao));
		}
		return listResult;
	}
}
