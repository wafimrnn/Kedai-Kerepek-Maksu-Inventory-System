document.getElementById('reportForm').addEventListener('submit', function (e) {
    const start = new Date(document.getElementById('startDate').value);
    const end = new Date(document.getElementById('endDate').value);
    const today = new Date();

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
