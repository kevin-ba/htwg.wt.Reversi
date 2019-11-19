let size = 8

function cellType(scalar) {
    switch(grid.cells[scalar]) {
        case 1: return "white";
        case 2: return "black";
        default: return "notSet";
    }
}

function toScalar(house, cell) {
    return house*size + cell;
}

function row(scalar) {
    return Math.floor(scalar / size);
}

function col(scalar) {
    return Math.floor(scalar % size);
}

class Grid {
    constructor(size){
        this.size = size;
        this.cells = [];
    }

    fill(json) {
        for (let scalar=0; scalar <this.size*this.size;scalar++) {
            this.cells[scalar]=(json[toScalar(row(scalar),col(scalar))].cell.value);
        }
    }
}

let grid = new Grid(8)

function updateGrid(grid) {
    console.log("Filling grid");
    for (let scalar=0; scalar <grid.size*grid.size;scalar++) {
            document.getElementById("scalar"+scalar).className = cellType(scalar);
    }
}

function setCell(scalar, value) {
    console.log("Setting cell " + scalar);
    document.getElementById("scalar"+scalar).className = "white";
    setCellOnServer(row(scalar), col(scalar))
    $("#scalar"+scalar).off("click");

}

function registerClickListener() {
    for (let scalar=0; scalar <grid.size*grid.size;scalar++) {
        if (grid.cells[scalar] == 0) {
            $("#scalar"+scalar).click(function() {setCell(scalar, grid.cells[scalar])});
        }
    }
}

function setCellOnServer(row, col) {
    $.get("/set/"+row+"/"+col, function(data) {
        console.log("Set cell on Server");
    });
}

function loadJson() {
    $.ajax({
        method: "GET",
        url: "/json",
        dataType: "json",

        success: function (result) {
            grid = new Grid(result.grid.size);
            grid.fill(result.grid.cells);
            updateGrid(grid);
            registerClickListener();
        }
    });
}

$( document ).ready(function() {
    console.log( "Document is ready" );
    loadJson();
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
