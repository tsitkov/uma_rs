package org.mit.uma.rcserver.defaultimpl.models;

//import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
//import javax.persistence.CollectionTable;
import javax.persistence.Column;
//import javax.persistence.ElementCollection;
//import javax.persistence.Embedded;
import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
//import javax.persistence.PrePersist;
//import javax.persistence.PreUpdate;
import javax.persistence.Table;
//import javax.persistence.Temporal;
//import javax.persistence.TemporalType;
import javax.persistence.Transient;



import org.mit.uma.rcserver.models.AuthServerDetails;
import org.mit.uma.rcserver.models.UserDetails;
import org.mit.uma.rcserver.defaultimpl.models.DefaultAuthServerDetails;

@Entity
@Table(name = "user_details")
@NamedQueries({
	@NamedQuery(name = "UserDetailsEntity.findAll", query = "SELECT c FROM DefaultUserDetails c"),
	@NamedQuery(name = "UserDetailsEntity.getById", query = "select c from DefaultUserDetails c where c.id = :id"),
	@NamedQuery(name = "UserDetailsEntity.getByUserName", query = "select c from DefaultUserDetails c where c.userId = :userId"),
	@NamedQuery(name = "UserDetailsEntity.deleteUser", query = "DELETE from DefaultUserDetails c where c.userId = :userId")
})
public class DefaultUserDetails implements UserDetails {
	

	private Long id;
	private String userId;
	private String pat;
	private String refreshToken;
	private DefaultAuthServerDetails authServer;
	
	public DefaultUserDetails() {
		this.authServer = new DefaultAuthServerDetails();
	}

	public void setAuthServer(AuthServerDetails authServerDetails) {
		this.authServer = new DefaultAuthServerDetails(authServerDetails);		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	@Override	
	public Long getId() {
		return id;
	}

	@Override	
	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name="user_id")
	@Override
	public String getUserId() {
		return userId;
	}

	@Override
	public void setUserId(String userId) {
		this.userId = userId;
	}
			
	@ManyToOne
	@JoinColumn(name="auth_server_id")
	public DefaultAuthServerDetails getAuthServerEntity() {
		return authServer;
	}

	public void setAuthServerEntity(DefaultAuthServerDetails authServer) {
		this.authServer = authServer;
	}
	
	@Basic
	@Column(name="access_token")
	@Override
	public String getAccessToken() {
		return pat;
	}
	
	@Override
	public void setAccessToken(String pat) {
		this.pat = pat;
	}

	@Basic
	@Column(name="refresh_token")
	@Override
	public String getRefreshToken() {
		return refreshToken;
	}
	
	@Override
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	
	@Transient
	@Override
	public AuthServerDetails getAuthServer() {
		return authServer;
	}
		
}
