
        $(document).ready(function(){
            $("#pickup, #destination, #cabType").change(function(){
                var pickup = $("#pickup").val();
                var destination = $("#destination").val();
                var cabType = $("#cabType").val();

                if (pickup && destination && cabType) {
                    var baseFare = 1000; 
                    var distance = Math.floor(Math.random() * (50 - 5) + 5); 
                    var typeMultiplier = (cabType === "Mini") ? 1 : (cabType === "Sedan") ? 1.5 : 2;
                    var estimatedFare = baseFare + (distance * 50 * typeMultiplier);

                    $("#fare").text("LKR " + estimatedFare.toFixed(2));
                    $("#fareBox").fadeIn();
                }
            });
        });
        
        
