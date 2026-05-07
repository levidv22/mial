function updateCartCount() {
    fetch('/user/carrito/cantidad')
            .then(response => response.json())
            .then(data => {
                document.getElementById('cart-count').textContent = data;
            })
            .catch(error => console.error('Error al obtener la cantidad del carrito:', error));
}

// Llamar a la función cuando se carga la página
document.addEventListener('DOMContentLoaded', updateCartCount);


function validateFileType() {
    var fileInput = document.getElementById('imagePago');
    var fileError = document.getElementById('fileError');
    var file = fileInput.files[0];

    if (file) {
        var allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
        if (!allowedTypes.includes(file.type)) {
            fileError.textContent = "Solo se permiten imágenes (JPG, PNG, GIF, WEBP).";
            fileInput.value = ""; // Borra el archivo inválido
        } else {
            fileError.textContent = "";
        }
    }
}
