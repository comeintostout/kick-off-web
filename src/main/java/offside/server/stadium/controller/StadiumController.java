package offside.server.stadium.controller;

import java.io.IOException;

import offside.server.stadium.service.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class StadiumController {
    private final StadiumService stadiumService;

    @Autowired
    public StadiumController(StadiumService stadiumService) {
        this.stadiumService = stadiumService;
    }
    @GetMapping("/home")
    public String HomePage(Model model) {
    
        return "SearchHomePage";
    }
    
    @GetMapping("/search")
    public String StadiumSearchWeb(Model model, @RequestParam("location") String location) {
        if (location.isEmpty() || location.equals("전체"))
            location = "";
        var tmpStadiumList = stadiumService.requestStadium(location,"");
        if (tmpStadiumList.size() > 30)
            tmpStadiumList = tmpStadiumList.subList(0,30);
        
        final var stadiumList = tmpStadiumList.stream().filter(stadium -> stadium.getExternalUrl().length() > 5).toList();
        model.addAttribute("stadiumList", stadiumList);
        model.addAttribute("location", location.equals("") ? "전체" : location);
        model.addAttribute("outputCnt", stadiumList.size());
        return "StadiumSearchPage";
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }
    
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIllegalStateException(IOException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }
}
