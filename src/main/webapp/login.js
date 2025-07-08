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
      this.submit();
    });
  }

  // Handle sign-up form validation and submission
  const signupForm = document.getElementById("signup-form-data");
  if (signupForm) {
    signupForm.addEventListener("submit", function (e) {
      const form = this;
      const signupMessageElement = document.getElementById("signup-message");

      if (!form.checkValidity()) {
        e.preventDefault();
        signupMessageElement.textContent = "Invalid input. Please enter the correct data type for each field.";
      } else {
        signupMessageElement.textContent = "";
        form.submit();
      }
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
