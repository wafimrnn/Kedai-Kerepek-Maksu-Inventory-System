window.onload = function () {
  // Toggle between login and signup forms
  window.toggleForm = function (formType) {
    const loginForm = document.getElementById('login-form');
    const signupForm = document.getElementById('signup-form');

    loginForm.style.display = (formType === 'signup') ? 'none' : 'block';
    signupForm.style.display = (formType === 'signup') ? 'block' : 'none';
  };

  // Handle login
  const loginForm = document.getElementById("login-form-data");
  if (loginForm) {
    loginForm.addEventListener("submit", function (e) {
      e.preventDefault();
      this.submit();
    });
  }

  // Handle sign-up with validation
  const signupForm = document.getElementById("signup-form-data");
  if (signupForm) {
    signupForm.addEventListener("submit", function (e) {
      e.preventDefault();

      const usernameField = document.getElementById("signup-username");
      const passwordField = document.getElementById("signup-password");
      const phoneField = document.getElementById("signup-phone");
      const addressField = document.getElementById("signup-address");
      const message = document.getElementById("signup-message");

      const username = usernameField.value.trim();
      const password = passwordField.value.trim();
      const phone = phoneField.value.trim();
      const address = addressField.value.trim();

      // Validation checks using regex and length
      const isUsernameValid = /^[a-zA-Z0-9]+$/.test(username);          // Alphanumeric only
      const isPasswordValid = password.length >= 6;                     // Min 6 characters
      const isPhoneValid = /^[0-9]{10,11}$/.test(phone);                // 10-11 digit numbers
      const isAddressValid = /^[a-zA-Z0-9\s,.\-]+$/.test(address);      // Letters, numbers, space, comma, dot, dash

      // Reset all borders before validation
      [usernameField, passwordField, phoneField, addressField].forEach(field => {
        field.style.border = "";
      });

      if (!isUsernameValid || !isPasswordValid || !isPhoneValid || !isAddressValid) {
        message.textContent = "Invalid input. Please enter the correct data type for each field.";

        if (!isUsernameValid) usernameField.style.border = "2px solid red";
        if (!isPasswordValid) passwordField.style.border = "2px solid red";
        if (!isPhoneValid) phoneField.style.border = "2px solid red";
        if (!isAddressValid) addressField.style.border = "2px solid red";

        return; // Stop form submission
      }

      // If all valid, clear message and submit form
      message.textContent = "";
      this.submit();
    });
  }

  // Show message from server if any
  const urlParams = new URLSearchParams(window.location.search);
  const msg = urlParams.get('message');

  const loginMsg = document.getElementById('login-message');
  if (loginMsg && msg) loginMsg.textContent = msg;

  const signupMsg = document.getElementById('signup-message');
  if (signupMsg && msg) signupMsg.textContent = msg;
};
