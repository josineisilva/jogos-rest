package br.unitau.inf.jogos.dto;

import java.util.List;

import br.unitau.inf.jogos.model.VotoCount;

public class ResultadoGetDTO {
	long total_votantes;
	long total_votos;
	List<VotoCount> resultados;
		
	public ResultadoGetDTO(long total_votantes, long total_votos, List<VotoCount> resultados) {
		this.total_votantes = total_votantes;
		this.total_votos = total_votos;
		this.resultados = resultados;
	}

	public long getTotal_votantes() {
		return total_votantes;
	}
	
	public long getTotal_votos() {
		return total_votos;
	}
	
	public List<VotoCount> getResultados() {
		return resultados;
	}
}