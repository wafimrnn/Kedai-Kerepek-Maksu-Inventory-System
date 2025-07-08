window.onload = function () {
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
  const loginForm = document.getElementById("login-form-data");
  if (loginForm) {
    loginForm.addEventListener("submit", function (e) {
      e.preventDefault();
      this.submit(); // Login can proceed
    });
  }

  // Handle sign-up form validation and submission
  const signupForm = document.getElementById("signup-form-data");
  if (signupForm) {
    signupForm.addEventListener("submit", function (e) {
      e.preventDefault();

      const username = document.getElementById("signup-username").value.trim();
      const password = document.getElementById("signup-password").value.trim();
      const phone = document.getElementById("signup-phone").value.trim();
      const address = document.getElementById("signup-address").value.trim();
      const signupMessageElement = document.getElementById("signup-message");

      const isUsernameValid = /^[a-zA-Z0-9]+$/.test(username);        // Alphanumeric only
      const isPasswordValid = password.length >= 6;                    // At least 6 characters
      const isPhoneValid = /^[0-9]{10,11}$/.test(phone);               // Only digits, 10–11
      const isAddressValid = /^[a-zA-Z0-9\s,.\-]+$/.test(address);     // Letters/numbers/spaces/.,-

      if (!isUsernameValid || !isPasswordValid || !isPhoneValid || !isAddressValid) {
        signupMessageElement.textContent = "Invalid input. Please enter the correct data type for each field.";
        return; // Stop submission
      }

      signupMessageElement.textContent = ""; // Clear message
      this.submit(); // ✅ Submit if all valid
    });
  }

  // Display feedback message on load
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
