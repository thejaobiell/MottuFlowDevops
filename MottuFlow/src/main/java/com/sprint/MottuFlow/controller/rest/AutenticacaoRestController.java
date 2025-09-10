package com.sprint.MottuFlow.controller.rest;

import com.sprint.MottuFlow.domain.autenticao.DadosLogin;
import com.sprint.MottuFlow.domain.autenticao.DadosRefreshToken;
import com.sprint.MottuFlow.domain.autenticao.DadosToken;
import com.sprint.MottuFlow.domain.autenticao.TokenService;
import com.sprint.MottuFlow.domain.funcionario.Funcionario;
import com.sprint.MottuFlow.domain.funcionario.FuncionarioService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping( "/api" )
public class AutenticacaoRestController {
	
	private final AuthenticationManager aM;
	private final TokenService tS;
	private final FuncionarioService fS;
	
	public AutenticacaoRestController( AuthenticationManager aM, TokenService tS, FuncionarioService fS ) {
		this.aM = aM;
		this.tS = tS;
		this.fS = fS;
	}
	
	@PostMapping( "/login" )
	public ResponseEntity<DadosToken> efetuarLogin( @Valid @RequestBody DadosLogin dados ) {
		var authToken = new UsernamePasswordAuthenticationToken( dados.email(), dados.senha() );
		var authentication = aM.authenticate( authToken );
		
		var funcionario = (Funcionario) authentication.getPrincipal();
		
		// gerar tokens
		String tokenAcesso = tS.gerarToken( funcionario );
		String refreshToken = tS.gerarRefreshToken( funcionario );
		LocalDateTime expiracaoRefresh = LocalDateTime.now().plusMinutes( 120 );
		
		// salvar refresh token no banco
		fS.atualizarRefreshTokenFuncionario( funcionario, refreshToken, expiracaoRefresh );
		
		return ResponseEntity.ok( new DadosToken( tokenAcesso, refreshToken ) );
	}
	
	@PostMapping( "/atualizar-token" )
	public ResponseEntity<DadosToken> atualizarToken( @Valid @RequestBody DadosRefreshToken dados ) {
		Funcionario funcionario = fS.validarRefreshTokenFuncionario( dados.refreshToken() );
		
		// gerar novos tokens
		String tokenAcesso = tS.gerarToken( funcionario );
		String novoRefreshToken = tS.gerarRefreshToken( funcionario );
		LocalDateTime expiracaoRefresh = LocalDateTime.now().plusMinutes( 120 );
		
		// atualizar refresh token no banco
		fS.atualizarRefreshTokenFuncionario( funcionario, novoRefreshToken, expiracaoRefresh );
		
		return ResponseEntity.ok( new DadosToken( tokenAcesso, novoRefreshToken ) );
	}
}
