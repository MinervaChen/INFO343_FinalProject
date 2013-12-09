// Utils

function apply(source, target) {
    for (prop in source) {
        target[prop] = source[prop];
    }
} //apply()