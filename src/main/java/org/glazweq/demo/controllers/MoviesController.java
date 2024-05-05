package org.glazweq.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.glazweq.demo.domain.*;

import org.glazweq.demo.repos.ReviewRepo;
import org.glazweq.demo.repos.UserRepo;
import org.glazweq.demo.service.*;
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
    private final ReviewService reviewService;
    private final UserServiceImpl userServiceImpl;

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
        } else if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            roleAuthUser = "user";
        } else {
            roleAuthUser = "guest";
        }
        model.addAttribute("userRole", roleAuthUser);
    }
     @Autowired
     public MoviesController(@Qualifier("imageService") ImageService imageService,
                             @Qualifier("filtersMovieService") FiltersMovieService filtersMovieService,
                             @Qualifier("apiKinopoiskDevService") ApiKinopoiskDevService apiKinopoiskDevService,
                             @Qualifier("reviewService")ReviewService reviewService,
                             @Qualifier("userServiceImpl") UserServiceImpl userServiceImpl) {
            this.imageService = imageService;
            this.filtersMovieService = filtersMovieService;
            this.apiKinopoiskDevService = apiKinopoiskDevService;

         this.reviewService = reviewService;
         this.userServiceImpl = userServiceImpl;
     }
    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }
    @Cacheable(value = "totalFilmsInApiCache")
    public int getTotalFilmsInApi(JsonNode jsonNode) throws JsonProcessingException {
        return filtersMovieService.getTotalFilmInApi(jsonNode);
    }

    @GetMapping("/movieId={id}")
    public String showMoviePage(@PathVariable("id") int movieId, Model model) throws JsonProcessingException {
        System.out.println("in show movie page method");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User authUser = userServiceImpl.findUserByEmail(currentPrincipalName);
        model.addAttribute("authUser", authUser);
        MoviePage moviePage = filtersMovieService.getMovieById(movieId);

//        add reviews on page
        List<Review> reviewsList = reviewService.getReviewsByMovieId(movieId);
        if (authUser != null){
            Review userReview = reviewService.opportunityToLeaveFeedback(authUser, reviewsList);
            model.addAttribute("userReview", userReview);
        }

        double streamVibeRating = reviewService.getAverageSVRating(reviewsList);
        if ( streamVibeRating != -1){
            moviePage.setStreamVibeRating(streamVibeRating);
        }
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("backImg", moviePage.getBackdrop());
        model.addAttribute(reviewsList);
        model.addAttribute("reviewListSize", reviewsList.size());

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
    @PostMapping("/find-movie")
    public String findMovies(@RequestParam String searchText,Model model){
//        List<MovieCard> movies = movieService.findFilmsByText(searchText);
//        model.addAttribute("movies", movies);
        return null;
    }

}
