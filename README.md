# Mutuus-project
Projeto de aplicação para vaga de Desenvolvedor Java na Mutuus Seguro.


## Instalação
Para executar é necessário instalar as seguintes dependências:

* Java Runtime Environment (JRE) 
* Java Development Kit (JDK) 
* MySQL* 
* Maven

*O projeto está configurado para executar o MySQL na porta 3306. Tendo como usuário o "root" e sua senha como "password". Caso seu ambiente esteja configurado de forma diferente, altere a linha 24 da classe ConnectionSingleton ou então configure localmente de forma que satisfaça esses requisitos. Garanta também que a porta 8080 não estará em uso.

Com todo ambiente configurado, para executar o projeto basta acessar à pasta pelo terminal e digitar o seguinte comando:


``` 
mvn spring-boot:run
```

### Descrição do projeto

Este é um projeto Maven, onde eu utilizei o Spring Boot para gerenciar a aplicação, juntamente com a biblioteca do MySQL para fazer a conexão e as requisições diretamente ao banco. Além da biblioteca HttpComponents para fazer a request para a API do Pokemon e a Json-simple para manusear o Json recebido pela request.

O projeto possui dois endpoints:

* Get All
* Get By ID (Não implementado).

#### Funcionamento do projeto

* Get All

O Get All recebe dois parâmetros: o primeiro pokemon que ele deseja obter e o limite de pokemon daquela página. 
Exemplo de request: 

``` 
http://localhost:8080/api/v1/pokemon?offset=0&limit=15
```
O sistema irá retornar uma lista contendo o pokemon do 1 até o 15. *OBS: Caso os valores não sejam informados, o valor padrão será respectivamente 0 e 10.*

Feito a request e recebido os parâmetros o sistema inicialmente irá gerar uma lista de ids dos pokemons que estão nesse limite. Depois disso ele enviar essa lista para checar se todos os ids estão no banco de dados.

Os ids que não estão no banco são enviados para uma função que gera uma lista de listas  de ids de forma que o sistema irá fazer o menor número de requests possíveis para obter os ids.

Ex.: Se o usuário pediu os ids de 1 a 10, mas no banco não tem os ids 2,3,7 e 8. Então nessa lista de ids vão ter duas listas, uma contendo os ids 2 e 3, e outra contendo 7, 8. De forma que cada request obtenham os 2 ids juntamente.
 
Gerado essa lista de ids para a request, esse dado enviado para outra função que faz a request e armazena os pokemons em uma lista.

Depois essa lista é enviada para a classe de repositório, onde ela insere no banco de dados esses novos pokemons e depois é enviado para o usuários todos os pokemons solicitados. 

* Get By Id 

O Get By Id foi criado somente o endpoint, sem nenhuma funcionalidade. Não consegui fazer a implementação da funcionalidade por questão de tempo.

#### Testes

Foram criados testes funcionais, para testar tanto a service quanto o repositório. Eles estão armazenados na src/test/java.

#### Comentários

O projeto tem um ciclo de vida diretamente relacionado com a API do pokemon, visto que as requests são feitas para obter os dados de lá. 
No entanto, o código foi bem documentado afim de que facilite o entendimento de alguém que venha manusear futuramente e conta com interfaces, que facilitam a manutenção do código.