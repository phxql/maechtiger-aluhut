package de.mkammerer.aluhut;

import de.mkammerer.aluhut.i18n.I18N;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

class TestServlet extends HttpServlet {
    private final I18N i18N;

    TestServlet(I18N i18N) {
        this.i18N = i18N;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println(i18N.getString(Locale.GERMAN, I18N.Key.WELCOME));
        resp.getWriter().flush();
    }
}
