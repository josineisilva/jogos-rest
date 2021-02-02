package br.unitau.inf.jogos.model;

public class VotoCount {
	Integer projeto;
	long total;
	double percentual;
	public VotoCount(Integer projeto, long total, double percentual) {
		super();
		this.projeto = projeto;
		this.total = total;
		this.percentual = percentual;
	}
	
	public Integer getProjeto() {
		return projeto;
	}
	
	public long getTotal() {
		return total;
	}
	
	public double getPercentual() {
		return percentual;
	}
}