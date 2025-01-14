const profile = document.getElementById("profile");
const profileContainer = document.getElementById("profile-container");
const overlay = document.getElementById("overlay");
const body = document.body; // Select the body for blurring

profile.addEventListener("click", () => {
    profileContainer.classList.toggle("show");
    overlay.style.display = profileContainer.classList.contains("show") ? "block" : "none";
});

// Close profile container and remove blur when clicking outside
document.addEventListener("click", (event) => {
    if (
        !profile.contains(event.target) &&
        !profileContainer.contains(event.target)
    ) {
        profileContainer.classList.remove("show");
        overlay.style.display = "none";
    }
});