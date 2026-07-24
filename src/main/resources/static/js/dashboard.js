document.addEventListener("DOMContentLoaded", () => {
    const monthInput = document.querySelector(".dashboard-month-picker input[type='month']");
    if (monthInput) {
        monthInput.addEventListener("change", () => {
            monthInput.form.submit();
        });
    }

    document.querySelectorAll(".bars i, .bars b").forEach((bar) => {
        const height = bar.style.height;
        bar.style.height = "0";
        requestAnimationFrame(() => {
            bar.style.transition = "height 600ms ease";
            bar.style.height = height;
        });
    });
});
