// Toggle between login and signup forms
  window.toggleForm = function (formType) {
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
    this.submit(); // Login always allowed to submit
  });

  // ✅ Sign Up form validation and submission
  document.getElementById("signup-form-data").addEventListener("submit", function (e) {
    e.preventDefault();

    const username = document.getElementById("signup-username").value.trim();
    const password = document.getElementById("signup-password").value.trim();
    const phone = document.getElementById("signup-phone").value.trim();
    const address = document.getElementById("signup-address").value.trim();
    const signupMessageElement = document.getElementById("signup-message");

    const isUsernameValid = /^[a-zA-Z0-9]+$/.test(username);        // Alphanumeric only
    const isPasswordValid = password.length >= 6;                    // At least 6 characters
    const isPhoneValid = /^[0-9]{10,11}$/.test(phone);               // Only digits, 10–11
    const isAddressValid = /^[a-zA-Z0-9\s,.\-]+$/.test(address);     // Only letters/numbers/comma/space/dot/hyphen

    if (!isUsernameValid || !isPasswordValid || !isPhoneValid || !isAddressValid) {
      signupMessageElement.textContent = "Invalid input. Please enter the correct data type for each field.";
      return; // Block submission
    }

    signupMessageElement.textContent = ""; // Clear error if valid
    this.submit(); // ✅ Submit only if all fields are valid
  });

  // Display feedback message on load
  window.onload = function () {
    const urlParams = new URLSearchParams(window.location.search);
    const message = urlParams.get('message');

    const loginMessageElement = document.getElementById('login-message');
    if (loginMessageElement && message) {
      loginMessageElement.textContent = message;
    }

    const signupMessageElement = document.getElementById('signup-message');
    if (signupMessageElement && message) {
      signupMessageElement.textContent = message;
    }
  };