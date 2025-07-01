window.onload = function () {
    console.log("Page loaded, checking selected report type...");

    var selectedReportType = document.getElementById("reportType").value;
    var salesData = document.getElementById("salesReportBody").innerHTML.trim();
    var inventoryData = document.getElementById("inventoryReportBody").innerHTML.trim();

    if (selectedReportType === "sales" && salesData !== "") {
        document.getElementById("salesReportSection").style.display = "block";
    } else if (selectedReportType === "inventory" && inventoryData !== "") {
        document.getElementById("inventoryReportSection").style.display = "block";
    } else {
        document.getElementById("noDataMessage").style.display = "block";
    }
};

// Debugging form submission
document.getElementById("reportForm").onsubmit = function () {
    console.log("Form submitted to ReportController");
};

document.getElementById('reportForm').addEventListener('submit', function (e) {
    const start = new Date(document.getElementById('startDate').value);
    const end = new Date(document.getElementById('endDate').value);
    const today = new Date();

    // Normalize time to midnight for accurate comparison
    today.setHours(0, 0, 0, 0);
    end.setHours(0, 0, 0, 0);

    if (isNaN(start) || isNaN(end)) {
        alert("Please enter both start and end dates.");
        e.preventDefault();
        return;
    }

    if (start > end) {
        alert("Start date cannot be after end date.");
        e.preventDefault();
        return;
    }

    if (end > today) {
        alert("End date cannot be in the future.");
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
