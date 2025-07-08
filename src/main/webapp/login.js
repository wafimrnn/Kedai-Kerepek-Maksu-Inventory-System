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

  // Custom Sign-up validation
  const signupForm = document.getElementById("signup-form-data");
  if (signupForm) {
    signupForm.addEventListener("submit", function (e) {
      e.preventDefault();

      const username = document.getElementById("signup-username").value.trim();
      const password = document.getElementById("signup-password").value.trim();
      const phone = document.getElementById("signup-phone").value.trim();
      const address = document.getElementById("signup-address").value.trim();
      const message = document.getElementById("signup-message");

      const isUsernameValid = /^[a-zA-Z0-9]+$/.test(username);        // Alphanumeric only
      const isPasswordValid = password.length >= 6;
      const isPhoneValid = /^[0-9]{10,11}$/.test(phone);
      const isAddressValid = /^[a-zA-Z0-9\s,.\-]+$/.test(address);

      if (!isUsernameValid || !isPasswordValid || !isPhoneValid || !isAddressValid) {
        message.textContent = "Invalid input. Please enter the correct data type for each field.";
        return;
      }

      message.textContent = "";
      this.submit(); // ✅ Only submits if all valid
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
