function validateFileType(event) {
    var file = event.target.files[0];
    var errorDiv = document.getElementById("fileError");
    var preview = document.getElementById("preview");

    if (!file) {
        errorDiv.textContent = "Debe seleccionar un archivo de imagen.";
        preview.style.display = "none";
        return;
    }

    var validTypes = ["image/jpeg", "image/png", "image/gif", "image/webp"];
    
    if (!validTypes.includes(file.type)) {
        errorDiv.textContent = "Solo se permiten imágenes en formato JPG, PNG, GIF o WEBP.";
        event.target.value = ""; // Resetear el input
        preview.style.display = "none";
        return;
    }

    errorDiv.textContent = ""; // Borrar errores si el archivo es válido
    
    // Mostrar vista previa de la imagen
    var reader = new FileReader();
    reader.onload = function(e) {
        preview.src = e.target.result;
        preview.style.display = "block";
    };
    reader.readAsDataURL(file);
}