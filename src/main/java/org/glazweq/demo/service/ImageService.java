package org.glazweq.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glazweq.demo.domain.MovieCard;
import org.glazweq.demo.domain.Poster;
import org.glazweq.demo.repos.PosterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    private ObjectMapper objectMapper;
    private PosterRepo posterRepo;
    private ApiKinopoiskDevService apiKinopoiskDevService;
    @Autowired
    public void FilmApiService(ObjectMapper objectMapper, PosterRepo posterRepo, ApiKinopoiskDevService apiKinopoiskDevService) {

        this.objectMapper = objectMapper;
        this.posterRepo = posterRepo;
        this.apiKinopoiskDevService = apiKinopoiskDevService;
    }
    private static final  String apiName = "https://api.kinopoisk.dev/v1.4/movie";

    @Cacheable(cacheNames = "posters", key = "#chaoticKeywords.toString()")
    public List<Poster> getPostersByKeyword(int limit, List<String> chaoticKeywords, String keyword) throws JsonProcessingException {


//        &lists=top250

        List<Poster> allPosters = new ArrayList<>();

        for (String chaoticKeyword : chaoticKeywords) {
            String keywordForDB = null;
//            parse keywords
            if (keyword.equals("genre")){
                keywordForDB = getGoodGenreForApi(chaoticKeyword);
                System.out.println("genre LIST: "+ chaoticKeyword);
            }
             else if (keyword.equals("top250")) {
                 keywordForDB = chaoticKeyword;
             }
            //достать нужное кол из дб
            int countFindPosters = 0;
            List<Poster> posters = posterRepo.findByKeyword(keywordForDB);
            for (Poster poster : posters) {
                countFindPosters++;
                allPosters.add(poster);
            }
            System.out.println(countFindPosters + "------------------");
            if (countFindPosters < limit) {
                int numberOfPostersNeeded = limit - countFindPosters;
                allPosters = postersRequestAndSaveInDB(numberOfPostersNeeded, keywordForDB, keyword, allPosters, chaoticKeyword);
            }
        }


        return allPosters;
    }


//for one keyword
    @Cacheable(cacheNames = "posters", key = "#chaoticKeyword")
    public List<Poster> getPostersByKeyword(int limit, String chaoticKeyword, String keyword) throws JsonProcessingException {
        System.out.println("v250top");
        List<Poster> allPosters = new ArrayList<>();


            String keywordForDB = null;
//            parse keywords
            if (keyword.equals("genre")){

                keywordForDB =getGoodGenreForApi(chaoticKeyword);
                System.out.println("genre STRING: " + chaoticKeyword);
            }
            else if (keyword.equals("top250")) {
                keywordForDB = chaoticKeyword;
            }
             System.out.println("keywordForDB: "+keywordForDB);
            //достать нужное кол из дб
            int countFindPosters = 0;
            List<Poster> posters = posterRepo.findByKeyword(keywordForDB);
            for (Poster poster : posters) {
                countFindPosters++;
                allPosters.add(poster);
            }

            if (countFindPosters < limit) {
                int numberOfPostersNeeded = limit - countFindPosters;
                allPosters = postersRequestAndSaveInDB(numberOfPostersNeeded, keywordForDB, keyword, allPosters, null);
            }
        return allPosters;
    }
    private List<Poster> postersRequestAndSaveInDB(int limit,String keywordForDB, String keyword, List<Poster> allPosters, String enKeyword) throws JsonProcessingException {

        String requestUrl = UriComponentsBuilder.fromHttpUrl(apiName)
                .queryParam("page", 1)
                .queryParam("limit", limit)
                .queryParam("selectFields","id")
                .queryParam("selectFields", "poster")
                .toUriString();


        if (keyword.equals("genre")) {
            requestUrl = requestUrl.concat("&genres.name=" + keywordForDB);
            System.out.println("genres--------------    ");
        }
        if (keyword.equals("top250")) {
            requestUrl = UriComponentsBuilder.fromUriString(requestUrl)
                    .queryParam("lists", keywordForDB)
                    .toUriString();
        }
        System.out.println("keyword " + keyword);
        System.out.println("keywordforDB " + keywordForDB);
        System.out.println(requestUrl);
        JsonNode jsonNode = apiKinopoiskDevService.getResponseFromApi(requestUrl);

        JsonNode docsNode = jsonNode.get("docs"); // Получаем узел "docs"
        for (JsonNode posterNode : docsNode) {
            int idMovie = posterNode.get("id").asInt();
            String previewImg = posterNode.get("poster").get("previewUrl").asText();
            Poster poster = new Poster(idMovie, previewImg, keywordForDB, enKeyword);
            System.out.println("poster saving...");
            posterRepo.save(poster);
            allPosters.add(poster);
        }
        return allPosters;
    }
    private void savePostersInDB(Poster poster) {
        posterRepo.save(poster);
    }
    public String getGoodGenreForApi(String genre) {
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
