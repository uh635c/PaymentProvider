package com.example.paymentprovider.security;

import com.example.paymentprovider.entity.MerchantEntity;
import com.example.paymentprovider.entity.Status;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
public class MerchantDetails implements UserDetails {

    private MerchantEntity merchantEntity;

    public String getMerchantId(){
        return merchantEntity.getId();
    }

    @Override
    public String getPassword() {
        return merchantEntity.getSecretKey();
    }

    @Override
    public String getUsername() {
        return merchantEntity.getName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
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
        return merchantEntity.getStatus().equals(Status.ACTIVE);
    }
}
