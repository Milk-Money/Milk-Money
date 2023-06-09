package com.milkmoney.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.milkmoney.Repositories.PoliticianRepository;
import com.milkmoney.Repositories.UserRepository;
import com.milkmoney.Services.APIService;
import com.milkmoney.models.Politician;
import com.milkmoney.models.Trade;
import com.milkmoney.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;


import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    private final UserRepository userDAO;
    private final PoliticianRepository politicianDAO;
    private APIService api;

    public HomeController(PoliticianRepository politicianDAO, UserRepository userDAO, APIService api) {
        this.politicianDAO = politicianDAO;
        this.userDAO = userDAO;
        this.api = api;
    }
    @GetMapping("/")
    public String returnHome() {

        return "redirect:/index";
    }


    @GetMapping("/index")
    public String returnUser(Authentication authentication, Model model) throws JsonProcessingException {
        List<Trade> recentTrades = new ArrayList<>();
        List<Trade> trades = api.getTrades();
        List<String> tradeNames = new ArrayList<>();
        int f = 0;


        if (authentication != null && authentication.isAuthenticated()) {
            User currentUser = (User) authentication.getPrincipal();

//            if(!userDAO.existsById(currentUser.getId())){
//                SecurityContextHolder.clearContext();
//
//            }
//            currentUser = (User) authentication.getPrincipal();
            User fixedUser = userDAO.findByUsername(currentUser.getUsername());
            List<String> names = new ArrayList<>();
        if(fixedUser != null) {
            for (Politician p : fixedUser.getPoliticians()) {
                names.add(p.getName());
            }
            model.addAttribute("names", names);
        }
        } else {
            System.out.println("logged out");
        }

        for (Trade t : trades) {
            if (f <= 20) {
                if (!tradeNames.contains(t.getPolitician().getName())) {
                    recentTrades.add(t);
                    tradeNames.add(t.getPolitician().getName());
                    f++;
                }
            } else {
                break;
            }
        }

        model.addAttribute("trades",recentTrades);

        return "index-user";

    }

    @PostMapping("/index")
    public String addFavorite(@RequestParam("pol_id") String name, @RequestParam("follow-btn") boolean follow) {

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User fixedUser = userDAO.findById(currentUser.getId()).get();
        boolean isPresent = false;
        if(follow){
            for(Politician p : fixedUser.getPoliticians()){
                if(p.getName().equals(name)){
                    isPresent = true;
                    break;
                }
            }
            if(!isPresent){
                fixedUser.addPolitician(politicianDAO.findByName(name));
                userDAO.save(fixedUser);
            }
        }else{
            fixedUser.removePolitician(politicianDAO.findByName(name));
            userDAO.save(fixedUser);
        }

        return "redirect:/index";
    }

}



