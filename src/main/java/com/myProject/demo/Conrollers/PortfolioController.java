package com.myProject.demo.Conrollers;

import com.myProject.demo.DTO.PortfolioRequest;
import com.myProject.demo.DTO.PortfolioResponse;
import com.myProject.demo.Services.PortfolioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portfolios")
@CrossOrigin(origins = "http://localhost:4200")
public class PortfolioController {


    @Autowired
    private PortfolioService portfolioService;


    @PostMapping("/add")
    public ResponseEntity<PortfolioResponse> createPortfolio(@Valid @RequestBody PortfolioRequest request) {
        PortfolioResponse response = portfolioService.CreatePortfolio(request);
        return ResponseEntity.ok(response);
    }



    @GetMapping("/user/{username}")
    public ResponseEntity<PortfolioResponse> getPortfolioByUsername(@PathVariable String username) {
        PortfolioResponse response = portfolioService.getPortfolioByUsername(username);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/all")
    public ResponseEntity<List<PortfolioResponse>> getAllPortfolios() {
        List<PortfolioResponse> list = portfolioService.getAllPortfolios();
        return ResponseEntity.ok(list);
    }


    @PutMapping("/edit/{username}")
    public ResponseEntity<PortfolioResponse> updatePortfolio(@PathVariable String username,
                                                            @Valid @RequestBody PortfolioRequest request) {
        PortfolioResponse response = portfolioService.updatePortfolio(username, request);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePortfolio(@PathVariable Long id) {
        portfolioService.deletePortfolio(id);
        return ResponseEntity.ok("Portfolio deleted successfully");
    }
}
