package io.jettra.flux.designer.pages;

import com.sun.net.httpserver.HttpExchange;
import io.jettra.core.login.NoLoginRequired;
import io.jettra.core.server.Page;
import io.jettra.flux.core.Widget;
import io.jettra.flux.model.CredentialFlux;
import io.jettra.flux.pages.FluxBaseHandler;
import io.jettra.flux.widgets.Paragraph;
import io.jettra.flux.widgets.Scaffold;
import io.jettra.json.SessionScoped;

import java.io.IOException;
import java.util.Map;

@NoLoginRequired
@SessionScoped
@Page(path = "/login")
public class LoginPage extends FluxBaseHandler {

    @Override
    protected String getTitle() {
        return "JettraFlux Designer • Iniciar Sesión";
    }

    @Override
    protected boolean onPost(HttpExchange exchange, Map<String, String> params) throws IOException {
        String user = params.get("username");
        String pass = params.get("password");
        if (user == null || user.trim().isEmpty() || pass == null || pass.trim().isEmpty()) {
            redirect(exchange, "/login?error=empty_fields");
            return true;
        }
        if (isValidUser(user.trim(), pass.trim())) {
            String role = "developer".equalsIgnoreCase(user) ? "DEVELOPER" : "ADMIN";
            CredentialFlux credentialFlux = new CredentialFlux(user, user + " Architect", role, "DesignStudio", "");
            io.jettra.server.core.JettraContext.getCurrent().set(io.jettra.server.core.JettraContext.Scope.SESSION, "credentialFlux", credentialFlux);
            setSessionCookie(exchange, user, credentialFlux.role(), credentialFlux.department());
            redirect(exchange, "/designer");
            return true;
        } else {
            redirect(exchange, "/login?error=invalid_credentials");
            return true;
        }
    }

    @Override
    protected boolean onGet(HttpExchange exchange, Map<String, String> params) throws IOException {
        if ("true".equals(params.get("logout"))) {
            clearSessionCookie(exchange);
            redirect(exchange, "/login");
            return true;
        }
        return false;
    }

    @Override
    protected Widget buildUI(HttpExchange exchange, Map<String, String> params, String currentTheme) {
        String html = """
            <style>
              * { box-sizing: border-box; }
              body { margin: 0; background: radial-gradient(circle at 50% 25%, #0f172a 0%, #030712 100%); font-family: 'Inter', system-ui, sans-serif; color: #f8fafc; min-height: 100vh; display: flex; align-items: center; justify-content: center; }
              .designer-login-card { width: 100%; max-width: 440px; background: rgba(12, 17, 29, 0.92); backdrop-filter: blur(20px); border: 1px solid rgba(56, 189, 248, 0.3); border-radius: 18px; padding: 40px; box-shadow: 0 25px 50px rgba(0,0,0,0.8), 0 0 35px rgba(56, 189, 248, 0.15); }
              .cyber-input { width: 100%; padding: 12px 14px; background: #090d16; border: 1px solid #334155; border-radius: 8px; color: #fff; font-size: 0.95rem; margin-top: 6px; box-sizing: border-box; }
              .cyber-input:focus { outline: none; border-color: #00d4ff; box-shadow: 0 0 0 3px rgba(0, 212, 255, 0.2); }
              .btn-login { width: 100%; padding: 13px; background: linear-gradient(135deg, #0284c7, #22c55e); color: #020617; border: none; border-radius: 8px; font-weight: 800; font-size: 1rem; cursor: pointer; margin-top: 14px; transition: 0.2s; }
              .btn-login:hover { transform: translateY(-1px); box-shadow: 0 8px 24px rgba(34, 197, 94, 0.4); }
              .chip { padding: 6px 12px; background: rgba(56, 189, 248, 0.1); border: 1px solid rgba(56, 189, 248, 0.25); border-radius: 6px; color: #38bdf8; font-size: 0.78rem; font-weight: 700; cursor: pointer; text-decoration: none; }
              .chip:hover { background: rgba(56, 189, 248, 0.25); }
            </style>
            <div class="designer-login-card">
              <div style="text-align: center; margin-bottom: 26px;">
                <div style="display: inline-flex; align-items: center; justify-content: center; width: 60px; height: 60px; border-radius: 14px; background: linear-gradient(135deg, #0284c7, #22c55e); margin-bottom: 12px; box-shadow: 0 8px 20px rgba(34, 197, 94, 0.4);">
                  <span style="font-size: 2rem;">🎨</span>
                </div>
                <h2 style="margin: 0; font-size: 1.6rem; font-weight: 900; color: #fff; letter-spacing: -0.02em;">JettraFlux Designer</h2>
                <p style="margin: 6px 0 0 0; color: #94a3b8; font-size: 0.85rem;">Diseñador Visual de Interfaces & Gestor Maven</p>
              </div>
              <form method="POST" action="/login" style="display: flex; flex-direction: column; gap: 16px;">
                <div>
                  <label style="font-size: 0.82rem; font-weight: 600; color: #cbd5e1;">Usuario o Diseñador</label>
                  <input id="usernameField" type="text" name="username" required class="cyber-input" placeholder="admin o developer" />
                </div>
                <div>
                  <label style="font-size: 0.82rem; font-weight: 600; color: #cbd5e1;">Contraseña</label>
                  <input id="passwordField" type="password" name="password" required class="cyber-input" placeholder="••••••••" />
                </div>
                <button type="submit" class="btn-login">Entrar al Estudio Visual ➔</button>
              </form>
              <div style="margin-top: 24px; padding-top: 18px; border-top: 1px solid rgba(255,255,255,0.08); text-align: center;">
                <span style="font-size: 0.72rem; color: #64748b; text-transform: uppercase; font-weight: 700; letter-spacing: 0.05em; display: block; margin-bottom: 10px;">Acceso Rápido</span>
                <div style="display: flex; gap: 8px; justify-content: center;">
                  <button type="button" class="chip" onclick="document.getElementById('usernameField').value='admin'; document.getElementById('passwordField').value='admin';">👤 Admin (admin)</button>
                  <button type="button" class="chip" onclick="document.getElementById('usernameField').value='developer'; document.getElementById('passwordField').value='developer';">⚡ Developer (developer)</button>
                </div>
              </div>
            </div>
            """;

        return Scaffold.of().body(Paragraph.of(html));
    }

    private boolean isValidUser(String user, String pass) {
        return ("admin".equals(user) && "admin".equals(pass)) ||
               ("developer".equals(user) && "developer".equals(pass)) ||
               ("demo".equals(user) && "demo".equals(pass)) ||
               ("avbravo".equals(user) && "avbravo".equals(pass));
    }
}
