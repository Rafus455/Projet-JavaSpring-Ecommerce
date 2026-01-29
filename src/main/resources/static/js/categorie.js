async function addToCart(element) {

    const product = {
		orderItemId: element.dataset.orderItemId,
        productId: element.dataset.id,
        name: element.dataset.name,
        category: element.dataset.category,
        price: parseFloat(element.dataset.price),
        quantity: 1,
		path_image: element.dataset.pathimage
    };
	console.log("ejehj", element.dataset.pathImage)
    let orderId = localStorage.getItem("orderId");
    let panier = JSON.parse(localStorage.getItem("panier")) || [];

    try {
        if (!orderId) {
            const orderResponse = await fetch("/api/order", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ status: "DRAFT" })
            });

            if (!orderResponse.ok) {
                throw new Error("Erreur création commande");
            }

            const order = await orderResponse.json();
            orderId = order.id;
            localStorage.setItem("orderId", orderId);
        }

        const existingItem = panier.find(p => p.productId == product.productId);

        if (existingItem) {
            existingItem.quantity += 1;

            const updateItem = await fetch(`/api/order-items/${existingItem.orderItemId}?quantity=${existingItem.quantity}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
            });

			console.log(updateItem);
			if (!updateItem.ok) {
				const errorData = await updateItem.json();

				  console.log(errorData.message);
				if(errorData.message === 'Stock insuffisant') {
					alert("Désolé le dernier produit vient d'être acheter");
					window.location.reload();
				    return;
				} else {
                throw new Error("Erreur lors de la création d'une commande");
				}
            }
        } else {
            const response = await fetch("/api/order-items", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    orderId: orderId,
                    productId: product.productId,
                    quantity: 1
                })
        	});

			if (!response.ok) {
                throw new Error("Erreur création commande");
            }
	
			const orderItem = await response.json();
			product.orderItemId = orderItem.id;

            panier.push(product);
        }

		Toastify({
		    text: `${element.dataset.name} vient d'être ajouter au panier`,
		    duration: 2_000,
		    gravity: "top",
		    position: "right",
		    backgroundColor: "#4CAF50",
		    close: true
	    }).showToast();
		
        localStorage.setItem("panier", JSON.stringify(panier));

    } catch (error) {
        console.error(error);
        alert("Erreur lors de l'ajout au panier");
    }
}
