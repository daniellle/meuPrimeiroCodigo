package br.com.ezvida.rst.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.com.ezvida.rst.dao.UatVeiculoDAO;
import br.com.ezvida.rst.model.UatVeiculo;
import br.com.ezvida.rst.model.UatVeiculoTipoAtendimento;
import br.com.ezvida.rst.model.UnidadeAtendimentoTrabalhador;
import br.com.ezvida.rst.model.dto.UatVeiculoDTO;
import fw.core.service.BaseService;

@Stateless
public class UatVeiculoService extends BaseService {

	private static final long serialVersionUID = 1L;

	@Inject
	private UatVeiculoDAO uatVeiculoDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UatVeiculo> listarTodos() {
		return uatVeiculoDAO.pesquisarTodos();
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
		uatVeiculo.setUnidadeAtendimentoTrabalhador(new UnidadeAtendimentoTrabalhador(dto.getIdUat()));
		if (dto.getIdVeiculoTipoAtendimento() != null) {
			uatVeiculo
					.setUnidadeVeiculoTipoAtendimento(new UatVeiculoTipoAtendimento(dto.getIdVeiculoTipoAtendimento()));
		}
		return uatVeiculo;
	}
}
