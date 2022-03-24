package com.gallop.microservice.auth.repository;

import com.gallop.microservice.auth.domain.Clients;
import org.springframework.data.repository.Repository;


/**
 * author gallop
 * date 2021-06-28 9:01
 * Description:
 * Modified By:
 */
public interface ClientsRepository extends Repository<Clients,String> {

    Clients findById(String clientId);
}
