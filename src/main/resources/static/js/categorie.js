async function addToCart(element) {

    const product = {
		orderItemId: element.dataset.orderItemId,
        productId: element.dataset.id,
        name: element.dataset.name,
        category: element.dataset.category,
        price: parseFloat(element.dataset.price),
        quantity: 1
    };

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

			console.log(response);
			if (!updateItem.ok) {
			console.log(response.message);
                throw new Error("Erreur création commande");
            }
				console.log("eee");

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
				console.log(response.message);
                throw new Error("Erreur création commande");
            }
	
			const orderItem = await response.json();
			product.orderItemId = orderItem.id;

            panier.push(product);
        }

        localStorage.setItem("panier", JSON.stringify(panier));

    } catch (error) {
        console.error(error);
        alert("Erreur lors de l'ajout au panier");
    }
}
