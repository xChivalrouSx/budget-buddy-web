package chivalrous.budgetbuddy.config.jwt;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import chivalrous.budgetbuddy.constant.ErrorMessage;
import chivalrous.budgetbuddy.exception.BbAuthException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

	private static final String AUTHORIZATION_STRING = "Authorization";
	private static final String BEARER_STRING = "Bearer ";

	private final JwtTokenManager jwtTokenManager;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		boolean shouldFilterDisable = (request.getRequestURI().endsWith("/authenticate") || request.getRequestURI().endsWith("/user"))
				&& request.getMethod().equals("POST");
		if (!shouldFilterDisable) {
			String authorizationHeader = request.getHeader(AUTHORIZATION_STRING);
			if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_STRING)) {
				String tokenWithoutBearer = authorizationHeader.substring(BEARER_STRING.length());
				UserDetails userDetails = jwtTokenManager.getTokenUser(tokenWithoutBearer);
				if (jwtTokenManager.isTokenValid(tokenWithoutBearer) && userDetails != null) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							userDetails.getUsername(), userDetails.getPassword(), new ArrayList<>());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				} else {
					throw new BbAuthException(ErrorMessage.AUTHENTICATION_NOT_VALID);
				}
			} else {
				throw new BbAuthException(ErrorMessage.AUTHENTICATION_NOT_FOUND);
			}
		}
		filterChain.doFilter(request, response);
	}
}
