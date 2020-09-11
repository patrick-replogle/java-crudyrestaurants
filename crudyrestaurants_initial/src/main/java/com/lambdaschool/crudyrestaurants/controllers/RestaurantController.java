package com.lambdaschool.crudyrestaurants.controllers;

import com.lambdaschool.crudyrestaurants.models.Restaurant;
import com.lambdaschool.crudyrestaurants.services.RestaurantServices;
import com.lambdaschool.crudyrestaurants.views.MenuCounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController
{
    @Autowired
    RestaurantServices restaurantServices;

    // http://localhost:2019/restaurants/restaurants
    @GetMapping(value = "/restaurants", produces = {"application/json"})
    public ResponseEntity<?> listAllRestaurants()
    {
        List<Restaurant> myList = restaurantServices.findAllRestaurants();

        return new ResponseEntity<>(myList, HttpStatus.OK);
    }

    //  http://localhost:2019/restaurants/restaurants/{id}
    @GetMapping(value = "/restaurant/{restaurantId}", produces = {"application/json"})
    public ResponseEntity<?> findRestaurantById(@PathVariable long restaurantId)
    {
        Restaurant r = restaurantServices.findRestaurantById(restaurantId);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    //  http://localhost:2019/restaurants/name/{name}
    @GetMapping(value = "/name/{restaurantName}", produces = {"application/json"})
    public ResponseEntity<?> findRestaurantByName(@PathVariable String restaurantName)
    {
        Restaurant r = restaurantServices.findRestaurantByName(restaurantName);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    //  http://localhost:2019/restaurants/likename/{name}
    @GetMapping(value = "/likename/{subname}", produces = {"application/json"})
    public ResponseEntity<?> findByLikeName(@PathVariable String subname)
    {
        List<Restaurant> rtnList = restaurantServices.findByLikeName(subname);

        return new ResponseEntity<>(rtnList, HttpStatus.OK);
    }

    //  http://localhost:2019/restaurants/menucounts
    @GetMapping(value = "/menucounts", produces = {"application/json"})
    public ResponseEntity<?> countMenuCounts()
    {
        List<MenuCounts> rtnList = restaurantServices.countMenusByRestaurant();

        return new ResponseEntity<>(rtnList, HttpStatus.OK);
    }

    //  http://localhost:2019/restaurants/likedish/cheese
    @GetMapping(value = "/likedish/{dish}", produces={"application/json"})
    public ResponseEntity<?> findRestaurantsByDish(@PathVariable String dish)
    {
        List<Restaurant> rtnList = restaurantServices.findRestaurantByDish(dish);

        return new ResponseEntity<>(rtnList, HttpStatus.OK);
    }
}
