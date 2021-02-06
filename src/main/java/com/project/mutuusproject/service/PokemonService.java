package com.project.mutuusproject.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.apache.commons.collections4.IterableUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.project.mutuusproject.mapper.PokemonRowMapper;
import com.project.mutuusproject.mapper.impl.PokemonResultSetRowMapperImpl;
import com.project.mutuusproject.model.Pokemon;
import com.project.mutuusproject.repository.PokemonRepository;
import com.project.mutuusproject.repository.impl.PokemonRepositoryImpl;

public class PokemonService {
	PokemonRepository pokemonRepository;

	public PokemonService(Connection connection) {
		pokemonRepository = new PokemonRepositoryImpl(connection, new PokemonResultSetRowMapperImpl());
	}

	public List<Pokemon> getAllPokemons(Integer pageNo, Integer pageSize) throws SQLException {
		List<Long> range = LongStream.rangeClosed(pageNo+1, pageNo + pageSize)
			    .boxed().collect(Collectors.toList());
		Collection<Pokemon> pokes = pokemonRepository.findAll(range);
		List<Pokemon> pokemons = new ArrayList<> (pokes);
		List<Long> pokemonsNotFound = checkRequest(new HashSet<>(pokes), range);
		List<ArrayList<Long>> pokemonsToRequest = new ArrayList<>();
		if(!pokemonsNotFound.isEmpty()) {
			pokemonsToRequest = makeRequest(pokemonsNotFound);
		}
		List<Pokemon> pokemonsToAdd = doRequest(pokemonsToRequest);
		pokemonRepository.insertAll(pokemonsToAdd);
		pokemons.addAll(pokemonsToAdd);
		
		return pokemons;
	}

	private List<Pokemon> doRequest(List<ArrayList<Long>> pokemonsToRequest) {
		List<Pokemon> listOfPokemonsToAdd = new ArrayList<Pokemon>();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		for(ArrayList<Long> p : pokemonsToRequest) {
			Long firstElement = p.get(0) - 1;
			try {
				String url = "https://pokeapi.co/api/v2/pokemon/?offset=" + firstElement.toString() + "&limit=" + String.valueOf(p.size());
				System.out.println(url);
				HttpUriRequest httpget = RequestBuilder.get()
				            .setUri(new URI(url))
				            .build();
				try {
					response = httpclient.execute(httpget);
					String json = EntityUtils.toString(response.getEntity());
					JSONParser jsonParser = new JSONParser();
					JSONObject jsonObj = null;
					try {
						jsonObj = (JSONObject) jsonParser.parse(json);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					JSONArray jsonArray = (JSONArray) jsonObj.get("results");
					for(int i = 0; i < jsonArray.size(); i++) {
						JSONObject pokemon = (JSONObject) jsonArray.get(i);
						Pokemon newPoke = new Pokemon();
						newPoke.setName(pokemon.get("name").toString());
						newPoke.setUrl(pokemon.get("url").toString());
						String[] urlId = pokemon.get("url").toString().split("/");
						String id = urlId[urlId.length-1];
						newPoke.setId(Long.parseLong(id));
						listOfPokemonsToAdd.add(newPoke);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			} catch (URISyntaxException e) {
				e.printStackTrace();
			} finally {
			}
			
		}
		return listOfPokemonsToAdd;
	}

	private ArrayList<ArrayList<Long>> makeRequest(List<Long> pokemonsNotFound) {
		ListIterator<Long> it = pokemonsNotFound.listIterator();
		ArrayList<ArrayList<Long>> listOfItens = new ArrayList<ArrayList<Long>>();
		ArrayList<Long> list = new ArrayList<>();
		boolean isDifferent = false;
		for(int i = 0; i < pokemonsNotFound.size(); i++) {
			list.add(pokemonsNotFound.get(i));
			try {
				if(pokemonsNotFound.get(i + 1) != pokemonsNotFound.get(i) + 1) {
					listOfItens.add(list);
					list = new ArrayList<>();
				}
			} catch(IndexOutOfBoundsException e) {
				listOfItens.add(list);
				break;
			}

		}
		return listOfItens;
	}

	private List<Long> checkRequest(HashSet<Pokemon> pokemons, List<Long> range) {
		List<Long> pokemonsFound = new ArrayList<>();
		for(Pokemon p : pokemons) {
			pokemonsFound.add(p.getId());
		}
		range.removeAll(pokemonsFound);
		return range;
	}
	
}
