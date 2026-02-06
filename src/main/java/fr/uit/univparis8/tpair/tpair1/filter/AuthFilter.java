package fr.uit.univparis8.tpair.tpair1.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (path.startsWith("/Login")
                || path.startsWith("/login.jsp")
                || path.startsWith("/Register")
                || path.startsWith("/register.jsp")
                || path.startsWith("/Logout")
                || path.startsWith("/AnnoncePublic")
                || path.startsWith("/css")
                || path.startsWith("/js")
                || path.startsWith("/images")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        boolean logged = (session != null && session.getAttribute("userId") != null);

        if (!logged) {
            resp.sendRedirect(req.getContextPath() + "/Login");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {}
}