package com.sprint.MottuFlow.domain.moto;

import com.sprint.MottuFlow.infra.exception.RegraDeNegocioException;
import com.sprint.MottuFlow.domain.patio.Patio;
import com.sprint.MottuFlow.domain.patio.PatioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotoService {
	
	private final MotoRepository repository;
	private final PatioRepository patioRepository;
	
	public MotoService(MotoRepository repository, PatioRepository patioRepository) {
		this.repository = repository;
		this.patioRepository = patioRepository;
	}
	
	public List<Moto> listarMotos() {
		return repository.findAll();
	}
	
	public Moto buscarMotoPorId(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new RegraDeNegocioException("Moto não encontrada com id: " + id));
	}
	
	public List<Moto> buscarPorFabricante(String fabricante) {
		return repository.findByFabricante(fabricante);
	}
	
	public List<Moto> buscarPorPatioId(long idPatio) {
		return repository.findByPatioId(idPatio);
	}
	
	@Transactional
	public Moto cadastrarMoto(Moto moto) {
		if (moto.getPatio() == null || moto.getPatio().getIdPatio() == null) {
			throw new RegraDeNegocioException("Pátio é obrigatório!");
		}
		
		Patio patio = patioRepository.findById(moto.getPatio().getIdPatio())
				.orElseThrow(() -> new RegraDeNegocioException(
						"Pátio não encontrado com id: " + moto.getPatio().getIdPatio()));
		moto.setPatio(patio);
		
		return repository.save(moto);
	}
	
	@Transactional
	public Moto editarMoto(Long id, Moto motoAtualizada) {
		Moto moto = buscarMotoPorId(id);
		
		if (motoAtualizada.getPlaca() != null && !motoAtualizada.getPlaca().isBlank()) {
			moto.setPlaca(motoAtualizada.getPlaca());
		}
		
		if (motoAtualizada.getModelo() != null && !motoAtualizada.getModelo().isBlank()) {
			moto.setModelo(motoAtualizada.getModelo());
		}
		
		if (motoAtualizada.getFabricante() != null && !motoAtualizada.getFabricante().isBlank()) {
			moto.setFabricante(motoAtualizada.getFabricante());
		}
		
		if (motoAtualizada.getAno() > 0) { // Atualiza somente se ano for válido
			moto.setAno(motoAtualizada.getAno());
		}
		
		if (motoAtualizada.getLocalizacaoAtual() != null && !motoAtualizada.getLocalizacaoAtual().isBlank()) {
			moto.setLocalizacaoAtual(motoAtualizada.getLocalizacaoAtual());
		}
		
		if (motoAtualizada.getPatio() != null && motoAtualizada.getPatio().getIdPatio() != null) {
			Patio patio = patioRepository.findById(motoAtualizada.getPatio().getIdPatio())
					.orElseThrow(() -> new RegraDeNegocioException(
							"Pátio não encontrado com id: " + motoAtualizada.getPatio().getIdPatio()));
			moto.setPatio(patio);
		}
		
		return repository.save(moto);
	}
	
	@Transactional
	public void deletarMoto(Long id) {
		Moto moto = buscarMotoPorId(id);
		repository.delete(moto);
	}
}
