# JettraWebBackEnd - Comprehensive Guide & Architecture Manual

## 1. Overview & Architecture
`JettraWebBackEnd` is the high-performance server-side rendering (SSR), template compilation, and API gateway backend for Jettra web applications. It coordinates web routing, asset caching, session contexts, and database transactions.

---

## 2. Key Features
- **Server-Side UI Rendering**: Renders dynamic HTML components directly from Java 25 models.
- **Fast Static Asset Pipeline**: Embeds compression (Gzip/Brotli) and caching headers for high Lighthouse scores.
- **Multi-Model Data Fetching**: Bundles queries across `DOCUMENT`, `RECORDS`, and `COLUMN` engines for page composition.
- **CSRF & Security Middleware**: Built-in protections against web vulnerabilities.

---

## 3. Usage & Code Examples

### 3.1 Web Controller Example
```java
package com.example.web;

import io.jettra.web.annotation.Controller;
import io.jettra.web.annotation.Get;
import io.jettra.web.view.ModelAndView;
import java.util.Map;

@Controller
public class DashboardController {

    @Get("/dashboard")
    public ModelAndView showDashboard() {
        return new ModelAndView("dashboard.html", Map.of(
            "title", "Enterprise Portal",
            "user", "Admin",
            "activeNodes", 3
        ));
    }
}
```
