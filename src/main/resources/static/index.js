const CLIENT_ID = "294307018578-mlelfvhktca0k84t1brnho8ssn25dsqe.apps.googleusercontent.com";

function start() {
    gapi.load('auth2', function() {
        auth2 = gapi.auth2.init({
            client_id: CLIENT_ID,
            scope: "https://www.googleapis.com/auth/calendar.events"
        });
    });
}

function signInCallback(authResult) {
    console.log('authResult', authResult);
    if (authResult['code']) {

        // Hide the sign-in button now that the user is authorized, for example:
        $('#signInButton').attr('style', 'display: none');

        // Send the code to the server
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/storeauthcode',
            // Always include an `X-Requested-With` header in every AJAX request,
            // to protect against CSRF attacks.
            headers: {
                'X-Requested-With': 'XMLHttpRequest'
            },
            contentType: 'application/octet-stream; charset=utf-8',
            success: function(result) {
                // Handle or verify the server response.
            },
            processData: false,
            data: authResult['code']
        });
    } else {
        // There was an error.
    }
}

$('#signInButton').click(function() {
// signInCallback defined in step 6.
    auth2.grantOfflineAccess().then(signInCallback);
});

//$('#periods-button').click(getFreePeriods);



/*$("#bookMovie").on("click", function(e){
    e.preventDefault();
    fetch("/periods")
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {

            for(var i = 0; i < myJson.length; i++) {

                var timeStart = myJson[i].start.value;
                var timeEnd = myJson[i].end.value;

                var dateStart = new Date(timeStart);
                var dateEnd = new Date(timeEnd);

                console.log(JSON.stringify("Start: " + dateStart + ", End: " + dateEnd));

                var p = $('<p class="timeItem"></p>');
                var button = $('<button class="bookItem">Book</button>');
                button.data({start:timeStart, end:timeEnd});
                p.text("Start: " + dateStart.toLocaleString() + " -  End: " + dateEnd.toLocaleString());
                $("#periods").append(p, button);
            }
        });
});*/

/*$("body").on("click",".bookItem", function() {
    console.log( $(this).data() );
    var data = $(this).data();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: '/booking',
        data: JSON.stringify(data),
        dataType: "json"
    });
});*/



// Submit on 'Return'-key
$('#search-input').keyup(function (e) {
    if (e.keyCode === 13) {
        getMovie();
    }
});

$('#submit-button').click(getMovie);

$('#periods-button').click(getFreePeriods);

function getMovie () {
    fetch('/movie?title=' + $("#search-input").val())
        .then(function(response) {
            return response.json();
        })

        .then(function(myJson) {
            console.log(JSON.stringify(myJson));
            $(".movie-id").text('ID: ' + myJson.id);
            $(".movie-title").text('Title: ' + myJson.title);
            $(".movie-genre").text('Genre: ' + myJson.genre);
            $(".movie-rating").text('IMDB Rating: ' + myJson.imdbRating);
            $(".movie-poster").attr('src', myJson.poster);
        });
}

function getFreePeriods () {

    fetch('/periods')
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
            console.log('inside function(myJson)');
            let startTime;
            let endTime;
            $("#periods").append($('<p>Select a time to book a movie night</p>'));
            for (let i = 0; i < myJson.length; i++) {
                startTime = new Date(myJson[i]['start']['value']);
                endTime = new Date(myJson[i]['end']['value']);
                let p = $('<button class="timeItem"></button>');
                p.text('Start: ' + startTime.toLocaleString() + '   End: ' + endTime.toLocaleString());
                $("#periods").append(p);
            }
        });
}

$("body").on('click', '.timeItem', function () {
    console.log("Creating new event");
    var data = $(this).data();

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: '/booking',
        data: JSON.stringify(data),
        dataType: "json"
    });
});






