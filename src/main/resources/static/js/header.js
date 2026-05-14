document.addEventListener("DOMContentLoaded", function () {

            const dropdown = document.querySelector(".nav-item.dropdown");
            const trigger = document.querySelector(".nav-item.dropdown > a");
            const menu = document.querySelector(".mega-menu");

            // abrir/cerrar con click
            trigger.addEventListener("click", function (e) {
                e.preventDefault();
                dropdown.classList.toggle("active");
            });

            // cerrar si clic fuera
            document.addEventListener("click", function (e) {
                if (!dropdown.contains(e.target)) {
                    dropdown.classList.remove("active");
                }
            });

            // mantener abierto si estás dentro del menú
            menu.addEventListener("mouseenter", function () {
                dropdown.classList.add("active");
            });

        });