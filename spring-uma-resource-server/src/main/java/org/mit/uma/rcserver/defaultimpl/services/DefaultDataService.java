package org.mit.uma.rcserver.defaultimpl.services;

import java.io.IOException;
import java.util.List;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.mit.uma.rcserver.models.AuthServerDetails;
import org.mit.uma.rcserver.models.ResourceDetails;
import org.mit.uma.rcserver.models.ResourceSet;
import org.mit.uma.rcserver.models.UserDetails;
import org.mit.uma.rcserver.services.DataService;
import org.mit.uma.rcserver.config.Configurator;
import org.mit.uma.rcserver.defaultimpl.models.DefaultAuthServerDetails;
import org.mit.uma.rcserver.defaultimpl.models.DefaultUserDetails;
import org.mit.uma.rcserver.defaultimpl.models.DefaultResourceSet;
import org.mit.uma.rcserver.defaultimpl.models.DefaultResourceDetails;

@Repository
@Transactional
public class DefaultDataService implements DataService {

	DefaultDataService() {
		System.out.print("Instantiatign Default DataService\n");
		System.out.print(transactionManager); 
		
	}

	@Autowired
	private Configurator config;
	
    @PersistenceContext
	private EntityManager transactionManager;
	
	@Override
	public Collection<? extends DefaultAuthServerDetails> findAllAuthServers() {
		TypedQuery<DefaultAuthServerDetails> query = transactionManager.createNamedQuery("AmDetailsEntity.findAll", DefaultAuthServerDetails.class);
		return query.getResultList();
	}
	
	@Override
	public DefaultAuthServerDetails getAuthServerDetails(String issuer) {
		
		TypedQuery<DefaultAuthServerDetails> query = transactionManager.createNamedQuery("AmDetailsEntity.getByServerUrl", DefaultAuthServerDetails.class);
		query.setParameter("issuer", issuer);
		DefaultAuthServerDetails result = _list2element(query.getResultList());
		if (result == null) {
			result = new DefaultAuthServerDetails();
			result.setIssuer(issuer);
		}
				
		return result;
	}
	
	@Override
	public DefaultAuthServerDetails saveAuthServerDetails(AuthServerDetails entity) {

		DefaultAuthServerDetails result = getAuthServerDetails(entity.getIssuer());
		assert(result != null);

		Long id = result.getAuthServerId();
		if ( id != null) {
			// record exists, this is update
			entity.setAuthServerId(id);
		}
		result = (DefaultAuthServerDetails) transactionManager.merge(entity);
		transactionManager.flush();
		
		return result;
	}
	
	@Override
	public DefaultUserDetails getUserDetails(String userId, String authServerHost) {
		TypedQuery<DefaultUserDetails> query = transactionManager.createNamedQuery("UserDetailsEntity.getByUserName", DefaultUserDetails.class);
		query.setParameter("userId", userId);
		DefaultUserDetails result = _list2element(query.getResultList());
		if (result == null) {
			// this is new user
			result = new DefaultUserDetails();
			result.setUserId(userId);
			// check if we are registered with a given autorization filter
			if (authServerHost != null) {		
				DefaultAuthServerDetails authServer = getAuthServerDetails(authServerHost);
				assert(authServer != null);
				result.setAuthServerEntity(authServer);
			}
		}
		return result;
	}
	
	@Override
	public DefaultUserDetails getUserDetailsById(Long id) {
		TypedQuery<DefaultUserDetails> query = transactionManager.createNamedQuery("UserDetailsEntity.getById", DefaultUserDetails.class);
		query.setParameter("id", id);
		DefaultUserDetails result = _list2element(query.getResultList());
		return result;
	}
	
	@Override
	public DefaultUserDetails saveUserDetails(UserDetails entity) {
		// check if user exists
		DefaultUserDetails result = getUserDetails(entity.getUserId(), null);
		assert(result != null);
		AuthServerDetails authServer = entity.getAuthServer();
		if (authServer.getAuthServerId() == null) {
			assert(authServer.getIssuer() != null);
			// check if authorization server exists
			DefaultAuthServerDetails authServerStored = getAuthServerDetails(authServer.getIssuer());
			authServer.setAuthServerId(authServerStored.getAuthServerId());
		}
		Long id = result.getId();
		if (id != null) {
			// record exists, this is update
			entity.setId(id);
		}
		result = (DefaultUserDetails) transactionManager.merge(entity);				
		transactionManager.flush();

		return result;
	}
	
	@Override
	public UserDetails removeUser(String userId) {
		UserDetails user = getUserDetails(userId, null);
		if (user != null) {
			transactionManager.remove(user);
			return user;
		} else {
			throw new IllegalArgumentException("User not found: " + userId);
		}
	}

	@Override
	public ResourceSet getRootResourceSet(Long userId, boolean create) {
		TypedQuery<DefaultResourceSet> query = transactionManager.createNamedQuery("ResourceSet.getRootByUserID", DefaultResourceSet.class);
		query.setParameter("userId", userId);
		DefaultResourceSet result = _list2element(query.getResultList());
		if ((result == null) && create) {
			result = new DefaultResourceSet();
		}
		return result;
	}

	@Override
	public ResourceSet getResourceSetById(Long resourceSetId) {
		TypedQuery<DefaultResourceSet> query = transactionManager.createNamedQuery("ResourceSet.getById", DefaultResourceSet.class);
		query.setParameter("id", resourceSetId);
		DefaultResourceSet result = _list2element(query.getResultList());
		return result;
	}

	@Override
	public ResourceSet getResourceSetByPath(String resourceSetPath) {
		TypedQuery<DefaultResourceSet> query = transactionManager.createNamedQuery("ResourceSet.getByPath", DefaultResourceSet.class);
		query.setParameter("path", resourceSetPath);
		DefaultResourceSet result = _list2element(query.getResultList());
		return result;
	}
	
	public ResourceDetails getResourceDetails(Long resourceId) {
		TypedQuery<DefaultResourceDetails> query = transactionManager.createNamedQuery("ResourceDetails.findById", DefaultResourceDetails.class);
		query.setParameter("id", resourceId);
		DefaultResourceDetails result = _list2element(query.getResultList());
		return result;
	}
	
	
	/*
	@Override
	public ResourceSet getResourceSet(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void addResource(ResourceSet parent, ResourceDetails resource) {
		// TODO Auto-generated method stub
		
	}
*/
	
	@Override
	public ResourceDetails saveResource(ResourceDetails resource) {
		// check if resource in db
		Long resourceId = resource.getResourceId();
		ResourceDetails result = null;
		if (resourceId == null) {
			// we assume that this is a new record
			transactionManager.persist(resource);
			result = resource;
		} else {
			result = transactionManager.merge(resource);
		}
		transactionManager.flush();
		return result;
	}
	
	public ResourceDetails removeResource(ResourceDetails resource) {
		// TODO Auto-generated method stub
		ResourceSet parent = resource.getParent();
		if (parent != null) {
			try {
				parent.removeResource(resource);
			} catch( IOException e) {
				throw new IllegalStateException(e.getMessage());
			}
		}
		// check if resource in db
		Long resourceId = resource.getResourceId();
		if (resource.isResourceSet()) {
			resource = getResourceSetById(resourceId);
		} else {
			resource = getResourceDetails(resourceId);
		}	
		if (resource != null) {
			transactionManager.remove(resource);
			return resource;
		} else {
			throw new IllegalArgumentException("Resource not found: " + resourceId);
		}
	}
	
	// Utility methods
	private <T> T _list2element(List<T> result) {
		switch(result.size()) {
		case 0:
			return null;
		case 1:
			return result.get(0);
		default:
			throw new IllegalStateException("Expected single result, got " + result.size());				
		}
	}


}
