let size = 8

function cellType(scalar) {
    switch(grid.cells[scalar]) {
        case 1: return "white";
        case 2: return "black";
        default: return "notSet";
    }
}

function row(scalar) {
    return Math.floor(scalar / size);
}

function col(scalar) {
    return Math.floor(scalar % size);
}

let gameJson = {
    size:8,
    0: {0:0,1:0,2:0,3:0,4:0,5:0,6:0,7:0},
    1: {0:0,1:0,2:0,3:0,4:0,5:0,6:0,7:0},
    2: {0:0,1:0,2:0,3:0,4:0,5:0,6:0,7:0},
    3: {0:0,1:0,2:0,3:1,4:2,5:0,6:0,7:0},
    4: {0:0,1:0,2:0,3:2,4:1,5:0,6:0,7:0},
    5: {0:0,1:0,2:0,3:0,4:0,5:0,6:0,7:0},
    6: {0:0,1:0,2:0,3:0,4:0,5:0,6:0,7:0},
    7: {0:0,1:0,2:0,3:0,4:0,5:0,6:0,7:0},
};

class Grid {
    constructor(size){
        this.size = size;
        this.cells = [];
    }

    fill(json) {
        for (let scalar=0; scalar <this.size*this.size;scalar++) {
            this.cells[scalar]=(json[row(scalar)][col(scalar)]);
        }
    }
}

let grid = new Grid(gameJson.size)
grid.fill(gameJson)

function fillGrid(grid) {
    console.log("Filling grid");
    for (let scalar=0; scalar <grid.size*grid.size;scalar++) {
            document.getElementById("scalar"+scalar).className = cellType(scalar);
    }
}

function setCell(scalar, value) {
    console.log("Setting cell " + scalar);
    document.getElementById("scalar"+scalar).className = "white";
    $("#scalar"+scalar).off("click");

}

function registerClickListener() {
    for (let scalar=0; scalar <grid.size*grid.size;scalar++) {
        if (grid.cells[scalar] == 0) {
            $("#scalar"+scalar).click(function() {setCell(scalar, grid.cells[scalar])});
        }
    }
}

$( document ).ready(function() {
    console.log( "Document is ready" );
    fillGrid(grid);
    registerClickListener();
    //registerMoveListener()

});


/*function registerMoveListener() {
    for (let scalar = 0; scalar < grid.size * grid.size; scalar++) {
        $("#scalar"+scalar).on("mousemove", function() {highlight(scalar)})
    }
}

function highlight(scalar) {
    /* Falls zug möglich -> grün highlighten
        Falls zug nicht möglich -> rot
    document.getElementById("scalar"+scalar).className = "white";
}*/
