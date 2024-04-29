package org.glazweq.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.glazweq.demo.domain.*;
import org.glazweq.demo.repos.GenreRepo;
import org.glazweq.demo.repos.MovieCardRepo;
import org.glazweq.demo.repos.PosterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class FiltersMovieService {
    private ObjectMapper objectMapper;
    private MovieCardRepo movieCardRepo;
    private GenreRepo genreRepo;
    private ApiKinopoiskDevService apiKinopoiskDevService;

    @Autowired
    public void FilmApiService( ObjectMapper objectMapper, MovieCardRepo movieCardRepo, ApiKinopoiskDevService apiKinopoiskDevService, GenreRepo genreRepo) {

        this.objectMapper = objectMapper;
        this.movieCardRepo = movieCardRepo;
        this.apiKinopoiskDevService = apiKinopoiskDevService;
        this.genreRepo = genreRepo;
    }



    private static final  String apiName = "https://api.kinopoisk.dev/v1.4/";
    private static final  String firstPartUrl = "https://api.kinopoisk.dev/v1.4/movie";
    //досьаем кол фильмов из апи
    public int getTotalFilmInApi(JsonNode rootNode) throws JsonProcessingException {
        //String urlSecondPart = getUrlForApi();
        return Integer.parseInt(rootNode.get("total").asText());

    }
//    public MoviePage getMoviePage(JsonNode movieNode){
//
//        int id = movieNode.get("id").asInt();
//        String name = movieNode.get("name").asText();
//        String previewImg = movieNode.get("poster").get("previewUrl").asText();
//        String backdropImg = movieNode.get("backdrop").get("url").asText();
//        double ratingKinopoisk = movieNode.get("rating").get("kp").asDouble();
//        double ratingImdb = movieNode.get("rating").get("imdb").asDouble();
//        String description = movieNode.get("description").asText();
//        int reliesYear = movieNode.get("year").asInt();
//        MoviePage moviePage = new MoviePage(id, name, previewImg, ratingKinopoisk, ratingImdb, description , reliesYear, backdropImg);
//        return  moviePage;
//    }
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
            String filterByGenre ="&genres.name=" + getGoodGenre(genre);
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


    @Cacheable(cacheNames = "movieCards", key = "#page + '-' + #limit + '-' + #enGenre + '-' + #yearRange + '-' + #sortType")
    public JsonNode getJsonByFilter(int page, int limit, String enGenre, String yearRange, String sortType) throws JsonProcessingException {
        String keywordGenre = getGoodGenre(enGenre);
        String requestUrl = UriComponentsBuilder.fromHttpUrl(firstPartUrl)
                .queryParam("page", page)
                .queryParam("limit", limit)
                .queryParam("selectFields", "id")
                .queryParam("selectFields", "name")
                .queryParam("selectFields", "genres")
                .queryParam("selectFields", "poster")
                .queryParam("selectFields", "rating")
                .queryParam("selectFields", "description")
                .queryParam("selectFields", "year")
                .queryParam("notNullFields", "poster.url")
                .queryParam("notNullFields", "rating.kp")
                .queryParam("notNullFields", "description")
                .queryParam("notNullFields", "year")
                .toUriString();

        if (!Objects.equals(keywordGenre, "Неизвестный жанр")) {
            requestUrl = requestUrl.concat("&genres.name=" + keywordGenre);
        }
        System.out.println("genres--------------    ");
        if (!yearRange.equals("notIndicated")) {
            requestUrl = requestUrl.concat("&year=" + yearRange);
        }
        if (!sortType.equals("notIndicated")) {
//            sortField=votes.kp&sortType=-1
            if (sortType.equals("Kinopoisk"))  requestUrl = requestUrl.concat("&sortField=votes.kp&sortType=-1");
            if (sortType.equals("Imdb"))  requestUrl = requestUrl.concat("&sortField=votes.imdb&sortType=-1");
            if (sortType.equals("Top250"))  requestUrl = requestUrl.concat("&sortField=top250&sortType=-1");
        }
        System.out.println(requestUrl);
        JsonNode jsonNode = apiKinopoiskDevService.getResponseFromApi(requestUrl);
        return jsonNode;
    }
    public int getTotalMoviesByFilters(JsonNode jsonNode){
        return jsonNode.get("total").asInt();
    }
    @Cacheable(cacheNames = "#jsonNode")
    public List<MovieCard> getMoviesCardsByFilter(JsonNode jsonNode) throws JsonProcessingException {
        JsonNode docsNode = jsonNode.get("docs"); // Получаем узел "docs"
        List<MovieCard> allMoviesCard = new ArrayList<>();
        for (JsonNode movieCardNode : docsNode) {
            int movieId = movieCardNode.get("id").asInt();
            String name = movieCardNode.get("name").asText();
            String posterUrl = movieCardNode.get("poster").get("previewUrl").asText();
            JsonNode genresNode = movieCardNode.get("genres");
            double ratingKinopoisk = movieCardNode.get("rating").get("kp").asDouble();
            double ratingImdb = movieCardNode.get("rating").get("imdb").asDouble();
            String description = movieCardNode.get("description").asText();
            int reliesYear = movieCardNode.get("year").asInt();



            MovieCard movieCard = new MovieCard(movieId, name, posterUrl, ratingKinopoisk, ratingImdb, description , reliesYear);
            List<String> genres = new ArrayList<>();
            for (JsonNode genreNode : genresNode) {
                String genreName = genreNode.get("name").asText();
                genres.add(genreName);

            }
            String genresString = String.join(", ", genres);
            movieCard.setGenres(genresString);
            allMoviesCard.add(movieCard);
        }



        return allMoviesCard;
    }
    private List<String> getListWithGenresToMoviePage(JsonNode jsonNode){
        JsonNode genresNode = jsonNode.get("genres");
        List<String> genresList = new ArrayList<>();
        if (genresNode != null && genresNode.isArray()){
            for (JsonNode genreNode : genresNode){
                String genreName = genreNode.get("name").asText();
                genreName = Character.toUpperCase(genreName.charAt(0)) + genreName.substring(1);
                genresList.add(genreName);
            }
        }
        return genresList;
    }
    public MoviePage getMovieById(int movieId) throws JsonProcessingException {
        String urlFindById = "https://api.kinopoisk.dev/v1.4/movie/" + movieId;
        JsonNode movieNode = apiKinopoiskDevService.getResponseFromApi(urlFindById);
        int id = movieNode.get("id").asInt();

        String previewImg = movieNode.get("poster").get("previewUrl").asText();
        String backdropImg = movieNode.get("backdrop").get("url").asText();
        double ratingKinopoisk = movieNode.get("rating").get("kp").asDouble();
        double ratingImdb = movieNode.get("rating").get("imdb").asDouble();
        String description = movieNode.get("description").asText();
        String shortDescription = movieNode.get("shortDescription").asText();
        int reliesYear = movieNode.get("year").asInt();

        String name = movieNode.get("alternativeName").asText();
        if (name.isEmpty() || name == null || name.equals("null")) name = movieNode.get("name").asText();


        List<ManFromMovie> peopleFromMovieList = new ArrayList<>();
        JsonNode personsNode = movieNode.get("persons");
        if (personsNode != null && personsNode.isArray()){
            for (JsonNode personNode : personsNode){
                int personId = personNode.get("id").asInt();
                String personName = personNode.get("enName").asText();
                if (personName.equals("null")) personName = personNode.get("name").asText();
                String profession  = personNode.get("enProfession").asText();
                if (profession.equals("null")) profession = personNode.get("profession").asText();
                profession = Character.toUpperCase(profession.charAt(0)) + profession.substring(1);
                String personPhoto = personNode.get("photo").asText();
                peopleFromMovieList.add(new ManFromMovie(personId, personName, personPhoto, profession));

            }
        }
        MoviePage moviePage = new MoviePage(id, name, previewImg, ratingKinopoisk, ratingImdb, description , reliesYear, backdropImg, shortDescription, peopleFromMovieList);
        moviePage.setGenres(getListWithGenresToMoviePage(movieNode));
        return  moviePage;
    }


    public List<SearchMovie> findMoviesByText(String text) throws JsonProcessingException {
        String urlForApi = firstPartUrl + "/search?page=1&limit=20&query=" + text;
        System.out.println(urlForApi);
        JsonNode moviesNode = apiKinopoiskDevService.getResponseFromApi(urlForApi);
//        /search?page=1&limit=7&query=ds'
        JsonNode docsNode = moviesNode.get("docs");
        List<SearchMovie> moviesList = new ArrayList<>();
        if (docsNode != null && docsNode.isArray()){
            for (JsonNode movieNode : docsNode){
                SearchMovie movie = new SearchMovie();

                String movieName = movieNode.get("alternativeName").asText();
                if (movieName.isEmpty() || movieName == null) movieName = movieNode.get("name").asText();
                if (!movieName.isEmpty() && movieName != null) movie.setName(movieName);

                String moviePreview = movieNode.get("poster").get("previewUrl").asText();
                if (!moviePreview.isEmpty() && moviePreview != null) movie.setPreviewUrl(moviePreview);

                int movieId = movieNode.get("id").asInt();
                movie.setId(movieId);
                System.out.println(movieId + " " + movieName + " " + moviePreview);
                moviesList.add(movie);
            }
        }
        return moviesList;
    }

    //get request by genre
    public String getGoodGenre(String genre){
        return switch (genre) {
            case "any" -> "any";
            case "Action" -> "боевик";
            case "Adventure" -> "приключения";
            case "Adult" -> "для взрослых";
            case "Anime" -> "аниме";
            case "Biography" -> "биография";
            case "Cartoon" -> "мультфильм";
            case "Ceremony" -> "церемония";
            case "Children's" -> "детский";
            case "Comedy" -> "комедия";
            case "Concert" -> "концерт";
            case "Crime" -> "криминал";
            case "Detective" -> "детектив";
            case "Documentary" -> "документальный";
            case "Drama" -> "драма";
            case "Family" -> "семейный";
            case "Fantasy" -> "фэнтези";
            case "Film noir" -> "фильм-нуар";
            case "Game" -> "игра";
            case "History" -> "история";
            case "Horror" -> "ужасы";
            case "Melodrama" -> "мелодрама";
            case "Music" -> "музыка";
            case "Musical" -> "мюзикл";
            case "News" -> "новости";
            case "Reality TV" -> "реальное ТВ";
            case "Short film" -> "короткометражка";
            case "Sport" -> "спорт";
            case "Talk show" -> "ток-шоу";
            case "Thriller" -> "триллер";
            case "War" -> "военный";
            case "Western" -> "вестерн";
            default -> "Неизвестный жанр";
        };


    }

}
