package org.mit.uma.rcserver.defaultimpl.models;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;






//import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
//import javax.persistence.CollectionTable;
//import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
//import javax.persistence.ElementCollection;
//import javax.persistence.ElementCollection;
//import javax.persistence.Embedded;
import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
//import javax.persistence.OrderColumn;
import javax.persistence.NamedQueries;
//import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
//import javax.persistence.PrePersist;
//import javax.persistence.PreUpdate;
import javax.persistence.Table;

//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.mit.uma.rcserver.json.JsonUtil;
import org.mit.uma.rcserver.json.ResourceDetailsJson;
import org.mit.uma.rcserver.json.ResourceSetJson;
import org.mit.uma.rcserver.models.ResourceDetails;
import org.mit.uma.rcserver.models.ResourceSet;
import org.springframework.util.StringUtils;

@Entity()
@DiscriminatorValue("S")
//@Table(name = "resource_sets")
@NamedQueries({
//	@NamedQuery(name = "ResourceSet.getRootByUserID", query = "select c from DefaultResourceSet c where c.ownerId = :userId AND c.resourceSetParent is NULL"),
	@NamedQuery(name = "ResourceSet.getRootByUserID", query = "select c from DefaultResourceSet c where c.ownerId = :userId AND c.resourceParent is NULL"),
	@NamedQuery(name = "ResourceSet.getByUserId", query = "select c from DefaultResourceSet c where c.ownerId = :userId"),
	@NamedQuery(name = "ResourceSet.getById", query = "select c from DefaultResourceSet c where c.resourceId = :id"),
	@NamedQuery(name = "ResourceSet.getByPath", query = "select c from DefaultResourceSet c where c.resourcePath = :path"),
//	@NamedQuery(name = "ResourceSet.getByParentId", query = "select c from DefaultResourceSet c where c.resourceSetParent = :parent")
	@NamedQuery(name = "ResourceSet.getByParentId", query = "select c from DefaultResourceSet c where c.resourceParent = :parent")
})

public class DefaultResourceSet extends DefaultResourceDetails implements ResourceSet {

//	private Long resourceSetId;
//	private Long ownerId;
//	private DefaultResourceSet parent;
//	private String resourceSetName;
//	private String resourceSetPath;
//	private String resourceSetDescription;
	private String protectionId;
	private String revisionId;
//	private String resourceUri;
//	private Set<DefaultResourceDetails> resources;
	private Map<String,DefaultResourceDetails> resources;
//	private Set<DefaultResourceSet> resourceSets;
	
	// no args constructor
	public DefaultResourceSet() {
	}
	
	// copy constructor
	protected DefaultResourceSet(ResourceSet resource, DefaultResourceSet parent) {
		this.resourceId = resource.getResourceId();
		this.parent = parent;
		this.ownerId = parent.getOwnerId();
		this.resourceName = resource.getResourceName();
		this.resourcePath = parent.getResourcePath() + "/" + resource.getResourceName();
		this.resourceDescription = resource.getResourceDescription();
		this.protectionId = resource.getProtectionId();
		this.revisionId = resource.getProtectionRevision();
		this.resources = null;
	}

	protected DefaultResourceSet(ResourceDetails resource, DefaultResourceSet parent) {
		this.resourceId = resource.getResourceId();
		this.parent = parent;
		this.ownerId = parent.getOwnerId();
		this.resourceName = resource.getResourceName();
		this.resourcePath = parent.getResourcePath() + "/" + resource.getResourceName();
		this.resourceDescription = resource.getResourceDescription();
		this.protectionId = null;
		this.revisionId = null;
		this.resources = null;
	}

	@Basic
	@Column(name="protection_id")
	@Override
	public String getProtectionId() {
		return protectionId;
	}

	@Override
	public void setProtectionId(String protectionId) {
		this.protectionId = protectionId;
	}

	@Basic
	@Column(name="protection_revision")
	@Override
	public String getProtectionRevision() {
		return revisionId;
	}

	@Override
	public void setProtectionRevision(String revisionId) {
		this.revisionId = revisionId;
	}

	@OneToMany(cascade=CascadeType.ALL, mappedBy="resourceParent")	
//	@JoinColumn(name="parent_id", nullable=false, updatable=false, insertable=false)
	public Collection<DefaultResourceDetails> getDefaultResources() {
		if (resources != null) {
			return resources.values();
		} else {
			return null;
		}
	}
	
	@Transient
	@Override
	public Map<String, ? extends ResourceDetails> getResources() {
		return resources;
	}
	
	public void setDefaultResources(Collection<DefaultResourceDetails> resources) {
		this.resources = new HashMap<String,DefaultResourceDetails>();
		for (DefaultResourceDetails r: resources) {
			this.resources.put(r.getResourcePath(), r);
		}
	}
	
	@Transient
	@Override
	public ResourceSet addResourceSet(ResourceSet resource) throws IOException {
		if (resource.isResourceSet()) {
			DefaultResourceSet defaultResource = new DefaultResourceSet(resource, this);			
			String path = defaultResource.getResourcePath();
			if (resources == null) {
				resources = new HashMap<String,DefaultResourceDetails>();
			} else {
				if (resources.containsKey(path)) {
					throw new IOException("Resource at " + path + " already exists");
				}
			}
			resources.put(defaultResource.getResourcePath(), defaultResource);
			return defaultResource;
		} else {
			throw new IllegalStateException("Attempt to insert ResourceSet object into 'resource' container");			
		}
	}

	@Transient
	@Override
	public ResourceSet addResourceSet(ResourceDetails resource) throws IOException {
		if (resource.isResourceSet()) {
			DefaultResourceSet defaultResource = new DefaultResourceSet(resource, this);			
			String path = defaultResource.getResourcePath();
			if (resources == null) {
				resources = new HashMap<String,DefaultResourceDetails>();
			} else {
				if (resources.containsKey(path)) {
					throw new IOException("Resource at " + path + " already exists");
				}
			}
			resources.put(defaultResource.getResourcePath(), defaultResource);
			return defaultResource;
		} else {
			throw new IllegalStateException("Attempt to insert ResourceSet object into 'resource' container");			
		}
	}

	@Transient
	@Override
	public ResourceDetails addResource(ResourceDetails resource) throws IOException {
		if (!resource.isResourceSet()) {
			DefaultResourceDetails defaultResource = new DefaultResourceDetails(resource, this);
			String path = defaultResource.getResourcePath();
			if (resources == null) {
				resources = new HashMap<String,DefaultResourceDetails>();
			} else {
				if (resources.containsKey(path)) {
					throw new IOException("Resource at " + path + " already exists");
				}
			}
			resources.put(defaultResource.getResourcePath(), defaultResource);
			return defaultResource;
		} else {
			throw new IllegalStateException("Attempt to insert ResourceDetails into 'resourceSet' container");						
		}
	}

	@Transient
	@Override
	public void removeResource(ResourceDetails resource) throws IOException {
		String path = resource.getResourcePath();
		if (resources != null && resources.containsKey(path)) {
			resources.remove(path);
		} else {
			throw new IOException("Can't remove non-existing resource at " + path);
		}
	}

	@Transient
	@Override
	public void replaceResource(ResourceDetails resource) throws IOException {
		String path = resource.getResourcePath();
		if (resources != null && resources.containsKey(path)) {
			DefaultResourceDetails oldResource = resources.get(path);
			oldResource.copy(resource);
		} else {
			throw new IOException("Can't replace non-existing resource at " + path);
		}		
	}

	
	@Transient
	@Override
	public boolean isResourceSet() {
		return true;
	}

	@Transient
	@Override
	public String getDefaultScopes() {
		Set<String> scopes = new HashSet<String>();
		for (DefaultScopes scope : DefaultScopes.values()) {
			  scopes.add(scope.toString().toLowerCase());
		}
		String result = StringUtils.collectionToDelimitedString(scopes, ",");
		return result;
	}

	@Transient
	@Override
	public String toJson() {
		
//		Set<ResourceDetailsJson> resources = new HashSet<ResourceDetailsJson>();
//		for (ResourceDetails resource : this.resources.values()) {
//			resources.add(new ResourceDetailsJson(resource));
//		}
		return JsonUtil.toString(new ResourceSetJson(this));
	}

}
