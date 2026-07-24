document.addEventListener("DOMContentLoaded", () => {
    const statusModal = document.getElementById("accountStatusModal");
    const statusForm = document.querySelector("[data-status-form]");
    const statusTitle = document.querySelector("[data-status-title]");
    const statusMessage = document.querySelector("[data-status-message]");
    const accountLabel = document.querySelector("[data-account-label]");
    const statusButton = document.querySelector("[data-status-button] span");

    if (!statusModal || !statusForm) {
        return;
    }

    statusModal.addEventListener("show.bs.modal", (event) => {
        const button = event.relatedTarget;
        const active = button.getAttribute("data-account-active") === "true";
        const name = button.getAttribute("data-account-name") || "";

        statusForm.action = button.getAttribute("data-status-url");
        accountLabel.textContent = name;
        statusTitle.textContent = active ? "Desactivar cuenta" : "Activar cuenta";
        statusMessage.textContent = active
            ? "La cuenta dejara de aparecer al registrar nuevas transacciones, pero conservara su historial."
            : "La cuenta volvera a estar disponible para registrar nuevas transacciones.";

        if (statusButton) {
            statusButton.textContent = active ? "Desactivar" : "Activar";
        }
    });
});
