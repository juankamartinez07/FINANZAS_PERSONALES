document.addEventListener("DOMContentLoaded", () => {
    const periodSelect = document.querySelector("[data-period-select]");
    const dateInputs = [document.getElementById("desde"), document.getElementById("hasta")];

    const toggleDates = () => {
        const custom = periodSelect && periodSelect.value === "personalizado";
        dateInputs.forEach((input) => {
            if (input) {
                input.disabled = !custom;
            }
        });
    };

    if (periodSelect) {
        periodSelect.addEventListener("change", toggleDates);
        toggleDates();
    }

    const data = window.reportChartData;
    if (!data || typeof Chart === "undefined") {
        document.querySelectorAll(".chart-fallback").forEach((fallback) => {
            fallback.style.display = "block";
        });
        return;
    }

    const moneyFormatter = new Intl.NumberFormat("es-CO", {
        style: "currency",
        currency: "COP",
        maximumFractionDigits: 0
    });

    const toNumbers = (values) => (values || []).map((value) => Number(value || 0));

    const tooltipMoney = {
        callbacks: {
            label: (context) => `${context.dataset.label || context.label}: ${moneyFormatter.format(context.parsed.y ?? context.parsed)}`
        }
    };

    const monthlyCanvas = document.getElementById("monthlyChart");
    if (monthlyCanvas) {
        new Chart(monthlyCanvas, {
            type: "bar",
            data: {
                labels: data.monthlyLabels || [],
                datasets: [
                    { label: "Ingresos", data: toNumbers(data.monthlyIncome), backgroundColor: "rgba(34, 197, 94, 0.72)" },
                    { label: "Gastos", data: toNumbers(data.monthlyExpense), backgroundColor: "rgba(239, 68, 68, 0.72)" },
                    { label: "Balance", data: toNumbers(data.monthlyBalance), type: "line", borderColor: "#1E3A8A", backgroundColor: "#1E3A8A", tension: 0.3 }
                ]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { tooltip: tooltipMoney },
                scales: { y: { ticks: { callback: (value) => moneyFormatter.format(value) } } }
            }
        });
    }

    const createDoughnut = (id, labels, values, palette) => {
        const canvas = document.getElementById(id);
        if (!canvas) {
            return;
        }
        new Chart(canvas, {
            type: "doughnut",
            data: {
                labels: labels || [],
                datasets: [{ data: toNumbers(values), backgroundColor: palette }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { tooltip: tooltipMoney }
            }
        });
    };

    createDoughnut("expenseChart", data.expenseLabels, data.expenseValues, ["#EF4444", "#F59E0B", "#3B82F6", "#8B5CF6", "#14B8A6", "#64748B", "#F97316", "#DC2626", "#94A3B8"]);
    createDoughnut("incomeChart", data.incomeLabels, data.incomeValues, ["#22C55E", "#3B82F6", "#14B8A6", "#84CC16", "#0EA5E9", "#10B981", "#64748B", "#16A34A", "#94A3B8"]);
});
