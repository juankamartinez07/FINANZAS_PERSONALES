document.addEventListener("DOMContentLoaded", () => {
    const statusModal = document.getElementById("conceptStatusModal");
    const statusForm = document.querySelector("[data-concept-status-form]");
    const statusTitle = document.querySelector("[data-concept-status-title]");
    const statusMessage = document.querySelector("[data-concept-status-message]");
    const conceptLabel = document.querySelector("[data-concept-label]");
    const statusButton = document.querySelector("[data-concept-status-button] span");

    if (statusModal && statusForm) {
        statusModal.addEventListener("show.bs.modal", (event) => {
            const button = event.relatedTarget;
            const active = button.getAttribute("data-concept-active") === "true";
            const name = button.getAttribute("data-concept-name") || "";

            statusForm.action = button.getAttribute("data-status-url");
            conceptLabel.textContent = name;
            statusTitle.textContent = active ? "Desactivar concepto" : "Activar concepto";
            statusMessage.textContent = active
                ? "El concepto se conservara en historicos, pero no aparecera en nuevos registros."
                : "El concepto volvera a estar disponible para nuevos registros.";

            if (statusButton) {
                statusButton.textContent = active ? "Desactivar" : "Activar";
            }
        });
    }

    document.querySelectorAll("[data-concept-inline-status]").forEach((form) => {
        form.addEventListener("submit", (event) => {
            const button = form.querySelector("[data-concept-active]");
            const active = button && button.getAttribute("data-concept-active") === "true";
            const message = active
                ? "Confirma que deseas desactivar este concepto."
                : "Confirma que deseas activar este concepto.";

            if (!window.confirm(message)) {
                event.preventDefault();
            }
        });
    });

    const typeSelect = document.querySelector("[data-concept-type]");
    const preview = document.querySelector("[data-concept-type-preview]");
    if (!typeSelect || !preview) {
        return;
    }

    const updatePreview = () => {
        preview.classList.remove("income", "expense");
        if (typeSelect.value === "INGRESO") {
            preview.classList.add("income");
            preview.innerHTML = '<i class="bi bi-arrow-down-left"></i><span>Este concepto sumara como ingreso en nuevas transacciones.</span>';
            return;
        }
        if (typeSelect.value === "GASTO") {
            preview.classList.add("expense");
            preview.innerHTML = '<i class="bi bi-arrow-up-right"></i><span>Este concepto sumara como gasto en nuevas transacciones.</span>';
            return;
        }
        preview.innerHTML = '<i class="bi bi-tags"></i><span>Selecciona el tipo del concepto.</span>';
    };

    typeSelect.addEventListener("change", updatePreview);
    updatePreview();
});
