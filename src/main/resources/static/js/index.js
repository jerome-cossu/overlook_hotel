const user = JSON.parse(localStorage.getItem("user"));

if (user) {
    const nav = document.querySelector("nav");
    const loginBtn = nav.querySelector(".login-btn");
    if (loginBtn) {
        loginBtn.textContent = "Hello " + user.firstName;
        loginBtn.removeAttribute("href");
    }
}
function logout() {
    localStorage.removeItem("user");
    localStorage.removeItem("token");

    window.location.href = "/login";
}