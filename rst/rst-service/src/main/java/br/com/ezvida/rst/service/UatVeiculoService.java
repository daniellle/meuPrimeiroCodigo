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

import br.com.ezvida.rst.dao.UatVeiculoDAO;
import br.com.ezvida.rst.enums.SimNao;
import br.com.ezvida.rst.model.UatVeiculo;
import br.com.ezvida.rst.model.UatVeiculoTipo;
import br.com.ezvida.rst.model.UatVeiculoTipoAtendimento;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.model.dto.UatVeiculoDTO;
import br.com.ezvida.rst.model.dto.UatVeiculoGroupedByTipoDTO;
import fw.core.service.BaseService;

@Stateless
public class UatVeiculoService extends BaseService {

	private static final long serialVersionUID = 1L;

	@Inject
	private UatVeiculoDAO uatVeiculoDAO;
	
	@Inject
	private UatVeiculoTipoService uatVeiculoTipoService;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UatVeiculo> listarTodos() {
		return uatVeiculoDAO.pesquisarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UatVeiculoGroupedByTipoDTO> listAllUatVeiculoGroupedByTipo(Long idUat) {
		return createListUatVeiculoGroupedByTipoDTO(uatVeiculoDAO.listAllUatVeiculosByIdUat(idUat));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<UatVeiculoDTO> salvar(List<UatVeiculoDTO> listVeiculoDTO) {
		return listVeiculoDTO.stream().map(item -> {
			UatVeiculo uatVeiculo = parseToEntity(item);
			uatVeiculoDAO.salvar(uatVeiculo);
			item.setId(uatVeiculo.getId());
			return item;
		}).collect(Collectors.toList());
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
	
	private List<UatVeiculoGroupedByTipoDTO> createListUatVeiculoGroupedByTipoDTO(List<UatVeiculo> list) {
		List<UatVeiculoGroupedByTipoDTO> listUatVeiculoGroupedByTipoDTO = new ArrayList<>();
		List<UatVeiculoTipo> uatVeiculoTipos = uatVeiculoTipoService.listarTodos();
		
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
