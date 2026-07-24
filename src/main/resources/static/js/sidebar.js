document.addEventListener("DOMContentLoaded", () => {
    const shell = document.querySelector(".app-shell");
    const toggles = document.querySelectorAll("[data-sidebar-toggle]");
    const closeTargets = document.querySelectorAll("[data-sidebar-close]");

    toggles.forEach((toggle) => {
        toggle.addEventListener("click", () => {
            if (window.matchMedia("(max-width: 991.98px)").matches) {
                shell.classList.toggle("sidebar-open");
                return;
            }

            shell.classList.toggle("sidebar-collapsed");
        });
    });

    closeTargets.forEach((target) => {
        target.addEventListener("click", () => shell.classList.remove("sidebar-open"));
    });

    document.querySelectorAll(".sidebar-link").forEach((link) => {
        link.addEventListener("click", () => {
            if (window.matchMedia("(max-width: 991.98px)").matches) {
                shell.classList.remove("sidebar-open");
            }
        });
    });
});
