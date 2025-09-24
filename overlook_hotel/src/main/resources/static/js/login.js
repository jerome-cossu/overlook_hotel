function togglePassword() {
    const passwordInput = document.getElementById('password');
    passwordInput.type = passwordInput.type === 'password' ? 'text' : 'password';
}

document.getElementById("loginForm").addEventListener("submit", async function(e) {
    e.preventDefault();

    const data = {
        email: document.getElementById("email").value,
        password: document.getElementById("password").value
    };

    try {
        const response = await fetch("/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            const result = await response.json(); 
            localStorage.setItem("user", JSON.stringify({
                email: result.email,
                firstName: result.firstName
            }));
            alert("Connexion done !");
            console.log("Login result:", result);

            localStorage.setItem("token", result.token);

            window.location.href = "/home";
        } else {
            const error = await response.json();
            alert("Error : " + (error.message || "Incorrect email or password"));
        }
    } catch (err) {
        console.error(err);
        alert("Network error");
    }
});