export default class Panier {
    constructor() {
        this.orderId = JSON.parse(localStorage.getItem("orderId"));

        this.panier = JSON.parse(localStorage.getItem("panier")) || [];

        localStorage.setItem("panier", JSON.stringify(this.panier));
        this.run();
    }

    run() {
        this.render();
        this.updateSummary();
    }

    render() {
        const container = document.querySelector(".cart-items");
        container.innerHTML = "";

        this.panier.forEach(item => {
            container.innerHTML += `
                <div class="cart-item">
                    <div class="item-image"><img src="${item.path_image}" alt="Produit"></div>
                    <div class="item-details">
                        <div class="item-category">${item.category}</div>
                        <div class="item-name">${item.name}</div>
                        <div class="item-specs">Specs ici</div>
                        <div class="item-stock">✓ En stock</div>
                    </div>

                    <div class="item-actions">
                        <div class="item-price">${item.price.toFixed(2).replace(".", ",")} €</div>
                        <div class="quantity-control">
                            <button class="qty-btn" data-action="reduce" data-id="${item.productId}">-</button>
                            <input type="number" class="qty-input" id="qty-${item.productId}" value="${item.quantity}" min="1" readonly>
                            <button class="qty-btn" data-action="add" data-id="${item.productId}">+</button>
                        </div>
                        <button class="remove-btn" data-id="${item.productId}">🗑️ Supprimer</button>
                    </div>
                </div>
            `;
        });

        this.initialEvent();
    }

	initialEvent() {
        this.onClickButtonsChangeQuantity();
        this.onClickButtonsDelete();
    }

    onClickButtonsChangeQuantity() {
        const buttons = document.querySelectorAll("[data-action='reduce'], [data-action='add']");
        buttons.forEach(button => {
            button.addEventListener("click", (e) => this.onChangeQuantity(e));
        });
    }

    onClickButtonsDelete() {
        const buttons = document.querySelectorAll("[data-action='delete']");
        buttons.forEach(button => {
            button.addEventListener("click", (e) => this.onDeleteItem(e));
        });
    }

    async onChangeQuantity(e) {
        const button = e.currentTarget;
		console.log(button)
        const productId = parseInt(button.dataset.id);
        const action = button.dataset.action;	
		console.log(productId)
        const item = this.panier.find(p => p.productId === productId);
		console.log("eee", item)
        if (!item) return;

        if (action === "reduce" && item.quantity > 1) {
            item.quantity -= 1;
        } else if (action === "add") {
            item.quantity += 1;
        }

        await this.modifyItem(productId, item.quantity);
        this.saveLocal();
        this.render();
    }

    async modifyItem(productId, quantity) {
        const response = await fetch(`/api/admin/order-items/${productId}?quantity=${quantity}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (!response.ok) {
            console.error("Erreur update quantity", await response.text());
            return;
        }

        console.log("Quantité mise à jour en DB");
    }

    async onDeleteItem(e) {
        const button = e.currentTarget;
        const productId = parseInt(button.dataset.productId);

        await this.deleteItem(productId);

        this.panier = this.panier.filter(p => p.productId !== productId);
        this.saveLocal();
        this.render();
    }

    async deleteItem(productId) {
        const response = await fetch(`/api/admin/order-items/${productId}`, {
            method: "DELETE"
        });

        if (!response.ok) {
            console.error("Erreur delete", await response.text());
            return;
        }

        console.log("Item supprimé en DB");
    }

    saveLocal() {
        localStorage.setItem("panier", JSON.stringify(this.panier));
    }

	updateSummary() {
	    let totalItems = 0;
	    let subtotal = 0;
 
	    this.panier.forEach(item => {
	        totalItems += item.quantity;
	        subtotal += item.price * item.quantity;
	    });

	    const tax = subtotal * 0.20;
	    const total = subtotal + tax;

	    document.getElementById("subtotal").textContent =
	        subtotal.toFixed(2).replace(".", ",") + " €";

	    document.getElementById("tax").textContent =
	        tax.toFixed(2).replace(".", ",") + " €";

	    document.getElementById("total").textContent =
	        total.toFixed(2).replace(".", ",") + " €";

	    const summaryItems = document.getElementById("summary-items");
	    if (summaryItems) {
	        summaryItems.textContent =
	            `Sous-total (${totalItems} article${totalItems > 1 ? "s" : ""})`;
	    }

	    const cartCountText = document.getElementById("cart-count-text");
	    if (cartCountText) {
	        cartCountText.innerHTML =
	            `Vous avez <strong>${totalItems} article${totalItems > 1 ? "s" : ""}</strong> dans votre panier`;
	    }

	    const badge = document.querySelector(".cart-badge");
	    if (badge) {
	        badge.textContent = totalItems;
	    }
	}
}
