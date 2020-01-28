package com.javamentor.revseev.rest.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {
        httpServletResponse.sendRedirect(determineTargetUrl(authentication));
    }

    protected String determineTargetUrl(Authentication authentication) {
        boolean isUser = false;
        boolean isAdmin = false;

        Collection<? extends GrantedAuthority> authorities
                = authentication.getAuthorities();

        for (GrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority()
                      .equals("USER"))
            {
                isUser = true;
                break;
            } else if (grantedAuthority.getAuthority()
                    .equals("ADMIN"))
            {
                isAdmin = true;
                break;
            }
        }

        if (isAdmin ) {
            return "/list";
        } else if (isUser) {
            return "/";
        } else {
            throw new IllegalStateException();
        }
    }
}

/** from https://www.baeldung.com/spring_redirect_after_login
 *
 * public class MySimpleUrlAuthenticationSuccessHandler
 *   implements AuthenticationSuccessHandler {
 *
 *     protected Log logger = LogFactory.getLog(this.getClass());
 *
 *     private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
 *
 *     @Override
 *     public void onAuthenticationSuccess(HttpServletRequest request,
 *       HttpServletResponse response, Authentication authentication)
 *       throws IOException {
 *
 *         handle(request, response, authentication);
 *         clearAuthenticationAttributes(request);
 *     }
 *
 *     protected void handle(HttpServletRequest request,
 *       HttpServletResponse response, Authentication authentication)
 *       throws IOException {
 *
 *         String targetUrl = determineTargetUrl(authentication);
 *
 *         if (response.isCommitted()) {
 *             logger.debug(
 *               "Response has already been committed. Unable to redirect to "
 *               + targetUrl);
 *             return;
 *         }
 *
 *         redirectStrategy.sendRedirect(request, response, targetUrl);
 *     }
 *
 *     protected String determineTargetUrl(Authentication authentication) {
 *         boolean isUser = false;
 *         boolean isAdmin = false;
 *         Collection<? extends GrantedAuthority> authorities
 *          = authentication.getAuthorities();
 *         for (GrantedAuthority grantedAuthority : authorities) {
 *             if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
 *                 isUser = true;
 *                 break;
 *             } else if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
 *                 isAdmin = true;
 *                 break;
 *             }
 *         }
 *
 *         if (isUser) {
 *             return "/homepage.html";
 *         } else if (isAdmin) {
 *             return "/console.html";
 *         } else {
 *             throw new IllegalStateException();
 *         }
 *     }
 *
 *     protected void clearAuthenticationAttributes(HttpServletRequest request) {
 *         HttpSession session = request.getSession(false);
 *         if (session == null) {
 *             return;
 *         }
 *         session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
 *     }
 *
 *     public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
 *         this.redirectStrategy = redirectStrategy;
 *     }
 *     protected RedirectStrategy getRedirectStrategy() {
 *         return redirectStrategy;
 *     }
 * }*/