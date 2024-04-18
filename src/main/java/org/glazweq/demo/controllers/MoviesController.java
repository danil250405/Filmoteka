package org.glazweq.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.glazweq.demo.domain.MovieCard;

import org.glazweq.demo.domain.MoviePage;
import org.glazweq.demo.domain.Poster;
import org.glazweq.demo.service.ApiKinopoiskDevService;
import org.glazweq.demo.service.FiltersMovieService;
import org.glazweq.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@EnableCaching
public class MoviesController {
    public int productPerPage = 24;
    private final FiltersMovieService filtersMovieService;
    private final  ImageService imageService;
    private final ApiKinopoiskDevService apiKinopoiskDevService;


//    constructor
    public List<String> getAllGenres(){
        List<String> genres = Arrays.asList(
                "Action", "Adventure", "Anime", "Biography", "Cartoon", "Comedy", "Concert", "Crime",
                "Detective", "Drama", "Fantasy",  "History", "Horror", "Melodrama", "Music",
                "Musical", "Sport","Thriller", "War", "Western"

        );
        return genres;
    }
    public List<String> getAllYearRanges(){
        List<String> yearRanges = new ArrayList<>();
        for (int year = 1960; year <= 2020; year += 10) {
            int endYear = year + 9;
            yearRanges.add(year + "-" + endYear);
        }
        return  yearRanges;
    }
    public List<String> getAllSortTypes(){
        List<String> sortTypes = Arrays.asList(
                "Kinopoisk", "Top250", "Imdb"
        );
        return sortTypes;
    }
    public void getAndSetUserRole(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String roleAuthUser;
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            roleAuthUser = "admin";
        } else {
            roleAuthUser = "guest";
        }
        model.addAttribute("userRole", roleAuthUser);
    }
     @Autowired
     public MoviesController(@Qualifier("imageService") ImageService imageService,
                             @Qualifier("filtersMovieService") FiltersMovieService filtersMovieService,
                             @Qualifier("apiKinopoiskDevService") ApiKinopoiskDevService apiKinopoiskDevService) {
            this.imageService = imageService;
            this.filtersMovieService = filtersMovieService;
            this.apiKinopoiskDevService = apiKinopoiskDevService;

     }
    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }
    @Cacheable(value = "totalFilmsInApiCache")
    public int getTotalFilmsInApi(JsonNode jsonNode) throws JsonProcessingException {
        return filtersMovieService.getTotalFilmInApi(jsonNode);
    }
    @PostMapping("/home-page")
    public String postHomePage(@RequestParam(defaultValue = "1") int page, @RequestParam("genre") String selectedGenre,
                              @RequestParam("yearRange") String selectedYearRange,  Model model){

        return "redirect:/home-page?genre=" + selectedGenre + "&year=" + selectedYearRange;

    }
    @GetMapping("/movieId={id}")
    public String showMoviePage(@PathVariable("id") int movieId, Model model) throws JsonProcessingException {

        MoviePage moviePage = filtersMovieService.getMovieById(movieId);
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("backImg", moviePage.getBackdrop());


        getAndSetUserRole(model);

        return "movie-page"; // Имя представления (HTML-шаблона)
    }

    @GetMapping("/home")
    public String home(Model model) throws JsonProcessingException {
        System.out.println("now works 'home' method");
//        take 27 posters from api
        System.out.println("pered vchodom v250top");
        List<Poster> postersForHero =  imageService.getPostersByKeyword(27, "top250","top250");
        model.addAttribute("postersForHero", postersForHero);

//        get genres list here full genres
        List<String> genres = getAllGenres();
        getAndSetUserRole(model);
        model.addAttribute("genres", genres);
        List<Poster> postersByGenres =  imageService.getPostersByKeyword(4, genres,"genre");
        model.addAttribute("postersByGenres", postersByGenres);
        return "home";
    }
    @PostMapping("/movies")
    public String postMoviesPage( @RequestParam("genre") String selectedGenre,
                                  @RequestParam("yearRange") String selectedYearRange,
                                  @RequestParam("sortType") String selectedSortType){
        String url = "movies";
        if (!selectedGenre.equals("---") || !selectedYearRange.equals("---")){
            url = url.concat("?");
            if (!selectedGenre.equals("---")) url = url.concat("genre=" + selectedGenre + "&");
            if (!selectedYearRange.equals("---")) url = url.concat("yearRange=" + selectedYearRange + "&");
            if (!selectedSortType.equals("---")) url = url.concat("sortType=" + selectedSortType + "&");
        }
        return "redirect:/" + url;
    }
    @GetMapping("/movies")
    public String getFilteredMovies(HttpServletRequest request, @RequestParam(defaultValue = "1") int page,
                                      Model model) throws JsonProcessingException {
        // Здесь вы должны реализовать логику для получения фильмов по выбранному жанру
        List<String> genres = getAllGenres();
        List<String> yearRanges = getAllYearRanges();
        List<String> sortTypes = getAllSortTypes();
        String selectedGenre = request.getParameter("genre");
        String selectedYearRange = request.getParameter("yearRange");
        String selectedSortType = request.getParameter("sortType");
        if (selectedGenre == null || selectedGenre.isEmpty())  selectedGenre = "notIndicated";
        if (selectedYearRange == null || selectedYearRange.isEmpty()) selectedYearRange = "notIndicated";
        if (selectedSortType == null || selectedSortType.isEmpty()) selectedSortType = "notIndicated";

        getAndSetUserRole(model);

        model.addAttribute("selectedSortType", selectedSortType);
        model.addAttribute("selectedYearRange", selectedYearRange);
        model.addAttribute("selectedGenre", selectedGenre);
        JsonNode jsonNode = filtersMovieService.getJsonByFilter(page, productPerPage, selectedGenre, selectedYearRange ,selectedSortType);
      List<MovieCard> filteredMovies = filtersMovieService.getMoviesCardsByFilter(jsonNode);
        int totalFilmsInApi = filtersMovieService.getTotalMoviesByFilters(jsonNode);
        model.addAttribute("movies", filteredMovies);

        model.addAttribute("genres", genres);
        model.addAttribute("yearRanges", yearRanges);
        model.addAttribute("sortTypes", sortTypes);

//        for pagination
        String baseUrl = "/movies?";
        if (!selectedGenre.equals("notIndicated")) {
            baseUrl += "genre=" + selectedGenre + "&";
        }
        if (!selectedYearRange.equals("notIndicated")) {
            baseUrl += "yearRange=" + selectedYearRange + "&";
        }
        if (!selectedSortType.equals("notIndicated")) {
            baseUrl += "sortType=" + selectedSortType + "&";
        }
        baseUrl += "page=";
        int pagesAmount = totalFilmsInApi / productPerPage;
        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalItems", totalFilmsInApi);
        model.addAttribute("pageSize", productPerPage);
        model.addAttribute("totalPages", pagesAmount); // max page
        return "filters-page";
    }
    @GetMapping("/home-page")
    public String getCardsList(Model model, @RequestParam(defaultValue = "1") int page, HttpServletRequest request)
            throws JsonProcessingException {
        System.out.println("now works getCardsList");
        // Получение значения параметра genre из URL
        String selectedGenre = request.getParameter("genre");
        String selectedYearRange = request.getParameter("year");

        // проверка если наши фильтры не заданы то будут равны нулю
        if (selectedGenre == null || selectedGenre.isEmpty()) {
            selectedGenre = "any"; // Значение по умолчанию, если параметр не задан
        }
        if (selectedYearRange == null || selectedYearRange.isEmpty()) {
            selectedYearRange = "any"; // Значение по умолчанию, если параметр не задан
        }

        model.addAttribute("selectedYearRange", selectedYearRange);
        model.addAttribute("selectedGenre", selectedGenre);


        // list with genres
        List<String> genres = Arrays.asList(
                "any", "action", "adventure", "adult", "anime", "biography", "cartoon", "ceremony", "children's", "comedy", "concert", "crime",
                "detective", "documentary", "drama", "family", "fantasy", "film noir", "game", "history", "horror", "melodrama", "music",
                "musical", "news", "reality TV", "short film", "sport", "talk show", "thriller", "war", "western");
        model.addAttribute("genres", genres);

        //range year
        List<String> yearRanges = new ArrayList<>();
        yearRanges.add("any");
        for (int year = 1960; year <= 2020; year += 10) {
            int endYear = year + 9;
            yearRanges.add(year + "-" + endYear);
        }
        model.addAttribute("yearRanges", yearRanges);

        String requestUrl;
        if ((selectedGenre == null || selectedGenre.equals("any")) && (selectedYearRange == null || selectedYearRange.equals("any"))) {
            // Если ни жанр, ни диапазон лет не заданы, получаем список всех фильмов без фильтрации
            requestUrl = filtersMovieService.getUrlForApi(page, productPerPage, null, null);
            System.out.println("1");
        } else if ((selectedGenre != null || !selectedGenre.equals("any")) && (selectedYearRange == null || selectedYearRange.equals("any"))) {
            // Если задан жанр, но не задан диапазон лет
            requestUrl = filtersMovieService.getUrlForApi(page, productPerPage, selectedGenre, null);
            System.out.println("2");
        } else if ((selectedGenre == null || selectedGenre.equals("any")) && (selectedYearRange != null || !selectedYearRange.equals("any"))) {
            // Если не задан жанр, но задан диапазон лет
            requestUrl = filtersMovieService.getUrlForApi(page, productPerPage, null, selectedYearRange);
            System.out.println("3");
        } else {
            // Если заданы и жанр, и диапазон лет
            requestUrl = filtersMovieService.getUrlForApi(page, productPerPage, selectedGenre, selectedYearRange);
            System.out.println("4");
        }

        //take full json from api
        JsonNode responseJson = apiKinopoiskDevService.getResponseFromApi(requestUrl);
        //take just branch total = how many films by this request in the api
        int totalFilmsInApi = getTotalFilmsInApi(responseJson);
        int pagesAmount = totalFilmsInApi / productPerPage;



        //take just branch docs = take card with movie
        List<MovieCard> movies = filtersMovieService.getMoviesList(responseJson);

        // list with films
        model.addAttribute("movies", movies);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //just check role user
        String roleAuthUser;
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            roleAuthUser = "admin";
        } else {
            roleAuthUser = "guest";
        }
        model.addAttribute("userRole", roleAuthUser);

        //generate url
        String baseUrl = "/home-page?";
        if (!selectedGenre.equals("any")) {
            baseUrl += "genre=" + selectedGenre + "&";
        }
        if (!selectedYearRange.equals("any")) {
            baseUrl += "year=" + selectedYearRange + "&";
        }
        baseUrl += "page=";

        model.addAttribute("baseUrl", baseUrl);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalItems", totalFilmsInApi);
        model.addAttribute("pageSize", productPerPage);
        model.addAttribute("totalPages", pagesAmount); // max page
        return "home-page";
    }
}
