document.addEventListener("DOMContentLoaded", () => {
    const statusModal = document.getElementById("debtStatusModal");
    const statusForm = document.querySelector("[data-debt-status-form]");
    const statusTitle = document.querySelector("[data-debt-status-title]");
    const statusMessage = document.querySelector("[data-debt-status-message]");
    const debtLabel = document.querySelector("[data-debt-label]");
    const statusButton = document.querySelector("[data-debt-status-button] span");

    if (statusModal && statusForm) {
        statusModal.addEventListener("show.bs.modal", (event) => {
            const button = event.relatedTarget;
            const active = button.getAttribute("data-debt-active") === "true";
            const name = button.getAttribute("data-debt-name") || "";

            statusForm.action = button.getAttribute("data-status-url");
            debtLabel.textContent = name;
            statusTitle.textContent = active ? "Desactivar deuda" : "Activar deuda";
            statusMessage.textContent = active
                ? "La deuda se conservara para consulta, pero dejara de sumar como deuda activa."
                : "La deuda volvera a contar dentro de tus obligaciones activas.";

            if (statusButton) {
                statusButton.textContent = active ? "Desactivar" : "Activar";
            }
        });
    }

    document.querySelectorAll("[data-debt-inline-status]").forEach((form) => {
        form.addEventListener("submit", (event) => {
            const button = form.querySelector("[data-debt-active]");
            const active = button && button.getAttribute("data-debt-active") === "true";
            const message = active
                ? "Confirma que deseas desactivar esta deuda."
                : "Confirma que deseas activar esta deuda.";

            if (!window.confirm(message)) {
                event.preventDefault();
            }
        });
    });

    const deudaForm = document.querySelector("[data-debt-form]");
    if (!deudaForm) {
        return;
    }

    const saldoInicial = deudaForm.querySelector("[data-saldo-inicial]");
    const saldoActual = deudaForm.querySelector("[data-saldo-actual]");
    const progressBar = deudaForm.querySelector("[data-progress-bar]");
    const progressLabel = deudaForm.querySelector("[data-progress-label]");

    const updateProgress = () => {
        const inicial = Number.parseFloat(saldoInicial.value);
        const actual = Number.parseFloat(saldoActual.value);

        if (!Number.isFinite(inicial) || inicial <= 0 || !Number.isFinite(actual)) {
            progressBar.style.setProperty("--progress", "0%");
            progressLabel.textContent = "0 %";
            return;
        }

        const boundedActual = Math.min(Math.max(actual, 0), inicial);
        const percent = Math.min(Math.max(((inicial - boundedActual) / inicial) * 100, 0), 100);
        progressBar.style.setProperty("--progress", `${percent}%`);
        progressLabel.textContent = `${Math.round(percent)} %`;
    };

    if (saldoInicial && saldoActual && progressBar && progressLabel) {
        saldoInicial.addEventListener("input", () => {
            if (!saldoActual.value && saldoInicial.value) {
                saldoActual.value = saldoInicial.value;
            }
            updateProgress();
        });
        saldoActual.addEventListener("input", updateProgress);
        updateProgress();
    }
});
