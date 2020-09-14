package com.lambdaschool.crudyrestaurants.services;

/*
 * Note: "Unless there's some extra information that isn't clear from the interface description (there rarely is), the implementation documentation should then simply link to the interface method."
 * Taken from https://stackoverflow.com/questions/11671989/best-practice-for-javadocs-interface-implementation-or-both?lq=1
 */

import com.lambdaschool.crudyrestaurants.models.Menu;
import com.lambdaschool.crudyrestaurants.models.Payment;
import com.lambdaschool.crudyrestaurants.models.Restaurant;
import com.lambdaschool.crudyrestaurants.repositories.PaymentRepository;
import com.lambdaschool.crudyrestaurants.repositories.RestaurantRepository;
import com.lambdaschool.crudyrestaurants.views.MenuCounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the RestaurantService Interface.
 */
@Transactional
@Service(value = "restaurantService")
public class RestaurantServicesImpl
        implements RestaurantServices
{
    /**
     * Connects this service to the Restaurant Table.
     */
    @Autowired
    private RestaurantRepository restrepos;

    @Autowired
    private PaymentRepository paymentrepos;

    @Override
    public List<Restaurant> findAllRestaurants()
    {
        List<Restaurant> list = new ArrayList<>();
        /*
         * findAll returns an iterator set.
         * iterate over the iterator set and add each element to an array list.
         */
        restrepos.findAll()
                .iterator()
                .forEachRemaining(list::add);
        return list;
    }

    @Override
    public Restaurant findRestaurantById(long id) throws
            EntityNotFoundException
    {
        return restrepos.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant " + id + " Not Found"));
    }

    @Override
    public Restaurant findRestaurantByName(String name) throws
            EntityNotFoundException
    {
        Restaurant restaurant = restrepos.findByName(name);

        if (restaurant == null)
        {
            throw new EntityNotFoundException("Restaurant " + name + " not found!");
        }

        return restaurant;
    }

    @Override
    public List<Restaurant> findByState(String state)
    {
        List<Restaurant> list = restrepos.findByStateIgnoringCase(state);
        return list;
    }

    @Override
    public List<Restaurant> findByNameLike(String thename)
    {
        List<Restaurant> list = restrepos.findByNameContainingIgnoringCase(thename);
        return list;
    }

    @Override
    public List<Restaurant> findByDish(String thedish)
    {
        List<Restaurant> list = restrepos.findByMenus_dishContainingIgnoringCase(thedish);
        return list;
    }

    @Override
    public List<MenuCounts> getMenuCounts()
    {
        List<MenuCounts> list = restrepos.findMenuCounts();
        return list;
    }

    @Transactional
    @Override
    public Restaurant save(Restaurant restaurant)
    {
        Restaurant newRestaurant = new Restaurant();

        if (restaurant.getRestaurantid() != 0)
        {
            findRestaurantById(restaurant.getRestaurantid());
            newRestaurant.setRestaurantid(restaurant.getRestaurantid());
        }
        // extra validation below
        // single value fields
        newRestaurant.setName(restaurant.getName());
        newRestaurant.setAddress(restaurant.getAddress());
        newRestaurant.setCity(restaurant.getCity());
        newRestaurant.setState(restaurant.getState());
        newRestaurant.setTelephone(restaurant.getTelephone());
        newRestaurant.setSeatcapacity(restaurant.getSeatcapacity());

        // collections
        newRestaurant.getPayments().clear();
        for (Payment p : restaurant.getPayments())
        {
            Payment newPayment = paymentrepos.findById(p.getPaymentid())
                    .orElseThrow(() -> new EntityNotFoundException("Payment " + p.getPaymentid() + " not found!"));

            newRestaurant.getPayments().add(newPayment);
        }

        newRestaurant.getMenus().clear();
        // menus
        for (Menu m : restaurant.getMenus())
        {
            Menu newMenu = new Menu(m.getDish(), m.getPrice(), newRestaurant);

            newRestaurant.getMenus().add(newMenu);
        }

        return restrepos.save(newRestaurant);
    }

    @Transactional
    @Override
    public void deleteAllRestaurants()
    {
        restrepos.deleteAll();
    }

    @Transactional
    @Override
    public void delete(long restaurantid)
    {
        if (restrepos.findById(restaurantid).isPresent())
        {
            restrepos.deleteById(restaurantid);
        } else
        {
            throw new EntityNotFoundException("Restaurant " + restaurantid + " not found!");
        }
    }

    @Transactional
    @Override
    public Restaurant update(Restaurant restaurant, long restid)
    {
        Restaurant updateRestaurant = findRestaurantById(restid);

        // extra validation below
        // single value fields
        if (restaurant.getName() != null)
        {
            updateRestaurant.setName(restaurant.getName());
        }

        if (restaurant.getAddress() != null)
        {
            updateRestaurant.setAddress(restaurant.getAddress());
        }

        if (restaurant.getCity() != null)
        {
            updateRestaurant.setCity(restaurant.getCity());
        }

        if (restaurant.getState() != null)
        {
            updateRestaurant.setState(restaurant.getState());
        }

        if (restaurant.getTelephone() != null)
        {
            updateRestaurant.setTelephone(restaurant.getTelephone());
        }

        if (restaurant.hasvalueforseatcapacity)
        {
            updateRestaurant.setSeatcapacity(restaurant.getSeatcapacity());
        }

        // collections
        if (restaurant.getPayments().size() > 0)
        {
            updateRestaurant.getPayments()
                    .clear();
            for (Payment p : restaurant.getPayments())
            {
                Payment newPayment = paymentrepos.findById(p.getPaymentid())
                        .orElseThrow(() -> new EntityNotFoundException("Payment " + p.getPaymentid() + " not found!"));

                updateRestaurant.getPayments()
                        .add(newPayment);
            }
        }

        if (restaurant.getMenus().size() > 0)
        {
            updateRestaurant.getMenus().clear();
            // menus
            for (Menu m : restaurant.getMenus())
            {
                Menu newMenu = new Menu(m.getDish(), m.getPrice(), updateRestaurant);

                updateRestaurant.getMenus().add(newMenu);
            }
        }

        return restrepos.save(updateRestaurant);
    }
}
