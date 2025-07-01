window.onload = function () {
    console.log("Page loaded, checking selected report type...");

    const selectedReportType = document.getElementById("reportType").value;
    const salesData = document.getElementById("salesReportBody").innerHTML.trim();
    const inventoryData = document.getElementById("inventoryReportBody").innerHTML.trim();

    if (selectedReportType === "sales" && salesData !== "") {
        document.getElementById("salesReportSection").style.display = "block";
    } else if (selectedReportType === "inventory" && inventoryData !== "") {
        document.getElementById("inventoryReportSection").style.display = "block";
    } else {
        document.getElementById("noDataMessage").style.display = "block";
    }
};

// Debug form submission
document.getElementById("reportForm").onsubmit = function () {
    console.log("Form submitted to ReportController");
};

// ✅ Updated date validation logic
document.getElementById('reportForm').addEventListener('submit', function (e) {
    const startInput = document.getElementById('startDate').value;
    const endInput = document.getElementById('endDate').value;

    if (!startInput || !endInput) {
        alert("Please enter both start and end dates.");
        e.preventDefault();
        return;
    }

    const start = new Date(startInput);
    const end = new Date(endInput);

    const today = new Date();
    today.setHours(0, 0, 0, 0); // Clear time for accurate comparison

    // Validation rules
    if (start > end) {
        alert("Start date cannot be after end date.");
        e.preventDefault();
        return;
    }

    if (end > today) {
        alert("End date must be today or earlier.");
        e.preventDefault();
        return;
    }
});

window.addEventListener('DOMContentLoaded', () => {
    const salesData = document.getElementById('salesReportBody').innerHTML.trim();
    const inventoryData = document.getElementById('inventoryReportBody').innerHTML.trim();

    if (salesData) {
        document.getElementById('salesReportSection').style.display = 'block';
    } else if (inventoryData) {
        document.getElementById('inventoryReportSection').style.display = 'block';
    } else {
        document.getElementById('noDataMessage').style.display = 'block';
    }
});
