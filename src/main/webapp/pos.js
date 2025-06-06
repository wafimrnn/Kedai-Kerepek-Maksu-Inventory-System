// Event Listener for Add to Order buttons
document.querySelectorAll(".add-to-order").forEach(button => {
    button.addEventListener("click", function() {
        const prodId = this.getAttribute("data-prodId");
        const prodName = this.getAttribute("data-prodName");
        const prodPrice = parseFloat(this.getAttribute("data-prodPrice"));

        addToOrder(prodName, prodPrice, prodId);
    });
});

// Function to toggle between payment methods
function togglePayment(method) {
    const formattedMethod = method.toUpperCase(); // Convert method to uppercase
    document.getElementById("CASH").style.display = formattedMethod === "CASH" ? "block" : "none";
    document.getElementById("QR").style.display = formattedMethod === "QR" ? "block" : "none";
    document.getElementById("payment-method").value = formattedMethod; // Set the uppercase value
}

// Function to add items to the order
function addToOrder(productName, productPrice, prodId) {
    const orderItems = document.getElementById("order-items");
    const existingRow = document.querySelector(`[data-product-name="${productName}"]`);

    if (existingRow) {
        // If the product already exists, update the quantity and subtotal
        const qtyInput = existingRow.querySelector(".qty input");
        const subtotalCell = existingRow.querySelector(".subtotal");

        const newQty = parseInt(qtyInput.value) + 1; // Increase quantity by 1
        qtyInput.value = newQty; // Update the quantity input field
        subtotalCell.textContent = `RM ${(newQty * productPrice).toFixed(2)}`; // Update subtotal
    } else {
        // If the product doesn't exist, create a new row
        const row = document.createElement("tr");
        row.setAttribute("data-product-name", productName);
        row.setAttribute("data-product-id", prodId);

        row.innerHTML = `
            <td>${productName}</td>
            <td class="qty">
                <input type="number" min="1" value="1" onchange="updateRowSubtotal(this, ${productPrice})">
            </td>
            <td class="subtotal">RM ${productPrice.toFixed(2)}</td>
            <td><button onclick="removeItem(this)">Remove</button></td>
        `;

        orderItems.appendChild(row); // Append the new row to the table
    }

    updateTotals(); // Recalculate totals
}

function updateRowSubtotal(input, productPrice) {
    const newQty = parseInt(input.value) || 1; // Default to 1 if input is invalid
    input.value = newQty; // Ensure the input value is valid
    const row = input.closest("tr");
    const subtotalCell = row.querySelector(".subtotal");
    subtotalCell.textContent = `RM ${(newQty * productPrice).toFixed(2)}`;

    updateTotals(); // Recalculate the overall totals
}

// Function to update totals
function updateTotals() {
    let subtotal = 0;
    document.querySelectorAll("#order-items tr").forEach((row) => {
        const subtotalText = row.querySelector(".subtotal").textContent;
        subtotal += parseFloat(subtotalText.replace("RM ", ""));
    });

    const total = subtotal;
    document.getElementById("subtotal").textContent = `RM ${subtotal.toFixed(2)}`;
    document.getElementById("total").textContent = `RM ${total.toFixed(2)}`;
    document.getElementById("total-amount").value = total;
}

// Function to remove items from the order
function removeItem(button) {
    const row = button.closest("tr");
    row.remove();
    updateTotals();
}

// Function to calculate change for cash payment
function calculateChange() {
    const moneyReceived = parseFloat(document.getElementById("money-received").value) || 0;
    const totalAmount = parseFloat(document.getElementById("total-amount").value) || 0;
    const change = moneyReceived - totalAmount;

    document.getElementById("change").value = change >= 0 ? `RM ${change.toFixed(2)}` : "Insufficient";
}

// Function to handle order completion
function completeOrder() {
    const totalAmount = parseFloat(document.getElementById("total-amount").value) || 0;
    const paymentMethod = document.getElementById("payment-method").value;
    const saleDate = new Date().toISOString().split("T")[0];

    if (totalAmount > 0) {
        const orderItems = [];
        document.querySelectorAll("#order-items tr").forEach((row) => {
            const prodId = parseInt(row.getAttribute("data-product-id"));
            const qty = parseInt(row.querySelector(".qty input").value);
            const subtotal = parseFloat(row.querySelector(".subtotal").textContent.replace("RM ", ""));
            orderItems.push({ prodId, qty, subtotal });
        });

        if (orderItems.length === 0) {
            alert("No items in the order!");
            return;
        }

        let moneyReceived = 0;
        if (paymentMethod === "CASH") {
            moneyReceived = parseFloat(document.getElementById("money-received").value || "0");
            if (moneyReceived < totalAmount) {
                alert("Insufficient money received. Please enter the correct amount.");
                return;
            }
        }

        const requestData = {
            totalAmount: totalAmount,
            paymentMethod: paymentMethod,
            saleDate: saleDate,
            orderItems: orderItems,
            moneyReceived: moneyReceived
        };

        fetch(contextPath + "/completeSale", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(requestData),
        })
        .then(response => {
            if (response.ok) return response.json();
            else throw new Error("Failed to complete the order.");
        })
        .then(data => {
            generateReceipt(); // ✅ Only print after successful response

            alert("Order Completed Successfully!");
            document.getElementById("order-items").innerHTML = ""; // Clear order items
            document.getElementById("money-received").value = "";   // ✅ Clear cash input
            document.getElementById("change").value = "";           // ✅ Clear change display
            updateTotals();
        })
        .catch(error => {
            alert("Error: " + error.message);
        });

    } else {
        alert("No items in the order to complete!");
    }
}

// Function to generate receipt
function generateReceipt() {
    const orderItems = document.querySelectorAll("#order-items tr");
    if (orderItems.length === 0) {
        alert("No items in the order!");
        return;
    }

    let receiptContent = "Kedai Kerepek Maksu\n===================\n";
    orderItems.forEach(row => {
        const productName = row.getAttribute("data-product-name");
        const qty = row.querySelector(".qty input").value;
        const subtotal = row.querySelector(".subtotal").textContent;
        receiptContent += `${productName} x${qty} - ${subtotal}\n`;
    });

    const total = document.getElementById("total").textContent;
    receiptContent += "-------------------\n";
    receiptContent += `Total: ${total}\n`;
    receiptContent += `Payment: ${document.getElementById("payment-method").value}\n`;
    receiptContent += "Thank you for your purchase!";

    // Open receipt in a new window for printing
    const receiptWindow = window.open("", "Receipt", "width=400,height=600");
    receiptWindow.document.write(`<pre>${receiptContent}</pre>`);
    receiptWindow.document.close();
    receiptWindow.print();
}
