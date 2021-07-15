/*
 *  Copyright 2020 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.*;
import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.*;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * Created by jt on 2019-01-26.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultBreweryLoader implements CommandLineRunner {

    public static final String TASTING_ROOM = "Tasting Room";
    public static final String ST_PETE_DISTRIBUTING = "St Pete Distributing";
    public static final String DUNEDIN_DISTRIBUTING = "Dunedin Distributing";
    public static final String KEY_WEST_DISTRIBUTORS = "Key West Distributors";
    public static final String STPETE_USER = "stpete";
    public static final String DUNEDIN_USER = "dunedin";
    public static final String KEYWEST_USER = "keywest";
    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";

    private final BreweryRepository breweryRepository;
    private final BeerRepository beerRepository;
    private final BeerInventoryRepository beerInventoryRepository;
    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) {
        loadData();
        loadBreweryData();
        loadTastingRoomData();
        loadCustomerData();
    }

    private void loadCustomerData() {
        Role customerRole = roleRepository.findByName("CUSTOMER").orElseThrow();

        //create customers
        Customer stPeteCustomer = customerRepository.save(Customer.builder()
                .customerName(ST_PETE_DISTRIBUTING)
                .apiKey(UUID.randomUUID())
                .build());

        Customer dunedinCustomer = customerRepository.save(Customer.builder()
                .customerName(DUNEDIN_DISTRIBUTING)
                .apiKey(UUID.randomUUID())
                .build());

        Customer keyWestCustomer = customerRepository.save(Customer.builder()
                .customerName(KEY_WEST_DISTRIBUTORS)
                .apiKey(UUID.randomUUID())
                .build());

        //create users
        User stPeteUser = userRepository.save(User.builder().username("stpete")
                .password(passwordEncoder.encode("password"))
                .customer(stPeteCustomer)
                .role(customerRole).build());

        User dunedinUser = userRepository.save(User.builder().username("dunedin")
                .password(passwordEncoder.encode("password"))
                .customer(dunedinCustomer)
                .role(customerRole).build());

        User keywest = userRepository.save(User.builder().username("keywest")
                .password(passwordEncoder.encode("password"))
                .customer(keyWestCustomer)
                .role(customerRole).build());

        //create orders
        createOrder(stPeteCustomer);
        createOrder(dunedinCustomer);
        createOrder(keyWestCustomer);

         log.debug("Orders Loaded: " + beerOrderRepository.count());
    }

    private BeerOrder createOrder(Customer customer) {
        return  beerOrderRepository.save(BeerOrder.builder()
                .customer(customer)
                .orderStatus(OrderStatusEnum.NEW)
                .beerOrderLines(Set.of(BeerOrderLine.builder()
                        .beer(beerRepository.findByUpc(BEER_1_UPC))
                        .orderQuantity(2)
                        .build()))
                .build());
    }

    private void loadTastingRoomData() {
        Customer tastingRoom = Customer.builder()
                .customerName(TASTING_ROOM)
                .apiKey(UUID.randomUUID())
                .build();

        customerRepository.save(tastingRoom);

        beerRepository.findAll().forEach(beer -> {
            beerOrderRepository.save(BeerOrder.builder()
                    .customer(tastingRoom)
                    .orderStatus(OrderStatusEnum.NEW)
                    .beerOrderLines(Set.of(BeerOrderLine.builder()
                            .beer(beer)
                            .orderQuantity(2)
                            .build()))
                    .build());
        });
    }

    private void loadBreweryData() {
        if (breweryRepository.count() == 0) {
            breweryRepository.save(Brewery
                    .builder()
                    .breweryName("Cage Brewing")
                    .build());

            Beer mangoBobs = Beer.builder()
                    .beerName("Mango Bobs")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_1_UPC)
                    .build();

            beerRepository.save(mangoBobs);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(mangoBobs)
                    .quantityOnHand(500)
                    .build());

            Beer galaxyCat = Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle(BeerStyleEnum.PALE_ALE)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_2_UPC)
                    .build();

            beerRepository.save(galaxyCat);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(galaxyCat)
                    .quantityOnHand(500)
                    .build());

            Beer pinball = Beer.builder()
                    .beerName("Pinball Porter")
                    .beerStyle(BeerStyleEnum.PORTER)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(BEER_3_UPC)
                    .build();

            beerRepository.save(pinball);
            beerInventoryRepository.save(BeerInventory.builder()
                    .beer(pinball)
                    .quantityOnHand(500)
                    .build());

        }
    }
    private void loadData(){

        Authority createBeer = authorityRepository.save(Authority.builder().permission("create.beer").build());
        Authority updateBeer = authorityRepository.save(Authority.builder().permission("update.beer").build());
        Authority readBeer = authorityRepository.save(Authority.builder().permission("read.beer").build());
        Authority deleteBeer = authorityRepository.save(Authority.builder().permission("delete.beer").build());

        Authority createCustomer = authorityRepository.save(Authority.builder().permission("create.Customer").build());
        Authority updateCustomer = authorityRepository.save(Authority.builder().permission("update.Customer").build());
        Authority readCustomer = authorityRepository.save(Authority.builder().permission("read.Customer").build());
        Authority deleteCustomer = authorityRepository.save(Authority.builder().permission("delete.Customer").build());

        Authority createBrewery = authorityRepository.save(Authority.builder().permission("create.Brewery").build());
        Authority updateBrewery = authorityRepository.save(Authority.builder().permission("update.Brewery").build());
        Authority readBrewery = authorityRepository.save(Authority.builder().permission("read.Brewery").build());
        Authority deleteBrewery = authorityRepository.save(Authority.builder().permission("delete.Brewery").build());

        Authority createOrder = authorityRepository.save(Authority.builder().permission("create.order").build());
        Authority updateOrder = authorityRepository.save(Authority.builder().permission("update.order").build());
        Authority readOrder = authorityRepository.save(Authority.builder().permission("read.order").build());
        Authority deleteOrder = authorityRepository.save(Authority.builder().permission("delete.order").build());

        Authority createOrderCustomer = authorityRepository.save(Authority.builder().permission("create.order.customer").build());
        Authority updateOrderCustomer = authorityRepository.save(Authority.builder().permission("update.order.customer").build());
        Authority readOrderCustomer = authorityRepository.save(Authority.builder().permission("read.order.customer").build());
        Authority deleteOrderCustomer = authorityRepository.save(Authority.builder().permission("delete.order.customer").build());

        Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
        Role userRole = roleRepository.save(Role.builder().name("USER").build());
        Role customerRole = roleRepository.save(Role.builder().name("CUSTOMER").build());

        adminRole.setAuthorities(new HashSet<>(Set.of(readBeer,deleteBeer,updateBeer,createBeer,createBrewery,createCustomer,updateBrewery,
                updateCustomer, readBrewery, readCustomer, deleteBrewery, deleteCustomer
                ,readOrder,createOrder,updateOrder,deleteOrder)));
        userRole.setAuthorities(new HashSet(Set.of(readBeer,createBeer)));
        customerRole.setAuthorities(new HashSet(Set.of(readBeer, readBrewery, readCustomer,createBeer,
                createOrderCustomer,readOrderCustomer,deleteOrderCustomer,updateOrderCustomer)));

        roleRepository.saveAll(Arrays.asList(adminRole,userRole,customerRole));

        User spring = User.builder().role(adminRole).username("spring").password("{bcrypt}$2a$10$iXjK0OOCjOUpnAXjwlxv2eEp4Kq5/je9c2J0EO1e1Oha.xlwerhcy").build();

        userRepository.save(spring);
        User user1 = User.builder().username("user").role(userRole).password("{sha256}695cca3adede6a3e22ed74ae610adb654d8f55330cbd445a01797b2142259d6e0f7dc8d4f693da0b").build();

        userRepository.save(user1);

        User scott = User.builder().username("scott").role(customerRole).password("{bcrypt15}$2a$15$cPt7VK1GivOqKnOe0z/nt.pvCk4I7AwmKsb/ejhu6RHMQFzB4/MYS").build();

        userRepository.save(scott);


    }
}
