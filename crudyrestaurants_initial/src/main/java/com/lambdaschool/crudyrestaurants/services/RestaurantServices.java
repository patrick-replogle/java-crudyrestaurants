package com.lambdaschool.crudyrestaurants.services;

import com.lambdaschool.crudyrestaurants.models.Restaurant;
import com.lambdaschool.crudyrestaurants.views.MenuCounts;

import java.util.List;

public interface RestaurantServices
{
    List<Restaurant> findAllRestaurants();

    Restaurant findRestaurantById(long id);

    Restaurant findRestaurantByName(String name);

    List<Restaurant> findByLikeName(String subname);

    List<MenuCounts> countMenusByRestaurant();

    List<Restaurant> findRestaurantByDish(String dish);

    Restaurant save(Restaurant restaurant);
}
