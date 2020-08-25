/*----- app's state (variables) -----*/

let board;
let turn = 'X';
let win;

/*----- constants -----*/
const winningCombos = [
    [0, 1, 2],
    [3, 4, 5],
    [6, 7, 8],
    [0, 3, 6],
    [1, 4, 7],
    [2, 5, 8],
    [0, 4, 8],
    [2, 4, 6]
    ];

/*----- cached element references -----*/

const squares = Array.from(document.querySelectorAll('#board div'));

/*----- event listeners -----*/
document.getElementById('board').addEventListener('click', handleTurn);
const messages = document.querySelector('h2');
// document.getElementById('reset-button').addEventListener('click', init);

$('#reset-button').click(function() {
    location.reload();
});

/*----- functions -----*/

function getWinner() {
    let winner = null;
    winningCombos.forEach(function(combo, index) {
        if (board[combo[0]] && board[combo[0]] === board[combo[1]] && board[combo[0]] === board[combo[2]]) winner = board[combo[0]];
        });
        return winner ? winner : board.includes('') ? null : 'T';
};

// function getPosition(playerName) {
//
//     const userAction = async () => {
//       const response = await fetch('/', {
//         method: 'GET',
//         body: '/data/3', // string or object
//         headers: {
//           'Content-Type': 'application/json'
//         }
//       });
//     const myJson = await response.json(); //extract JSON from the http response
//     conole.log('myJson' , myJson)
//         // do something with myJson
// }
//
//     var client = new HttpClient();
//     client.get('/data/3', function(response) {
//        console.log('response is'+response)
//         return response
//     });
//     console.log('Getting position for player'+playerName)
// }

var HttpClient = function() {
    this.get = function(aUrl, aCallback) {
        var anHttpRequest = new XMLHttpRequest();
        anHttpRequest.onreadystatechange = function() {
            if (anHttpRequest.readyState == 4 && anHttpRequest.status == 200)
                aCallback(anHttpRequest.responseText);
        }

        anHttpRequest.open( "GET", aUrl, true );
        anHttpRequest.send( null );
    }
}

function handleTurn() {

    console.log('handleTurn')


    if(turn == 'X') {

        var client = new HttpClient();
            client.get('/tictactoe/data?player=X&board='+board, function(response) {
            console.log('response is'+JSON.parse(response)['positionValue'])
            board[JSON.parse(response)['positionValue']] = 'X';
            turn = turn === 'X' ? 'O' : 'X';
            win = getWinner();
            setTimeout(function() {  render(); }, 500);

        });
    }
    else {

            let idx = squares.findIndex(function(square) {
        return square === event.target;
    });
    console.log('Turn is '+turn)

        board[idx] = turn;
        turn = turn === 'X' ? 'O' : 'X';

        win = getWinner();
        render();
        handleTurn()
    }

};

function init() {
    board = [
    '', '', '',
    '', '', '',
    '', '', ''
    ];
    render();
};

function render() {
    board.forEach(function(mark, index) {
    //this moves the value of the board item into the squares[idx]
        console.log('index'+index)
    squares[index].textContent = mark;
    });
    messages.textContent = win === 'T' ? `Drawn game` : win ? `${win} wins the game!` : `It's ${turn}'s turn to move`;
    };



function wait(ms)
{
    var d = new Date();
    var d2 = null;
    do { d2 = new Date(); }
    while(d2-d < ms);
}

init();

handleTurn()