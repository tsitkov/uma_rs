package org.mit.uma.rcserver.defaultimpl.models;

//import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
//import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
//import javax.persistence.ElementCollection;
//import javax.persistence.Embedded;
import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.FetchType;
//import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
//import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
//import javax.persistence.PrePersist;
//import javax.persistence.PreUpdate;
import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.mit.uma.rcserver.models.ResourceDetails;
import org.mit.uma.rcserver.models.ResourceSet;

@Entity()
@Table(name = "resource_details", schema="rserverdb")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="is_composite")
@DiscriminatorValue("D")
@NamedQueries({
	@NamedQuery(name = "ResourceDetails.findById", query = "SELECT c FROM DefaultResourceDetails c where c.resourceId = :id"),
	@NamedQuery(name = "ResourceDetails.getByPath", query = "select c from DefaultResourceDetails c where c.resourcePath = :path"),

})
public class DefaultResourceDetails implements ResourceDetails {

//	private Long resourceId;
//	private DefaultResourceSet parent;
//	private String resourceName;
//	private String resourcePath;
//	private String resourceDescription;
//	private String resourceUri;

	protected Long resourceId;
	protected DefaultResourceSet parent;
	protected Long ownerId;
	protected String resourceName;
	protected String resourcePath;
	protected String resourceDescription;
	protected String resourceUri;

	
	public DefaultResourceDetails() {
		// no args constructor
	}
	
	// copy constructor
	public DefaultResourceDetails(ResourceDetails resource, DefaultResourceSet parent) {
		this.resourceId = resource.getResourceId();
		this.parent = parent;
		this.ownerId = parent.getOwnerId();
		this.resourceName = resource.getResourceName();
		this.resourcePath = parent.getResourcePath() + "/" + resource.getResourceName();
		this.resourceDescription = resource.getResourceDescription();
		this.resourceUri = resource.getResourceUri();
	}
	
	@Transient
	public void copy(ResourceDetails other) {
		this.resourceDescription = other.getResourceDescription();
		this.resourceUri = other.getResourceUri();		
	}
		
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="resource_id")
	@Override
	public Long getResourceId() {
		// TODO Auto-generated method stub
		return resourceId;
	}

	@Override
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	
	@ManyToOne
	@JoinColumn(name="parent_id")
	public DefaultResourceSet getResourceParent() {
		return parent;
	}
	
	public void setResourceParent(DefaultResourceSet parent) {
		this.parent = parent;
	}

	@Transient
	@Override
	public ResourceSet getParent() {
		return parent;
	}
	
	@Basic
	@Column(name="owner_id")
	@Override
	public Long getOwnerId() {
		return ownerId;
	}

	@Override
	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	@Basic
	@Column(name="resource_name")
	public String getResourceName() {
		// TODO Auto-generated method stub
		return resourceName;
	}

	@Override
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	@Override
	@Basic
	@Column(name="resource_path")
	public String getResourcePath() {
		return resourcePath;
	}

	@Override
	public void setResourcePath(String resourcePath) {
		// TODO Auto-generated method stub
		this.resourcePath = resourcePath;
	}
		
	@Basic
	@Column(name="resource_description")	
	@Override
	public String getResourceDescription() {
		return resourceDescription;
	}

	@Override
	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}

	@Basic
	@Column(name="resource_uri")
	@Override
	public String getResourceUri() {
		return resourceUri;
	}

	@Override
	public void setResourceUri(String resourceUri) {
		this.resourceUri = resourceUri;
	}

//	@Basic
//	@Column(name="is_resource_set")
	@Transient
	@Override
	public boolean isResourceSet() {
		return false;
	}

}
