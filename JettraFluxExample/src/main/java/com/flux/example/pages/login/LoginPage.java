package com.flux.example.pages.login;

import io.jettra.flux.pages.FluxBaseHandler;
import io.jettra.flux.widgets.Paragraph;
import io.jettra.flux.widgets.Scaffold;
import io.jettra.flux.widgets.Column;
import io.jettra.flux.widgets.Row;
import io.jettra.flux.widgets.Card;
import io.jettra.flux.widgets.Center;
import io.jettra.flux.widgets.Header;
import io.jettra.flux.widgets.ElevatedButton;
import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.model.CredentialFlux;
import io.jettra.json.SessionScoped;
import io.jettra.server.JettraServer;

import java.util.Map;

@io.jettra.core.login.NoLoginRequired
@SessionScoped
@Page(path = "/login")
public class LoginPage extends FluxBaseHandler {

    @Override
    protected String getTitle() {
        return "Iniciar Sesión • JettraFlux Enterprise Suite";
    }

    @Override
    protected boolean onPost(HttpExchange exchange, Map<String, String> params) throws java.io.IOException {
        String user = params.get("username");
        String pass = params.get("password");
        if (user == null || user.trim().isEmpty() || pass == null || pass.trim().isEmpty()) {
            redirect(exchange, "/login?error=empty_fields");
            return true;
        }
        if (isValidUser(user.trim(), pass.trim())) {
            String role = "demo".equalsIgnoreCase(user) ? "DEMO" : "ADMIN";
            CredentialFlux credentialFlux = new CredentialFlux(user, user + " Administrator", role, "Engineering", "");
            io.jettra.server.core.JettraContext.getCurrent().set(io.jettra.server.core.JettraContext.Scope.SESSION, "credentialFlux", credentialFlux);
            setSessionCookie(exchange, user, credentialFlux.role(), credentialFlux.department());
            redirect(exchange, "/dashboard");
            return true;
        } else {
            redirect(exchange, "/login?error=invalid_credentials");
            return true;
        }
    }

    @Override
    protected boolean onGet(HttpExchange exchange, Map<String, String> params) throws java.io.IOException {
        if ("true".equals(params.get("logout"))) {
            clearSessionCookie(exchange);
            redirect(exchange, "/login");
            return true;
        }
        return false;
    }

    @Override
    protected Widget buildUI(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        String errorMsg = null;
        if (params.containsKey("error")) {
            String err = params.get("error");
            if ("empty_fields".equals(err)) {
                errorMsg = "Por favor ingrese su usuario y contraseña para continuar.";
            } else {
                errorMsg = "Credenciales incorrectas. Verifique el usuario o use los accesos rápidos abajo.";
            }
        }

        String customCss = 
            "<style>" +
            "  * { box-sizing: border-box; }" +
            "  body { margin: 0; background: radial-gradient(circle at 50% 20%, #0f172a 0%, #030712 100%); font-family: system-ui, -apple-system, sans-serif; color: #f8fafc; min-height: 100vh; display: flex; align-items: center; justify-content: center; }" +
            "  .pro-login-card { width: 100%; max-width: 440px; background: rgba(15, 23, 42, 0.85); backdrop-filter: blur(16px); border: 1px solid rgba(56, 189, 248, 0.25); border-radius: 16px; padding: 36px; box-shadow: 0 20px 40px rgba(0,0,0,0.6), 0 0 30px rgba(56, 189, 248, 0.1); }" +
            "  .pro-input { width: 100%; padding: 12px 14px; background: rgba(3, 7, 18, 0.7); border: 1px solid #334155; border-radius: 8px; color: #fff; font-size: 0.95rem; margin-top: 6px; transition: border-color 0.2s; }" +
            "  .pro-input:focus { outline: none; border-color: #38bdf8; box-shadow: 0 0 0 3px rgba(56, 189, 248, 0.2); }" +
            "  .pro-btn { width: 100%; padding: 13px; background: linear-gradient(135deg, #0284c7, #2563eb); color: white; border: none; border-radius: 8px; font-weight: 700; font-size: 1rem; cursor: pointer; transition: transform 0.15s, box-shadow 0.15s; margin-top: 10px; }" +
            "  .pro-btn:hover { transform: translateY(-1px); box-shadow: 0 6px 20px rgba(37, 99, 235, 0.4); }" +
            "  .pro-demo-chip { padding: 6px 12px; background: rgba(56, 189, 248, 0.1); border: 1px solid rgba(56, 189, 248, 0.2); border-radius: 6px; color: #38bdf8; font-size: 0.78rem; font-weight: 600; cursor: pointer; text-decoration: none; }" +
            "  .pro-demo-chip:hover { background: rgba(56, 189, 248, 0.2); }" +
            "</style>";

        StringBuilder html = new StringBuilder();
        html.append(customCss);
        html.append("<div class='pro-login-card'>");
        
        // Brand Header
        html.append("<div style='text-align: center; margin-bottom: 28px;'>")
            .append("  <div style='display: inline-flex; align-items: center; justify-content: center; width: 54px; height: 54px; border-radius: 12px; background: linear-gradient(135deg, #38bdf8, #2563eb); margin-bottom: 12px; box-shadow: 0 8px 16px rgba(56, 189, 248, 0.3);'>")
            .append("    <span style='font-size: 1.8rem;'>⚡</span>")
            .append("  </div>")
            .append("  <h2 style='margin: 0; font-size: 1.5rem; font-weight: 800; color: #fff; letter-spacing: -0.02em;'>JettraFlux Pro</h2>")
            .append("  <p style='margin: 6px 0 0 0; color: #94a3b8; font-size: 0.85rem;'>Plataforma Reactiva de Alto Rendimiento</p>")
            .append("</div>");

        // Error notification if present
        if (errorMsg != null) {
            html.append("<div style='background: rgba(239, 68, 68, 0.15); border: 1px solid #ef4444; color: #fca5a5; padding: 10px 14px; border-radius: 8px; font-size: 0.85rem; margin-bottom: 20px; display: flex; align-items: center; gap: 8px;'>")
                .append("  <span>⚠️</span> <span>").append(errorMsg).append("</span>")
                .append("</div>");
        }

        // Form
        html.append("<form method='POST' action='").append(JettraServer.resolvePath("/login")).append("' style='display: flex; flex-direction: column; gap: 16px;'>")
            .append("  <div>")
            .append("    <label style='font-size: 0.82rem; font-weight: 600; color: #cbd5e1;'>Usuario o Email</label>")
            .append("    <input id='usernameField' type='text' name='username' required class='pro-input' placeholder='admin o demo' />")
            .append("  </div>")
            .append("  <div>")
            .append("    <div style='display: flex; justify-content: space-between;'>")
            .append("      <label style='font-size: 0.82rem; font-weight: 600; color: #cbd5e1;'>Contraseña</label>")
            .append("      <a href='").append(JettraServer.resolvePath("/forgot-password")).append("' style='color: #38bdf8; font-size: 0.78rem; text-decoration: none;'>¿Olvidaste tu clave?</a>")
            .append("    </div>")
            .append("    <input id='passwordField' type='password' name='password' required class='pro-input' placeholder='••••••••' />")
            .append("  </div>")
            .append("  <button type='submit' class='pro-btn'>Iniciar Sesión ➔</button>")
            .append("</form>");

        // Fast Quick Demo logins
        html.append("<div style='margin-top: 24px; padding-top: 20px; border-top: 1px solid rgba(255,255,255,0.08); text-align: center;'>")
            .append("  <span style='font-size: 0.75rem; color: #64748b; text-transform: uppercase; font-weight: 700; letter-spacing: 0.05em; display: block; margin-bottom: 10px;'>Accesos Rápidos de Prueba</span>")
            .append("  <div style='display: flex; gap: 8px; justify-content: center;'>")
            .append("    <button type='button' class='pro-demo-chip' onclick=\"document.getElementById('usernameField').value='admin'; document.getElementById('passwordField').value='admin';\">👤 Admin / admin</button>")
            .append("    <button type='button' class='pro-demo-chip' onclick=\"document.getElementById('usernameField').value='demo'; document.getElementById('passwordField').value='demo';\">⚡ Demo / demo</button>")
            .append("  </div>")
            .append("</div>");

        html.append("</div>");

        return Scaffold.of().body(Paragraph.of(html.toString()));
    }

    private boolean isValidUser(String user, String pass) {
        return ("admin".equals(user) && "admin".equals(pass)) || 
               ("demo".equals(user) && "demo".equals(pass)) || 
               ("avbravo".equals(user) && "avbravo".equals(pass));
    }
}
