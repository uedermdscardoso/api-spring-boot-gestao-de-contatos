package br.com.uedercardoso.gestaodecontatos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.uedercardoso.gestaodecontatos.models.Contato;

public interface RepositorioContato extends JpaRepository<Contato, Integer>{

	@Query(value="SELECT c.* FROM Contato c WHERE c.nome LIKE %:nome%",nativeQuery=true)
	public List<Contato> findContactByName(@Param("nome") String nome);
	
	@Query(value="SELECT c.* FROM Contato c WHERE c.sobrenome LIKE %:sobrenome%",nativeQuery=true)
	public List<Contato> findContactBySobrenome(@Param("sobrenome") String sobrenome);
	
	@Query(value="SELECT c.* FROM Contato c WHERE c.telefone LIKE :telefone%",nativeQuery=true)
	public List<Contato> findContactByTelephone(@Param("telefone") long telefone);
	
	//Ordenar Listas
	@Query(value="SELECT c.* FROM Contato c ORDER BY c.codigo",nativeQuery=true)
	public List<Contato> findContactsOrderById();
		
	@Query(value="SELECT c.* FROM Contato c ORDER BY c.nome",nativeQuery=true)
	public List<Contato> findContactsOrderByName();
	
	@Query(value="SELECT c.* FROM Contato c ORDER BY c.sobrenome",nativeQuery=true)
	public List<Contato> findContactsOrderBySobrenome(); 
	
	@Query(value="SELECT c.* FROM Contato c ORDER BY c.telefone",nativeQuery=true)
	public List<Contato> findContactsOrderByTelephone();
	
}
