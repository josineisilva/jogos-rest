package br.unitau.inf.jogos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.unitau.inf.jogos.model.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Integer> {

}
