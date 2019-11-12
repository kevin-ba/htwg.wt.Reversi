function registerMoveListener() {
    for (let x = 0; x < grid.size * grid.size; x++) {
        if (grid.cells[x] == 0) {
            $("#scalar"+scalar).on(“mousemove“, function() {highlight()})
        }
    }
}

$( document ).ready(function() {
    console.log( "Document is ready" );
    registerMoveListener();

});
