package org.glazweq.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.glazweq.demo.domain.MovieCard;
import org.glazweq.demo.domain.MoviePage;
import org.glazweq.demo.domain.MoviePoster;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class FilmApiService {
    private static final  String apiName = "https://api.kinopoisk.dev/v1.4/";
    private static final  String firstPartUrl = "https://api.kinopoisk.dev/v1.4/movie";
    /*
    keys: D386SAK-6YR4GXR-KYNQ26E-0JHQX0C batya
          H1CX9MG-T83MTC4-PPV7CQE-SYP1474 my
          90T8CSE-7P34EBK-HHTP44M-DA7KSDG carhartt
          27AG916-69D40PB-N6WACS4-H90QVSE kolyan
          H659JZ2-ZV64B13-QQK91XR-T33YW3Q mama
          8HK60K1-GN54HEM-NA6DYES-5HQBVWJ vitalya
          Y8EGHT0-CSZ4PGE-QZ82S6C-KN2CVA7 sonya
     */
    private static final String header1 = "D386SAK-6YR4GXR-KYNQ26E-0JHQX0C";
    private static final String header2 = "application/json";

    @Autowired
    private RestTemplate restTemplate;
    public String getResponceByRequest(String request){
        try {
            //header value is set
            HttpHeaders headers = new HttpHeaders();

            headers.set("X-API-KEY", header1);
            headers.set("accept", header2);
            //make GET  call
            ResponseEntity<String> response = restTemplate.exchange(request, HttpMethod.GET,new HttpEntity<>(headers),String.class);

            log.info("Output from KinoApi:{}", response.getBody());

            return response.getBody();

        }catch (Exception e){
            log.error("something went wrong while getting value from KINOAPI", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Exception while calling end poing from KinoApi",
                    e
            );
        }
    }

    //досьаем кол фильмов из апи
    public int getTotalFilmInApi(JsonNode rootNode) throws JsonProcessingException {
        //String urlSecondPart = getUrlForApi();
        return Integer.parseInt(rootNode.get("total").asText());

    }
    @Cacheable(cacheNames = "apiResponseCache", key = "#requestFromController")
    public JsonNode getResponseFromApi(String requestFromController) throws JsonProcessingException {
        String answerFromApi = getResponceByRequest(requestFromController);
        System.out.println("get Response from api");
        return JsonDecoderService.parse(answerFromApi);
    }
    public MoviePage getMoviePage(JsonNode movieNode){

        int id = movieNode.get("id").asInt();
        String name = movieNode.get("name").asText();
        String previewImg = movieNode.get("poster").get("previewUrl").asText();
        String backdropImg = movieNode.get("backdrop").get("url").asText();
        double ratingKinopoisk = movieNode.get("rating").get("kp").asDouble();
        double ratingImdb = movieNode.get("rating").get("imdb").asDouble();
        String description = movieNode.get("description").asText();
        int reliesYear = movieNode.get("year").asInt();
        MoviePage moviePage = new MoviePage(id, name, previewImg, ratingKinopoisk, ratingImdb, description , reliesYear, backdropImg);
        return  moviePage;
    }
    //dostaem List filmov тут dеlаем page
    public List<MovieCard> getMoviesList( JsonNode rootNode) throws JsonProcessingException {
        JsonNode docsNode = rootNode.get("docs"); // Получаем узел "docs"

        List<MovieCard> moviesCards = new ArrayList<>();

        if (docsNode != null && docsNode.isArray()) {
            for (JsonNode movieNode : docsNode) {
                int id = movieNode.get("id").asInt();
                String name = movieNode.get("name").asText();
                String previewImg = movieNode.get("poster").get("previewUrl").asText();
                double ratingKinopoisk = movieNode.get("rating").get("kp").asDouble();
                double ratingImdb = movieNode.get("rating").get("imdb").asDouble();
                String description = movieNode.get("description").asText();
                int reliesYear = movieNode.get("year").asInt();
                moviesCards.add(new MovieCard(id, name, previewImg, ratingKinopoisk, ratingImdb, description , reliesYear));

            }
        }

        return moviesCards;
    }

public  String getResponseAndNotNullFields(){
    String responseFields = "&selectFields=id&selectFields=description&selectFields=rating&selectFields=enName&selectFields=name&selectFields=releaseYears&selectFields=poster&selectFields=year";
    String notNullFields = "&notNullFields=name&notNullFields=poster.url";
    return responseFields + notNullFields;
}

    public String getUrlForApi(int currentPage, int productPerPage, String genre, String yearRange){
        String filmsOnPage = "?page="+ currentPage +"&limit="+ productPerPage;
        //List of fields required in the response from the model
        String sortType = "&sortType=-1";
        String defaultRequest = "&selectFields=votes&sortField=votes.imdb" + sortType;
        String fullUrl;
        fullUrl = firstPartUrl + filmsOnPage + getResponseAndNotNullFields();
        System.out.println("222222222222222222222"+genre);
        System.out.println("year:"+ yearRange);
        if (!Objects.equals(genre, "any") && genre != null) {
            String filterByGenre ="&genres.name=" + getRequestByGenre(genre);
             fullUrl = fullUrl.concat(filterByGenre);

        }
        if (!Objects.equals(yearRange, "any") && yearRange != null) {
            String filterByYears ="&year=" + yearRange;
            fullUrl = fullUrl.concat(filterByYears);
            System.out.println(filterByYears);

        }

        fullUrl = fullUrl.concat(defaultRequest);
        System.out.println(fullUrl);
        return fullUrl;
    }


//    for home

    public String get27PostersUrl(){
        return apiName + "movie?page=1&limit=27&selectFields=poster&lists=top250";
    }
    @Cacheable(cacheNames = "posterCache", key = "#rootNode")
    public List<MoviePoster> getPostersList(JsonNode rootNode){
        JsonNode docsNode = rootNode.get("docs"); // Получаем узел "docs"
        System.out.println("posters cache");


        List<MoviePoster> moviesPosters = new ArrayList<>();
        if (docsNode != null && docsNode.isArray()) {
            for (JsonNode movieNode : docsNode) {
                String previewImg = movieNode.get("poster").get("previewUrl").asText();
                moviesPosters.add(new MoviePoster(previewImg));
            }
        }
        return moviesPosters;
    }


    //get request by genre
    public String getRequestByGenre(String genre){
        return switch (genre) {
            case "any" -> "any";
            case "action" -> "боевик";
            case "adventure" -> "приключения";
            case "adult" -> "для взрослых";
            case "anime" -> "аниме";
            case "biography" -> "биография";
            case "cartoon" -> "мультфильм";
            case "ceremony" -> "церемония";
            case "children's" -> "детский";
            case "comedy" -> "комедия";
            case "concert" -> "концерт";
            case "crime" -> "криминал";
            case "detective" -> "детектив";
            case "documentary" -> "документальный";
            case "drama" -> "драма";
            case "family" -> "семейный";
            case "fantasy" -> "фэнтези";
            case "film noir" -> "фильм-нуар";
            case "game" -> "игра";
            case "history" -> "история";
            case "horror" -> "ужасы";
            case "melodrama" -> "мелодрама";
            case "music" -> "музыка";
            case "musical" -> "мюзикл";
            case "news" -> "новости";
            case "reality TV" -> "реальное ТВ";
            case "short film" -> "короткометражка";
            case "sport" -> "спорт";
            case "talk show" -> "ток-шоу";
            case "thriller" -> "триллер";
            case "war" -> "военный";
            case "western" -> "вестерн";
            default -> "Неизвестный жанр";
        };


    }

}
