package org.glazweq.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glazweq.demo.domain.Poster;
import org.glazweq.demo.repos.PosterRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private ObjectMapper objectMapper;
    private PosterRepo posterRepo;
    ApiKinopoiskDevService apiKinopoiskDevService;
    @Autowired
    public void FilmApiService(ObjectMapper objectMapper, PosterRepo posterRepository) {

        this.objectMapper = objectMapper;
        this.posterRepo = posterRepository;
    }
    private static final  String apiName = "https://api.kinopoisk.dev/v1.4/movie";
    public List<String> getPostersByGenres(String genre, int limit) {
        List<Poster> posters = posterRepo.findByGenre(genre);
        if (posters.size() >= limit) {
            return posters.stream()
                    .map(Poster::getImageUrl)
                    .collect(Collectors.toList());
        } else {
            String url =
            JsonNode jsonNode = apiKinopoiskDevService.getResponseFromApi();
            List<String> imageUrls = getImageUrlsFromApi(genre, limit - posters.size());
            savePosters(genre, imageUrls);
            return posters.stream()
                    .map(Poster::getImageUrl)
                    .collect(Collectors.toList())
                    .stream()
                    .concat(imageUrls.stream())
                    .collect(Collectors.toList());
        }
    }



    public String getUrlForApi( int limit, List<String> genres){

        for (String genre : genres) {
            String parseGenre = getGoodGenreForApi(genre);
            String requestUrl = UriComponentsBuilder.fromHttpUrl(apiName)
                    .queryParam("page", 1)
                    .queryParam("limit", limit)
                    .queryParam("selectFields", "poster")
                    .queryParam("genres.name", parseGenre)
                    .toUriString();
            System.out.println(requestUrl);
        }
        return null;
    }


    public String getGoodGenreForApi(String genre){
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
