package br.com.uedercardoso.gestaodecontatos.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.uedercardoso.gestaodecontatos.models.Contato;
import br.com.uedercardoso.gestaodecontatos.repositories.RepositorioContato;

@RestController
public class Contatos {

	private static String HOST = "C:/Users/User/Desktop";
	
	@Autowired
	private RepositorioContato repositorioContato;
	
	////https://spring.io/guides/gs/rest-service-cors/
	//@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/adicionar/contato")
	public Map<String,String> adicionarContato(@Valid @RequestParam String nome, @Valid @RequestParam String sobrenome,
			@Valid @RequestParam long telefone, @Valid @RequestParam MultipartFile foto, @Valid @RequestParam String email,
			@Valid @RequestParam String linkTwitter, @Valid @RequestParam String contatoSkype) {
	
		Map<String,String> map =  new HashMap<String,String>(); 
		
		try {
			String path = HOST+"/fotos/";
			this.criarPasta(path); //Criar pasta "fotos"
				        
			Contato contato = this.criarContato(nome, sobrenome, telefone, null, email, linkTwitter, contatoSkype);
			contato = repositorioContato.save(contato);
			
			path += "contato_"+contato.getCodigo()+"/";
			this.criarPasta(path);
			String pathFile = this.absolutePath(foto, path);
			contato.setPathFoto(pathFile);
			
	        this.download(foto, pathFile); //Faz download da foto
	        
	        repositorioContato.save(contato); 
	        
	        map.put("result", "ok");
			
		} catch(Exception e) {
			//e.printStackTrace();
			map.put("result", "failed");
			map.put("message", e.getMessage());
			
		}
		
		return map;
	}
		
	@PutMapping("/atualizar/contato/{codContato}")
	public Map<String,String> atualizarContato(@PathVariable("codContato") int codContato, @Valid @RequestParam String nome, 
			@Valid @RequestParam String sobrenome,@Valid @RequestParam long telefone, 
			@Valid @RequestParam MultipartFile foto, @Valid @RequestParam String email,
			@Valid @RequestParam String linkTwitter, @Valid @RequestParam String contatoSkype) {
		
		Map<String,String> map =  new HashMap<String,String>(); 
		
		if(repositorioContato.existsById(codContato)) {
			
			String path = HOST+"/fotos/contato_"+codContato+"/";
			String pathFile = null;
			
			Optional<Contato> op = repositorioContato.findById(codContato);
			Contato contato = op.get();
			
			try {
				if(contato.getPathFoto() != null) {
					File antigo = new File(contato.getPathFoto()); 
					
					if(antigo.isFile() && antigo.exists()) {
						antigo.delete(); //Excluir a foto antiga
								
						this.criarPasta(path);
						pathFile = this.absolutePath(foto, path);
						this.download(foto, pathFile);
					}
				} else {
					this.criarPasta(path);
					pathFile = this.absolutePath(foto, path);
					this.download(foto, pathFile);
				}
				
				contato = this.criarContato(nome, sobrenome, telefone, pathFile, email, linkTwitter, contatoSkype);
				contato.setCodigo(codContato);
				
				repositorioContato.save(contato);
				
				map.put("result", "ok"); 
				
			} catch(Exception e) {
				//e.printStackTrace();
				map.put("message", e.getMessage());
			}
		} else {
			map.put("result", "failed");
		}
		
		return map;
		
	}
	
	@DeleteMapping("/excluir/contato/{codContato}")
	public Map<String, String> excluirContato(@PathVariable("codContato") int codContato) {
		
		Map<String, String> map = new HashMap<String,String>(); 
		
		if(repositorioContato.existsById(codContato)) {
			
			Optional<Contato> op = repositorioContato.findById(codContato); 
			Contato contato = op.get(); 
			
			try {
				
				//excluir foto
				
				if(contato.getPathFoto() != null) {
					File filePhoto = new File(contato.getPathFoto()); 
					if(filePhoto.isFile() && filePhoto.exists()) {
						filePhoto.delete(); //Deletando a foto
						
						File path = new File(HOST+"/fotos/contato_"+codContato+"/");
						path.delete(); //Deletando a pasta do contato específico
					}
				}
				
				repositorioContato.deleteById(codContato);	
				
				map.put("result", "deleted");
				
			} catch(Exception e) {
				//e.printStackTrace();
				map.put("message", e.getMessage());
				map.put("result", "failed"); 
			}
			
		}  else {
			map.put("result", "failed"); 
		}
		
		return map;
		
	}
	
	@GetMapping("/contatos/ordenar/{ordenacao}")
	public Map<String,Object> listarContatoOrdenadoPeloValor(@PathVariable("ordenacao") String ordenacao) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		List<Contato> listaContato = new LinkedList<Contato>();
		
		try {
			
			switch(ordenacao) {
				case "codigo":{
					listaContato = repositorioContato.findContactsOrderById();
					break;
				}
				case "nome":{
					listaContato = repositorioContato.findContactsOrderByName();
					break; 
				} 
				case "sobrenome":{
					listaContato = repositorioContato.findContactsOrderBySobrenome();
					break;
				}
				case "telefone":{
					listaContato = repositorioContato.findContactsOrderByTelephone();
					break;
				}
				default:{
					listaContato = repositorioContato.findAll();
					break;
				}
			}
			
			map = this.criarMap(listaContato);
					
		} catch(Exception e) {
			//e.printStackTrace();
			map = this.criarMap(null);
		}
		
		return map;
		
	}
	
	
	@GetMapping("/buscar/contato/{codContato}")
	public Map<String,Object> buscarContato(@PathVariable("codContato") int codContato) {
		
		Map<String, Object> map = new HashMap<String,Object>();
		
		if(repositorioContato.existsById(codContato)) {
			
			try {
				Optional<Contato> op = repositorioContato.findById(codContato); 
				Contato contato = op.get();
				
				List<Contato> listaContato = new LinkedList<Contato>();
				listaContato.add(contato);
				
				map = this.criarMap(listaContato); 
			} catch(Exception e) {
				//e.printStackTrace();
				map = this.criarMap(null);
			}
			
		} else {
			map = this.criarMap(null);
		}
		
		return map;
	}
	
	@GetMapping("/contatos")
	public Map<String,Object> listarContatos(){
		
		Map<String,Object> map = new HashMap<String,Object>();
		 
		try {
			
			List<Contato> listaContato = repositorioContato.findAll();
			
			map = this.criarMap(listaContato);
			
		} catch(Exception e) {
			//e.printStackTrace();
			map = this.criarMap(null);
		}
		
		return map;
		
	}
	
	@GetMapping("/contatos/consultar/{atributo}/{valor}")
	public Map<String,Object> listarContatosPorValor(@PathVariable("atributo") String atributo, @PathVariable("valor") String valor){
		
		List<Contato> listaContato = new LinkedList<Contato>();
		
		Map<String,Object> map = new HashMap<String,Object>(); 
		
		try {
			switch(atributo) {
				case "nome":{
					listaContato = repositorioContato.findContactByName(valor);
					break;
				}
				case "sobrenome":{
					listaContato = repositorioContato.findContactBySobrenome(valor);
					break;
				}
				case "telefone":{
					listaContato = repositorioContato.findContactByTelephone(Long.parseLong(valor));
					break;
				}
				default:{
					listaContato = null;
					break;
				}
			}
			
			map = this.criarMap(listaContato);
			
		} catch(Exception e) {
			//e.printStackTrace();
			map = this.criarMap(null);
		}
				
		return map;
	}
	
	
	public Contato criarContato(String nome, String sobrenome, long telefone, String pathFoto, String email,
			String linkTwitter, String contatoSkype){
		Contato contato = new Contato(); 
		contato.setNome(nome);
		contato.setSobrenome(sobrenome);
		contato.setTelefone(telefone);
		contato.setPathFoto(pathFoto);
		contato.setEmail(email);
		contato.setLinkTwitter(linkTwitter);
		contato.setContatoSkype(contatoSkype);
		
		return contato;
	}
	
	public Map<String,Object> criarMap(List<Contato> listaContato){
		Map<String,Object> map = new HashMap<String,Object>();
		if(listaContato != null) {
			map.put("totalItems", listaContato.size()); 
		} else {
			map.put("totalItems", 0);
		}
		map.put("items", listaContato); 
		return map;
	}
	
	public void criarPasta(String path) {
        File diretorio = new File(path);
        
        if(!diretorio.exists()) {
        	diretorio.mkdir();
        } 
	}
	
	public String absolutePath(MultipartFile arquivo, String path) {
		String extensao = arquivo.getOriginalFilename();
		int aux = extensao.lastIndexOf('.'); 
		extensao = extensao.substring(aux+1, extensao.length());
		
		return path+"foto."+extensao;
	}
	
	public void download(MultipartFile arquivo, String pathFile) throws IOException {
        byte[] bytes = arquivo.getBytes();
        Path path2 = Paths.get(pathFile);
        Files.write(path2, bytes);
	}
	
}
