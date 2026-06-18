package com.IAM.fintech.config;

import com.IAM.fintech.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return utilisateurRepository.findByUsername(username)
                .map(u -> {
                    // Force le préfixe "ROLE_" si il est absent
                    String role = u.getRole();
                    if (role != null && !role.startsWith("ROLE_")) {
                        role = "ROLE_" + role;
                    }

                    return User.withUsername(u.getUsername())
                            .password(u.getPassword())
                            .authorities(role) // Envoie ROLE_AGENT même si en base il y a écrit AGENT
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));
    }
}