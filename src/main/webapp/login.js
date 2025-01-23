const apiUrl = "https://microserviceaccount-a6bvgxbgh9hfdffe.southeastasia-01.azurewebsites.net"; // Your Azure microservice URL

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

// Handle login
document.getElementById("login-form-data").addEventListener("submit", async function (e) {
  e.preventDefault();
  const data = {
    userName: document.getElementById("login-username").value,
    password: document.getElementById("login-password").value
  };

  try {
    const response = await fetch(`${apiUrl}/auth/login`, { // Use Azure URL here
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });

    if (response.ok) {
      const res = await response.json();
      alert("Login Successful! Redirecting...");
      window.location.href = res.redirectUrl; // Redirect to dashboard (using redirectUrl from backend)
    } else {
      const error = await response.json();
      alert("Login Failed: " + error.message);
    }
  } catch (err) {
    alert("An error occurred: " + err);
  }
});

// Handle signup
document.getElementById("signup-form-data").addEventListener("submit", async function (e) {
  e.preventDefault();
  const data = {
    userName: document.getElementById("signup-username").value,
    password: document.getElementById("signup-password").value,
    phone: document.getElementById("signup-phone").value,
    role: document.getElementById("signup-role").value,
    address: document.getElementById("signup-address").value
  };

  try {
    const response = await fetch(`${apiUrl}/auth/signup`, { // Use Azure URL here
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });

    if (response.ok) {
      const res = await response.json();
      alert("Signup Successful! Redirecting...");
      window.location.href = res.redirectUrl; // Redirect to dashboard (using redirectUrl from backend)
    } else {
      const error = await response.json();
      alert("Signup Failed: " + error.message);
    }
  } catch (err) {
    alert("An error occurred: " + err);
  }
});

