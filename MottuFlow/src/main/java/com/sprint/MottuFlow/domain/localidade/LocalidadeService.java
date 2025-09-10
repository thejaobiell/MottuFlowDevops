package com.sprint.MottuFlow.domain.localidade;

import com.sprint.MottuFlow.infra.exception.RegraDeNegocioException;
import com.sprint.MottuFlow.domain.camera.Camera;
import com.sprint.MottuFlow.domain.camera.CameraRepository;
import com.sprint.MottuFlow.domain.moto.Moto;
import com.sprint.MottuFlow.domain.moto.MotoRepository;
import com.sprint.MottuFlow.domain.patio.Patio;
import com.sprint.MottuFlow.domain.patio.PatioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LocalidadeService {
	
	private final LocalidadeRepository repository;
	private final MotoRepository motoRepository;
	private final PatioRepository patioRepository;
	private final CameraRepository cameraRepository;
	
	public LocalidadeService(LocalidadeRepository repository, MotoRepository motoRepository,
	                         PatioRepository patioRepository, CameraRepository cameraRepository) {
		this.repository = repository;
		this.motoRepository = motoRepository;
		this.patioRepository = patioRepository;
		this.cameraRepository = cameraRepository;
	}
	
	public List<Localidade> listarLocalidades() {
		return repository.findAll();
	}
	
	public Localidade buscarLocalidadePorId(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new RegraDeNegocioException("Localidade não encontrada com id: " + id));
	}
	
	public List<Localidade> buscarPorPontoReferencia(String ponto) {
		return repository.findByPontoReferencia(ponto);
	}
	
	public List<Localidade> buscarPorIntervaloData(LocalDateTime dataInicio, LocalDateTime dataFim) {
		return repository.findDatas(dataInicio, dataFim);
	}
	
	@Transactional
	public Localidade cadastrarLocalidade(Localidade localidade) {
		Moto moto = motoRepository.findById(localidade.getMoto().getIdMoto())
				.orElseThrow(() -> new RegraDeNegocioException("Moto não encontrada com id: " + localidade.getMoto().getIdMoto()));
		
		Patio patio = patioRepository.findById(localidade.getPatio().getIdPatio())
				.orElseThrow(() -> new RegraDeNegocioException("Patio não encontrado com id: " + localidade.getPatio().getIdPatio()));
		
		Camera camera = cameraRepository.findById(localidade.getCamera().getIdCamera())
				.orElseThrow(() -> new RegraDeNegocioException("Camera não encontrada com id: " + localidade.getCamera().getIdCamera()));
		
		localidade.setMoto(moto);
		localidade.setPatio(patio);
		localidade.setCamera(camera);
		
		return repository.save(localidade);
	}
	
	@Transactional
	public Localidade editarLocalidade(Long id, Localidade localidadeAtualizada) {
		Localidade localidade = buscarLocalidadePorId(id);
		
		if (localidadeAtualizada.getDataHora() != null) {
			localidade.setDataHora(localidadeAtualizada.getDataHora());
		}
		
		if (localidadeAtualizada.getPontoReferencia() != null && !localidadeAtualizada.getPontoReferencia().isBlank()) {
			localidade.setPontoReferencia(localidadeAtualizada.getPontoReferencia());
		}
		
		if (localidadeAtualizada.getMoto() != null && localidadeAtualizada.getMoto().getIdMoto() != null) {
			Moto moto = motoRepository.findById(localidadeAtualizada.getMoto().getIdMoto())
					.orElseThrow(() -> new RegraDeNegocioException(
							"Moto não encontrada com id: " + localidadeAtualizada.getMoto().getIdMoto()));
			localidade.setMoto(moto);
		}
		
		if (localidadeAtualizada.getPatio() != null && localidadeAtualizada.getPatio().getIdPatio() != null) {
			Patio patio = patioRepository.findById(localidadeAtualizada.getPatio().getIdPatio())
					.orElseThrow(() -> new RegraDeNegocioException(
							"Patio não encontrado com id: " + localidadeAtualizada.getPatio().getIdPatio()));
			localidade.setPatio(patio);
		}
		
		if (localidadeAtualizada.getCamera() != null && localidadeAtualizada.getCamera().getIdCamera() != null) {
			Camera camera = cameraRepository.findById(localidadeAtualizada.getCamera().getIdCamera())
					.orElseThrow(() -> new RegraDeNegocioException(
							"Camera não encontrada com id: " + localidadeAtualizada.getCamera().getIdCamera()));
			localidade.setCamera(camera);
		}
		
		return repository.save(localidade);
	}
	
	@Transactional
	public void deletarLocalidade(Long id) {
		Localidade localidade = buscarLocalidadePorId(id);
		repository.delete(localidade);
	}
}
