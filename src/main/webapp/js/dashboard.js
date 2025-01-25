document.addEventListener("DOMContentLoaded", function () {
        const notificationIcon = document.getElementById("notification-icon");

        // Function to fetch notifications
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
                        // If there are notifications, change the icon's color to indicate activity
                        notificationIcon.style.color = "red";
                        notificationIcon.setAttribute("title", `You have ${notifications.length} notifications`);
                    } else {
                        // If no notifications, reset the icon
                        notificationIcon.style.color = "";
                        notificationIcon.setAttribute("title", "Notifications");
                    }
                })
                .catch(error => {
                    console.error("Error fetching notifications:", error);
                });
        }

        // Fetch notifications immediately and set an interval to check periodically
        fetchNotifications();
        setInterval(fetchNotifications, 60000); // Check every 60 seconds
    });
	
//pop up notification
document.addEventListener("DOMContentLoaded", () => {
    const notificationIcon = document.getElementById("notification-icon");
    const notificationPopup = document.getElementById("notification-popup");

    // Toggle the notification popup visibility when clicking the bell icon
    notificationIcon.addEventListener("click", () => {
        notificationPopup.style.display = notificationPopup.style.display === "none" ? "block" : "none";

        // Fetch notifications when the popup is displayed
        if (notificationPopup.style.display === "block") {
            fetchNotifications();
        }
    });

    // Function to fetch notifications from the backend
    function fetchNotifications() {
        fetch('/api/notification') // Call your backend endpoint
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then(notifications => {
                displayNotifications(notifications); // Populate the popup
            })
            .catch(error => {
                console.error('Error fetching notifications:', error);
            });
    }

    // Function to populate the notifications in the popup
    function displayNotifications(notifications) {
        const notificationList = document.getElementById("notification-list");
        notificationList.innerHTML = ""; // Clear previous notifications

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
});