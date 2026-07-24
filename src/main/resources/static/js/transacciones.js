document.addEventListener("DOMContentLoaded", () => {
    const deleteModal = document.getElementById("deleteTransactionModal");
    const deleteForm = document.querySelector("[data-delete-form]");
    const deleteLabel = document.querySelector("[data-delete-label]");
    const conceptSelect = document.querySelector("[data-concept-select]");
    const conceptType = document.querySelector("[data-concept-type]");

    if (deleteModal && deleteForm) {
        deleteModal.addEventListener("show.bs.modal", (event) => {
            const button = event.relatedTarget;
            deleteForm.action = button.getAttribute("data-delete-url");

            if (deleteLabel) {
                deleteLabel.textContent = button.getAttribute("data-delete-name") || "";
            }
        });
    }

    function updateConceptType() {
        if (!conceptSelect || !conceptType) {
            return;
        }

        const selectedOption = conceptSelect.options[conceptSelect.selectedIndex];
        const tipo = selectedOption ? selectedOption.dataset.tipo : "";
        conceptType.classList.remove("income", "expense");

        if (!tipo) {
            conceptType.textContent = "Selecciona un concepto para ver su tipo";
            return;
        }

        conceptType.textContent = `Tipo: ${tipo}`;
        conceptType.classList.add(tipo === "INGRESO" ? "income" : "expense");
    }

    if (conceptSelect) {
        conceptSelect.addEventListener("change", updateConceptType);
        updateConceptType();
    }
});
