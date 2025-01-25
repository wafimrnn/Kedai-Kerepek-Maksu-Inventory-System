document.addEventListener("DOMContentLoaded", function () {
    const notificationIcon = document.getElementById("notification-icon");
    const notificationPopup = document.getElementById("notification-popup");
    const notificationList = document.getElementById("notification-list");

    // Function to fetch notifications from the backend
    function fetchNotifications() {
        fetch("https://microservice-notification-epdsg9cygpgzb4hr.southeastasia-01.azurewebsites.net/api/notification") // Use your deployed microservice URL
            .then(response => {
                if (!response.ok) {
                    throw new Error("Failed to fetch notifications");
                }
                return response.json();
            })
            .then(notifications => {
                // If there are notifications, change the icon's color to indicate activity
                if (notifications.length > 0) {
                    notificationIcon.style.color = "red";
                    notificationIcon.setAttribute("title", `You have ${notifications.length} notifications`);
                    displayNotifications(notifications); // Populate the popup when clicked
                } else {
                    notificationIcon.style.color = "";
                    notificationIcon.setAttribute("title", "Notifications");
                    notificationList.innerHTML = "<li>No new notifications</li>";
                }
            })
            .catch(error => {
                console.error("Error fetching notifications:", error);
            });
    }

    // Function to populate the notifications in the popup
    function displayNotifications(notifications) {
        notificationList.innerHTML = ""; // Clear previous notifications

        if (notifications.length === 0) {
            notificationList.innerHTML = "<li>No new notifications</li>";
        } else {
            notifications.forEach(notification => {
                const li = document.createElement("li");
                li.textContent = `${notification.prodName} is low on stock!`; // Ensure this matches the backend response
                notificationList.appendChild(li);
            });
        }
    }

    // Fetch notifications immediately and set an interval to check periodically
    fetchNotifications(); // Initial fetch when the page loads
    setInterval(fetchNotifications, 60000); // Periodic check every 60 seconds

    // Toggle the notification popup visibility when clicking the bell icon
    notificationIcon.addEventListener("click", () => {
        notificationPopup.style.display = notificationPopup.style.display === "none" ? "block" : "none";

        // Fetch notifications when the popup is displayed
        if (notificationPopup.style.display === "block") {
            fetchNotifications(); // You could also call displayNotifications directly here if needed
        }
    });
});
