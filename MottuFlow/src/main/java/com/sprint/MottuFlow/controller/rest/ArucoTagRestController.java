package com.sprint.MottuFlow.controller.rest;

import com.sprint.MottuFlow.domain.arucotag.ArucoTagDTO;
import com.sprint.MottuFlow.domain.arucotag.ArucoTag;
import com.sprint.MottuFlow.domain.moto.Moto;
import com.sprint.MottuFlow.domain.arucotag.ArucoTagService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/aruco-tags")
public class ArucoTagRestController {
	
	private final ArucoTagService service;
	
	public ArucoTagRestController(ArucoTagService service) {
		this.service = service;
	}
	
	private ArucoTagDTO paraDTO(ArucoTag tag) {
		return new ArucoTagDTO(
				tag.getIdTag(),
				tag.getCodigo(),
				tag.getMoto().getIdMoto(),
				tag.getStatus()
		);
	}
	
	private ArucoTag paraEntity(ArucoTagDTO dto) {
		ArucoTag tag = new ArucoTag();
		tag.setIdTag(dto.getIdTag());
		tag.setCodigo(dto.getCodigo());
		
		if (dto.getIdMoto() != 0) {
			Moto moto = new Moto();
			moto.setIdMoto(dto.getIdMoto());
			tag.setMoto(moto);
		}
		tag.setStatus(dto.getStatus());
		return tag;
	}
	
	@GetMapping("/listar")
	public List<ArucoTagDTO> listarRest() {
		return service.listarArucoTags().stream().map(this::paraDTO).collect(Collectors.toList());
	}
	
	@GetMapping("/buscar-por-id/{id}")
	public ResponseEntity<ArucoTagDTO> buscarPorIdRest(@PathVariable Long id) {
		ArucoTag tag = service.buscarTagPorId(id);
		return ResponseEntity.ok(paraDTO(tag));
	}
	
	@GetMapping("/buscar-por-status/{status}")
	public List<ArucoTagDTO> buscarPorStatusRest(@PathVariable String status) {
		return service.buscarPorStatus(status).stream().map(this::paraDTO).collect(Collectors.toList());
	}
	
	@GetMapping("/buscar-por-codigo/{codigo}")
	public ResponseEntity<ArucoTagDTO> buscarPorCodigoRest(@PathVariable String codigo) {
		ArucoTag tag = service.buscarPorCodigo(codigo);
		return ResponseEntity.ok(paraDTO(tag));
	}
	
	@PostMapping("/cadastrar")
	public ResponseEntity<ArucoTagDTO> cadastrarRest(@RequestBody @Valid ArucoTagDTO dto) {
		ArucoTag tag = paraEntity(dto);
		ArucoTag salvo = service.cadastrarTag(tag);
		return ResponseEntity.ok(paraDTO(salvo));
	}
	
	@PutMapping("/editar/{id}")
	public ResponseEntity<ArucoTagDTO> editarRest(@PathVariable Long id, @RequestBody ArucoTagDTO dto) {
		ArucoTag tag = paraEntity(dto);
		ArucoTag atualizado = service.editarTag(id, tag);
		return ResponseEntity.ok(paraDTO(atualizado));
	}
	
	@DeleteMapping("/deletar/{id}")
	public ResponseEntity<Void> deletarRest(@PathVariable Long id) {
		service.deletarTag(id);
		return ResponseEntity.noContent().build();
	}
}