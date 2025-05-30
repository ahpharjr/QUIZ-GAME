document.addEventListener("DOMContentLoaded", attachStarListeners);

function attachStarListeners() {
    document.querySelectorAll(".star").forEach(star => {
        const cardElement = star.closest(".flashcard");
        const cardId = cardElement.getAttribute("data-card-id");
        const userId = document.body.getAttribute("data-user-id"); // Get the correct userId

        console.log(`User ID: ${userId}`);

        fetch(`/collections/check?cardId=${cardId}`)
            .then(response => response.json())
            .then(isInCollection => {
                const tooltip = star.querySelector(".tooltip");
                if (isInCollection) {
                    tooltip.textContent = "Remove Card";
                    star.classList.add("in-collection");
                } else {
                    tooltip.textContent = "Add to Collection";
                    star.classList.remove("in-collection");
                }

                star.addEventListener("click", function (event) {
                    event.stopPropagation();
                    if (star.classList.contains("in-collection")) {
                        // Remove card from collection
                        fetch(`/collections/remove`, {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/x-www-form-urlencoded",
                            },
                            body: `cardId=${cardId}`
                        })
                        .then(response => {
                            if (response.ok) {
                                tooltip.textContent = "Add to Collection";
                                star.classList.remove("in-collection");
                                console.log("Card removed successfully");
                            } else {
                                console.error("Failed to remove card");
                            }
                        });
                    } else {
                        // Add card to collection
                        fetch('/collections/add', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded',
                            },
                            body: `cardId=${cardId}`
                        })
                        .then(response => {
                            if (response.ok) {
                                tooltip.textContent = "Remove Card";
                                star.classList.add("in-collection");
                                console.log("Card added successfully");
                            } else {
                                console.error("Failed to add card");
                            }
                        });
                    }
                });
            });
    });
}
