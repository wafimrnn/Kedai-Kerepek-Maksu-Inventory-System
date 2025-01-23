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

// Handle signup form submission
document.getElementById("signup-form-data").addEventListener("submit", function (e) {
    e.preventDefault();
    this.submit(); // Directly submit the form
});

