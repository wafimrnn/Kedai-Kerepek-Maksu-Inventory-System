// Function to toggle between login and signup forms
    window.toggleForm = function (formType) {
      console.log(`toggleForm called with formType: ${formType}`);
      const loginForm = document.getElementById('login-form');
      const signupForm = document.getElementById('signup-form');

      if (formType === 'signup') {
        loginForm.style.display = 'none';
        signupForm.style.display = 'block';
      } else {
        loginForm.style.display = 'block';
        signupForm.style.display = 'none';
      }
    };

    // Handle login form submission
    document.getElementById("login-form-data").addEventListener("submit", function (e) {
      e.preventDefault();
      this.submit(); // Directly submit the form
    });

    // ✅ Handle signup form submission with validation (updated)
    document.getElementById("signup-form-data").addEventListener("submit", function (e) {
      e.preventDefault(); // Prevent form submission for validation first

      const username = document.getElementById("signup-username").value.trim();
      const password = document.getElementById("signup-password").value.trim();
      const phone = document.getElementById("signup-phone").value.trim();
      const address = document.getElementById("signup-address").value.trim();
      const signupMessageElement = document.getElementById("signup-message");

      // Validation checks
      const isUsernameValid = /^[a-zA-Z0-9]+$/.test(username); // Alphanumeric only
      const isPasswordValid = password.length >= 6; // At least 6 characters
      const isPhoneValid = /^[0-9]{10,11}$/.test(phone); // 10 or 11 digit numbers
      const isAddressValid = /^[a-zA-Z0-9\s,.\-]+$/.test(address); // Letters, numbers, spaces, , . -

      if (!isUsernameValid || !isPasswordValid || !isPhoneValid || !isAddressValid) {
        signupMessageElement.textContent = "Invalid input. Please enter the correct data type for each field.";
        return; // Block form submission
      }

      signupMessageElement.textContent = ""; // Clear any previous error
      this.submit(); // Proceed to backend if all inputs are valid
    });

    // Display feedback message for login or signup based on the page
    window.onload = function () {
      const urlParams = new URLSearchParams(window.location.search);
      const message = urlParams.get('message');

      // Check if the login message element exists
      const loginMessageElement = document.getElementById('login-message');
      if (loginMessageElement && message) {
        loginMessageElement.textContent = message;
      }

      // Check if the signup message element exists
      const signupMessageElement = document.getElementById('signup-message');
      if (signupMessageElement && message) {
        signupMessageElement.textContent = message;
      }
    };