fun euclidToRing(coord: Int, ringLength: Int): Int =
    if (coord >= ringLength) {
        coord % ringLength
    } else if (coord >= 0) {
        coord
    } else {
        euclidToRing(ringLength + coord, ringLength)
    }