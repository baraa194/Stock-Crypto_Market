package com.myProject.demo.Conrollers;

import com.myProject.demo.DTO.PortfolioRequest;
import com.myProject.demo.DTO.PortfolioResponse;
import com.myProject.demo.Services.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portfolios")
public class PortfolioController {


    @Autowired
    private PortfolioService portfolioService;


    @PostMapping("/add")
    public ResponseEntity<PortfolioResponse> createPortfolio(@RequestBody PortfolioRequest request) {
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


    @PutMapping("/edit/{id}")
    public ResponseEntity<PortfolioResponse> updatePortfolio(@PathVariable Long id,
                                                             @RequestBody PortfolioRequest request) {
        PortfolioResponse response = portfolioService.updatePortfolio(id, request);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePortfolio(@PathVariable Long id) {
        portfolioService.deletePortfolio(id);
        return ResponseEntity.ok("Portfolio deleted successfully");
    }
}
