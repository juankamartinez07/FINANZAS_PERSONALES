document.addEventListener("DOMContentLoaded", () => {
    const statusModal = document.getElementById("savingStatusModal");
    const statusForm = document.querySelector("[data-saving-status-form]");
    const statusTitle = document.querySelector("[data-saving-status-title]");
    const statusMessage = document.querySelector("[data-saving-status-message]");
    const savingLabel = document.querySelector("[data-saving-label]");
    const statusButton = document.querySelector("[data-saving-status-button] span");

    if (statusModal && statusForm) {
        statusModal.addEventListener("show.bs.modal", (event) => {
            const button = event.relatedTarget;
            const active = button.getAttribute("data-saving-active") === "true";
            const name = button.getAttribute("data-saving-name") || "";

            statusForm.action = button.getAttribute("data-status-url");
            savingLabel.textContent = name;
            statusTitle.textContent = active ? "Desactivar meta" : "Activar meta";
            statusMessage.textContent = active
                ? "La meta se conservara para consulta, pero dejara de sumar como ahorro activo."
                : "La meta volvera a contar dentro de tus ahorros activos.";

            if (statusButton) {
                statusButton.textContent = active ? "Desactivar" : "Activar";
            }
        });
    }

    document.querySelectorAll("[data-saving-inline-status]").forEach((form) => {
        form.addEventListener("submit", (event) => {
            const button = form.querySelector("[data-saving-active]");
            const active = button && button.getAttribute("data-saving-active") === "true";
            const message = active
                ? "Confirma que deseas desactivar esta meta."
                : "Confirma que deseas activar esta meta.";

            if (!window.confirm(message)) {
                event.preventDefault();
            }
        });
    });

    const savingForm = document.querySelector("[data-saving-form]");
    if (!savingForm) {
        return;
    }

    const target = savingForm.querySelector("[data-saving-target]");
    const current = savingForm.querySelector("[data-saving-current]");
    const progressBar = savingForm.querySelector("[data-saving-progress-bar]");
    const progressLabel = savingForm.querySelector("[data-saving-progress-label]");

    const updateProgress = () => {
        const objetivo = Number.parseFloat(target.value);
        const ahorrado = Number.parseFloat(current.value);

        if (!Number.isFinite(objetivo) || objetivo <= 0 || !Number.isFinite(ahorrado)) {
            progressBar.style.setProperty("--progress", "0%");
            progressLabel.textContent = "0 %";
            return;
        }

        const boundedCurrent = Math.min(Math.max(ahorrado, 0), objetivo);
        const percent = Math.min(Math.max((boundedCurrent / objetivo) * 100, 0), 100);
        progressBar.style.setProperty("--progress", `${percent}%`);
        progressLabel.textContent = `${Math.round(percent)} %`;
    };

    if (target && current && progressBar && progressLabel) {
        target.addEventListener("input", updateProgress);
        current.addEventListener("input", updateProgress);
        updateProgress();
    }
});
