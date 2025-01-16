
const searchInput = document.getElementById("search-input");
const suggestionsBox = document.getElementById("suggestions-box");

searchInput.addEventListener("input", () => {
    const query = searchInput.value;
    suggestionsBox.style.display = "block";

    if (query.length < 2) {
        suggestionsBox.innerHTML = ""; // Clear suggestions if input is too short
        return;
    }

    fetch(`/suggest-keywords?prefix=${encodeURIComponent(query)}`)
        .then(response => response.json())
        .then(data => {
            suggestionsBox.innerHTML = data
                .map(keyword => `<div class="suggestion-item" onclick="selectSuggestion('${keyword}')">${keyword}</div>`)
                .join("");
        })
        .catch(error => console.error("Error fetching suggestions:", error));
});

document.getElementById("search-input").addEventListener("keydown", (event) => { 
    if (event.key === "Enter") {
        event.preventDefault(); // Prevent form submission
        
        const firstSuggestion = document.querySelector(".suggestion-item");
        if (firstSuggestion) {
            searchInput.value = firstSuggestion.textContent; // Set input to the first suggestion
        }
        
        document.querySelector("form.search-form").submit(); // Submit the form
        
        clearSearch(); // Clear input and suggestions after search
    }
});

function selectSuggestion(keyword) {
    searchInput.value = keyword; // Set input value to selected suggestion
    suggestionsBox.innerHTML = ""; // Clear suggestions
    document.querySelector("form.search-form").submit(); // Submit the form
    
    clearSearch(); // Clear input and suggestions after search
}

function clearSearch() {
    searchInput.value = ""; // Clear the search input
    suggestionsBox.innerHTML = ""; // Clear the suggestions box
    suggestionsBox.style.display = "none"; // Hide the suggestions box
}

// Add an event listener to the search icon (submit button)
const searchButton = document.querySelector(".search-icon");
searchButton.addEventListener("click", (event) => {
    event.preventDefault(); // Prevent the form from submitting immediately
    
    // You can check if the input has a value, if needed
    if (searchInput.value.trim() !== "") {
        document.querySelector("form.search-form").submit(); // Submit the form
    }

    clearSearch(); // Clear input and suggestions when search icon is clicked
});
