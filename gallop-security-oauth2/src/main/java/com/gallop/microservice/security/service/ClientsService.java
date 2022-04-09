package com.gallop.microservice.security.service;

import com.gallop.microservice.security.domain.Clients;
import com.gallop.microservice.security.repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * author gallop
 * date 2021-06-28 10:52
 * Description:
 * Modified By:
 */
@Service
public class ClientsService {
    @Autowired
    private ClientsRepository clientsRepository;

    public BaseClientDetails getByClientId(String clientId) {
        Clients clients = clientsRepository.findById(clientId);
        if(clients==null){
            return null;
        }
        BaseClientDetails clientDetails = new BaseClientDetails(clients.getClientId(),clients.getResourceIds(),clients.getScope(),clients.getAuthorizedGrantTypes(),clients.getAuthorities(),clients.getRedirectUri());
        clientDetails.setClientSecret(clients.getClientSecret());
        clientDetails.setAccessTokenValiditySeconds(clients.getAccessTokenValidity());
        clientDetails.setRefreshTokenValiditySeconds(clients.getRefreshTokenValidity());
        Set scopeList;
        scopeList = StringUtils.commaDelimitedListToSet(clients.getAutoapprove());
        if (!scopeList.isEmpty()) {
            clientDetails.setAutoApproveScopes(scopeList);
        }

        return clientDetails;
    }
}
