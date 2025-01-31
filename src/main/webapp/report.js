window.onload = function () {
    console.log("Page loaded, checking selected report type...");

    // Fetch values from JSP
    var selectedReportType = document.getElementById("reportType").value;
    var salesData = document.getElementById("salesReportBody").innerHTML.trim();
    var inventoryData = document.getElementById("inventoryReportBody").innerHTML.trim();

    console.log("Selected Report Type:", selectedReportType);
    console.log("Sales Data:", salesData);
    console.log("Inventory Data:", inventoryData);

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
