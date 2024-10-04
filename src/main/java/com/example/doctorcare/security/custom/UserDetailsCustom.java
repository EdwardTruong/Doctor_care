package com.example.doctorcare.security.custom;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.doctorcare.entity.UserEntity;

public class UserDetailsCustom implements UserDetails {

	private static final long serialVersionUID = 1L;

	private int id;
	private String username;
	private String password;
	private int idDoc;
	private int isActive;
	private Collection<? extends GrantedAuthority> authorities;

	
	public UserDetailsCustom() {
	}

	
	
	
	public UserDetailsCustom(int id, int idDoc, String username, String password, int isActive,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.idDoc = idDoc;
		this.isActive = isActive;
		this.authorities = authorities;
	}

	public UserDetailsCustom(int id, String username, String password, int isActive,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.isActive = isActive;
		this.authorities = authorities;
	}

	public static UserDetailsCustom build(UserEntity user) {
			    List<GrantedAuthority> authorities = user.getRoles()
			    		.stream()
			    		.map(role -> new SimpleGrantedAuthority(role.getName().name()))
			    		.collect(Collectors.toList());
			    		
			  if(user.getDoctorEntity() != null) {
				  return new UserDetailsCustom(
					        user.getId(), 
					        user.getDoctorEntity().getId(),	
					        user.getEmail(),
					        user.getPassword(), 
					        user.getActive(),
					        authorities);
			  }
			  return new UserDetailsCustom(
					  					user.getId(),
									  user.getEmail(),
									  user.getPassword(), 
									  user.getActive(),
								      authorities);	    
			  }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public int getIdDoc() {
		return idDoc;
	}

	public void setIdDoc(int idDoc) {
		this.idDoc = idDoc;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;

	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return isActive == 1;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsCustom user = (UserDetailsCustom) o;
		return Objects.equals(id, user.id);
	}
}
