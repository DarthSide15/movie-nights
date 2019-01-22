const CLIENT_ID = "294307018578-mlelfvhktca0k84t1brnho8ssn25dsqe.apps.googleusercontent.com";

function start() {
    gapi.load('auth2', function() {
        auth2 = gapi.auth2.init({
            client_id: CLIENT_ID,
            scope: "https://www.googleapis.com/auth/calendar.events"
        });
    });
}

$('#signInButton').click(function() {
// signInCallback defined in step 6.
    auth2.grantOfflineAccess().then(signInCallback);
});

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
    console.log('Inside getFreePeriods()');
    fetch('/periods')
        .then(function(response) {
            return response.json();
        })
        .then(function(myJson) {
            console.log('inside function(myJson)');
            let startTime;
            let endTime;
            for (let i = 0; i < myJson.length; i++) {
                startTime = new Date(myJson[i]['start']['value']);
                endTime = new Date(myJson[i]['end']['value']);
                let p = $('<p class="timeItem"></p>');
                p.text('Start: ' + startTime.toLocaleString() + '   End: ' + endTime.toLocaleString());
                $("#periods").append(p);
            }
        });
}

