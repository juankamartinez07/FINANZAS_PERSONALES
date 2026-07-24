document.addEventListener("DOMContentLoaded", () => {
    const dateTarget = document.querySelector("[data-current-date]");
    const themeToggle = document.querySelector("[data-theme-toggle]");
    const themeIcon = document.querySelector("[data-theme-icon]");
    const storageKey = "finanzas-theme";

    function applyTheme(theme) {
        document.documentElement.dataset.theme = theme;
        if (themeIcon) {
            themeIcon.className = theme === "dark" ? "bi bi-sun" : "bi bi-moon";
        }
        if (themeToggle) {
            themeToggle.setAttribute(
                    "aria-label",
                    theme === "dark" ? "Activar modo claro" : "Activar modo oscuro");
            themeToggle.title = theme === "dark" ? "Activar modo claro" : "Activar modo oscuro";
        }
    }

    const storedTheme = localStorage.getItem(storageKey);
    const preferredTheme = window.matchMedia("(prefers-color-scheme: dark)").matches ? "dark" : "light";
    applyTheme(storedTheme || preferredTheme);

    if (themeToggle) {
        themeToggle.addEventListener("click", () => {
            const nextTheme = document.documentElement.dataset.theme === "dark" ? "light" : "dark";
            localStorage.setItem(storageKey, nextTheme);
            applyTheme(nextTheme);
        });
    }

    if (dateTarget) {
        dateTarget.textContent = new Intl.DateTimeFormat("es-CO", {
            weekday: "long",
            day: "numeric",
            month: "long",
            year: "numeric"
        }).format(new Date());
    }

    document.querySelectorAll(".sidebar-link").forEach((link) => {
        const route = link.dataset.route;

        if (route === window.location.pathname
                || (route !== "/" && window.location.pathname.startsWith(route + "/"))
                || (window.location.pathname === "/" && route === "/dashboard")) {
            document.querySelectorAll(".sidebar-link").forEach((item) => item.classList.remove("active"));
            link.classList.add("active");
        }
    });
});
