function calculateFare() {
    var baseRate = parseFloat(document.getElementById("baseRate").value);
    var perKmRate = parseFloat(document.getElementById("perKmRate").value);
    var distance = parseFloat(document.getElementById("distance").value);
    var peakHourMultiplier = parseFloat(document.getElementById("peakHourMultiplier").value);
    var isPeakHour = document.getElementById("isPeakHour").checked;

    if (!isNaN(distance) && !isNaN(perKmRate) && !isNaN(baseRate)) {
        var totalFare = baseRate + (perKmRate * distance);
        if (isPeakHour) {
            totalFare *= peakHourMultiplier;
        }
        document.getElementById("totalFare").value = totalFare.toFixed(2);
    }
}