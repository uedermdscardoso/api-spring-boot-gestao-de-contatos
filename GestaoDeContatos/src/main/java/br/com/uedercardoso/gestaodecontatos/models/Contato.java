package br.com.uedercardoso.gestaodecontatos.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Contato {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int codigo; 
	
	@NotEmpty
	@Size(min=3, max=15)
	@Column(length=15,nullable=false)
	private String nome;
	
	@NotEmpty
	@Size(min=5, max=50)
	@Column(length=50,nullable=false)
	private String sobrenome;
	
	@NotNull
	@Column(length=12,nullable=false)
	private long telefone;
	
	@Column(length=255,nullable=true)
	private String pathFoto; 
	
	@Size(max=50)
	@Column(length=50, nullable=true)
	private String email; 
	
	@Size(max=30)
	@Column(length=30, nullable=true)
	private String linkTwitter;
	
	@Size(max=30)
	@Column(length=30, nullable=true)
	private String contatoSkype;
	
	public Contato() {
		
	}

	public Contato(int codigo, String nome, String sobrenome, long telefone, String pathFoto, String email,
			String linkTwitter, String contatoSkype) {
		this.codigo = codigo;
		this.nome = nome;
		this.sobrenome = sobrenome;
		this.telefone = telefone;
		this.pathFoto = pathFoto;
		this.email = email;
		this.linkTwitter = linkTwitter;
		this.contatoSkype = contatoSkype;
	}

	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSobrenome() {
		return sobrenome;
	}
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
	public long getTelefone() {
		return telefone;
	}
	public void setTelefone(long telefone) {
		this.telefone = telefone;
	}
	public String getPathFoto() {
		return pathFoto;
	}
	public void setPathFoto(String pathFoto) {
		this.pathFoto = pathFoto;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLinkTwitter() {
		return linkTwitter;
	}
	public void setLinkTwitter(String linkTwitter) {
		this.linkTwitter = linkTwitter;
	}
	public String getContatoSkype() {
		return contatoSkype;
	}
	public void setContatoSkype(String contatoSkype) {
		this.contatoSkype = contatoSkype;
	}
	
}
