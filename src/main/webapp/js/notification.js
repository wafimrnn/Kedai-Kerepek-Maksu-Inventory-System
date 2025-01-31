document.addEventListener("DOMContentLoaded", function () {
    const notificationIcon = document.getElementById("notification-icon");
    const notificationPopup = document.getElementById("notification-popup");
    const notificationList = document.getElementById("notification-list");

    // Check if elements exist before using them
    if (!notificationIcon || !notificationPopup || !notificationList) {
        console.error("Notification elements are missing in the DOM.");
        return; // Exit script early if elements are not found
    }

    // Function to fetch notifications from the backend
    function fetchNotifications() {
        fetch("https://microservice-notification-epdsg9cygpgzb4hr.southeastasia-01.azurewebsites.net/api/notification")
            .then(response => {
                if (!response.ok) {
                    throw new Error("Failed to fetch notifications");
                }
                return response.json();
            })
            .then(notifications => {
                if (notifications.length > 0) {
                    notificationIcon.style.color = "red";
                    notificationIcon.setAttribute("title", `You have ${notifications.length} notifications`);
                    displayNotifications(notifications);
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
        notificationList.innerHTML = "";
        if (notifications.length === 0) {
            notificationList.innerHTML = "<li>No new notifications</li>";
        } else {
            notifications.forEach(notification => {
                const li = document.createElement("li");
                li.textContent = `${notification.prodName} is low on stock!`;
                notificationList.appendChild(li);
            });
        }
    }

    // Fetch notifications immediately and set an interval to check periodically
    fetchNotifications(); // Initial fetch when the page loads
    setInterval(fetchNotifications, 60000); // Check every 60 seconds

    // Toggle the notification popup when clicking the bell icon
    notificationIcon.addEventListener("click", () => {
        notificationPopup.style.display = notificationPopup.style.display === "none" ? "block" : "none";

        if (notificationPopup.style.display === "block") {
            fetchNotifications();
        }
    });
});
