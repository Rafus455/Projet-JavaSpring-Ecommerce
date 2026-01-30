export default class Panier {
    constructor() {
        this.orderId = JSON.parse(localStorage.getItem("orderId"));
        this.panier = [];
        this.cartItemsContainer = document.querySelector(".cart-items");
        this.init();
    }

    async init() {
        if (this.orderId) {
            try {
                const response = await fetch(`/api/order-items?orderId=${this.orderId}`);
                if (response.ok) {
                    this.panier = await response.json();
                    console.log("Panier chargé :", this.panier);
                } else {
                    this.panier = [];
                }
            } catch (e) {
                console.error("Erreur connexion panier", e);
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
        this.cartItemsContainer.innerHTML = `		
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

        if (this.panier.length === 0) {
            this.cartItemsContainer.innerHTML += `<p style="text-align:center; padding:20px; width:100%;">Votre panier est vide.</p>`;
            return;
        }

        this.panier.forEach(item => {
            const product = item.product;

            const basePrice = product.price;
            const onSale = product.onSale;
            let priceHtml = '';

            if (onSale > 0) {
                const discountedPrice = basePrice - (basePrice * onSale / 100);
                
                priceHtml = `
                    <div class="cart-item-price-container">
                        <span class="cart-old-price">${basePrice.toFixed(2).replace('.', ',')} €</span>
                        <div style="display:flex; align-items:center; gap:5px;">
                            <span class="cart-badge-sale">-${onSale}%</span>
                            <span class="cart-new-price">${discountedPrice.toFixed(2).replace('.', ',')} €</span>
                        </div>
                    </div>
                `;
            } else {
                priceHtml = `<div class="cart-standard-price">${basePrice.toFixed(2).replace('.', ',')} €</div>`;
            }

            this.cartItemsContainer.innerHTML += `
                <div class="cart-item">
                    <div class="item-image">
                        <img src="${product.pathImage}" alt="${product.name}">
                    </div>
                    <div class="item-details">
                        <div class="item-category">${product.categoryName || ''}</div>
                        <div class="item-name">${product.name}</div>
                        <div class="item-specs">${product.description ? product.description.substring(0, 50) + '...' : ''}</div>
                        <div class="item-stock">✓ En stock</div>
                    </div>
                    <div class="item-actions">
                        <!-- Affichage du prix géré dynamiquement plus haut -->
                        ${priceHtml}
                        
                        <div class="quantity-control">
                            <button class="qty-btn" data-action="reduce" data-id="${item.id}" data-qty="${item.quantity}">-</button>
                            <input type="number" class="qty-input" value="${item.quantity}" min="1" readonly>
                            <button class="qty-btn" data-action="add" data-id="${item.id}" data-qty="${item.quantity}">+</button>
                        </div>
                        <button class="remove-btn" data-action="delete" data-id="${item.id}">🗑️ Supprimer</button>
                    </div>
                </div>
            `;
        });

        this.initialEvent();
    }

    initialEvent() {
        const qtyButtons = document.querySelectorAll("[data-action='reduce'], [data-action='add']");
        qtyButtons.forEach(btn => btn.addEventListener("click", (e) => this.onChangeQuantity(e)));

        const delButtons = document.querySelectorAll("[data-action='delete']");
        delButtons.forEach(btn => btn.addEventListener("click", (e) => this.onDeleteItem(e)));
    }

    async onChangeQuantity(e) {
        const button = e.currentTarget;
        const orderItemId = parseInt(button.dataset.id);
        const currentQty = parseInt(button.dataset.qty);
        const action = button.dataset.action;

        let newQuantity = currentQty;

        if (action === "add") {
            newQuantity += 1;
        } else if (action === "reduce") {
            if (currentQty > 1) {
                newQuantity -= 1;
            } else {
                return;
            }
        }

        await this.modifyItem(orderItemId, newQuantity);
    }

    async modifyItem(orderItemId, quantity) {
        try {
            const response = await fetch(`/api/order-items/${orderItemId}?quantity=${quantity}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" }
            });

            if (!response.ok) {
                alert("Impossible de mettre à jour la quantité (Stock insuffisant ?)");
                return;
            }
            await this.init();
        } catch (error) {
            console.error("Erreur modif quantité", error);
        }
    }

    async onDeleteItem(e) {
        if(!confirm("Voulez-vous supprimer cet article ?")) return;
        
        const button = e.currentTarget;
        const orderItemId = parseInt(button.dataset.id);
        
        try {
            const response = await fetch(`/api/order-items/${orderItemId}`, {
                method: "DELETE"
            });

            if (response.ok) {
                await this.init();
            } else {
                alert("Erreur lors de la suppression");
            }
        } catch (error) {
            console.error("Erreur suppression", error);
        }
    }

    updateSummary() {
        let totalItems = 0;
        let subtotal = 0;

        this.panier.forEach(item => {
            const product = item.product;
            totalItems += item.quantity;

            let finalUnitPrice = product.price;
            if (product.onSale > 0) {
                finalUnitPrice = product.price - (product.price * product.onSale / 100);
            }

            subtotal += finalUnitPrice * item.quantity;
        });

        const tax = subtotal * 0.20;
        
        const total = subtotal + tax;
        const elSub = document.getElementById("subtotal");
        if(elSub) elSub.textContent = subtotal.toFixed(2).replace(".", ",") + " €";

        const elTax = document.getElementById("tax");
        if(elTax) elTax.textContent = tax.toFixed(2).replace(".", ",") + " €";

        const elTotal = document.getElementById("total");
        if(elTotal) elTotal.textContent = total.toFixed(2).replace(".", ",") + " €";

        const summaryItems = document.getElementById("summary-items");
        if (summaryItems) {
            summaryItems.textContent = `Sous-total (${totalItems} article${totalItems > 1 ? "s" : ""})`;
        }

        const cartCountText = document.getElementById("cart-count-text");
        if (cartCountText) {
            cartCountText.innerHTML = `Vous avez <strong>${totalItems} article${totalItems > 1 ? "s" : ""}</strong> dans votre panier`;
        }
        
        const badge = document.querySelector(".cart-badge"); 
        if (badge) {
            badge.textContent = totalItems;
            badge.style.display = totalItems > 0 ? 'flex' : 'none';
        }
    }
}