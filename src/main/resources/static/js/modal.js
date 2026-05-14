            document.addEventListener("DOMContentLoaded", function () {
                const zoomableImages = document.querySelectorAll(
                    '.payment-img, .img-thumb, .product-image'
                );
                const modalImage = document.getElementById('modalImage');

                zoomableImages.forEach(img => {
                    img.addEventListener('click', function () {
                        const imgSrc = img.getAttribute('data-bs-img-src');
                        modalImage.setAttribute('src', imgSrc);
                    });
                });
            });