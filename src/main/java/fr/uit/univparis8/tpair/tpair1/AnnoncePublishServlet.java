package fr.uit.univparis8.tpair.tpair1;

import fr.uit.univparis8.tpair.tpair1.service.AnnonceService;
import jakarta.servlet.http.*;
import java.io.IOException;
public class AnnoncePublishServlet extends HttpServlet {

    private final AnnonceService service = new AnnonceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            Long id = Long.parseLong(req.getParameter("id"));
            HttpSession session = req.getSession();
            Long userId = (Long) session.getAttribute("userId");
            service.publish(id, userId);
        } catch (Exception ignored) {}

        resp.sendRedirect("AnnonceList");
    }
}