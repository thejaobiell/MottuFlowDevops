package com.sprint.MottuFlow.domain.status;

import com.sprint.MottuFlow.infra.exception.RegraDeNegocioException;
import com.sprint.MottuFlow.domain.funcionario.Funcionario;
import com.sprint.MottuFlow.domain.funcionario.FuncionarioRepository;
import com.sprint.MottuFlow.domain.moto.Moto;
import com.sprint.MottuFlow.domain.moto.MotoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {
	
	private final StatusRepository repository;
	private final MotoRepository motoRepository;
	private final FuncionarioRepository funcionarioRepository;
	
	public StatusService(StatusRepository repository, MotoRepository motoRepository, FuncionarioRepository funcionarioRepository) {
		this.repository = repository;
		this.motoRepository = motoRepository;
		this.funcionarioRepository = funcionarioRepository;
	}
	
	public List<Status> listarStatus() {
		return repository.findAll();
	}
	
	public Status buscarStatusPorId(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new RegraDeNegocioException("Status não encontrado com id: " + id));
	}
	
	public List<Status> buscarPorTipoStatus(TipoStatus tipoStatus) {
		return repository.findByTipoStatus(tipoStatus);
	}
	
	public List<Status> buscarPorDescricaoStatus(String descricao) {
		return repository.findByDescricao(descricao);
	}
	
	@Transactional
	public Status cadastrarStatus(Status status) {
		if (status.getMoto() == null || status.getMoto().getIdMoto() == null) {
			throw new RegraDeNegocioException("Moto é obrigatória!");
		}
		if (status.getFuncionario() == null || status.getFuncionario().getId_funcionario() == null) {
			throw new RegraDeNegocioException("Funcionario é obrigatório!");
		}
		
		Moto moto = motoRepository.findById(status.getMoto().getIdMoto())
				.orElseThrow(() -> new RegraDeNegocioException("Moto não encontrada com id: " + status.getMoto().getIdMoto()));
		
		Funcionario funcionario = funcionarioRepository.findById(status.getFuncionario().getId_funcionario())
				.orElseThrow(() -> new RegraDeNegocioException("Funcionario não encontrado com id: " + status.getFuncionario().getId_funcionario()));
		
		status.setMoto(moto);
		status.setFuncionario(funcionario);
		
		return repository.save(status);
	}
	
	@Transactional
	public Status editarStatus(Long id, Status statusAtualizado) {
		Status status = buscarStatusPorId(id);
		
		if (statusAtualizado.getTipoStatus() != null) {
			status.setTipoStatus(statusAtualizado.getTipoStatus());
		}
		
		if (statusAtualizado.getDescricao() != null && !statusAtualizado.getDescricao().isBlank()) {
			status.setDescricao(statusAtualizado.getDescricao());
		}
		
		if (statusAtualizado.getDataStatus() != null) {
			status.setDataStatus(statusAtualizado.getDataStatus());
		}
		
		if (statusAtualizado.getMoto() != null && statusAtualizado.getMoto().getIdMoto() != null) {
			Moto moto = motoRepository.findById(statusAtualizado.getMoto().getIdMoto())
					.orElseThrow(() -> new RegraDeNegocioException("Moto não encontrada com id: " + statusAtualizado.getMoto().getIdMoto()));
			status.setMoto(moto);
		}
		
		if (statusAtualizado.getFuncionario() != null && statusAtualizado.getFuncionario().getId_funcionario() != null) {
			Funcionario funcionario = funcionarioRepository.findById(statusAtualizado.getFuncionario().getId_funcionario())
					.orElseThrow(() -> new RegraDeNegocioException("Funcionario não encontrado com id: " + statusAtualizado.getFuncionario().getId_funcionario()));
			status.setFuncionario(funcionario);
		}
		
		return repository.save(status);
	}
	
	@Transactional
	public void deletarStatus(Long id) {
		Status status = buscarStatusPorId(id);
		repository.delete(status);
	}
}
