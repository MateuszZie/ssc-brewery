package guru.sfg.brewery.security.perms;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('create.order') OR hasAuthority('create.order.customer') " +
        "AND @beerOrderAuthenticationManager.CustomerIDMatcher(authentication, #customerId)" )
public @interface CreateOrderPermission {
}
