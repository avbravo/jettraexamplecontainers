package com.jettra.main.login;

import java.io.IOException;
import java.util.Map;

import io.jettra.server.JettraServer;
import io.jettra.wui.components.Login;
import io.jettra.wui.components.Notification;
import io.jettra.wui.core.Page;

public class LoginPage extends Page {

    private Login loginForm;

    public LoginPage() {
        super("Login");
    }

    @Override
    protected void onInit(Map<String, String> params) {
        // Handle Logout
        if (params.containsKey("logout") || "/logout".equals(getRequestPath())) {
            logout();
            return;
        }

        loginForm = new Login(JettraServer.resolvePath("/login"));
        loginForm.setApplicationName("Jettra Web Example");
        add(loginForm);

        if (params.containsKey("error")) {
            Notification notif = new Notification();
            notif.setType("error");
            notif.showMessage("Error: Username y/o password no válidos");
            add(notif);
        }
    }

    @Override
    protected void onPost(Map<String, String> params) {
        String user = params.get("username");
        String pass = params.get("password");

        System.out.println("[LoginPage] POST login attempt: " + user);

        if (isValidUser(user, pass)) {
            String cPath = JettraServer.getContextPath();
            if (cPath == null || cPath.isEmpty()) cPath = "/";
            
            System.out.println("[LoginPage] Success: user=" + user + " | cookiePath=" + cPath);
            // In a real Jettra app, we might use JettraContext for session management, 
            // but for this example we'll stick to the cookie approach as requested.
            setSessionCookie(user, cPath);
            io.jettra.server.core.JettraContext.getCurrent().set(io.jettra.server.core.JettraContext.Scope.SESSION, "username", user);
            
            try {
                redirect(currentExchange, JettraServer.resolvePath("/dashboard"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("[LoginPage] Failure: user=" + user);
            try {
                redirect(currentExchange, JettraServer.resolvePath("/login?error=invalid_credentials"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getRequestPath() {
        return currentExchange.getRequestURI().getPath();
    }

    private void logout() {
        String cPath = JettraServer.getContextPath();
        currentExchange.getResponseHeaders().set("Set-Cookie", "username=; Path=" + cPath + "; Max-Age=0");
        try {
            redirect(currentExchange, JettraServer.resolvePath("/login"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSessionCookie(String user, String path) {
        currentExchange.getResponseHeaders().set("Set-Cookie", "username=" + user + "; Path=" + path);
    }

    private boolean isValidUser(String user, String pass) {
        return ("admin".equals(user) && "admin".equals(pass)) || 
               ("demo".equals(user) && "demo".equals(pass)) || 
               ("avbravo".equals(user) && "avbravo".equals(pass));
    }
}
