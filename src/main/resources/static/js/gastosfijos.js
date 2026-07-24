document.addEventListener("DOMContentLoaded", () => {
    const statusModal = document.getElementById("fixedStatusModal");
    const statusForm = document.querySelector("[data-fixed-status-form]");
    const statusTitle = document.querySelector("[data-fixed-status-title]");
    const statusMessage = document.querySelector("[data-fixed-status-message]");
    const fixedLabel = document.querySelector("[data-fixed-label]");
    const statusButton = document.querySelector("[data-fixed-status-button] span");

    if (statusModal && statusForm) {
        statusModal.addEventListener("show.bs.modal", (event) => {
            const button = event.relatedTarget;
            const active = button.getAttribute("data-fixed-active") === "true";
            const name = button.getAttribute("data-fixed-name") || "";

            statusForm.action = button.getAttribute("data-status-url");
            fixedLabel.textContent = name;
            statusTitle.textContent = active ? "Desactivar gasto fijo" : "Activar gasto fijo";
            statusMessage.textContent = active
                ? "El gasto fijo se conservara para consulta, pero dejara de sumar como compromiso activo."
                : "El gasto fijo volvera a contar dentro de los compromisos activos.";

            if (statusButton) {
                statusButton.textContent = active ? "Desactivar" : "Activar";
            }
        });
    }

    document.querySelectorAll("[data-fixed-inline-status]").forEach((form) => {
        form.addEventListener("submit", (event) => {
            const button = form.querySelector("[data-fixed-active]");
            const active = button && button.getAttribute("data-fixed-active") === "true";
            const message = active
                ? "Confirma que deseas desactivar este gasto fijo."
                : "Confirma que deseas activar este gasto fijo.";

            if (!window.confirm(message)) {
                event.preventDefault();
            }
        });
    });

    const fixedForm = document.querySelector("[data-fixed-form]");
    if (!fixedForm) {
        return;
    }

    const dayInput = fixedForm.querySelector("[data-fixed-day]");
    const preview = fixedForm.querySelector("[data-fixed-preview]");

    const formatDate = (date) => {
        return new Intl.DateTimeFormat("es-CO", {
            day: "2-digit",
            month: "2-digit",
            year: "numeric"
        }).format(date);
    };

    const dateWithValidDay = (year, month, day) => {
        const lastDay = new Date(year, month + 1, 0).getDate();
        return new Date(year, month, Math.min(day, lastDay));
    };

    const updatePreview = () => {
        const day = Number.parseInt(dayInput.value, 10);
        if (!Number.isInteger(day) || day < 1 || day > 31) {
            preview.textContent = "Define un dia de pago valido";
            return;
        }

        const today = new Date();
        today.setHours(0, 0, 0, 0);
        let nextDate = dateWithValidDay(today.getFullYear(), today.getMonth(), day);
        if (nextDate < today) {
            nextDate = dateWithValidDay(today.getFullYear(), today.getMonth() + 1, day);
        }
        preview.textContent = formatDate(nextDate);
    };

    if (dayInput && preview) {
        dayInput.addEventListener("input", updatePreview);
        updatePreview();
    }
});
