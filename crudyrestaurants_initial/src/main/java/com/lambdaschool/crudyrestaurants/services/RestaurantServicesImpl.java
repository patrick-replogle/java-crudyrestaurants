package com.lambdaschool.crudyrestaurants.services;

import com.lambdaschool.crudyrestaurants.models.Restaurant;
import com.lambdaschool.crudyrestaurants.repositories.RestaurantRepository;
import com.lambdaschool.crudyrestaurants.views.MenuCounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service(value = "restaurantService")
public class RestaurantServicesImpl implements RestaurantServices
{
    @Autowired
    RestaurantRepository restrepos;

    @Override
    public List<Restaurant> findAllRestaurants() {
        List<Restaurant> myList = new ArrayList<>();
        restrepos.findAll().iterator().forEachRemaining(myList::add);
        return myList;
    }

    @Override
    public Restaurant findRestaurantById(long id)
    {
        return restrepos.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant " + id + " not found."));
    }

    @Override
    public Restaurant findRestaurantByName(String name)
    {
        Restaurant restaurant = restrepos.findByName(name);

        if (restaurant == null)
        {
            throw new EntityNotFoundException("Restaurant " + name + " not found.");
        }
        return restaurant;
    }

    @Override
    public List<Restaurant> findByLikeName(String subname)
    {
        List<Restaurant> list = restrepos.findByNameContainingIgnoringCase(subname);

       return list;
    }

    @Override
    public List<MenuCounts> countMenusByRestaurant()
    {
        List<MenuCounts> list = restrepos.findMenuCounts();

        return list;
    }

    @Override
    public List<Restaurant> findRestaurantByDish(String dish)
    {
       List<Restaurant> list = restrepos.findByMenus_dishContainingIgnoringCase(dish);
       return list;
    }

    @Transactional
    @Override
    public Restaurant save(Restaurant restaurant)
    {
        return restrepos.save(restaurant);
    }
}
