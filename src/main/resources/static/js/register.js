function togglePassword() {
    const passwordInput = document.getElementById('password');
    passwordInput.type = passwordInput.type === 'password' ? 'text' : 'password';
}

document.getElementById("registerForm").addEventListener("submit", async function(e) {
    e.preventDefault();

    const data = {
        first_name: document.getElementById("firstName").value,
        last_name: document.getElementById("lastName").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        roleId: parseInt(document.getElementById("role").value)
    };

    try {
        const response = await fetch("/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            alert("Signing up done !");
            window.location.href = "/login";
        } else {
            const error = await response.json();
            alert("Error : " + (error.message || "Unable to create account."));
        }
    } catch (err) {
        console.error(err);
        alert("An error occurred.");
    }
});