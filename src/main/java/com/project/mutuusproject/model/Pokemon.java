package com.project.mutuusproject.model;

/**
 * Esta classe implementa os dados de um Pokemon. Portanto, ela
 * contém os atributos nome, id e url. 
 * 
 * Exemplo de uso:
 *
 * Pokemon pokemon = new Pokemon(1L, "nomePokemon", "urlDoPokemon");
 * 
 */
public class Pokemon {

	private long id;
	
	private String name;
	
	private String url;
	
	public Pokemon() {
	}
	
	public Pokemon(long id, String name, String url) {
		this.id = id;
		this.name = name;
		this.url = url;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
