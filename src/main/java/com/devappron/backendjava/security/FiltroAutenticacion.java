package com.devappron.backendjava.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.devappron.backendjava.utils.AppContexto;
import com.devappron.backendjava.models.peticiones.UsuarioLoginRequestModel;
import com.devappron.backendjava.services.IUsuarioService;
import com.devappron.backendjava.shared.UsuarioDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class FiltroAutenticacion extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    
    public FiltroAutenticacion(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
      
        try {            
            UsuarioLoginRequestModel usuarioLoginRequestModel= new ObjectMapper().readValue(
                                                                request.getInputStream(), 
                                                                UsuarioLoginRequestModel.class);
        
            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    usuarioLoginRequestModel.getUserName(), 
                    usuarioLoginRequestModel.getPassword(), 
                    new ArrayList<>()));

        } catch (IOException e) {
           throw new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
        String userName = ((User) authResult.getPrincipal()).getUsername();

        String token= Jwts.builder()
        .setSubject(userName)
        .setExpiration(new Date(System.currentTimeMillis() + ConstantesSecurity.EXPIRATION_DATE))
        .signWith(SignatureAlgorithm.HS512, ConstantesSecurity.getTokenSecret())
        .compact();


        IUsuarioService iUsuarioServicio= (IUsuarioService) AppContexto.getBean("usuarioService");
        UsuarioDto usuarioDto= iUsuarioServicio.obtenerUsuario(userName);


        response.addHeader("Access-Control-Expose-Headers", "Authorization, UserId");
        response.addHeader("UserId", usuarioDto.getUserId());
        response.addHeader(ConstantesSecurity.HEADER_STRING, ConstantesSecurity.TOKEN_PREFIX + token);
    }    

}





