document.addEventListener("DOMContentLoaded", () => {
    const periodSelect = document.querySelector("[data-analysis-period]");
    const dateInputs = [document.getElementById("desde"), document.getElementById("hasta")];

    function syncDateState() {
        const customPeriod = periodSelect && periodSelect.value === "personalizado";
        dateInputs.forEach((input) => {
            if (!input) {
                return;
            }
            input.disabled = !customPeriod;
        });
    }

    if (periodSelect) {
        periodSelect.addEventListener("change", syncDateState);
        syncDateState();
    }

    const showButton = document.querySelector("[data-show-recommendations]");
    if (showButton) {
        showButton.addEventListener("click", () => {
            document.querySelectorAll(".recommendation-item").forEach((item) => {
                item.hidden = false;
            });
            showButton.hidden = true;
        });
    }

    if (!window.Chart || !window.analysisChartData) {
        return;
    }

    const generalChart = document.getElementById("analysisGeneralChart");
    if (generalChart) {
        new Chart(generalChart, {
            type: "doughnut",
            data: {
                labels: ["Gastos", "Margen disponible"],
                datasets: [{
                    data: [window.analysisChartData.gastos || 0, window.analysisChartData.margen || 0],
                    backgroundColor: ["#ef4444", "#22c55e"],
                    borderWidth: 0
                }]
            },
            options: {
                plugins: { legend: { position: "bottom" } },
                responsive: true,
                maintainAspectRatio: true
            }
        });
    }

    const categoryChart = document.getElementById("analysisCategoryChart");
    if (categoryChart) {
        new Chart(categoryChart, {
            type: "bar",
            data: {
                labels: window.analysisChartData.categorias || [],
                datasets: [{
                    label: "Gastos",
                    data: window.analysisChartData.valoresCategorias || [],
                    backgroundColor: "#3b82f6",
                    borderRadius: 6
                }]
            },
            options: {
                plugins: { legend: { display: false } },
                responsive: true,
                maintainAspectRatio: true,
                scales: { y: { beginAtZero: true } }
            }
        });
    }
});
