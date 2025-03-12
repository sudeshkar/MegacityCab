// Load booking details when a booking ID is selected
function loadBookingDetails() {
    const bookingId = document.getElementById("bookingId").value;
    const pickupLocationInput = document.getElementById("pickupLocation");
    const destinationInput = document.getElementById("destination");
    const distanceInput = document.getElementById("distance");

    // Reset fields if no booking is selected
    if (!bookingId) {
        pickupLocationInput.value = "";
        destinationInput.value = "";
        distanceInput.value = "";
        calculateFare();
        return;
    }

    // Find the selected booking from the global bookingList
    const selectedBooking = bookingList.find(booking => booking.bookingNumber == bookingId);
    if (selectedBooking) {
        pickupLocationInput.value = selectedBooking.pickupLocation || "N/A";
        destinationInput.value = selectedBooking.destination || "N/A";
        distanceInput.value = selectedBooking.distance || "";
        
        // Highlight the corresponding table row
        highlightTableRow(bookingId);
    } else {
        pickupLocationInput.value = "";
        destinationInput.value = "";
        distanceInput.value = "";
    }
    calculateFare();
}

// Highlight the row in the table that corresponds to the selected booking
function highlightTableRow(bookingId) {
    // Remove selected class from all rows
    const tableRows = document.querySelectorAll('#bookingsTable tbody tr');
    tableRows.forEach(row => row.classList.remove('selected-row'));
    
    // Add selected class to the row with the matching booking ID
    const selectedRow = document.querySelector(`#bookingsTable tbody tr[data-booking-id="${bookingId}"]`);
    if (selectedRow) {
        selectedRow.classList.add('selected-row');
        
        // Scroll to the selected row to ensure it's visible
        selectedRow.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
}

// Calculate the total fare dynamically
function calculateFare() {
    const baseRate = parseFloat(document.getElementById("baseRate").value) || 0;
    const perKmRate = parseFloat(document.getElementById("perKmRate").value) || 0;
    const distance = parseFloat(document.getElementById("distance").value) || 0;
    const peakHourMultiplier = parseFloat(document.getElementById("peakHourMultiplier").value) || 1;
    const isPeakHour = document.getElementById("isPeakHour").checked;
    const discountAmount = parseFloat(document.getElementById("discountAmount").value) || 0;

    let totalFare = baseRate + (perKmRate * distance);
    if (isPeakHour) {
        totalFare *= peakHourMultiplier;
    }
    totalFare -= discountAmount;

    // Ensure total fare is not negative
    totalFare = Math.max(totalFare, 0);
    document.getElementById("totalFare").value = totalFare.toFixed(2);
}

// Initialize calculation on page load
window.onload = function() {
    calculateFare();
    
    // Add event listener to booking dropdown
    const bookingSelect = document.getElementById("bookingId");
    if (bookingSelect) {
        bookingSelect.addEventListener("change", loadBookingDetails);
    }
    
    // Initialize table row click events
    const tableRows = document.querySelectorAll('#bookingsTable tbody tr');
    if (tableRows.length > 0) {
        tableRows.forEach(row => {
            row.addEventListener('click', function() {
                const bookingId = this.getAttribute('data-booking-id');
                if (bookingId && bookingSelect) {
                    bookingSelect.value = bookingId;
                    loadBookingDetails();
                }
            });
        });
    }
};