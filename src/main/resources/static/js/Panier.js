
export default class Panier {
    constructor() {
        this.orderId = JSON.parse(localStorage.getItem("orderId"));
        this.panier = [];
        this.init();
    }

    async init() {
        if (this.orderId) {
            try {
                const response = await fetch(`/api/order-items?orderId=${this.orderId}`);
                if (response.ok) {
                    this.panier = await response.json();
					console.log(this.panier)
                } else {
                    this.panier = [];
                }
            } catch (e) {
                this.panier = [];
            }
        } else {
            this.panier = [];
        }
        this.run();
    }

    run() {
        this.render();
        this.updateSummary();
    }

    render() {
        const container = document.querySelector(".cart-items");
        container.innerHTML = `		
		<div class="trust-badges">
            <div class="trust-badge">
                <div class="trust-badge-icon">🚚</div>
                <div class="trust-badge-text">Livraison gratuite</div>
            </div>
            <div class="trust-badge">
                <div class="trust-badge-icon">🛡️</div>
                <div class="trust-badge-text">Paiement sécurisé</div>
            </div>
            <div class="trust-badge">
                <div class="trust-badge-icon">↩️</div>
                <div class="trust-badge-text">Retour sous 30 jours</div>
            </div>
        </div>`;

        this.panier.forEach(item => {
            const product = item.product;
            container.innerHTML += `
                <div class="cart-item">
                    <div class="item-image"><img src="${product.pathImage}" alt="Produit"></div>
                    <div class="item-details">
                        <div class="item-category">${product.category ? product.category.name : ''}</div>
                        <div class="item-name">${product.name}</div>
                        <div class="item-specs">${product.description || 'Specs ici'}</div>
                        <div class="item-stock">✓ En stock</div>
                    </div>
                    <div class="item-actions">
                        <div class="item-price">${item.unitPrice.toFixed(2).replace('.', ',')} €</div>
                        <div class="quantity-control">
                            <button class="qty-btn" data-action="reduce" data-id="${product.id}">-</button>
                            <input type="number" class="qty-input" id="qty-${product.id}" value="${item.quantity}" min="1" readonly>
                            <button class="qty-btn" data-action="add" data-id="${product.id}">+</button>
                        </div>
                        <button class="remove-btn" data-action="delete" data-id="${item.id}">🗑️ Supprimer</button>
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

        const productId = parseInt(button.dataset.id);
        const action = button.dataset.action;
        const item = this.panier.find(p => p.product && p.product.id === productId);
        if (!item) return;
        if (action === "reduce" && item.quantity > 1) {
            item.quantity -= 1;
        } else if (action === "add") {
            item.quantity += 1;
        }
        await this.modifyItem(item.id, item.quantity);
        this.run();
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
        await this.init();
    }

    async onDeleteItem(e) {
        const button = e.currentTarget;
        const orderItemId = parseInt(button.dataset.id);
        await this.deleteItem(orderItemId);
        await this.init();
    }

    async deleteItem(orderItemId) {
        const response = await fetch(`/api/admin/order-items/${orderItemId}`, {
            method: "DELETE"
        });

        if (!response.ok) {
            console.error("Erreur delete", await response.text());
            return;
        }
    }

    saveLocal() {
        localStorage.setItem("panier", JSON.stringify(this.panier));
    }

	updateSummary() {
	    let totalItems = 0;
	    let subtotal = 0;
 
        this.panier.forEach(item => {
            totalItems += item.quantity;
            subtotal += item.unitPrice * item.quantity;
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
